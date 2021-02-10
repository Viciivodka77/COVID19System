package graduatedesign.ghost.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graduatedesign.ghost.dto.ProductCarDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    //购物车的用户id
    private int userID;

    //购物车的商品list
    private List<ProductCarDTO> productCarDTOList = new ArrayList<ProductCarDTO>();


    //获取总价格
    @JsonIgnore
    public BigDecimal calTotalPrice(){
        BigDecimal total = new BigDecimal(0);
        for (ProductCarDTO productCarDTO : productCarDTOList) {
            BigDecimal price = productCarDTO.getPrice();
            total = total.add(price.multiply(new BigDecimal(productCarDTO.getAmount())));
        }
        return total;
    }

    @JsonIgnore
    public void addProducts(ProductCarDTO newProduct) throws Exception {
        //判断是否包含同类
        //判断是否包含该商品
        boolean isAdd = false;
        for (ProductCarDTO productCarDTO : productCarDTOList) {
            if (productCarDTO.getProductId() == newProduct.getProductId()) {
                productCarDTO.setAmount(productCarDTO.getAmount() + 1);
                isAdd = true;
            }
        }
        if (!isAdd){
            productCarDTOList.add(newProduct);
        }
    }

    @JsonIgnore
    public void deleteProducts(int productId) throws Exception {
        boolean isDelete = false;
        //判断是否包含该商品
        Iterator<ProductCarDTO> iterator = productCarDTOList.iterator();
        while (iterator.hasNext()){
            ProductCarDTO next = iterator.next();
            if (next.getProductId() == productId){
                iterator.remove();
                isDelete = true;
            }
        }
        if (!isDelete){
            throw new Exception("购物车中无该商品");
        }
    }

    @JsonIgnore
    public void addOneProducts(int productId) throws Exception {
        //判断是否包含该商品
        boolean isAdd = false;
        for (ProductCarDTO productCarDTO : productCarDTOList) {
            if (productCarDTO.getProductId() == productId) {
                productCarDTO.setAmount(productCarDTO.getAmount() + 1);
                isAdd = true;
            }
        }
        if (!isAdd){
            throw new Exception("购物车中无该商品");
        }
    }

    @JsonIgnore
    public void reduceOneProducts(int productId) throws Exception {
        boolean isReduce = false;
        //判断是否包含该商品
        Iterator<ProductCarDTO> iterator = productCarDTOList.iterator();
        while (iterator.hasNext()){
            ProductCarDTO next = iterator.next();
            if (next.getProductId() == productId){
                if (next.getAmount() > 1){
                    next.setAmount(next.getAmount() - 1);
                    isReduce = true;
                }else if (next.getAmount() == 1){
                    iterator.remove();
                    isReduce = true;
                }
            }
        }
        if (!isReduce){
            throw new Exception("购物车中无该商品");
        }
    }

}
