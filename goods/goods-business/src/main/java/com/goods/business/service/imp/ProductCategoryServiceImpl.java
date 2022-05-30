package com.goods.business.service.imp;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ProductCategoryMapper;
import com.goods.business.service.ProductCategoryService;
import com.goods.common.model.business.ProductCategory;
import com.goods.common.model.system.Department;
import com.goods.common.utils.CategoryTreeBuilder;
import com.goods.common.vo.business.ProductCategoryTreeNodeVO;
import com.goods.common.vo.business.ProductCategoryVO;
import com.goods.common.vo.business.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public PageVO<ProductCategoryTreeNodeVO> getProductCategory(PageVO pageVO) {
//        创建分页对象
//        查询条件
//        带分页条件查询
//        PageHelper.startPage(pageNum,pageSize);
//        Integer pageNum, Integer pageSize

        Example o = new Example(ProductCategory.class);
//        o.createCriteria().andEqualTo("pid",0);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(o);

//        转vo
        List<ProductCategoryTreeNodeVO> list = convertProductCategoryTreeNodeVO(productCategoryList);
        List<ProductCategoryTreeNodeVO> build = CategoryTreeBuilder.build(list);
//        List<ProductCategoryTreeNodeVO> list1 = CategoryTreeBuilder.buildParent(build);

        PageInfo<ProductCategoryTreeNodeVO> pageInfo = new PageInfo<>(build);
        List<ProductCategoryTreeNodeVO> buildList = new ArrayList<>();
//        手动分

        Integer pageNum = pageVO.getPageNum();
        Integer pageSize = pageVO.getPageSize();
//        分页如果有则这样返回，没有则直接返回作为别的地方查询用
        if (pageNum != null && pageSize != null) {
            int count = 0;
            for (int i = (pageNum - 1) * pageSize; i < build.size(); i++) {
                if (count < pageSize.intValue()) {
                    buildList.add(build.get(i));
                    count++;
                }
            }
        } else {
            buildList = build;
        }

        return new PageVO<>(pageInfo.getTotal(), buildList);
    }

    @Override
    public List<ProductCategoryTreeNodeVO> getParentCategoryTree() {
        Example o = new Example(ProductCategory.class);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(o);
        List<ProductCategoryTreeNodeVO> list = convertProductCategoryTreeNodeVO(productCategoryList);
        List<ProductCategoryTreeNodeVO> list1 = CategoryTreeBuilder.buildParent(list);
        return list1;
    }

    //    转树形
    private List<ProductCategoryTreeNodeVO> convertProductCategoryTreeNodeVO(List<ProductCategory> productCategoryList) {
        List<ProductCategoryTreeNodeVO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(productCategoryList)) {
            for (ProductCategory productCategory : productCategoryList) {
                ProductCategoryTreeNodeVO productCategoryTreeNodeVO = new ProductCategoryTreeNodeVO();
                BeanUtils.copyProperties(productCategory, productCategoryTreeNodeVO);
                list.add(productCategoryTreeNodeVO);
            }
        }
        return list;
    }

    @Override
    public void add(ProductCategoryVO vo) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(vo, productCategory);
        productCategory.setCreateTime(new Date());
        productCategory.setModifiedTime(new Date());
        productCategoryMapper.insert(productCategory);
    }

    @Override
    public ProductCategoryVO edit(Long id) {
        Example example = new Example(ProductCategory.class);
        example.createCriteria().andEqualTo("id", id);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(example);
        ProductCategoryVO productCategoryVO = new ProductCategoryVO();
        BeanUtils.copyProperties(productCategoryList.get(0), productCategoryVO);
        return productCategoryVO;
    }

    @Override
    public void update(ProductCategoryVO productCategoryVO) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(productCategoryVO, productCategory);
        productCategoryMapper.updateByPrimaryKeySelective(productCategory);
    }

    @Override
    public Boolean deleteById(Long id) {
        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(id);
        if (productCategory.getId() != null) {
//            List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(new Example(ProductCategory.class).createCriteria().andEqualTo("pid", productCategory.getId()));
//            明白了，因为对example再调用就是另一个对象了，而我们需要的是example对象
            Example example = new Example(ProductCategory.class);
            example.createCriteria().andEqualTo("pid", productCategory.getId());
            List<ProductCategory> productCategoryList = productCategoryMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(productCategoryList)) {
                return false;
            }
            productCategoryMapper.deleteByPrimaryKey(id);
            return true;
        }
        return false;
    }




}

