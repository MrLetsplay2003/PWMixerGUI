package me.mrletsplay.pwmixergui.channel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.mrletsplay.pwmixer.PWMixer;
import me.mrletsplay.pwmixergui.filter.Filter;

public class ChannelConnection {

	private OutputChannel output;
	private InputChannel input;
	private float volume;
	private ObservableList<Filter> filters;

	public ChannelConnection(InputChannel input, OutputChannel output) {
		this.input = input;
		this.output = output;
		this.volume = 1.0f;
		this.filters = FXCollections.observableArrayList();
	}

	public OutputChannel getOutput() {
		return output;
	}

	public InputChannel getInput() {
		return input;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
		PWMixer.ioSetConnectionVolume(input.getInput(), output.getOutput(), volume);
	}

	public ObservableList<Filter> getFilters() {
		return filters;
	}

	public void applyFilters() {
		if(filters.isEmpty()) {
			PWMixer.ioSetFilterFunction(input.getInput(), output.getOutput(), null);
			return;
		}

		PWMixer.ioSetFilterFunction(input.getInput(), output.getOutput(), this::runFilters);
	}

	private void runFilters(float[] samples) {
		for(Filter filter : filters) {
			if(!filter.isActive()) continue;
			filter.filter(samples);
		}
	}

}
