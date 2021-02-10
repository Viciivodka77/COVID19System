package graduatedesign.ghost.controller;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.pojo.Ticket;
import graduatedesign.ghost.pojo.User;
import graduatedesign.ghost.service.TicketService;
import graduatedesign.ghost.service.UserService;
import graduatedesign.ghost.utils.LoginUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TicketController {

    private static final int pageSize = 10;

    private TicketService ticketService;
    private UserService userService;
    private LoginUtils loginUtils;

    @Autowired
    public TicketController(TicketService ticketService,
                            UserService userService,
                            LoginUtils loginUtils) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.loginUtils = loginUtils;
    }

    @RequestMapping(value = "/ticket")
    public String goToTicket(){
        return "redirect:/ticket/1";
    }

    @RequestMapping(value = "/ticket/{page}")
    public String toTicket(@PathVariable int page,
                          Model model,
                          HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        PageInfo<Ticket> ticketPageInfo = ticketService.queryTicketByUserId(userId, page, pageSize);
        model.addAttribute("ticketPageInfo",ticketPageInfo);
        model.addAttribute("urlString","/order/");
        return "ticket";
    }

    @RequestMapping(value = "/ticket/createNew" ,method = RequestMethod.GET)
    public String toCreateTicket(HttpServletRequest request,
                                 Model model){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        User user = userService.selectUserByUID(userId);
        model.addAttribute("user",user);
        return "ticket_create";
    }

    @RequestMapping(value = "/ticket/createNew" ,method = RequestMethod.POST)
    public String doCreateTicket(@RequestParam("userName") String userName,
                                 @RequestParam("userPhone") long userPhone,
                                 @RequestParam("department") int department,
                                 @RequestParam("subject") String subject,
                                 @RequestParam("content") String content,
                                 HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        Ticket ticket = new Ticket();
        ticket.setTicketNo(IdUtil.getSnowflake(1,1).nextId());
        ticket.setUserId(userId);
        ticket.setUserName(userName);
        ticket.setUserPhone(userPhone);
        ticket.setDepartment(department);
        ticket.setSubject(subject);
        ticket.setContent(content);
        ticket.setStatus(0);
        ticketService.addTicket(ticket);
        return "redirect:/ticket/1";
    }


    @RequestMapping(value = "/ticket/check/{id}")
    public String toCheckTicket(@PathVariable int id,
                                Model model,
                                HttpServletRequest request){
        loginUtils.isLoginStatus(request);
        int userId = loginUtils.getId();
        User user = userService.selectUserByUID(userId);
        Ticket mainTicket = ticketService.queryTicketById(id);
        model.addAttribute("mainTicket",mainTicket);
        List<Ticket> replyTicketList = ticketService.queryTicketReplyByNo(mainTicket.getTicketNo());
        model.addAttribute("replyTicketList",replyTicketList);
        model.addAttribute("user",user);
        return "ticket_check";
    }

    //回复工单
    @RequestMapping(value = "/ticket/reply",method = RequestMethod.POST)
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
        return "redirect:/ticket/1/";
    }


}
