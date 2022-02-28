package com.demo.atenea.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TransactionDTO {
    @NonNull
    private Boolean estado = false;
    @NonNull
    private String mensaje;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;
}