package graduatedesign.ghost.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCarDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //商品id
    private int productId;
    //商品名
    private String name;
    //商品价格
    private BigDecimal price;
    //商品图片
    private String imgPath;
    //购买的数量
    private int amount = 1;



}
