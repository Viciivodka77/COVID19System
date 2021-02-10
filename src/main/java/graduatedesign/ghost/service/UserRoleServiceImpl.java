package graduatedesign.ghost.service;

import graduatedesign.ghost.mapper.UserRoleMapper;
import graduatedesign.ghost.pojo.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private UserRoleMapper mapper;

    public UserRoleServiceImpl(UserRoleMapper mapper){
        this.mapper = mapper;
    }

    @Override
    public List<Integer> selectUserRoleByUid(int uID) {
        return mapper.selectUserRoleByUid(uID);
    }

    @Override
    public int addRole(UserRole userRole){
        return mapper.addUserRole(userRole);
    }

    @Override
    public int deleteRole(UserRole userRole) {
        return mapper.deleteUserRole(userRole);
    }

}
