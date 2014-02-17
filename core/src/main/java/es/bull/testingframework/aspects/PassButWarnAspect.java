package es.bull.testingframework.aspects;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import es.bull.testingframework.matchers.AssertionWarn;
import gherkin.formatter.model.Result;
import cucumber.runtime.Runtime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PassButWarnAspect {

	@Pointcut("call(public gherkin.formatter.model.Result.new(String, Long, Throwable, Object))"
			+ " && args(status, duration, error, dummy)")
	protected void warnCallPointcut(String status, Long duration,
			Throwable error, Object dummy) {
	}

	@Around(value = "warnCallPointcut(status, duration, error, dummy)")
	public Result aroundWarnCalls(ProceedingJoinPoint pjp, String status,
			Long duration, Throwable error, Object dummy) throws Throwable {

		Result result = (Result) pjp.proceed();
		if ((error != null) && (error instanceof AssertionWarn)) {
			// change status field for result object
			Runtime rt = (Runtime) pjp.getThis();
			Field skipNextStep = rt.getClass().getDeclaredField("skipNextStep");
			skipNextStep.setAccessible(true);
			skipNextStep.setBoolean(rt, false);

			Class<?> resultClass = result.getClass();
			Field newStatus = resultClass.getDeclaredField("status");
			newStatus.setAccessible(true);

			Field modifiersField = newStatus.getClass().getDeclaredField(
					"modifiers");

			modifiersField.setAccessible(true);
			modifiersField.setInt(newStatus, newStatus.getModifiers()
					& ~Modifier.FINAL);

			newStatus.set(result, Result.PASSED);
		}
		return result;
	}
}