package com.zhongzhou.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName TreeVO
 * @Description 树
 * @Date 2020/8/6 9:47
 * @Author
 */
@Data
public class TreeVO implements Serializable {

    private static final long serialVersionUID = -5824027986130494605L;

    /**
     * 节点唯一索引值
     */
    private Long id;
    /**
     * 节点标题
     */
    private String title;
    /**
     * 节点字段名
     */
    private String field;
    /**
     * 点击节点弹出新窗口对应的 url
     */
    private String href;
    /**
     * 节点是否初始展开，默认 false
     */
    private Boolean spread;
    /**
     * 节点是否初始为选中状态（如果开启复选框的话），默认 false
     */
    private Boolean checked;
    /**
     * 节点是否为禁用状态。默认 false
     */
    private Boolean disabled;
    /**
     * 子级列表
     */
    private List<TreeVO> children;

}
