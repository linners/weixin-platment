package com.lin.bulter.wxplatement.common.param;

import com.lin.bulter.wxplatement.common.dto.common.PageParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatAppParam {

    private PageParam page;

    private Integer id;

    /**
    * 应用类型，1：公众号  2：小程序
    */
    private Integer appType;

    /**
    * 微信公众号app_id
    */
    private String appId;

    /**
    * 微信公众号app_secret
    */
    private String appSecret;

    /**
    * 微信公众号token
    */
    private String appToken;

    /**
    * 微信公众号描述
    */
    private String appDesc;

    /**
    * 是否有效，0：无效 1：有效
    */
    private Integer status;

    /**
    * 创建时间
    */
    private Date cT;

    /**
    * 修改时间
    */
    private Date uT;


}
