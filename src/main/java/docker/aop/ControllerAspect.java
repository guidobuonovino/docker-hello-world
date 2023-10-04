package docker.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import docker.utils.StringHelper;

@Aspect
@Component
public class ControllerAspect {

	private Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Before("execution( * docker.controller.*.*(..))")
	public void checkValidator(JoinPoint joinPoint) {

		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

		logger.info("--------------------------------------");
		logger.info("API METHOD - {}", method.getName());

		for (int i = 0; i < codeSignature.getParameterNames().length; i++) {
			String paramName = codeSignature.getParameterNames()[i];
			Object paramValue = joinPoint.getArgs()[i];
			String jsonString = StringHelper.toJsonString(paramValue);
			logger.info("API param  - {} = {}", paramName, jsonString);
		}
	}
}