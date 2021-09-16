package com.zj.web.dao;

import com.zj.web.entity.UserInfoEntity;
import com.zj.web.entity.UserRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户关系持久层
 * @author xiaozj
 */
@Repository
public interface UserRelationDao extends JpaRepository<UserRelationEntity, Long>, JpaSpecificationExecutor<UserRelationEntity> {

    /**
     * 通过username找到该用户所有的好友
     * @param username
     * @return
     */
    @Modifying
    @Query(value = "SELECT * FROM user_relation AS t WHERE t.user_name1 = ?1", nativeQuery = true)
    List<UserRelationEntity> findFriendsByUsername(String username);


    void deleteUserRelationEntityByUserName1AndAndUserName2(String userName1, String userName2);

}
