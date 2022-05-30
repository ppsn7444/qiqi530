package com.goods.business.service;

import com.goods.common.vo.business.PageVO;
import com.goods.common.vo.business.SupplierVO;

public interface ProductSupplierService {
    PageVO<SupplierVO> getSupplierList(PageVO productVO);
}
