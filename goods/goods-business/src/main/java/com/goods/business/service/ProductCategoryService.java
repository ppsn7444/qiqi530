package com.goods.business.service;

import com.goods.common.model.business.ProductCategory;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.business.PageVO;

import java.util.List;

public interface ProductCategoryService {

    PageVO<ProductCategoryTreeNodeVO> getProductCategory(PageVO pageVO);

    List<ProductCategoryTreeNodeVO> getParentCategoryTree();

    void add(ProductCategoryVO vo);

    ProductCategoryVO edit(Long id);

    void update(ProductCategoryVO productCategoryVO);

    Boolean deleteById(Long id);


}
