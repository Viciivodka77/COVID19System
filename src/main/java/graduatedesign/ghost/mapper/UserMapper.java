package graduatedesign.ghost.mapper;

import graduatedesign.ghost.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    List<User> selectUserList();

    List<User> selectUserListByName(String uName);

    List<User> selectUserListByStatus(int uStatus);

    User selectUserById(int uID);

    User selectUserByPhone(long phone);

    User selectUserByEmail(String email);

    int addUser(User user);

    int updateUser(User user);

    int updateUserBasic(User user);

    int deleteUser(int uID);

    int countUser();

    int updateUserPwd(User user);
}
