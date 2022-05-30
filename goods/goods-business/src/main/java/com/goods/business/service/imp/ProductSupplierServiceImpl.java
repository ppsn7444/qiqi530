package com.goods.business.service.imp;

import com.goods.business.service.ProductSupplierService;
import com.goods.common.vo.business.PageVO;
import com.goods.common.vo.business.SupplierVO;
import org.springframework.stereotype.Service;

@Service
public class ProductSupplierServiceImpl implements ProductSupplierService {
    @Override
    public PageVO<SupplierVO> getSupplierList(PageVO productVO) {
        return null;
    }
}
