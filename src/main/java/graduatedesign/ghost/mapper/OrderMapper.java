package graduatedesign.ghost.mapper;

import graduatedesign.ghost.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {

    //查
    List<Order> queryAllOrder();

    Order queryOrderByID(int id);

    Order queryOrderByOrderNo(Long orderNo);

    List<Order> queryOrderByUserId(int userId);

    //增
    int insertOrder(Order order);

    //改
    int updateOrderStatus(int id,int status);

    //删
}
