package es.bull.testingframework.cucumber.converter;

import cucumber.api.Transformer;

public class WarnBooleanConverter extends Transformer<Boolean> {

	@Override
	public Boolean transform(String input) {

		if ((input != null) && (input.contains("WARN"))) {
			return true;
		}
		else {
			return false;
		}
	}
}
