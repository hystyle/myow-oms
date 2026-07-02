package com.myow.user.application.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(description = "Current user frontend bootstrap payload")
public class ProfileBootstrapVO {

    @Schema(description = "Current user profile")
    private UserRespVO user;

    @Schema(description = "Current user menu tree source list")
    private List<UserMenuRespVO> menuList = List.of();

    @Schema(description = "Current user button and operation permission codes")
    private List<String> permissionList = List.of();

    @Schema(description = "Current user role id list")
    private List<Long> roleIdList = List.of();

    @Schema(description = "Current user role name list")
    private List<String> roleNameList = List.of();

    @Schema(description = "Whether current user is platform administrator")
    private Boolean adminFlag;

    @Schema(description = "Whether current user must change password before using the system")
    private Boolean forceChangePassword;

    @Schema(description = "Alias of forceChangePassword for backward compatibility")
    private Boolean mustChangePassword;

    @Schema(description = "Whether tenant feature is enabled in frontend")
    private Boolean tenantModeEnabled;

    @Schema(description = "Whether current tenant is enabled")
    private Boolean tenantEnabled;

    @Schema(description = "Current user data scope summary")
    private String dataScope;

    @Schema(description = "Frontend system config summary")
    private Map<String, Object> systemConfig = Map.of();
}
