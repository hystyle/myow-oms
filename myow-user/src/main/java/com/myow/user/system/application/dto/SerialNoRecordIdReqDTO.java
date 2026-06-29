package com.myow.user.system.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SerialNoRecordIdReqDTO {

    @NotNull(message = "serialNumberId cannot be null")
    private Integer serialNumberId;

    @NotNull(message = "recordDate cannot be null")
    private LocalDate recordDate;
}
