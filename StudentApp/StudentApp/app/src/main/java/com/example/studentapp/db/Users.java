package com.example.studentapp.db;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.paperdb.Paper;

public class Users {

    @SerializedName("id")
    private Integer id;
    @SerializedName("login")
    private String login;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("matchingPassword")
    private String matchingPassword;
    @SerializedName("updateDbTime")
    private String updateDbTime;
    public Users(Integer id, String login) {
        this.id = id;
        this.login = login;
    }

    public Users(Integer id, String login, String email, String password) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    public String getUpdateDbTime() {
        return updateDbTime;
    }

    public void setUpdateDbTime(String updateDbTime) {
        this.updateDbTime = updateDbTime;
    }
    public void currentUpdateDbTime() {

        Calendar cal = new GregorianCalendar();
        String time = "" + cal.get(Calendar.YEAR)+
                "-" +  checkDateFor0(cal.get(Calendar.MONTH)+1)+
                "-" +  checkDateFor0(cal.get(Calendar.DATE))+
                "-" +  checkDateFor0(cal.get(Calendar.HOUR_OF_DAY))+
                "-" +  checkDateFor0(cal.get(Calendar.MINUTE));
        this.updateDbTime = time;
        writeUser(this);
    }
    private String checkDateFor0(int figure){
        return figure < 10 ? "0" + figure : "" + figure;
    }

    public Users(Integer id, String login, String email, String password, String matchingPassword) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.password = password;
        this.matchingPassword = matchingPassword;
    }

    public Users(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static void writeUser(Users user){
        Paper.book("user").write("user", user);
    }

    public static void deleteUser(){
        Paper.book("user").delete("user");
    }

    public static Users getUser(){
        return Paper.book("user").read("user");
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
