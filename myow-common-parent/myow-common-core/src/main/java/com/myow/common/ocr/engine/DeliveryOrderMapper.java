package com.myow.common.ocr.engine;

import com.myow.common.ocr.config.OcrConfig;
import com.myow.common.ocr.layout.PageLayout;
import com.myow.common.ocr.model.CargoInfo;
import com.myow.common.ocr.model.ContainerInfo;
import com.myow.common.ocr.model.DeliveryOrderDoc;
import com.myow.common.ocr.model.DoInfo;
import com.myow.common.ocr.model.Issuer;
import com.myow.common.ocr.model.ParseResult;
import com.myow.common.ocr.model.ShipmentDetails;
import com.myow.common.ocr.normalize.ContainerNoValidator;
import com.myow.common.ocr.normalize.ValueNormalizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: 把网格解析出的原始字段映射为标准单证模型，并执行归一化与自校验。
 */
public class DeliveryOrderMapper {

    /** 抬头区右边界占页宽比例（右侧为"THE MERCHANDISE DESCRIBED BELOW"等说明文字） */
    private static final float ISSUER_RIGHT_RATIO = 0.60f;

    private final OcrConfig config;

    public DeliveryOrderMapper(OcrConfig config) {
        this.config = config;
    }

    /**
     * 解析单页并把结果合并进 result（多页时先到先得，不覆盖已有非空值）。
     */
    public void mapPage(PageLayout page, ParseResult result) {
        GridResolver resolver = GridResolver.of(page, config, DoTemplate.VOCABULARY);

        Map<String, String> raw = new LinkedHashMap<>();
        for (LabelSpec spec : DoTemplate.SPECS) {
            raw.put(spec.getKey(), resolver.resolve(spec));
        }
        raw.forEach((k, v) -> {
            if (v != null && !result.getRawFields().containsKey(k)) {
                result.getRawFields().put(k, v);
            }
        });

        DeliveryOrderDoc doc = result.getDocument();
        fillDoInfo(doc.getDoInfo(), raw);
        fillIssuer(doc.getIssuer(), resolver, page);
        fillShipment(doc.getShipmentDetails(), raw);
        fillCargo(doc.getCargo(), raw);
        fillContainers(doc, result, resolver, raw);

        validate(doc, result);
    }

    // ---------------- do_info ----------------

    private void fillDoInfo(DoInfo info, Map<String, String> raw) {
        setIfBlank(info::getDoNumber, info::setDoNumber,
                ValueNormalizer.normalizeRefNo(raw.get(DoTemplate.F_DO_NUMBER)));
        setIfBlank(info::getOurRefNo, info::setOurRefNo,
                ValueNormalizer.normalizeRefNo(raw.get(DoTemplate.F_OUR_REF_NO)));
        setIfBlank(info::getDate, info::setDate,
                ValueNormalizer.normalizeDate(raw.get(DoTemplate.F_DATE)));
        setIfBlank(info::getCustRefNo, info::setCustRefNo,
                ValueNormalizer.normalizeRefNo(raw.get(DoTemplate.F_CUST_REF_NO)));
    }

    // ---------------- issuer ----------------

    /**
     * 抬头区没有标签，用<b>区域定位</b>：标题下沿 ~ 第一行表头之间的左半区。
     */
    private void fillIssuer(Issuer issuer, GridResolver resolver, PageLayout page) {
        if (issuer.getCompanyName() != null) {
            return;
        }
        float top = resolver.lineBottomContaining("DELIVERY ORDER");
        if (top < 0) {
            top = 0f;
        }
        float bottom = firstGridRowTop(resolver, page);
        float right = page.getWidth() * ISSUER_RIGHT_RATIO;

        List<String> textLines = resolver.resolveRegionLines(top, bottom, 0f, right);
        if (textLines.isEmpty()) {
            return;
        }
        issuer.setCompanyName(ValueNormalizer.clean(textLines.get(0)));
        if (textLines.size() > 1) {
            issuer.setAddress(ValueNormalizer.clean(String.join(", ", textLines.subList(1, textLines.size()))));
        }
    }

    /** 第一行网格表头的纵向位置，作为抬头区下边界 */
    private float firstGridRowTop(GridResolver resolver, PageLayout page) {
        float y = resolver.labelLineTop("CARRIER");
        if (y > 0) {
            return y - 1f;
        }
        y = resolver.labelLineTop("B/L OR AWB. NO.");
        if (y > 0) {
            return y - 1f;
        }
        return page.getHeight() * 0.28f;
    }

    // ---------------- shipment_details ----------------

    private void fillShipment(ShipmentDetails s, Map<String, String> raw) {
        setIfBlank(s::getCarrier, s::setCarrier, ValueNormalizer.clean(raw.get(DoTemplate.F_CARRIER)));
        setIfBlank(s::getBlOrAwbNo, s::setBlOrAwbNo,
                ValueNormalizer.normalizeRefNo(raw.get(DoTemplate.F_BL_OR_AWB_NO)));
        setIfBlank(s::getHouseNo, s::setHouseNo,
                ValueNormalizer.normalizeRefNo(raw.get(DoTemplate.F_HOUSE_NO)));
        setIfBlank(s::getLocation, s::setLocation, ValueNormalizer.clean(raw.get(DoTemplate.F_LOCATION)));
        setIfBlank(s::getOriginDestinationPort, s::setOriginDestinationPort,
                ValueNormalizer.clean(raw.get(DoTemplate.F_ORIGIN_DEST_PORT)));
        setIfBlank(s::getArrivalDate, s::setArrivalDate,
                ValueNormalizer.normalizeDate(raw.get(DoTemplate.F_ARRIVAL_DATE)));
        setIfBlank(s::getFreeTimeExp, s::setFreeTimeExp,
                ValueNormalizer.normalizeDate(raw.get(DoTemplate.F_FREE_TIME_EXP)));
    }

    // ---------------- cargo ----------------

    private void fillCargo(CargoInfo cargo, Map<String, String> raw) {
        if (cargo.getTotalPackages() == null) {
            cargo.setTotalPackages(ValueNormalizer.toInteger(raw.get(DoTemplate.F_TOTAL_PACKAGES)));
        }
        setIfBlank(cargo::getDescription, cargo::setDescription,
                ValueNormalizer.clean(raw.get(DoTemplate.F_DESCRIPTION)));
        setIfBlank(cargo::getTotalWeight, cargo::setTotalWeight,
                ValueNormalizer.normalizeWeight(raw.get(DoTemplate.F_TOTAL_WEIGHT)));
    }

    // ---------------- container ----------------

    /**
     * 集装箱子表可能有多行；document.container 取第一箱，全量放入 result.containers。
     */
    private void fillContainers(DeliveryOrderDoc doc, ParseResult result,
                                GridResolver resolver, Map<String, String> raw) {
        List<String> nos = resolver.resolveList(specOf(DoTemplate.F_CONTAINER_NO));
        List<String> types = resolver.resolveList(specOf(DoTemplate.F_SIZE_TYPE));
        List<String> seals = resolver.resolveList(specOf(DoTemplate.F_SEAL_NO));
        List<String> weights = resolver.resolveList(specOf(DoTemplate.F_CONTAINER_WEIGHT));
        List<String> qtys = resolver.resolveList(specOf(DoTemplate.F_CONTAINER_QUANTITY));

        int rows = Math.max(nos.size(), Math.max(types.size(), seals.size()));
        List<ContainerInfo> list = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            ContainerInfo c = new ContainerInfo();
            c.setContainerNo(ValueNormalizer.normalizeContainerNo(at(nos, i)));
            c.setSizeType(ValueNormalizer.clean(at(types, i)));
            c.setSealNo(ValueNormalizer.clean(at(seals, i)));
            c.setWeight(ValueNormalizer.normalizeWeight(at(weights, i)));
            c.setQuantity(ValueNormalizer.clean(at(qtys, i)));
            list.add(c);
        }
        if (list.isEmpty()) {
            return;
        }
        if (result.getContainers().isEmpty()) {
            result.getContainers().addAll(list);
        }
        ContainerInfo first = list.get(0);
        ContainerInfo target = doc.getContainer();
        setIfBlank(target::getContainerNo, target::setContainerNo, first.getContainerNo());
        setIfBlank(target::getSizeType, target::setSizeType, first.getSizeType());
        setIfBlank(target::getSealNo, target::setSealNo, first.getSealNo());
        setIfBlank(target::getWeight, target::setWeight, first.getWeight());
        setIfBlank(target::getQuantity, target::setQuantity, first.getQuantity());

        // 单据未单独填写 CUST. REF. NO. 时，部分版式以箱号作为客户参考号
        if (doc.getDoInfo().getCustRefNo() == null && raw.get(DoTemplate.F_CUST_REF_NO) == null) {
            doc.getDoInfo().setCustRefNo(first.getContainerNo());
        }
    }

    private LabelSpec specOf(String key) {
        for (LabelSpec spec : DoTemplate.SPECS) {
            if (spec.getKey().equals(key)) {
                return spec;
            }
        }
        throw new IllegalArgumentException("未定义的字段: " + key);
    }

    private static String at(List<String> list, int index) {
        return index < list.size() ? list.get(index) : null;
    }

    // ---------------- 校验 ----------------

    private void validate(DeliveryOrderDoc doc, ParseResult result) {
        result.getMissingFields().clear();
        if (doc.getDoInfo().getDoNumber() == null) {
            result.getMissingFields().add(DoTemplate.F_DO_NUMBER);
        }
        if (doc.getContainer().getContainerNo() == null) {
            result.getMissingFields().add(DoTemplate.F_CONTAINER_NO);
        }

        String cno = doc.getContainer().getContainerNo();
        if (cno != null) {
            if (!ContainerNoValidator.matchesFormat(cno)) {
                result.addWarning("箱号格式不符合 ISO 6346（4 字母 + 7 数字）: " + cno);
            } else if (!ContainerNoValidator.isValid(cno)) {
                result.addWarning("箱号校验位不符，疑似识别错误: " + cno);
            }
        }
        String date = doc.getDoInfo().getDate();
        if (date != null && !ValueNormalizer.looksLikeDate(date)) {
            result.addWarning("签发日期格式异常: " + date);
        }
    }

    // ---------------- 工具 ----------------

    private static void setIfBlank(java.util.function.Supplier<String> getter,
                                   java.util.function.Consumer<String> setter,
                                   String value) {
        if (value != null && getter.get() == null) {
            setter.accept(value);
        }
    }
}
