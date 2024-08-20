package me.mrletsplay.pwmixergui.filter;

import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import me.mrletsplay.pwmixergui.channel.ChannelConnection;

public class FilterController {

	@FXML
	private Label labelFilterName;

	@FXML
	private Button
		buttonMute,
		buttonUp,
		buttonDown,
		buttonEdit,
		buttonRemove;

	private FiltersController filtersController;
	private ChannelConnection connection;
	private Filter filter;

	public void init(FiltersController filtersController, ChannelConnection connection, Filter filter) {
		this.filtersController = filtersController;
		this.connection = connection;
		this.filter = filter;

		labelFilterName.setText(filter.getClass().getSimpleName());

		List<Filter> filters = connection.getFilters();
		buttonUp.setDisable(filters.indexOf(filter) == 0);
		buttonDown.setDisable(filters.indexOf(filter) == filters.size() - 1);
		buttonEdit.setDisable(Filters.FILTERS.get(filter.getClass()).isEmpty());
	}

	@FXML
	void up(ActionEvent event) {
		int idx = connection.getFilters().indexOf(filter);
		Collections.swap(connection.getFilters(), idx, idx - 1);
		filtersController.updateFiltersList();
	}

	@FXML
	void down(ActionEvent event) {
		int idx = connection.getFilters().indexOf(filter);
		Collections.swap(connection.getFilters(), idx, idx + 1);
		filtersController.updateFiltersList();
	}

	@FXML
	void edit(ActionEvent event) {
		Filters.showFilterDialog(filter);
	}

	@FXML
	void mute(ActionEvent event) {

	}

	@FXML
	void remove(ActionEvent event) {
		connection.getFilters().remove(filter);
		connection.applyFilters();
		filtersController.updateFiltersList();
	}

}
