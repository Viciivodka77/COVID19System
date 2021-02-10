package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int id;
    private String name;
    private String details;
    private int stock;
    private BigDecimal price;
    private Date createdTime;
    private Date updatedTime;
    private String imgPath;
}
