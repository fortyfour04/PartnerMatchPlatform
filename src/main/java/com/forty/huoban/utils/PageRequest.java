package com.forty.huoban.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: FortyFour
 * @description: 分页请求配置类
 * @time: 2025/2/4 17:15
 * @version:
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *  页面大小 默认15
     */
    public int pageSize = 15;

    /**
     *  页面号 默认1
     */
    public int pageNum = 1;
}
