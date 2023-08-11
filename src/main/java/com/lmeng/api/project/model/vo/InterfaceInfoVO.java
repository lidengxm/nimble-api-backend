package com.lmeng.api.project.model.vo;

import com.lmeng.apicommon.entity.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 已登录用户视图（脱敏）
 **/
@Data

public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 接口剩余可调用次数
     */
    private String availablePieces;

    private static final long serialVersionUID = 1L;
}