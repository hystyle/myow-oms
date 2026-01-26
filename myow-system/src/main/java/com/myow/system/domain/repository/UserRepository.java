package com.myow.system.domain.repository;

import com.myow.common.response.PageParam;
import com.myow.common.response.PageResult;
import com.myow.system.domain.entity.User;

/**
 * @author yss
 */
public interface UserRepository {
    /**
     * find user by id
     * @param id user id
     * @return user
     */
    User findById(Long id);

    /**
     * save user
     * @param user user
     * @return row
     */
    int save(User user);

    /**
     * update user
     * @param user user
     * @return row
     */
    int update(User user);

    /**
     * delete by id
     * @param id user id
     * @return row
     */
    int deleteById(Long id);

    /**
     * select page
     * @param pageParam page param
     * @return page result
     */
    PageResult<User> selectPage(PageParam pageParam);
}
