package me.mrletsplay.pwmixergui.filters;

import me.mrletsplay.pwmixer.PWMFilterFunction;

public class CombinedFilter implements PWMFilterFunction {

	private PWMFilterFunction[] filters;

	public CombinedFilter(PWMFilterFunction[] filters) {
		this.filters = filters;
	}

	@Override
	public void filter(float[] samples) {
		for(PWMFilterFunction filter : filters) {
			filter.filter(samples);
		}
	}

}
