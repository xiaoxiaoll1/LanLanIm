package com.zj.web.dao;

import com.zj.web.entity.HistoryMsgEntity;
import com.zj.web.entity.UserInfoEntity;
import com.zj.web.entity.UserRelationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author xiaozj
 */
@Repository
public interface HistoryMsgDao extends JpaRepository<HistoryMsgEntity, Long>, JpaSpecificationExecutor<UserInfoEntity> {


    /**
     *
     * @param username
     * @param limit
     * @param pageSize
     * @return
     */
    @Modifying
    @Query(value = "SELECT * FROM history_msg AS A INNER JOIN (SELECT id FROM history_msg WHERE from_username = ?1 OR dest_username = ?1 ORDER BY msg_id LIMIT ?2, ?3) AS B on A.id = B.id", nativeQuery = true)
    List<HistoryMsgEntity> findHistoryMsgPage(String username, int limit, int pageSize);

}
