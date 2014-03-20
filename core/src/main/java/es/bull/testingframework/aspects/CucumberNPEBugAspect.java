package es.bull.testingframework.aspects;

//@see https://github.com/cucumber/cucumber-jvm/issues/591

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CucumberNPEBugAspect {

	@Pointcut("call(* cucumber.runtime.xstream.LocalizedXStreams.LocalizedXStream.unsetParameterInfo())")
	protected void unsetParameterInfoPointcut() {
	}

	@Around(value = "unsetParameterInfoPointcut()")
	public void aroundUnsetParameterInfo(ProceedingJoinPoint pjp)
			throws Throwable {
		synchronized (this) {
			pjp.proceed();
		}
	}

	@Pointcut("call(* cucumber.runtime.xstream.LocalizedXStreams.LocalizedXStream.setParameterInfo(..))")
	protected void setParameterTypePointcut() {
	}

	@Around(value = "setParameterTypePointcut()")
	public void setParameterTypeInfo(ProceedingJoinPoint pjp) throws Throwable {
		synchronized (this) {
			pjp.proceed();
		}
	}
}