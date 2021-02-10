package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    private int id; // not null auto_increase
    private long ticketNo; // null
    private long parentNo; // 新工单为空 回复则为工单的ticket
    private int userId; // not null
    private String userName; // not null
    private long userPhone; // not null
    private String subject;// null
    private String content;//not null
    private int department; // 0:其他 1:销售 2:健康 3:维修  //null
    private int status; // 0:待回复 1:已回复 2:关闭  // null
    private Date createTime;//not null
    private Date updateTime;//not null


}
