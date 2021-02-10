package graduatedesign.ghost.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.mapper.TicketMapper;
import graduatedesign.ghost.pojo.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketMapper ticketMapper;

    @Autowired
    public TicketServiceImpl(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public int addTicket(Ticket ticket) {
        //是回复
        if (ticket.getParentNo() != 0){
            ticket.setCreateTime(new Date());
            ticket.setUpdateTime(new Date());
            int ticketId = queryTicketIdByNo(ticket.getParentNo());
            Ticket parentTicket = ticketMapper.queryTicketById(ticketId);
            int i = ticketMapper.addTicket(ticket);
            if (parentTicket.getUserId() != ticket.getUserId()){//管理员回复
                updateTicketStatusToRepliedById(ticketId);
            }else { // 用户回复
                updateTicketStatusToWaitById(ticketId);
            }
            return i;
        }else {
            ticket.setCreateTime(new Date());
            ticket.setUpdateTime(new Date());
            return ticketMapper.addTicket(ticket);
        }
    }

    @Override
    public PageInfo<Ticket> queryAllTicket(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Ticket> tickets = ticketMapper.queryAllTicket();
        return new PageInfo<>(tickets,5);
    }

    @Override
    public PageInfo<Ticket> queryTicketByUserId(int userId, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Ticket> tickets = ticketMapper.queryTicketByUserId(userId);
        return new PageInfo<>(tickets,5);
    }

    @Override
    public Ticket queryTicketById(int id) {
        return ticketMapper.queryTicketById(id);
    }

    @Override
    public int queryTicketIdByNo(long ticketNo) {
        return ticketMapper.queryTicketIdByNo(ticketNo);
    }

    @Override
    public List<Ticket> queryTicketReplyByNo(long parentNo) {
        return ticketMapper.queryTicketReplyByNo(parentNo);
    }

    @Override
    public int updateTicketStatusToCloseById(int id) {
        return ticketMapper.updateTicketStatusById(id,2);
    }

    @Override
    public int updateTicketStatusToRepliedById(int id) {
        return ticketMapper.updateTicketStatusById(id,1);
    }

    @Override
    public int updateTicketStatusToWaitById(int id) {
        return ticketMapper.updateTicketStatusById(id,0);
    }
}
