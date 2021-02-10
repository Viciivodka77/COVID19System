package graduatedesign.ghost.mapper;

import graduatedesign.ghost.pojo.Ticket;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TicketMapper {

    //查工单
    List<Ticket> queryAllTicket();
    List<Ticket> queryTicketByUserId(int id);
    Ticket queryTicketById(int id);
    int queryTicketIdByNo(long ticketNo);
    //查回复
    List<Ticket> queryTicketReplyByNo(long parentNo);

    //增
    int addTicket(Ticket ticket);
    //改
    int updateTicketStatusById(int id,int status);

}
