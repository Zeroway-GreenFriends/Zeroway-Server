package com.zeroway.aop;

import com.zeroway.common.BaseException;
import com.zeroway.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.zeroway.common.BaseResponseStatus.REQUEST_ERROR;
import static com.zeroway.common.BaseResponseStatus.UNAUTHORIZED_REQUEST;

@Slf4j
@Aspect
public class LogAspect {

    @Pointcut("within(@org.springframework.stereotype.Repository *) || " +
            "within(@org.springframework.stereotype.Service *)")
    public void beanPointcut() {}


    // service, repository 로깅
    @Around("beanPointcut()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("{} args={}", joinPoint.getSignature(), joinPoint.getArgs());
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("{} args={} error={}", joinPoint.getSignature(), joinPoint.getArgs(), e.toString());
            throw e;
        }
    }

    // controller 로깅
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object controllerLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("{} args={}", joinPoint.getSignature(), joinPoint.getArgs());
            return joinPoint.proceed();
        } catch (BaseException e) {

            log.error("{} args={} error={}", joinPoint.getSignature(), joinPoint.getArgs(), e.toString());
            if (e.getStatus().equals(UNAUTHORIZED_REQUEST))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(e.getStatus()));
            else
                return ResponseEntity.badRequest().body(new BaseResponse<>(e.getStatus()));
        } catch (Exception e) {
            log.error("{} args={} error={}", joinPoint.getSignature(), joinPoint.getArgs(), e.toString());
            return ResponseEntity.badRequest().body(new BaseResponse<>(REQUEST_ERROR));
        }
    }

}

