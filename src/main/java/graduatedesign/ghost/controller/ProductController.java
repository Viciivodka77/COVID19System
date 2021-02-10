package graduatedesign.ghost.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.dto.ProductCarDTO;
import graduatedesign.ghost.pojo.Product;
import graduatedesign.ghost.pojo.ShoppingCart;
import graduatedesign.ghost.service.ProductService;
import graduatedesign.ghost.utils.LoginUtils;
import javafx.scene.chart.ValueAxis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@Slf4j
public class ProductController {

    static final private int pageSize = 9;

    private ProductService productService;
    private StringRedisTemplate stringRedisTemplate;
    private LoginUtils loginUtils;

    @Autowired
    public ProductController(ProductService productService,
                             StringRedisTemplate stringRedisTemplate,
                             LoginUtils loginUtils) {
        this.productService = productService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.loginUtils = loginUtils;
    }

    @RequestMapping("/products")
    public String goToProducts(){
        return "redirect:/products/1";
    }

    @RequestMapping("/products/{page}")
    public String toProducts(@PathVariable(value = "page") int page,
                             Model model){
        PageInfo<Product> productPageInfo = productService.queryAllProduct(page, pageSize);
        model.addAttribute("productPageInfo",productPageInfo);
        model.addAttribute("urlString","/products/");
        return "products";
    }
    //查看商品详情内容
    @RequestMapping(value = "/products/check/{id}")
    public String toCheckProduct(@PathVariable(value = "id") String id,
                                 Model model){
        Product dbProduct = productService.queryProductById(Integer.parseInt(id));
        model.addAttribute("dbProduct",dbProduct);
        String imgPath = dbProduct.getImgPath();
        String fileName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        model.addAttribute("fileName", fileName);
        return "products_check";
    }

    //把商品添加至购物车中
    @RequestMapping(value = "/products/addToCart/{productId}")
    public String addToShoppingCart(@PathVariable String productId,
                                    HttpServletRequest request) throws Exception {
        //获取用户id
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        //判断商品id是否存在
        Product dbProduct = productService.queryProductById(Integer.parseInt(productId));
        if (dbProduct == null){
            throw new Exception("商品不存在!");
        }
        //存在商品 尝试从redis中获取购物车
        ShoppingCart cart = getShoppingCartByUserId(userId);

        //创建dto
        ProductCarDTO newProduct = new ProductCarDTO();
        newProduct.setProductId(dbProduct.getId());
        newProduct.setAmount(1);
        newProduct.setImgPath(dbProduct.getImgPath());
        newProduct.setPrice(dbProduct.getPrice());
        newProduct.setName(dbProduct.getName());
        //加入购物车
        cart.addProducts(newProduct);
        //加入redis
        //json
        addCartToRedis(cart,userId);
        //转跳至购物车页面
        return "redirect:/products/cart";
    }

    @RequestMapping(value = "/products/cart")
    public String toCart(HttpServletRequest request,
                         Model model){
        //获取userId
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        //判断redis中是否有购物车
        ShoppingCart cart = getShoppingCartByUserId(userId);

        if (cart != null){
            model.addAttribute("status","true");
            model.addAttribute("cart",cart);
        }else {
            model.addAttribute("status","false");
            model.addAttribute("cart",null);
        }
        return "products_cart";
    }

    //删除购物车中商品
    @RequestMapping(value = "/products/deleteCartProducts/{id}")
    public String deleteCartProducts(@PathVariable(value = "id") int id,
                                     HttpServletRequest request) throws Exception {
        //获取用户id
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        //判断商品id是否存在
        Product dbProduct = productService.queryProductById(id);
        if (dbProduct == null){
            throw new Exception("商品不存在!");
        }
        ShoppingCart cart = getShoppingCartByUserId(userId);
        if (ObjectUtil.isEmpty(cart)){
            throw new Exception("购物车已经为空！");
        }
        cart.deleteProducts(id);
        //重新放入redis
        addCartToRedis(cart,userId);
        return "redirect:/products/cart";
    }

    //增加购物车中商品数量
    @RequestMapping(value = "/products/addCartProducts/{id}")
    public String addCartProducts(@PathVariable(value = "id") int id,
                                     HttpServletRequest request) throws Exception {
        //获取用户id
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        //判断商品id是否存在
        Product dbProduct = productService.queryProductById(id);
        if (dbProduct == null){
            throw new Exception("商品不存在!");
        }
        ShoppingCart cart = getShoppingCartByUserId(userId);
        if (ObjectUtil.isEmpty(cart)){
            throw new Exception("购物车已经为空！");
        }
        cart.addOneProducts(id);
        //重新放入redis
        addCartToRedis(cart,userId);
        return "redirect:/products/cart";
    }

    //减少购物车商品数量
    @RequestMapping(value = "/products/reduceCartProducts/{id}")
    public String reduceCartProducts(@PathVariable(value = "id") int id,
                                  HttpServletRequest request) throws Exception {
        //获取用户id
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        //判断商品id是否存在
        Product dbProduct = productService.queryProductById(id);
        if (dbProduct == null){
            throw new Exception("商品不存在!");
        }
        ShoppingCart cart = getShoppingCartByUserId(userId);
        if (ObjectUtil.isEmpty(cart)){
            throw new Exception("购物车已经为空！");
        }
        cart.reduceOneProducts(id);
        //重新放入redis
        addCartToRedis(cart,userId);
        return "redirect:/products/cart";
    }

    //购物车下单
    @RequestMapping(value = "/products/toCartOrder")
    public String toCartOrder(HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        return "redirect:/order/setCartOrder/"+userId ;
    }

    //直接下单
    @RequestMapping(value = "/products/toOneOrder/{productId}")
    public String toOneOrder(@PathVariable String productId,
                             HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        return "redirect:/order/setOneOrder/" + userId + "/" + productId;
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
