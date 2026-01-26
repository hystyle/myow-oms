package com.myow.common.enums;


/**
 * 所有业务枚举的统一接口
 * 约定：每个枚举都要有 code（通常是 int 或 String）和 desc（描述）
 */
public interface BaseEnum {

    /**
     * 枚举的唯一编码（数据库、接口传输用）
     */
    String getCode();

    /**
     * 枚举的中文描述（前端展示用）
     */
    String getDesc();

    /**
     * 通过 code 查找枚举（静态方法建议放在每个枚举类里，但这里提供默认实现参考）
     *
     * @param <T>       枚举类型
     * @param enumClass 枚举类
     * @param code      编码
     * @return 匹配的枚举 或 null
     */
    static <T extends BaseEnum> T fromCode(Class<T> enumClass, String code) {
        if (code == null) {
            return null;
        }
        for (T e : enumClass.getEnumConstants()) {
            if (code.equals(e.getCode())) {
                return e;
            }
        }
        return null;  // 或 throw IllegalArgumentException("无效的code:" + code)
    }

    /**
     * 通过 desc 查找（不常用，慎用，因为 desc 可能重复或变更）
     */
    static <T extends BaseEnum> T fromDesc(Class<T> enumClass, String desc) {
        if (desc == null) {
            return null;
        }
        for (T e : enumClass.getEnumConstants()) {
            if (desc.equals(e.getDesc())) {
                return e;
            }
        }
        return null;
    }
}
