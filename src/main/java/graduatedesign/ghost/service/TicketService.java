package graduatedesign.ghost.service;

import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.pojo.Ticket;

import java.util.List;

public interface TicketService {

    int addTicket(Ticket ticket);

    PageInfo<Ticket> queryAllTicket(int page,int pageSize);
    PageInfo<Ticket> queryTicketByUserId(int userId,int page,int pageSize);
    Ticket queryTicketById(int id);
    int queryTicketIdByNo(long ticketNo);

    //回复
    List<Ticket> queryTicketReplyByNo(long parentNo);


    //改
    int updateTicketStatusToCloseById(int id);
    int updateTicketStatusToRepliedById(int id);
    int updateTicketStatusToWaitById(int id);

}
