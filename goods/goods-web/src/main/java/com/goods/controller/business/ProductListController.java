package com.goods.controller.business;

import com.goods.business.service.ProductListService;
import com.goods.common.model.business.Product;
import com.goods.common.response.ResponseBean;
import com.goods.common.vo.business.ProductListSelectVo;
import com.goods.common.vo.business.ProductVO;
import com.goods.common.vo.system.PageVO;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/business/product/")
public class ProductListController {
    //读取配置文件
    @Value("${minio.endpointUrl}")
    private String endpointUrl;
    @Value("${minio.accessKey}")
    public String accessKey;

    @Value("${minio.secreKey}")
    public String secreKey;

    @Value("${minio.bucketName}")
    public String bucketName;

    @Autowired
    private ProductListService productListService;

    //    http://www.localhost:8989/business/product/findProductList?pageNum=1&pageSize=6&name=&categoryId=&supplier=&status=0
//    get("business/product/findProductList"
//    params: this.queryMap：pageNum,PageSize,name,categoryId,supplier,status,categorys
//    返回 this.total = res.data.total;this.productData = res.data.rows;
    @ApiOperation("物资资料列表一栏")
    @GetMapping("findProductList")
    public ResponseBean findProductList(ProductListSelectVo productListSelectVo) {
        PageVO<ProductVO> productVos = productListService.findProductList(productListSelectVo);
        return ResponseBean.success(productVos);
    }

    //     post "business/product/add",this.addRuleForm
//     name,model,categoryKeys,unit,sort,remark，还有一个file但没写？imageUrl
    @ApiOperation("添加")
    @PostMapping("add")
    public ResponseBean addProduct(@RequestBody ProductVO productVO) {
        productListService.addProduct(productVO);
        return ResponseBean.success();
    }


    //  获取上传的文件
    @PostMapping("fileUpload")
    public ResponseBean fileUpload(MultipartFile file) {
//        声明一个url
        String url = "";
//        创建MinioClient
        try {
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpointUrl)
                            .credentials(accessKey, secreKey)
                            .build();
//        创建存储桶
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                // Make a new bucket called bucketName.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            } else {
                System.out.println("Bucket 'gmall' already exists.");
            }
//        文件上传
//        定义一个上传后的文件名
            String fileName = System.currentTimeMillis() + UUID.randomUUID().toString();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                            file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            url = endpointUrl + "/" + bucketName + "/" + fileName;
        } catch (ErrorResponseException e) {
            e.printStackTrace();
        } catch (InsufficientDataException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (XmlParserException e) {
            e.printStackTrace();
        }
        return ResponseBean.success(url);
    }

    //    put,business/product/publish/" + id
    @ApiOperation("审核通过")
    @PutMapping("publish/{id}")
    public ResponseBean publishById(@PathVariable Long id) {
        productListService.publishById(id);
        return ResponseBean.success();
    }

    //    delete("business/product/delete/" + id
    @ApiOperation("删除物资")
    @DeleteMapping("delete/{id}")
    public ResponseBean deleteById(@PathVariable Long id) {
        productListService.deleteById(id);
        return ResponseBean.success();
    }

    //    get("business/product/edit/" + id
    @ApiOperation("编辑之回显")
    @GetMapping("edit/{id}")
    public ResponseBean editById(@PathVariable Long id) {
        ProductVO productVO = productListService.editById(id);
        return ResponseBean.success(productVO);
    }

    //    put business/product/remove/" + id
    @ApiOperation("回收站")
    @PutMapping("remove/{id}")
    public ResponseBean removeById(@PathVariable Long id) {
        productListService.removeById(id);
        return ResponseBean.success();
    }

    //   put business/product/back/" + id
    @ApiOperation("回收站回收")
    @PutMapping("back/{id}")
    public ResponseBean backById(@PathVariable Long id) {
        productListService.backById(id);
        return ResponseBean.success();
    }

//    put("business/product/update/" + this.editRuleForm.id,this.editRuleForm
    @ApiOperation("更新物资")
    @PutMapping("update/{id}")
    public ResponseBean updateById(@PathVariable Long id,@RequestBody ProductVO productVO){
        productVO.setId(id);
        productListService.updateById(productVO);
        return ResponseBean.success();
    }

//    business/product/findProductList



}
