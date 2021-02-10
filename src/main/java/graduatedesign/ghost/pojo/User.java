package graduatedesign.ghost.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private int uID;//主键
    private int uStatus;//0：健康  1：待观察  2：生病
    private int age;
    private String uName;
    private String uPassword;//加密保存
    private String address;
    private String email;//字段唯一 可用作登陆 通过邮箱验证修改 可用作找回密码
    //private double currentTemperature;
    private long birthday;
    private long phone;//字段唯一 可用作登陆 暂时无法修改
    private long createTime;
    private long updateTime;

    public User(String name, Long phone, String email, String password,int status) {
        this.uName = name;
        this.phone = phone;
        this.email = email;
        this.uPassword = password;
        this.uStatus = status;
    }

    public User(int uID, String uName, int age, long birthday, String address, int status) {
        this.uID = uID;
        this.uName = uName;
        this.age = age;
        this.birthday = birthday;
        this.address = address;
        this.uStatus = status;
    }
}
