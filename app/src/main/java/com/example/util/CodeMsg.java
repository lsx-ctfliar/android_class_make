package com.example.util;

public class CodeMsg {
    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(200, "success");
    public static CodeMsg Login_ERROR = new CodeMsg(500, "当前登录用户不存在");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg ACCESS_LIMIT_REACHED= new CodeMsg(500104, "访问高峰期，请稍等！");
    //登录模块 5002XX
    public static CodeMsg LOGIN_ERROR = new CodeMsg(5002001, "账号或者密码错误");
    public static CodeMsg ACCOUNT_LOGOUT = new CodeMsg(5002002, "账号已经注销");
    public static CodeMsg REGISTER_ERROR = new CodeMsg(5002003, "注册账号失败");
    public static CodeMsg LOGIN_ID_ERROR = new CodeMsg(5002004, "此id不存在失败");
    public static CodeMsg UPDATE_INFO_ERROR = new CodeMsg(5002005, "修改用户信息失败");







    private CodeMsg() {
    }

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回带参数的错误码
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }

}
