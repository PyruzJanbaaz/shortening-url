package com.pyruz.shortening.model.dto.base;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceExceptionDTO extends  RuntimeException{
    private Integer code;
    private String message;
}
