package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebInfo {
    private int id;
    private String notice;
    private String footer;
    private String logo;
    private String title;
    private String updateTime;
    private int submittedBy;
}
