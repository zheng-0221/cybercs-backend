package com.zpp.cybercs.common;

import com.zpp.cybercs.constant.CommonConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zheng
 * @description 分页请求类
 * @date 2023/11/4 14:23:33
 * @version 1.0
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    @Min(message = "当前页号最小为 1", value = 1)
    private long current;

    /**
     * 页面大小
     */
    @Min(message = "页面容量最小为 10", value = 10)
    private long pageSize;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
