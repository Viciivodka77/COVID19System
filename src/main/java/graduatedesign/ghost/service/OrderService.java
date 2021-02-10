package graduatedesign.ghost.service;

import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.dto.OrderUserDTO;
import graduatedesign.ghost.pojo.Order;

public interface OrderService {

    //查
    PageInfo<OrderUserDTO> queryAllOrder(int page, int pageSize);
    PageInfo<Order> queryOrderByUserId(int userId,int page,int pageSize);
    Order queryOrderByID(int id);
    Order queryOrderByOrderNo(Long orderNo);

    //增
    int insertOrder(Order order);

    //改
    int updateOrderStatus(int id,int status);
}
