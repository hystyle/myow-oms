package com.myow.common.ocr.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: yss
 * @date: 2026-08-06
 * @description: Delivery Order 抽取模板（声明式）。
 * <p>
 * <b>适配新版式时，绝大多数情况只需在此处增加 alias，不必改动解析代码。</b>
 * 这是"系统无关的单据智能处理中间层"能横向扩展到更多单证类型的关键。
 */
public final class DoTemplate {

    // ---- 字段键 ----
    public static final String F_DO_NUMBER = "do_number";
    public static final String F_OUR_REF_NO = "our_ref_no";
    public static final String F_DATE = "date";
    public static final String F_CUST_REF_NO = "cust_ref_no";

    public static final String F_CARRIER = "carrier";
    public static final String F_BL_OR_AWB_NO = "bl_or_awb_no";
    public static final String F_HOUSE_NO = "house_no";
    public static final String F_LOCATION = "location";
    public static final String F_ORIGIN_DEST_PORT = "origin_destination_port";
    public static final String F_ARRIVAL_DATE = "arrival_date";
    public static final String F_FREE_TIME_EXP = "free_time_exp";

    public static final String F_TOTAL_PACKAGES = "total_packages";
    public static final String F_DESCRIPTION = "description";
    public static final String F_TOTAL_WEIGHT = "total_weight";

    public static final String F_CONTAINER_NO = "container_no";
    public static final String F_SIZE_TYPE = "size_type";
    public static final String F_SEAL_NO = "seal_no";
    public static final String F_CONTAINER_WEIGHT = "container_weight";
    public static final String F_CONTAINER_QUANTITY = "container_quantity";

    /** 集装箱子表锚点（消除 WEIGHT / QUANTITY 与货物表头的歧义） */
    private static final String ANCHOR_CONTAINER = "Container No.";
    /** 货物表锚点 */
    private static final String ANCHOR_CARGO = "NO. OF PKGS.";

    public static final List<LabelSpec> SPECS;

    /**
     * 标签词表。除字段标签外，还包含仅用于"识别这是一行表头、从而终止取值"的停用标签。
     */
    public static final Set<String> VOCABULARY;

    static {
        List<LabelSpec> specs = new ArrayList<>();

        // ---------- do_info ----------
        specs.add(LabelSpec.key(F_DO_NUMBER)
                .aliases("ENTRY-B/L NO.", "ENTRY B/L NO.", "ENTRY-BL NO.", "D/O NO.", "DELIVERY ORDER NO.")
                .required().build());
        specs.add(LabelSpec.key(F_OUR_REF_NO)
                .aliases("OUR REF. NO.", "OUR REFERENCE NO.", "OUR REF").build());
        specs.add(LabelSpec.key(F_DATE)
                .aliases("DATE", "ISSUE DATE", "ISSUED DATE")
                // 用同行锚点排除页脚的 "Date:  Time:"
                .anchor("OUR REF. NO.").build());
        specs.add(LabelSpec.key(F_CUST_REF_NO)
                .aliases("CUST. REF. NO.", "CUSTOMER REF. NO.", "CUST REF").build());

        // ---------- shipment_details ----------
        specs.add(LabelSpec.key(F_CARRIER)
                .aliases("CARRIER", "VESSEL/VOYAGE", "VESSEL & VOYAGE").build());
        specs.add(LabelSpec.key(F_BL_OR_AWB_NO)
                .aliases("B/L OR AWB. NO.", "B/L OR AWB NO.", "MASTER B/L NO.", "MBL NO.", "B/L NO.").build());
        specs.add(LabelSpec.key(F_HOUSE_NO)
                .aliases("HOUSE NO.", "HOUSE B/L NO.", "HBL NO.").build());
        specs.add(LabelSpec.key(F_LOCATION)
                .aliases("LOCATION", "TERMINAL", "PICK UP LOCATION").maxValueLines(2).build());
        specs.add(LabelSpec.key(F_ORIGIN_DEST_PORT)
                .aliases("ORIGIN/DESTINATION PORT", "ORIGIN / DESTINATION PORT", "PORT OF DISCHARGE").build());
        specs.add(LabelSpec.key(F_ARRIVAL_DATE)
                .aliases("ARRIVAL DATE", "ETA", "ESTIMATED ARRIVAL").build());
        specs.add(LabelSpec.key(F_FREE_TIME_EXP)
                .aliases("FREE TIME EXP.", "FREE TIME EXPIRY", "LAST FREE DAY", "LFD").build());

        // ---------- cargo ----------
        specs.add(LabelSpec.key(F_TOTAL_PACKAGES)
                .aliases("NO. OF PKGS.", "NO. OF PACKAGES", "NO OF PKG", "PACKAGES").build());
        specs.add(LabelSpec.key(F_DESCRIPTION)
                .aliases("DESCRIPTION OF ARTICLES", "DESCRIPTION OF GOODS", "DESCRIPTION")
                .mode(LabelSpec.MatchMode.PREFIX).maxValueLines(3).build());
        specs.add(LabelSpec.key(F_TOTAL_WEIGHT)
                .aliases("WEIGHT", "GROSS WEIGHT").anchor(ANCHOR_CARGO).build());

        // ---------- container ----------
        specs.add(LabelSpec.key(F_CONTAINER_NO)
                .aliases("CONTAINER NO.", "CONTAINER NUMBER", "CNTR NO.").build());
        specs.add(LabelSpec.key(F_SIZE_TYPE)
                .aliases("CONTAINER SIZE/TYPE", "SIZE/TYPE", "SIZE TYPE", "CNTR SIZE/TYPE").build());
        specs.add(LabelSpec.key(F_SEAL_NO)
                .aliases("SEAL NOS.", "SEAL NO.", "SEAL NUMBER").anchor(ANCHOR_CONTAINER).build());
        specs.add(LabelSpec.key(F_CONTAINER_WEIGHT)
                .aliases("WEIGHT").anchor(ANCHOR_CONTAINER).build());
        specs.add(LabelSpec.key(F_CONTAINER_QUANTITY)
                .aliases("QUANTITY", "QTY").anchor(ANCHOR_CONTAINER).build());

        SPECS = Collections.unmodifiableList(specs);

        Set<String> vocab = new HashSet<>();
        for (LabelSpec spec : specs) {
            for (String alias : spec.getAliases()) {
                vocab.add(LabelSpec.normalize(alias));
            }
        }
        // 停用标签：只参与"表头行"判定，帮助准确切断取值区间
        vocab.addAll(Arrays.asList(
                LabelSpec.normalize("FOR DELIVERY TO"),
                LabelSpec.normalize("ROUTE"),
                LabelSpec.normalize("INLAND FREIGHT"),
                LabelSpec.normalize("PREPAID/COLLECT"),
                LabelSpec.normalize("RECEIVED IN GOOD ORDER"),
                LabelSpec.normalize("ORIGINAL DELIVERY ORDER"),
                LabelSpec.normalize("DELIVERY CLERK:"),
                LabelSpec.normalize("PER:"),
                LabelSpec.normalize("TIME"),
                LabelSpec.normalize("DO NOT USE"),
                LabelSpec.normalize("DO USE NOT")
        ));
        VOCABULARY = Collections.unmodifiableSet(vocab);
    }

    private DoTemplate() {
    }
}
