package com.lin.bulter.wxplatement.business.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.wxplatement.common.param.WechatAppParam;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;

import java.util.List;

public interface WechatAppService {

    /**
     * 新增
     *
     * @return
     */
    Integer insertWechatApp(WechatApp wechatApp);

    /**
     * 修改
     *
     * @return
     */
    Integer updateWechatAppById(WechatApp wechatApp);

    /**
     * 删除
     */
    Integer deleteWechatAppById(Integer wechatAppId);

    /**
     * 按主键查询
     */
    WechatApp selectWechatAppById(Integer wechatAppId);

    /**
     * 查询所有
     */
    List<WechatApp> selectAllWechatApps();

    /**
     * 分页查询
     */
    PageInfo<WechatApp> selectWechatAppByPage(WechatAppParam wechatAppParam);
}
