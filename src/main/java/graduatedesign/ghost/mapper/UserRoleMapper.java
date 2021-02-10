package graduatedesign.ghost.mapper;

import graduatedesign.ghost.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRoleMapper {

    List<Integer> selectUserRoleByUid(int uID);

    int addUserRole(UserRole userRole);

    int deleteUserRole(UserRole userRole);
}
