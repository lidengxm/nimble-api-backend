package com.lmeng.api.project.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 增加调用接口
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求类型
     */
    private String method;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}