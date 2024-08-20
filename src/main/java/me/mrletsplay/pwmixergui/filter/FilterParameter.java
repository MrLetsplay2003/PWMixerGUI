package me.mrletsplay.pwmixergui.filter;

public class FilterParameter<T> {

	private Type type;
	private String name;
	private String friendlyName;
	private T defaultValue;
	private T minValue, maxValue;

	private FilterParameter(Type type, String name, String friendlyName, T defaultValue, T minValue, T maxValue) {
		this.type = type;
		this.name = name;
		this.friendlyName = friendlyName;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public T getMinValue() {
		return minValue;
	}

	public T getMaxValue() {
		return maxValue;
	}

	@Override
	public String toString() {
		return "FilterParameter[" + type + " " + name + "]";
	}

	public static FilterParameter<Float> ofFloat(String name, String friendlyName, float defaultValue, float minValue, float maxValue) {
		return new FilterParameter<>(Type.FLOAT, name, friendlyName, defaultValue, minValue, maxValue);
	}

	public static FilterParameter<Float> ofFloat(String name, String friendlyName, float defaultValue) {
		return new FilterParameter<>(Type.FLOAT, name, friendlyName, defaultValue, null, null);
	}

	public static FilterParameter<Integer> ofInteger(String name, String friendlyName, int defaultValue, int minValue, int maxValue) {
		return new FilterParameter<>(Type.INTEGER, name, friendlyName, defaultValue, minValue, maxValue);
	}

	public static FilterParameter<Integer> ofInteger(String name, String friendlyName, int defaultValue) {
		return new FilterParameter<>(Type.INTEGER, name, friendlyName, defaultValue, null, null);
	}

	public static enum Type {

		FLOAT,
		INTEGER;

	}

}
