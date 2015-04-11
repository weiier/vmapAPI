package com.buptmap.action;

public class ErrorMessage {
	public static String OK = "请求成功，对数据有疑惑请联系902。";
	
    public static String URIError="URI格式不对，缺少client或者vkey ";
    
    public static String ClientError = "该帐号不存在或者被冻结，请联系管理员获取帐号 ";
    
    public static String ApiError = "该帐号权限不足，本次API访问失败 ";
    
    public static String VkeyError="Vkey有误，请使用正确的key加密 ";
    
    public static String PlaceError="该帐号权限不足，无权获取该place相关信息 ";
    
    public static String CityError="该帐号权限不足，无权获取该city相关信息 ";
    
    public static String ParametersValueError = "参数值不对 ";
    
    public static String ParameterError = "参数不足 ";
    
    public static String NoResultError = "没有匹配数据";
    
    public static String AddUserError = "执行过程中出现意外，导致新增用户失败";
    
    public static String FileNotExistError = "图片存储路径有误，或者图片不存在";
    
    public static String MapErrorPath = "../../Content/Image/error.jpg ";
    
}
