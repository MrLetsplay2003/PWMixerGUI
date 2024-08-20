package me.mrletsplay.pwmixergui.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.mrletsplay.pwmixer.PWMFilterFunction;

public abstract class Filter implements PWMFilterFunction {

	private Map<FilterParameter<?>, Object> parameters;

	public Filter() {
		this.parameters = new HashMap<>();
	}

	protected <T> void setParameter(FilterParameter<T> parameter, T value) {
		Objects.requireNonNull(parameter, "parameter must not be null");
		Objects.requireNonNull(value, "value must not be null");
		parameters.put(parameter, value);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getParameter(FilterParameter<T> parameter) {
		Objects.requireNonNull(parameter, "parameter must not be null");
		return (T) parameters.getOrDefault(parameter, parameter.getDefaultValue());
	}

	public abstract void applyParameters() throws IllegalStateException;

	@Override
	public String toString() {
		return getClass().getSimpleName() + parameters;
	}

}
