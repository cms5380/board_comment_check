package me.minseok.board.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggerAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* me.minseok.board..Controller.*Controller.*(..)) or execution(* me.minseok.board..Service.*Impl.*(..)) or execution(* me.minseok.board..Mapper.*Mapper.*(..))")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName();

        if (name.contains("Controller") == true) {
            type = "Controller ===> ";

        } else if (name.contains("Service") == true) {
            type = "ServiceImpl ===> ";

        } else if (name.contains("Mapper") == true) {
            type = "Mapper ===> ";
        }

        logger.debug(type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }

}