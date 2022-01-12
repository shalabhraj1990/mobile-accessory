package com.spring.boot.accessory.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
@Component
@Aspect
@Slf4j
public class LogExecutionTimeAspect {
	@Pointcut(value = "execution(* com.spring.boot.accessory..*.*(..))")
	public void logExecutionTimeAllMethodPointCut() {

	}

	@Pointcut(value = "@annotation(com.spring.boot.accessory.annotations.LogExecutionTime)")
	public void logOnlyAnnotatedMethod() {

	}

	@Around(value = "logOnlyAnnotatedMethod()")
	public Object logExecutionTimeAspect(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object returnVal = null;

		try {
			returnVal = joinPoint.proceed();
		} catch (Throwable e) {
			log.error("Exception while executing " + joinPoint.getSignature().toShortString(), e);
			throw e;
		}

		log.warn("Tota execution time for  " + joinPoint.getSignature().toShortString() + "in mills = "
				+ (System.currentTimeMillis() - startTime));
		return returnVal;
	}
}
