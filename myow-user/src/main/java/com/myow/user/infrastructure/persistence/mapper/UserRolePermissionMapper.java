package com.myow.user.infrastructure.persistence.mapper;

import com.myow.user.infrastructure.persistence.po.MenuDO;
import com.myow.user.infrastructure.persistence.po.RoleDO;
import com.myow.user.application.vo.UserRoleInfoVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRolePermissionMapper {

    @Select("""
            SELECT DISTINCT r.role_id, r.tenant_id, r.role_name, r.role_code, r.status, r.deleted_flag
            FROM sys_user_role ur
            LEFT JOIN sys_role r ON r.role_id = ur.role_id
            WHERE ur.user_id = #{userId}
              AND (r.deleted_flag = false OR r.deleted_flag = 0)
            """)
    List<RoleDO> selectRolesByUserId(@Param("userId") Long userId);

    @Select("""
            <script>
            SELECT ur.user_id, ur.role_id, r.role_name
            FROM sys_user_role ur
            LEFT JOIN sys_role r ON r.role_id = ur.role_id
            WHERE ur.user_id IN
            <foreach item="userId" collection="userIds" open="(" separator="," close=")">
                #{userId}
            </foreach>
            </script>
            """)
    List<UserRoleInfoVO> selectRoleInfoByUserIds(@Param("userIds") List<Long> userIds);

    @Select("""
            <script>
            SELECT DISTINCT m.*
            FROM sys_menu m
            <if test="adminFlag == null or adminFlag == false">
                LEFT JOIN sys_role_menu rm ON rm.menu_id = m.menu_id
            </if>
            WHERE (m.deleted_flag = false OR m.deleted_flag = 0)
            <if test="adminFlag == null or adminFlag == false">
                AND rm.role_id IN
                <foreach item="roleId" collection="roleIdList" open="(" separator="," close=")">
                    #{roleId}
                </foreach>
            </if>
            ORDER BY m.sort ASC
            </script>
            """)
    List<MenuDO> selectMenusByRoleIdList(@Param("roleIdList") List<Long> roleIdList, @Param("adminFlag") Boolean adminFlag);
}
