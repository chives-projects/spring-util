package com.csc.common.utils.calculation;

import com.csc.common.exception.BusinessException;
import com.csc.common.constant.CharacterInfo;
import com.csc.common.enums.ApplicationStatus;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @description: 数据计算工具类
 * @create: 2022/11/24
 */
public class NumberUtils {
    private static final NumberFormat numberToPercentWithZero = DecimalFormat.getPercentInstance();
    private static final NumberFormat numberToPercentWithoutZero = DecimalFormat.getPercentInstance();

    /**
     * 四舍五入，保留指定位数的小数
     *
     * @param number 数据
     * @param scale  小数位
     * @return
     */
    public static String rounding(String number, int scale) {
        try {
            if (scale < 0) {
                scale = 0;
            }
            return new BigDecimal(number).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
        } catch (Exception e) {
            throw new BusinessException(ApplicationStatus.ILLEGAL_ARGUMENT_EXCEPTION.getCode(), "数据计算异常");
        }
    }

    /**
     * 将数据转换为百分比,四舍五入，默认CharacterUtils.ZERO
     * CharacterUtils.ZERO 小数位不存在时补零，默认值 如：0.12345(保留两位小数)->12.35%
     * CharacterUtils.ZERO 小数位不存在时补零，默认值 如：0.123(保留两位小数)->12.30%
     * CharacterUtils.HASH_SYMBOL 小数位不存在时为空 如：0.12(保留两位小数)->12%
     *
     * @param number 数据
     * @param scale  精度（保留小数位）
     * @param symbol 数字占位符(0实际位数不够时补零; #实际位数不够时不补零)
     */
    public static String getPercent(String number, int scale, String... symbol) {
        if (StringUtils.isEmpty(number)) return null;
        boolean withOutZero = symbol.length >= 1 && StringUtils.equals(symbol[0], CharacterInfo.HASH_SYMBOL);
        NumberFormat numberFormat;
        if (withOutZero) {
            numberFormat = numberToPercentWithoutZero;
            numberFormat.setMaximumFractionDigits(scale);
        } else {
            numberFormat = numberToPercentWithZero;
            numberFormat.setMinimumFractionDigits(scale);
        }
        return numberFormat.format(Double.valueOf(number));
    }
}
