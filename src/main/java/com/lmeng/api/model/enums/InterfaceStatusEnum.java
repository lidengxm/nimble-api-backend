package com.lmeng.api.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 */
public enum InterfaceStatusEnum {

    ONLINE("上线", 1),
    OFFLINE("下线", 0);

    private final String text;

    private final int value;

    InterfaceStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }


    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
