package com.zj.web.dao;

import com.zj.web.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 用户信息持久层
 * @author xiaozj
 */
@Repository
public interface UserInfoDao extends JpaRepository<UserInfoEntity, Long>, JpaSpecificationExecutor<UserInfoEntity> {

    /**
     * 通过用户名查找
     * @param username
     * @return
     */
    UserInfoEntity findByUserName(String username);
}
