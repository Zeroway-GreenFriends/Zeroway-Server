package com.zeroway.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BaseException extends RuntimeException {
    private final BaseResponseStatus status;
}
