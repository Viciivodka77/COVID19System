package graduatedesign.ghost.service;

import graduatedesign.ghost.pojo.UserRole;

import java.util.List;

public interface UserRoleService {

    List<Integer> selectUserRoleByUid(int uID);
    int addRole(UserRole userRole);

    int deleteRole(UserRole userRole);
}
