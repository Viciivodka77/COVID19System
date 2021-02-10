package graduatedesign.ghost.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.dto.OrderUserDTO;
import graduatedesign.ghost.mapper.OrderMapper;
import graduatedesign.ghost.mapper.UserMapper;
import graduatedesign.ghost.pojo.Order;
import graduatedesign.ghost.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderMapper orderMapper;
    private UserMapper userMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper,
                            UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.userMapper = userMapper;
    }

    @Override
    public PageInfo<OrderUserDTO> queryAllOrder(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Order> orders = orderMapper.queryAllOrder();
        List<OrderUserDTO> orderUserDTOList = new ArrayList<>();
        for (Order order : orders) {
            OrderUserDTO orderUserDTO = new OrderUserDTO();
            Order next = order;
            orderUserDTO.setId(next.getId());
            orderUserDTO.setUserId(next.getId());
            orderUserDTO.setOrderNo(next.getOrderNo());
            orderUserDTO.setOrderDetail(next.getOrderDetail());
            orderUserDTO.setOrderPrice(next.getOrderPrice());
            orderUserDTO.setOrderStatus(next.getOrderStatus());
            orderUserDTO.setCreateTime(next.getCreateTime());
            orderUserDTO.setUpdateTime(next.getUpdateTime());
            User user = userMapper.selectUserById(next.getUserId());
            orderUserDTO.setUserName(user.getUName());
            orderUserDTO.setUserAddress(user.getAddress());
            orderUserDTO.setUserPhone(user.getPhone());
            orderUserDTOList.add(orderUserDTO);
        }
        return new PageInfo<>(orderUserDTOList,5);
    }

    @Override
    public PageInfo<Order> queryOrderByUserId(int userId,int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Order> orders = orderMapper.queryOrderByUserId(userId);
        return new PageInfo<>(orders,5);
    }

    @Override
    public Order queryOrderByID(int id) {
        return orderMapper.queryOrderByID(id);
    }

    @Override
    public Order queryOrderByOrderNo(Long orderNo) {
        return orderMapper.queryOrderByOrderNo(orderNo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    public int insertOrder(Order order) {
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        return orderMapper.insertOrder(order);
    }

    @Override
    public int updateOrderStatus(int id, int status) {
        return orderMapper.updateOrderStatus(id,status);
    }
}
