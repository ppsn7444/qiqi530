package com.goods.controller.business;


import com.github.pagehelper.PageHelper;
import com.goods.business.service.ProductCategoryService;
import com.goods.business.service.ProductListService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.business.PageVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "交易模块-产品分类接口")
@RestController
@RequestMapping("/business/productCategory")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;
    //    categoryTree?pageNum=1&pageSize=5
    @ApiOperation(value = "分类-数据字典")
    @GetMapping("categoryTree")
    public ResponseBean ProductCategory(PageVO pageVO) {
//        PageHelper.
//        Page<ProductCategory>   = new schevo
        PageVO<ProductCategoryTreeNodeVO> productCategory = productCategoryService.getProductCategory(pageVO);

        return ResponseBean.success(productCategory);
    }

    /**
     * 获取父类分级信息
     * @return
     */
//    business/productCategory/getParentCategoryTree
    @ApiOperation("加载父类")
    @GetMapping("getParentCategoryTree")
    public ResponseBean getParentCategoryTree(){
       List<ProductCategoryTreeNodeVO> list =  productCategoryService.getParentCategoryTree();
        return ResponseBean.success(list);
    }

    /**
     * 添加分类数据
     * @param vo
     * @return
     */
//business/productCategory/add
    @ApiOperation("添加分类数据")
    @PostMapping("add")
    public ResponseBean add(@RequestBody ProductCategoryVO vo){
        productCategoryService.add(vo);
        return ResponseBean.success();
    }

//    get("business/productCategory/edit/" + id)
    @ApiOperation("修改分类数据")
    @GetMapping("edit/{id}")
    public  ResponseBean editById(@PathVariable Long id){
        ProductCategoryVO vo = productCategoryService.edit(id);
        return ResponseBean.success(vo);
    }

//      put"business/productCategory/update/" + this.editRuleForm.id,
//      this.editRuleForm  name,remark,sort
    @ApiOperation("修改")
    @PutMapping("update/{id}")
    public  ResponseBean updateById (@PathVariable Long id,
                                     @RequestBody ProductCategoryVO productCategoryVO){
        productCategoryVO.setId(id);
        productCategoryService.update(productCategoryVO);
        return ResponseBean.success();
    }
//delete(
//  "business/productCategory/delete/" + id
    @ApiOperation("删除分类")
    @DeleteMapping("delete/{id}")
    public  ResponseBean deleteById(@PathVariable Long id){
        Boolean flag = productCategoryService.deleteById(id);
        if(flag){
            return ResponseBean.success();
        }
        return ResponseBean.error("删除失败");
    }


}
