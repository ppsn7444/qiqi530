package com.goods.business.service.imp;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goods.business.mapper.ProductListMapper;
import com.goods.business.service.ProductCategoryService;
import com.goods.business.service.ProductListService;
import com.goods.common.model.business.Product;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductListSelectVo;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import tk.mybatis.mapper.entity.Example;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductListServiceImpl implements ProductListService {
    @Autowired
    private ProductListMapper productListMapper;

    @Override
    public PageVO<ProductVO> findProductList(ProductListSelectVo productListSelectVo) {
        PageHelper.startPage(productListSelectVo.getPageNum(),productListSelectVo.getPageSize());
        Example o = new Example(Product.class);
        List<Product> products = new ArrayList<>();
        if(productListSelectVo.getName()!=null&&!"".equals(productListSelectVo.getName())){
            o.createCriteria().andLike("name","%"+productListSelectVo.getName()+"%");
            products = productListMapper.selectByExample(o);
        }
//        暂时先这样,看案例也是for循环查 = =？？

        String categorys = productListSelectVo.getCategorys();


        List<Product> productsList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(products)){
            for (Product product : products) {
                if(categorys !=null&&!"".equals(categorys)){
                  String []  split = categorys.split(",");
                o.createCriteria().andEqualTo("id",product.getId()).andEqualTo("status",productListSelectVo.getStatus())
                        .andEqualTo("threeCategoryId",Integer.parseInt(split[2]));
                }else{
                    o.createCriteria().andEqualTo("id",product.getId()).andEqualTo("status",productListSelectVo.getStatus());
                }
                List<Product> productsTemp = productListMapper.selectByExample(o);
                if(!CollectionUtils.isEmpty(productsTemp)){
                    productsList.add(productsTemp.get(0));
                }
            }
        }else{
            //        貌似是只有三级，那就直接弄了
            if(categorys !=null&&!"".equals(categorys)){
                String []  split = categorys.split(",");
                System.out.println("split[2] = " + split[2]);
                o.createCriteria().andEqualTo("threeCategoryId",Integer.parseInt(split[2])).andEqualTo("status",productListSelectVo.getStatus());
               productsList =  productListMapper.selectByExample(o);
            }else {
                o.createCriteria().andEqualTo("status",productListSelectVo.getStatus());
                productsList =productListMapper.selectByExample(o);
            }
        }


        PageInfo<Product> pageInfo =new PageInfo<>(productsList);
        List<ProductVO> productVOs = new ArrayList<>();
        for (Product product : productsList) {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(product,productVO);
            productVOs.add(productVO);
        }
        return new PageVO<>(pageInfo.getTotal(),productVOs);
    }

    @Override
    public void addProduct(ProductVO productVO) {
//       name,model,categoryKeys,unit,sort,remark，还有一个file但没写？imageUrl
        Long[] categoryKeys = productVO.getCategoryKeys();
        Product product = new Product();
//        name,model,,unit,sort,remark,imageUrl
        BeanUtils.copyProperties(productVO,product);
        product.setOneCategoryId(categoryKeys[0]);
        product.setTwoCategoryId(categoryKeys[1]);
        product.setThreeCategoryId(categoryKeys[2]);
//        pName,create,update
        product.setPNum(UUID.randomUUID().toString());
        product.setCreateTime(new Date());
        product.setModifiedTime(new Date());
        product.setStatus(2);
        productListMapper.insert(product);
    }

    @Override
    public void publishById(Long id) {
        Product product = productListMapper.selectByPrimaryKey(id);
        if(product!=null){
            product.setStatus(0);
        }
        productListMapper.updateByPrimaryKey(product);
    }

    @Override
    public void deleteById(Long id) {
        productListMapper.deleteByPrimaryKey(id);
    }

    @Override
    public ProductVO editById(Long id) {
        Product product = productListMapper.selectByPrimaryKey(id);
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(product,productVO);
        return productVO;
    }

    @Override
    public void removeById(Long id) {
        Product product = productListMapper.selectByPrimaryKey(id);
        if(product!=null){
            product.setStatus(1);
        }
        productListMapper.updateByPrimaryKey(product);
    }

    @Override
    public void backById(Long id) {
        Product product = productListMapper.selectByPrimaryKey(id);
        if(product!=null){
            product.setStatus(2);
        }
        productListMapper.updateByPrimaryKey(product);
    }

    @Override
    public void updateById(ProductVO productVO) {
        Product product = new Product();
        BeanUtils.copyProperties(productVO,product);
        productListMapper.updateByPrimaryKey(product);
    }
}
