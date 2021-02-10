package graduatedesign.ghost.controller;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.dto.OrderUserDTO;
import graduatedesign.ghost.pojo.*;
import graduatedesign.ghost.service.*;
import graduatedesign.ghost.utils.FileUtils;
import graduatedesign.ghost.utils.LoginUtils;
import graduatedesign.ghost.utils.RoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.events.Event;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final static int pageSize = 10;

    private UserService userService;
    private UserRoleService userRoleService;
    private LoginUtils loginUtils;
    private RoleUtils roleUtils;
    private ClockInService clockInService;
    private WebInfoService webInfoService;
    private ProductService productService;
    private OrderService orderService;
    private TicketService ticketService;
    private FileUtils fileUtils;

    @Autowired
    public AdminController(UserService userService,
                           LoginUtils loginUtils,
                           UserRoleService userRoleService,
                           ClockInService clockInService,
                           WebInfoService webInfoService,
                           ProductService productService,
                           OrderService orderService,
                           TicketService ticketService,
                           RoleUtils roleUtils,
                           FileUtils fileUtils){
        this.userService = userService;
        this.loginUtils = loginUtils;
        this.userRoleService = userRoleService;
        this.roleUtils = roleUtils;
        this.clockInService = clockInService;
        this.webInfoService = webInfoService;
        this.productService = productService;
        this.fileUtils = fileUtils;
        this.orderService = orderService;
        this.ticketService = ticketService;
    }

    @RequestMapping(value = "/1/userList")
    public String redirectPage(){
        return "redirect:/admin/1/userList/1";
    }

    @RequestMapping(value = "/1/userList/{page}")
    public String toAllUsers(@PathVariable("page") int page, Model model){
        //设置一个url方便取用
        String urlString = "/admin/1/userList/";
        model.addAttribute("urlString",urlString);
        int userCount = userService.countUser();
        model.addAttribute("userCount",userCount);
        PageInfo<User> pageInfo = userService.selectUserList(page, pageSize);
        model.addAttribute("pageInfo",pageInfo);

        return "admin_user";
    }
    @RequestMapping(value = "/1/userList/status/{status}/{page}")
    public String toUsersStatus(@PathVariable("page") int page,
                                @PathVariable("status") int uStatus,
                                Model model){
        //设置一个url方便取用
        String urlString = "/admin/1/userList/status/" + uStatus + "/";
        model.addAttribute("urlString",urlString);
        int userCount = userService.countUser();
        model.addAttribute("userCount",userCount);
        //分页
        PageInfo<User> pageInfo = userService.selectUserListByStatus(uStatus, page, pageSize);
        model.addAttribute("pageInfo",pageInfo);
        return "admin_user";
    }


    @RequestMapping(value = "/2/check/{id}/{page}")
    public String toCheck(@PathVariable String id,
                          @PathVariable String page,
                          Model model,
                          HttpServletRequest request){
        //验证登陆
        boolean loginStatus = loginUtils.isLoginStatus(request);
        if (loginStatus){
            //判断是否是自己
            if (Integer.parseInt(id) == loginUtils.getId()){
                return "redirect:/profile";
            }else {
                //不是自己
                User user = userService.selectUserByUID(Integer.parseInt(id));
                Date birthday = new Date(user.getBirthday());
                model.addAttribute("user",user);
                model.addAttribute("birthday",birthday);
                List<Integer> roles = userRoleService.selectUserRoleByUid(user.getUID());
                Integer integer = roles.get(roles.size() - 1);
                String role = roleUtils.transformRoleToController(integer);
                model.addAttribute("role",role);
                //签到信息
                PageInfo<ClockIn> clockInPageInfo = clockInService.querySomeoneClockIn(Integer.parseInt(id), Integer.parseInt(page), 10);
                List<ClockIn> list = clockInPageInfo.getList();
                model.addAttribute("pageInfo",clockInPageInfo);
                model.addAttribute("urlString","/admin/2/check/" + id+"/");
                return "profile_check";
            }
        }else {
            //登陆验证失效
            return "redirect:/login";
        }

    }


    @RequestMapping(value = "/2/upgrade/{id}")
    public String doUpgrade(@PathVariable String id,
                            HttpServletRequest request,
                            Model model){
        //验证登陆
        int userID = Integer.parseInt(id);
        boolean loginStatus = loginUtils.isLoginStatus(request);
        int adminID = loginUtils.getId();
        if (loginStatus){
            List<Integer> adminRole = userRoleService.selectUserRoleByUid(adminID);
            Integer adminMaxRole = adminRole.get(adminRole.size() - 1);
            //判断权限等级是否满足条件
            if (adminMaxRole > 2){//需要二或三级管理员才能提升权限
                //获取提升用户的权限
                List<Integer> userRole = userRoleService.selectUserRoleByUid(userID);
                Integer userMaxRole = adminRole.get(userRole.size() - 1);
                if (userMaxRole < adminMaxRole){//需要该用户的权限比当前管理员的权限小
                    int upgradeRole = userMaxRole + 1;
                    if (upgradeRole >= 4){//4级为最大 且只有一个唯一 不可提升
                        //model.addAttribute("errorMsg","无法再提升权限");
                        return "redirect:/admin/2/check/" + userID;
                    }else {//可以提升
                        UserRole upgradeUserRole = new UserRole();
                        upgradeUserRole.setRID(upgradeRole);
                        upgradeUserRole.setUID(userID);
                        userRoleService.addRole(upgradeUserRole);
                        //model.addAttribute("successMsg","该用户权限已提升!");
                        return "redirect:/admin/2/check/" + userID;
                    }
                }else {
                    //如果该用户的权限比当前管理员的权限相等或者大于则不能提升
                    //model.addAttribute("errorMsg","当前管理员无法提升该用户权限");
                    return "redirect:/admin/2/check/" + userID;
                }
            }else {
                //model.addAttribute("errorMsg","权限不够");
                return "redirect:/admin/2/check/" + userID;
            }
        }else {
            //model.addAttribute("errorMsg","登录失效");
            return "redirect:/login";
        }
    }


    @RequestMapping(value = "/2/degrade/{id}")
    public String doDegrade(@PathVariable String id,
                            HttpServletRequest request,
                            Model model){
        //验证登陆
        int userID = Integer.parseInt(id);
        boolean loginStatus = loginUtils.isLoginStatus(request);
        int adminID = loginUtils.getId();
        if (loginStatus){
            List<Integer> adminRole = userRoleService.selectUserRoleByUid(adminID);
            Integer adminMaxRole = adminRole.get(adminRole.size() - 1);
            //判断权限等级是否满足条件
            if (adminMaxRole > 2){//需要二或三级管理员才能降低权限
                //获取用户的权限
                List<Integer> userRole = userRoleService.selectUserRoleByUid(userID);
                Integer userMaxRole = adminRole.get(userRole.size() - 1);
                if (userMaxRole < adminMaxRole){//需要该用户的权限比当前管理员的权限小
                    if (userMaxRole < 2){//1级为游客 不可降低
                        //model.addAttribute("errorMsg","无法再降低权限");
                        return "redirect:/admin/2/check/" + userID;
                    }else {//可以提升
                        UserRole degradeUserRole = new UserRole();
                        degradeUserRole.setRID(userMaxRole);
                        degradeUserRole.setUID(userID);
                        userRoleService.deleteRole(degradeUserRole);
                        //model.addAttribute("successMsg","该用户权限已降低!");
                        return "redirect:/admin/2/check/" + userID;
                    }
                }else {
                    //如果该用户的权限比当前管理员的权限相等或者大于则不能降低
                    //model.addAttribute("errorMsg","当前管理员无法降低该用户权限");
                    return "redirect:/admin/2/check/" + userID;
                }
            }else {
                //model.addAttribute("errorMsg","权限不够");
                return "redirect:/admin/2/check/" + userID;
            }
        }else {
            //model.addAttribute("errorMsg","登录失效");
            return "redirect:/login";
        }
    }


    @RequestMapping(value = "/2/other")
    public String toOtherOperate(){
        return "admin_other";
    }


    /**
     * 修改公告   存入数据库
     * @param notice
     * @return
     */
    @RequestMapping(value = "/2/other/notice",method = RequestMethod.POST)
    public String doNotice(@RequestParam("notice") String notice,
                           HttpServletRequest request,
                           Model model){
        //验证登陆
        boolean loginStatus = loginUtils.isLoginStatus(request);
        int adminID = loginUtils.getId();
        if (loginStatus) {
            List<Integer> adminRole = userRoleService.selectUserRoleByUid(adminID);
            Integer adminMaxRole = adminRole.get(adminRole.size() - 1);
            //判断权限等级是否满足条件
            if (adminMaxRole > 2) {//需要 二级管理员才能修改
                //提交notice
                webInfoService.updateNotice(notice,adminID);
            }else {//权限不够
                model.addAttribute("msg","验证权限失败,权限不足!");
            }
        }else {
            model.addAttribute("msg","验证身份失败,请重新登陆!");
        }
        return "redirect:/admin/2/other";
    }


    @RequestMapping(value = "/1/productList",method = RequestMethod.GET)
    public String redirectProduct(){
        return "redirect:/admin/1/product/1";
    }

    @RequestMapping(value = "/1/product/{page}",method = RequestMethod.GET)
    public String toProduct(@PathVariable String page,
                            Model model){
        PageInfo<Product> productPageInfo = productService.queryAllProduct(Integer.parseInt(page), pageSize);
        model.addAttribute("urlString","/admin/1/product/");
        model.addAttribute("pageInfo",productPageInfo);
        return "admin_product";
    }

    @RequestMapping(value = "/1/product/add",method = RequestMethod.GET)
    public String toAddProduct(Model model){
        model.addAttribute("type","add");
        model.addAttribute("dbProduct", "null");
        return "admin_product_edit";
    }

    @RequestMapping(value = "/1/product/add",method = RequestMethod.POST)
    public String doAddProduct(@RequestParam("name") String name,
                               @RequestParam("details")String details,
                               @RequestParam("stock") int stock,
                               @RequestParam("price") BigDecimal price,
                               @RequestParam("img") MultipartFile img,
                               @RequestParam("id") int id,
                               Model model) throws Exception {
        Map<String, String> map = null;
        if (!img.isEmpty()){
            map = fileUtils.saveImg(img);
        }

        //验证id是否存在或者为空 判断是修改 还是新增
        Product dbProduct = productService.queryProductById(id);
        if (dbProduct != null){ //修改
            Product newProduct = new Product();
            if (map != null){
                newProduct.setImgPath(map.get("realPath"));
            }else {
                newProduct.setImgPath(dbProduct.getImgPath());
            }
            newProduct.setId(id);
            newProduct.setName(name);
            newProduct.setDetails(details);
            newProduct.setStock(stock);
            newProduct.setPrice(price);
            //提交数据库
            productService.updateProduct(newProduct);
            return "redirect:/admin/1/product/check/"+id;
        }else { //新增
            Product product = new Product();
            if (map != null){
                product.setImgPath(map.get("realPath"));
            }else {
                throw new Exception("未提交照片");
            }
            product.setName(name);
            product.setDetails(details);
            product.setStock(stock);
            product.setPrice(price);
            //存入数据库
            productService.addProduct(product);
            return "redirect:/admin/1/productList";
        }


    }

    @RequestMapping(value = "/1/product/check/{id}",method = RequestMethod.GET)
    public String toCheck(@PathVariable String id,
                          Model model){
        Product dbProduct = productService.queryProductById(Integer.parseInt(id));
        model.addAttribute("dbProduct",dbProduct);
        String imgPath = dbProduct.getImgPath();
        String fileName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        model.addAttribute("fileName", fileName);
        return "admin_product_check";
    }

    @RequestMapping(value = "/1/product/edit/{id}",method = RequestMethod.GET)
    public String toEdit(@PathVariable String id,
                         Model model){
        model.addAttribute("type","edit");
        Product dbProduct = productService.queryProductById(Integer.parseInt(id));
        model.addAttribute("dbProduct",dbProduct);
        String imgPath = dbProduct.getImgPath();
        String fileName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        model.addAttribute("fileName", fileName);
        return "admin_product_edit";
    }

    //order页面

    @RequestMapping(value = "/1/order")
    public String goToOrder(){
        return "redirect:/admin/1/order/1";
    }

    @RequestMapping(value = "/1/order/{page}")
    public String toOrder(@PathVariable int page,
                          Model model){
        PageInfo<OrderUserDTO> orderPageInfo = orderService.queryAllOrder(page, pageSize);
        model.addAttribute("orderPageInfo",orderPageInfo);
        model.addAttribute("urlString","/admin/1/order/");
        return "admin_order";
    }

    @RequestMapping(value = "/1/order/finish/{orderId}")
    public String finishOrder(@PathVariable(value = "orderId") int orderId){
        orderService.updateOrderStatus(orderId,1); // 完成订单
        return "redirect:/admin/1/order/1";
    }

    @RequestMapping(value = "/1/order/cancel/{orderId}")
    public String cancelOrder(@PathVariable(value = "orderId") int orderId){
        orderService.updateOrderStatus(orderId,2); // 取消订单
        return "redirect:/admin/1/order/1";
    }


    //查询所有工单
    @RequestMapping(value = "/1/ticket/{page}")
    public String toTicket(@PathVariable int page,
                           Model model){
        PageInfo<Ticket> ticketPageInfo = ticketService.queryAllTicket(page, pageSize);
        model.addAttribute("ticketPageInfo",ticketPageInfo);
        model.addAttribute("urlString","/admin/1/ticket/");
        return "admin_ticket";
    }

    //关闭工单
    @RequestMapping(value = "/1/ticket/close/{id}")
    public String toCloseTicket(@PathVariable int id){
        ticketService.updateTicketStatusToCloseById(id);
        return "redirect:/admin/1/ticket/1";
    }

    //查看工单详情
    @RequestMapping(value = "/1/ticket/check/{id}")
    public String toCheckTicket(@PathVariable int id,
                                Model model,
                                HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        User user = userService.selectUserByUID(userId);
        Ticket ticket = ticketService.queryTicketById(id);
        List<Ticket> replyTicketList = ticketService.queryTicketReplyByNo(ticket.getTicketNo());
        model.addAttribute("replyTicketList",replyTicketList);
        model.addAttribute("mainTicket",ticket);
        model.addAttribute("user",user);
        return "admin_ticket_check";
    }

    //管理员回复工单
    @RequestMapping(value = "/1/ticket/reply",method = RequestMethod.POST)
    public String doReplyTicket(@RequestParam("mainId") String mainId,
                                @RequestParam("userName") String userName,
                                @RequestParam("userPhone") String userPhone,
                                @RequestParam("content") String content,
                                HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        Ticket mainTicket = ticketService.queryTicketById(Integer.parseInt(mainId));
        Ticket replyTicket = new Ticket();
        replyTicket.setParentNo(mainTicket.getTicketNo());
        replyTicket.setTicketNo(IdUtil.getSnowflake(1,1).nextId());
        replyTicket.setUserId(userId);
        replyTicket.setUserName(userName);
        replyTicket.setUserPhone(Long.parseLong(userPhone));
        replyTicket.setContent(content);
        ticketService.addTicket(replyTicket);
        return "redirect:/admin/1/ticket/1/";
    }

}
