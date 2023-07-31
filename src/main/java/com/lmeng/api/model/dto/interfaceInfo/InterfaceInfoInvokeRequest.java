package com.lmeng.api.model.dto.interfaceInfo;

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

    /**
     * id主键
     */
    private Long id;

    /**
     * 用户填的请求参数
     */
    private String userRequestParams;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
