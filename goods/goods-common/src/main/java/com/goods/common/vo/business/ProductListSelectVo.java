package com.goods.common.vo.business;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProductListSelectVo {
    //    pageNum,PageSize,name,categoryId,supplier,status
    @NotBlank(message = "页码不能为空")
    private Integer pageNum;
    @NotBlank(message = "页码size不能为空")
    private Integer pageSize;
    private String name;
    private String categorys;
    private String supplier;
    @NotBlank(message = "状态不能为空，默认0")
    private Integer status;
}
