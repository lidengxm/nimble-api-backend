package com.lmeng.api.project.model.dto.userInterfaceInfo;

import com.lmeng.apicommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询接口请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoInvokeRequest extends PageRequest implements Serializable {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 剩余调用次数
     */
//    private Integer leftNum;

    private static final long serialVersionUID = 1L;
}