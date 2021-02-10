package graduatedesign.ghost.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.mapper.ProductMapper;
import graduatedesign.ghost.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public PageInfo<Product> queryAllProduct(int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Product> products = productMapper.queryAllProduct();
        return new PageInfo<>(products,5);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addProduct(Product product) {
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());
        productMapper.addProduct(product);
    }

    @Override
    public Product queryProductById(int id) {
        return productMapper.queryProductById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateProduct(Product product) {
        product.setUpdatedTime(new Date());
        return productMapper.updateProduct(product);
    }
}
