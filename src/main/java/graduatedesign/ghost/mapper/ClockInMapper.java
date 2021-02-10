package graduatedesign.ghost.mapper;

import graduatedesign.ghost.pojo.ClockIn;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ClockInMapper {

    List<ClockIn> queryAllClockIn();
    List<ClockIn> querySomeoneClockIn(int uID);
    ClockIn querySomeoneNewOneClockIn(int uID);
    int addClockIn(ClockIn clockIn);

}
