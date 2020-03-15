package com.lin.bulter.wxplatement.repository.mysql.dao;

import com.lin.bulter.wxplatement.common.dto.UserParam;
import com.lin.bulter.wxplatement.repository.mysql.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    
    /**
     *  按主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入一条记录
     */
    int insert(User record);

    /**
     *  按主键查询
     */
    User selectByPrimaryKey(Integer id);

    /**
     *  查询所有记录
     */
    List<User> selectAll();

    /**
     *  按主键更新
     */
    int updateByPrimaryKeySelective(User record);

    /**
     *  按条件分页查询
     */
    List<User> selectByCondition(@Param("param") UserParam userParam);
}
