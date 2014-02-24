package es.bull.testingframework.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class MatcherAssert {
    
    public static <T> void assertWarnThat(String reason, T actual, Matcher<? super T> matcher)  {
        if (!matcher.matches(actual)) {
            Description description = new StringDescription();
            description.appendText(reason)
                       .appendText("\nExpected: ")
                       .appendDescriptionOf(matcher)
                       .appendText("\n     but: ");
            matcher.describeMismatch(actual, description);
            
            throw new AssertionWarn(description.toString());
        }
    }    
}
