package com.chu.dao;

import com.chu.model.UserLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by P70 on 2016/11/22.
 */
public interface IUserLogDao extends JpaRepository<UserLog, Integer> {

    /**
     * @param userName
     * @return
     */
    @Query(value = "select u from UserLog u where u.userName=?1")
    List<UserLog> findByUserName(String userName);

    /**
     * @param string
     * @param string2
     * @return
     */
    List<UserLog> findByUserNameAndUserIp(String string, String string2);

    /**
     * @param userName
     * @param pageable
     * @return
     */
    Page<UserLog> findByUserName(String userName, Pageable pageable);


}
