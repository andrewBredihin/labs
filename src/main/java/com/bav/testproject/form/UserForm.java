package com.bav.testproject.form;

public class UserForm {

    private String username;
    private String email;
    private String password;
    private String passwordConfirm;
    private String[] authorities = new String[]{"ROLE_USER"};

    public UserForm(){}
    public UserForm(String username, String email, String password, String passwordConfirm){
        this.username = username;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public boolean checkPasswordConfirm() {
        if (password == passwordConfirm || password.equals(passwordConfirm))
            return true;
        return false;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
}
