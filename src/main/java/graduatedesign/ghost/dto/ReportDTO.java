package graduatedesign.ghost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private String generalRemark;	//全国疫情信息概览
    private String remark1;	//注释内容，X为1~5
    private String remark2;	//注释内容，X为1~5
    private String remark3;	//注释内容，X为1~5
    private String note1;//	病毒名称
    private String note2;//	传染源
    private String note3;//	传播途径
    private int currentConfirmedCount;//(Incr)	现存确诊人数（较昨日增加数量）值为confirmedCount(Incr) - curedCount(Incr) - deadCount(Incr)
    private int currentConfirmedCountIncr;
    private int confirmedCount;//(Incr)	累计确诊人数（较昨日增加数量）
    private int confirmedCountIncr;
    private int suspectedCount;//(Incr)	疑似感染人数（较昨日增加数量）
    private int suspectedCountIncr;
    private int curedCount;//(Incr)	治愈人数（较昨日增加数量）
    private int curedCountIncr;
    private int deadCount;//(Incr)	死亡人数（较昨日增加数量）
    private int deadCountIncr;
    private int seriousCount;//()	重症病例人数（较昨日增加数量）
    private int seriousCountIncr;
    private long updateTime	;//数据最后变动时间
}
