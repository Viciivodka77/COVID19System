package graduatedesign.ghost.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface WebInfoMapper {

    int updateNotice(String notice, int adminID);

}
