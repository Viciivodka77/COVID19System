package graduatedesign.ghost.service;

import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.pojo.Product;

public interface ProductService {

    PageInfo<Product> queryAllProduct(int page,int pageSize);

    void addProduct(Product product);

    Product queryProductById(int id);

    int updateProduct(Product product);

}
