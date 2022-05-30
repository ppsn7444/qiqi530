package com.goods.business.service;

import com.goods.common.vo.business.ProductListSelectVo;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;

public interface ProductListService {
    PageVO<ProductVO> findProductList(ProductListSelectVo productListSelectVo);

    void addProduct(ProductVO productVO);

    void publishById(Long id);

    void deleteById(Long id);

    ProductVO editById(Long id);

    void removeById(Long id);

    void backById(Long id);

    void updateById(ProductVO productVO);
}
