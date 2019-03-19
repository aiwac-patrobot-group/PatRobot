package com.aiwac.cilentapp.patrobot.bean;

/**     实体对象
 * Created by luwang on 2017/10/17.
 */

public class User extends BaseEntity{

    private Integer id;
    private Boolean isRegister;
    private String number;
    private String password;



    public User(){}

    public User(String number){
        this.number = number;
    }

    public User(String number, String password){
        this.number = number;
        this.password = password;
    }

    public User(int id, String number){
        this.id = id;
        this.number = number;
    }
    public User(int id, String number,String password){
        this.id = id;
        this.password=password;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRegister() {
        return isRegister;
    }

    public void setRegister(Boolean register) {
        isRegister = register;
    }

    @Override
    public String toString() {
        return "User["+ id + ", "  + number + ", "  + password +"]";
    }
}
