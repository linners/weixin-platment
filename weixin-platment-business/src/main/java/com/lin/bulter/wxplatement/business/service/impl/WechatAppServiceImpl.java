package com.lin.bulter.wxplatement.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.wxplatement.business.service.WechatAppService;
import com.lin.bulter.wxplatement.common.param.WechatAppParam;
import com.lin.bulter.wxplatement.repository.mysql.dao.WechatAppMapper;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WechatAppServiceImpl implements WechatAppService {

    @Autowired
    private WechatAppMapper wechatAppMapper;

    @Override
    @Transactional
    public Integer insertWechatApp(WechatApp wechatApp) {
        return wechatAppMapper.insert(wechatApp);
    }

    @Override
    @Transactional
    public Integer updateWechatAppById(WechatApp wechatApp) {
        return wechatAppMapper.updateByPrimaryKeySelective(wechatApp);
    }

    @Override
    @Transactional
    public Integer deleteWechatAppById(Integer wechatAppId) {
        return wechatAppMapper.deleteByPrimaryKey(wechatAppId);
    }

    @Override
    public WechatApp selectWechatAppById(Integer wechatAppId) {
        return wechatAppMapper.selectByPrimaryKey(wechatAppId);
    }

    @Override
    public List<WechatApp> selectAllWechatApps() {
        return wechatAppMapper.selectAll();
    }

    @Override
    public PageInfo<WechatApp> selectWechatAppByPage(WechatAppParam wechatAppParam) {
        PageInfo<WechatApp> wechatApps = PageHelper.startPage(wechatAppParam.getPage().getPageNum(), wechatAppParam.getPage().getPageSize())
                .doSelectPageInfo(() -> wechatAppMapper.selectByCondition(wechatAppParam));
        return wechatApps;
    }
}
