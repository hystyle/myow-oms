package com.myow.system.domain.policy.rule;

import com.myow.common.support.serialnumber.SerialTemplate;
import com.myow.common.support.serialnumber.segment.FixedSegment;
import com.myow.common.support.serialnumber.segment.SeqSegment;
import com.myow.common.support.serialnumber.segment.SerialSegment;

import java.util.List;

/**
 * @author: yss
 * @date: 2026-01-29 21:49
 * @description:
 */
public class UserSerialTemplate implements SerialTemplate {

    @Override
    public List<String> getSupportedTypes() {
        return List.of("USER", "MEMBER");
    }

    @Override
    public List<SerialSegment> getSegments() {
        return List.of(
                new FixedSegment("U"),
                new SeqSegment(5, '0', "NONE")          // 全局递增，不重置
        );
    }
}


//@Component
//public class OrderSerialTemplate implements SerialTemplate {
//
//    @Override
//    public List<String> getSupportedTypes() {
//        return List.of("ORDER", "SO");
//    }
//
//    @Override
//    public List<SerialSegment> getSegments() {
//        return List.of(
//            new FixedSegment("ORD-"),
//                    new BusinessFieldSegment("userId", obj -> {
//                            if (obj instanceof PurchaseCreateCmd cmd) {
//                            return cmd.getDepartmentCode();
//                            }
//                            return "";
//                            }),     // 或直接用固定前缀 + seq
//            new FixedSegment("-"),
//            new DateSegment("yyyyMMdd"),
//            new FixedSegment("-"),
//            new SeqSegment(5, '0', "DAY")   // 每天重置，5位补0
//        );
//    }
//}