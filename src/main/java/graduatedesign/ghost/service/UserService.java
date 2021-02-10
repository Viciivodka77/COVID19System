package graduatedesign.ghost.service;

import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.pojo.User;


public interface UserService {

    PageInfo<User> selectUserList(int page, int pageSize);
    PageInfo<User> selectUserListByName(String name, int page, int pageSize);
    PageInfo<User> selectUserListByStatus(int uStatus, int page, int pageSize);
    User selectUserByUID(int uID);
    User selectUserByPhone(long phone);
    User selectUserByEmail(String email);
    int addUser(User user);
    int countUser();
    int updateUserBasic(User user);
    int updateUserPwd(int id, String newPwd);
}
