package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockIn {
    private int id;
    private int uID;//用户id
    private float temperature;
    /*
     * 0:良好
     * 1:不舒适
     * 2:难受
     * */
    private int bmStatus;
    private Date time;
}
