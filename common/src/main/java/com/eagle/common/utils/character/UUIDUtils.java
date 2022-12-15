package com.eagle.common.utils.character;

import com.eagle.common.constant.CharacterInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * 唯一标识生成工具类
 */
public class UUIDUtils {
    /**
     * 自动生成简洁版UUID,即：删除横杠
     */
    public static String randomSimpleUUID() {
        return StringUtils.replace(randomUUID(), CharacterInfo.LINE_THROUGH_CENTER, "");
    }

    /**
     * 生成唯一标识
     *
     * @return
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
