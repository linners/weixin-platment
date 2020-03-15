package com.lin.bulter.wxplatement.repository.mysql.dao;

import com.lin.bulter.wxplatement.common.param.WechatAppParam;
import com.lin.bulter.wxplatement.repository.mysql.entity.WechatApp;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WechatAppMapper {
    
    /**
     *  按主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入一条记录
     */
    int insert(WechatApp record);

    /**
     *  按主键查询
     */
    WechatApp selectByPrimaryKey(Integer id);

    /**
     *  查询所有记录
     */
    List<WechatApp> selectAll();

    /**
     *  按主键更新
     */
    int updateByPrimaryKeySelective(WechatApp record);

    /**
     *  按条件分页查询
     */
    List<WechatApp> selectByCondition(@Param("param") WechatAppParam wechatAppParam);
}
