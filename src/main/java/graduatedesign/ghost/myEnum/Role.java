package graduatedesign.ghost.myEnum;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum Role {
    /*
     *  rID: 1 -> 普通用户
     *  rID: 2 -> 普通管理员 ->进行用户服务操作等..
     *  rID: 3 -> 高级管理员 ->和超级管理员无区别（可撤销）
     *  rID: 4 -> 超级管理员 ->不可撤销 且 只有一个
     *  */
    ROLE_USELESS("ROLE_USELESS",0),
    ROLE_USER("ROLE_USER",1),
    ROLE_ADMIN1("ROLE_ADMIN1",2),
    ROLE_ADMIN2("ROLE_ADMIN2",3),
    ROLE_ADMIN3("ROLE_ADMIN3",4);


    private String name;
    private int index;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
