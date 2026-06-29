package com.myow.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Schema(description = "Change current user password request")
public class ChangePasswordReqDTO {

    @Schema(description = "Old password")
    @NotBlank(message = "oldPassword cannot be blank")
    private String oldPassword;

    @Schema(description = "New password")
    @NotBlank(message = "newPassword cannot be blank")
    @Length(min = 8, max = 64, message = "newPassword length must be 8-64")
    private String newPassword;

    @Schema(description = "Confirm new password")
    @NotBlank(message = "confirmPassword cannot be blank")
    private String confirmPassword;
}
