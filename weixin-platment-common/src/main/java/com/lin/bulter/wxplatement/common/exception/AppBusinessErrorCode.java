package com.lin.bulter.wxplatement.common.exception;


/**
 * 应用业务异常代码总表
 * 注意，此处定义的错误码用户前台可见,code与description用户可见,innerMsg内部使用
 * 0-9999       系统级别或通用异常,一般在网关上定义
 * 200000 - 209999	(网关业务自定义异常20为固定，后四位业务自定义).
 * <p>
 * 其他: 建议从200010开始依次往后排
 * <p>
 *
 * @author tanwei34
 * @date 2018-11-12 15:08:45
 */
public enum AppBusinessErrorCode implements ErrorCode {

    /**
     * 没有数据
     */
    HAVE_NO_DATA("207010", "没有数据", "没有数据"),
    /**
     * 服务器开小差了
     */
    SERVER_ERROR("207011", "服务器开小差了", "服务器开小差了"),
    /**
     * 请求参数有误
     */
    PARAM_INVALID("207012", "请求参数有误", "请求参数有误"),

    /**
     * RPC调用异常
     */
    RPC_ERROR("207017", "服务器开小差了", "RPC调用异常"),

    /**
     * 未知异常（msg提醒）
     */
    UNDEFINED_ERROR("207020", "服务器开小差了", "其他系统异常信息透传"),

    /**
     * 获取百度accessToken异常
     */
    BAIDU_GET_TOKEN_ERROR("207021", "服务器开小差了", "获取百度accessToken异常"),

    /**
     * 无效的Question_id
     */
    BAIDU_QUESTION_ID_INVALID("207022", "无效的Question_id", "无效的Question_id"),

    /**
     * 提交问题接口已关闭
     */
    BAIDU_SUBMIT_QUESTION_INVALID("207023", "提交问题接口已关闭", "提交问题接口已关闭"),

    /**
     * Redis取数据后，解析异常
     */
    REDIS_DATA_PARSE_ERROR("1000000", "Redis取数据后，解析异常"),

    /**
     * 微信请求操作异常
     */
    WEIXIN_ERROR("201031", "微信操作异常", "微信操作异常"),
    ;

    /**
     * code
     */
    private final String code;
    /**
     * 对外错误描述
     */
    private String description;

    /**
     * 内部错误描述
     */
    private final String innerMsg;

    /**
     * @param code
     * @param description
     * @param innerMsg
     */
    AppBusinessErrorCode(String code, String description, String innerMsg) {
        this.code = code;
        this.description = description;
        this.innerMsg = innerMsg;
    }

    /**
     * @param code
     * @param description
     */
    AppBusinessErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
        this.innerMsg = description;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getInnerMsg() {
        return innerMsg;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("Code:[%s], Description:[%s]. ", this.code, this.description);
    }

}
