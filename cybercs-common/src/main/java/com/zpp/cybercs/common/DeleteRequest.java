package com.zpp.cybercs.common;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zheng
 * @description 删除请求类
 * @date 2023/11/4 14:23:33
 * @version 1.0
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = -4466558853111737527L;
    /**
     * id
     */
    @NotNull
    private Long id;

}