package com.goods.controller.business;


import com.goods.business.service.ProductSupplierService;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.PageVO;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.business.SupplierVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/business/supplier")
class ProductSupplierController {
    @Autowired
    private ProductSupplierService productSupplierService;
//    get("business/supplier/findSupplierList", {params: this.queryMap
    @ApiOperation("加载列表")
    @RequestMapping("findSupplierList")
    public ResponseBean findSupplierList(@RequestBody PageVO productVO){
        PageVO<SupplierVO> productVOS = productSupplierService.getSupplierList(productVO);
        return ResponseBean.success();
    }
}