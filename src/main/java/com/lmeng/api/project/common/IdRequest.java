package com.lmeng.api.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 便于接口传递
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}