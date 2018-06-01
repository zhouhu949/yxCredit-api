package com.api.model.credit;



import java.io.Serializable;

/**
 * 征信请求类
 * @author luochaofang
 */
public class CreditRequest implements Serializable {

    /**  征信账号 */
    private String account;

    /**  征信密码 */
    private String password;

    /**  验证码 */
    private String smsCode;

    /** 校验登录 */
    private String token;

    /** 客户id */
    private String customerId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
