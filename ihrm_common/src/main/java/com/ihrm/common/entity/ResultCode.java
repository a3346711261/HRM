package com.ihrm.common.entity;

/**
 * 公共的返回码
 *    返回码code：
 *      成功：10000
 *      失败：10001
 *      未登录：10002
 *      未授权：10003
 *      抛出异常：99999
 */
public enum ResultCode {

    /**
     * 公共的返回码
     */

    SUCCESS(true,10000,"操作成功！"),
    //---系统错误返回码-----
    FAIL(false,10001,"操作失败"),
    UNAUTHENTICATED(false,10002,"您还未登录"),
    UNAUTHORISE(false,10003,"权限不足"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！"),

    //---用户操作返回码  2xxxx----already
    MOBILEAIREADY(false,20005,"用户名已存在"),
    AUTOCODEOUTOF(false,20004,"短信验证码已用完"),
    AUTOCODEERROR(false,20003,"验证码错误"),
    MOBILEORPASSWORDERROR(false,20002,"用户名或密码错误"),
    MOBILEPASSWORDFORMATERROR(false,2001,"用户名或密码格式错误");



    //---用户操作返回码  3xxxx----
    //---权限操作返回码----
    //---其他操作返回码----

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;

    ResultCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
