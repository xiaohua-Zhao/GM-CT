package com.chengtai.fashionmgmt.common;

import org.springframework.beans.BeanUtils;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** 业务保存和修改使用的通用值处理工具。 */
public final class BusinessValueUtils {
    /**
     * 私有构造方法：工具类不允许创建实例。
     */
    private BusinessValueUtils() {}

    /**
     * 将空金额转换为 0。
     */
    public static BigDecimal zero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    /**
     * 将金额规范为两位小数，空值按 0 处理。
     */
    public static BigDecimal money(BigDecimal value) {
        return zero(value).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 将税率规范为 0 至 1 之间的小数，空值按 0 处理。
     */
    public static BigDecimal taxRate(BigDecimal value) {
        return value == null ? new BigDecimal("0.13") : value;
    }

    /**
     * 按金额和税率计算价税合计。
     */
    public static BigDecimal taxIncluded(BigDecimal amount, BigDecimal taxRate) {
        return money(zero(amount).multiply(BigDecimal.ONE.add(taxRate(taxRate))));
    }

    /**
     * 安全计算两个金额的乘积，并对结果保留两位小数。
     */
    public static BigDecimal multiply(BigDecimal left, BigDecimal right) {
        return money(zero(left).multiply(zero(right)));
    }

    /** 只复制请求中非 null 字段，避免局部修改把数据库原值覆盖为 null。 */
    public static void copyNonNullProperties(Object source, Object target) {
        for (PropertyDescriptor sourcePd : BeanUtils.getPropertyDescriptors(source.getClass())) {
            Method read = sourcePd.getReadMethod();
            if (read == null || "class".equals(sourcePd.getName()) || "id".equals(sourcePd.getName())) continue;
            try {
                Object value = read.invoke(source);
                if (value == null) continue;
                PropertyDescriptor targetPd = BeanUtils.getPropertyDescriptor(target.getClass(), sourcePd.getName());
                if (targetPd == null || targetPd.getWriteMethod() == null) continue;
                targetPd.getWriteMethod().invoke(target, value);
            } catch (Exception ignored) {
                // 非业务字段无法复制时跳过。
            }
        }
    }
}
