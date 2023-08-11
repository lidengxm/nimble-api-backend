package com.lmeng.api.project.model.dto.interfaceInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @learner Lmeng
 * @date 2023/7/30
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 调用接口id
     */
    private Long id;


    /**
     * 用户请求参数
     */
    private String userRequestParams;

}
