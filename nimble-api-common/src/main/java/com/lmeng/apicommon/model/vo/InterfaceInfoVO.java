package com.lmeng.apicommon.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 已登录用户视图（脱敏）
 **/
@Data

public class InterfaceInfoVO implements Serializable {

    /**
     * 调用次数
     */
    private Integer totalNum;

    private String name;

    private static final long serialVersionUID = 1L;
}