package graduatedesign.ghost.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import graduatedesign.ghost.mapper.UserMapper;
import graduatedesign.ghost.mapper.UserRoleMapper;
import graduatedesign.ghost.pojo.User;
import graduatedesign.ghost.pojo.UserRole;
import graduatedesign.ghost.utils.EncoderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private UserMapper mapper;
    private UserRoleMapper userRoleMapper;
    private DataSourceTransactionManager transactionManager;
    private EncoderUtils encoderUtils;

    @Autowired
    public UserServiceImpl(DataSourceTransactionManager transactionManager,
                           UserMapper mapper,
                           UserRoleMapper userRoleMapper,
                           EncoderUtils encoderUtils){
        this.transactionManager = transactionManager;
        this.mapper = mapper;
        this.userRoleMapper = userRoleMapper;
        this.encoderUtils = encoderUtils;
    }

    @Override
    public PageInfo<User> selectUserList(int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<User> userList = mapper.selectUserList();
        return new PageInfo<>(userList,5);
    }

    @Override
    public PageInfo<User> selectUserListByName(String name, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<User> userList = mapper.selectUserListByName(name);

        return new PageInfo<>(userList,5);
    }

    @Override
    public PageInfo<User> selectUserListByStatus(int uStatus, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<User> userList = mapper.selectUserListByStatus(uStatus);
        return new PageInfo<>(userList,5);
    }

    @Override
    public User selectUserByUID(int uID) {
        return mapper.selectUserById(uID);
    }

    @Override
    public User selectUserByPhone(long phone) {
        return mapper.selectUserByPhone(phone);
    }

    @Override
    public User selectUserByEmail(String email) {
        return mapper.selectUserByEmail(email);
    }

    @Override
    public int addUser(User user) {
        //配置事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("register-transaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //设置状态点
        TransactionStatus status = transactionManager.getTransaction(def);
        int i = 0;
        try {
            //添加用户数据
            long createTime = System.currentTimeMillis();
            user.setCreateTime(createTime);
            user.setUpdateTime(createTime);
            mapper.addUser(user);
            //设置权限
            User userForID = selectUserByPhone(user.getPhone());
            UserRole userRole = new UserRole();
            userRole.setUID(userForID.getUID());
            //添加基础用户权限
            userRole.setRID(1);
            i = userRoleMapper.addUserRole(userRole);
            //手动提交
            transactionManager.commit(status);
        }catch (Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
        }
        return i;
    }

    @Override
    public int countUser() {
        return mapper.countUser();
    }

    @Override
    public int updateUserBasic(User user) {
        user.setUpdateTime(System.currentTimeMillis());
        return mapper.updateUserBasic(user);
    }

    @Override
    public int updateUserPwd(int id, String newPwd) {
        User user = new User();
        user.setUID(id);
        String encodePwd = encoderUtils.passwordEncoder().encode(newPwd);
        user.setUPassword(encodePwd);
        user.setUpdateTime(System.currentTimeMillis());
        return mapper.updateUserPwd(user);
    }


}
