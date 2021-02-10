package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int id; //
    private int userId;
    private Long orderNo; //订单编号 唯一
    private String orderDetail; //订单详情
    private BigDecimal orderPrice; // 订单金额
    private int orderStatus; //订单状态  0-未配送 1-已配送
    private Date createTime;
    private Date updateTime;

}
