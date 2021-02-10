package graduatedesign.ghost.service;

import graduatedesign.ghost.mapper.UserMapper;
import graduatedesign.ghost.myEnum.Role;
import graduatedesign.ghost.pojo.User;
import graduatedesign.ghost.utils.RoleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private UserMapper userMapper;

    private UserRoleService userRoleService;
    private RoleUtils roleUtils;

    @Autowired
    public CustomUserDetailsService(UserMapper userMapper,UserRoleService userRoleService,RoleUtils roleUtils){
        this.userMapper = userMapper;
        this.userRoleService = userRoleService;
        this.roleUtils = roleUtils;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //1 查询用户
        //判断是邮箱登陆还是手机号登陆
        User user = null;
        if (s.contains("@")){
            user = userMapper.selectUserByEmail(s);
        }else {
            user = userMapper.selectUserByPhone(Long.parseLong(s));
        }

        if (user == null){
            //这里可以用日记记录 抛出异常
            throw new UsernameNotFoundException("User" + s +"was not found in db");
        }
        //2 设置角色
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        List<Integer> integers = userRoleService.selectUserRoleByUid(user.getUID());
        //得到的是多个角色 所以需要循环加入grantedAuthorities
        for (Integer integer : integers) {
            Role role = roleUtils.transformRoleToSecurity(integer);
            //当我们不使用ROLE_做前缀时，会报
            //spring security There was an unexpected error (type=Forbidden, status=403).
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            //String authority = grantedAuthority.getAuthority();
            //System.out.println(authority);
            grantedAuthorities.add(grantedAuthority);
        }

        return new org.springframework.security.core.userdetails.User(s,user.getUPassword(),grantedAuthorities);
    }




}
