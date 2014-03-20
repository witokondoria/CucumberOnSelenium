package es.bull.testingframework.cucumber.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Parameter {

	public enum Name {
		WARN, SECONDS, URL, NUMBER, ATTRIBUTE, TEXT, HTMLTAG, COOKIE
	}

	public enum Type {
		BOOL, INT, STRING
	}

	public Name name();

	public Type type();

}