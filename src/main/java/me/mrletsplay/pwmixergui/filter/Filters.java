package me.mrletsplay.pwmixergui.filter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.mrletsplay.pwmixergui.util.dialog.DialogData;
import me.mrletsplay.pwmixergui.util.dialog.SimpleInputDialog;

public class Filters {

	private static final Map<Class<? extends Filter>, List<FilterParameter<?>>> REGISTERED_FILTERS;
	public static final Map<Class<? extends Filter>, List<FilterParameter<?>>> FILTERS;

	static {
		REGISTERED_FILTERS =  new HashMap<>();
		FILTERS = Collections.unmodifiableMap(REGISTERED_FILTERS);

		registerFilter(EchoFilter.class, EchoFilter.PARAMETERS);
		registerFilter(NOPFilter.class, Collections.emptyList());
	}

	public static void showFilterDialog(Filter filter) {
		Class<? extends Filter> filterClass = filter.getClass();
		if(!FILTERS.containsKey(filterClass)) throw new IllegalArgumentException("Filter class is not registered");

		SimpleInputDialog dialog = new SimpleInputDialog();

		for(FilterParameter<?> param : FILTERS.get(filter.getClass())) {
			switch(param.getType()) {
				case INTEGER:
					dialog.addInteger(param.getName(), param.getFriendlyName(), (int) filter.getParameter(param), (int) param.getMinValue(), (int) param.getMaxValue());
					break;
				case FLOAT:
					dialog.addFloat(param.getName(), param.getFriendlyName(), (float) filter.getParameter(param), (float) param.getMinValue(), (float) param.getMaxValue());
					break;
			}
		}

		DialogData data = dialog.show("Edit Filter", null);
		if(data != null ) {
			for(FilterParameter<?> param : FILTERS.get(filter.getClass())) {
				filter.setParameter(param, data.get(param.getName()));
			}
			filter.applyParameters();
		}
	}

	public static void registerFilter(Class<? extends Filter> filterClass, List<FilterParameter<?>> parameters) {
		REGISTERED_FILTERS.put(filterClass, parameters);
	}

}
