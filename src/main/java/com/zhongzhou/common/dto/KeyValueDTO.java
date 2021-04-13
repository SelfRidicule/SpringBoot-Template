package com.zhongzhou.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName KeyValueDTO
 * @Description 键值对
 * @Date 2020/7/22 14:53
 * @Author
 */
@Data
public class KeyValueDTO implements Serializable {
    private static final long serialVersionUID = -781600138171604350L;

    private String key;
    private String value;
}
