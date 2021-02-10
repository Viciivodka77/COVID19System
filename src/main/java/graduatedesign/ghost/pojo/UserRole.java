package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRole {
    int id;//主键
    int uID;//用户id
    int rID;//角色id
    /*
    *  rID: 1 -> 普通用户
    *  rID: 2 -> 普通管理员 ->进行用户服务操作等..
    *  rID: 3 -> 高级管理员 ->和超级管理员无区别（可撤销）
    *  rID: 4 -> 超级管理员 ->不可撤销 且 只有一个
    *  */
}
