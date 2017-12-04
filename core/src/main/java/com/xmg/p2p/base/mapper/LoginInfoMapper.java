package com.xmg.p2p.base.mapper;

import com.xmg.p2p.base.domain.LoginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LoginInfoMapper {

    int insert(LoginInfo record);

    LoginInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKey(LoginInfo record);

    int queryByUsername(String username);

    LoginInfo selectByNameAndPassword(@Param("username")String username, @Param("password")String password,@Param("userType") int userType);

    int selectByType( int userType);

    List<LoginInfo> autoComplate(String keyword);
}