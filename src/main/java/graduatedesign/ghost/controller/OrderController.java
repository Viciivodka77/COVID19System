package graduatedesign.ghost.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.dto.ProductCarDTO;
import graduatedesign.ghost.pojo.Order;
import graduatedesign.ghost.pojo.Product;
import graduatedesign.ghost.pojo.ShoppingCart;
import graduatedesign.ghost.service.OrderService;
import graduatedesign.ghost.service.ProductService;
import graduatedesign.ghost.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    static final private int pageSize = 10;

    private OrderService orderService;
    private ProductService productService;
    private LoginUtils loginUtils;
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    public OrderController(OrderService orderService,
                           ProductService productService,
                           LoginUtils loginUtils,
                           StringRedisTemplate stringRedisTemplate) {
        this.orderService = orderService;
        this.loginUtils = loginUtils;
        this.stringRedisTemplate = stringRedisTemplate;
        this.productService = productService;
    }

    @RequestMapping(value = "/order")
    public String goToOrder(){
        return "redirect:/order/1";
    }

    @RequestMapping(value = "/order/{page}")
    public String toOrder(@PathVariable("page") int page,
                          Model model,
                          HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        //查询order 分页
        PageInfo<Order> orderPageInfo = orderService.queryOrderByUserId(userId,page, pageSize);
        model.addAttribute("orderPageInfo",orderPageInfo);
        model.addAttribute("urlString","/order/");
        return "order";
    }

    @RequestMapping(value = "/order/setOneOrder/{userId}/{productId}")
    public String setOneOrder(@PathVariable int userId,
                              @PathVariable int productId){
        Order order = new Order();
        order.setUserId(userId);
        Product product = productService.queryProductById(productId);
        order.setOrderPrice(product.getPrice());
        order.setOrderDetail(product.getId() + ":" + product.getName() + " X 1 份");
        order.setOrderNo(IdUtil.getSnowflake(1, 1).nextId());
        order.setOrderStatus(0);
        orderService.insertOrder(order);
        return "redirect:/order";
    }

    @RequestMapping(value = "/order/setCartOrder/{userId}")
    public String setCartOrder(@PathVariable int userId){
        ShoppingCart cart = getShoppingCartByUserId(userId);
        Order order = new Order();
        order.setOrderNo(IdUtil.getSnowflake(1, 1).nextId());
        order.setUserId(userId);
        order.setOrderDetail(getOrderDetail(cart));
        order.setOrderPrice(cart.calTotalPrice());
        order.setOrderStatus(0);
        orderService.insertOrder(order);
        //清空购物车
        addCartToRedis(new ShoppingCart(),userId);
        return "redirect:/order";
    }


    /**
     * 购物车订单
     * @param cart  当前用户购物车
     * @return 订单的详情
     */
    public String getOrderDetail(ShoppingCart cart){
        StringBuilder stringBuilder = new StringBuilder();
        List<ProductCarDTO> productCarDTOList = cart.getProductCarDTOList();
        for (ProductCarDTO productCarDTO : productCarDTOList) {
            stringBuilder.append("productID:").append(productCarDTO.getProductId());
            stringBuilder.append(":").append(productCarDTO.getName());
            stringBuilder.append(" X ").append(productCarDTO.getAmount()).append("份");
            stringBuilder.append(" // ");
        }
        return stringBuilder.toString();
    }

    /**
     *  尝试从redis中获取购物车列表
     * @return cart
     */
    private ShoppingCart getShoppingCartByUserId(int userId){
        //存在商品 尝试从redis中获取购物车
        ShoppingCart cart = null;
        String s = stringRedisTemplate.opsForValue().get("carts:" + userId);
        if (!"".equals(s) && JSONUtil.isJson(s)){ //存在购物车
            JSON parse = JSONUtil.parse(s, JSONConfig.create());
            cart = parse.toBean(ShoppingCart.class);
        }else { //不存在 新建购物车
            cart = new ShoppingCart();
        }
        return cart ;
    }


    /**
     *  把购物车转换成json加入redis中
     * @param cart
     * @param userId
     */
    private void addCartToRedis(ShoppingCart cart,int userId){
        JSON parse = JSONUtil.parse(cart);
        String stringCart = parse.toString();
        stringRedisTemplate.opsForValue().set("carts:" + userId,stringCart);
    }

}
