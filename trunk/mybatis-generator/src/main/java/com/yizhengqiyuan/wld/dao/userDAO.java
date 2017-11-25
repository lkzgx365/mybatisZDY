package com.yizhengqiyuan.wld.dao;

import com.yizhengqiyuan.wld.po.user;
import java.util.List;

public interface userDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(user record);

    int insertSelective(user record);

    user selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(user record);

    int updateByPrimaryKey(user record);

    List<user> selectAll();
}