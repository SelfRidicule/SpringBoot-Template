package com.zhongzhou.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName KeyValueVO
 * @Description 键值对
 * @Date 2020/7/22 14:53
 * @Author
 */
@Data
public class KeyValueVO implements Serializable {
    private static final long serialVersionUID = -781600138171604350L;

    private String key;
    private String value;
}
