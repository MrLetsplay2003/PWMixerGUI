package me.mrletsplay.pwmixergui.filter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.WeakInvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import me.mrletsplay.pwmixergui.PWMixerGUI;
import me.mrletsplay.pwmixergui.channel.ChannelConnection;
import me.mrletsplay.pwmixergui.exception.FilterException;
import me.mrletsplay.pwmixergui.util.dialog.DialogData;
import me.mrletsplay.pwmixergui.util.dialog.SimpleInputDialog;

public class FiltersController {

	@FXML
	private VBox vboxFilterList;

	private ChannelConnection connection;

	public void init(ChannelConnection connection) {
		this.connection = connection;
		connection.getFilters().addListener(new WeakInvalidationListener(o -> updateFiltersList()));
		updateFiltersList();
	}

	public void updateFiltersList() {
		try {
			vboxFilterList.getChildren().clear();

			for(Filter filter : connection.getFilters()) {
				FXMLLoader filterLoader = new FXMLLoader(PWMixerGUI.class.getResource("/filter.fxml"));
				Parent filterView = filterLoader.load();
				FilterController controller = filterLoader.getController();
				controller.init(this, connection, filter);

				vboxFilterList.getChildren().add(filterView);
			}
		}catch(IOException e) {}
	}

	@FXML
	void addFilter(ActionEvent event) {
		List<Class<? extends Filter>> filterClasses = new ArrayList<>(Filters.FILTERS.keySet());
		List<String> filterNames = filterClasses.stream().map(f -> f.getSimpleName()).collect(Collectors.toList());

		SimpleInputDialog dialog = new SimpleInputDialog();
		dialog.addChoice("filterType", "Filter Type", Filters.FILTERS.keySet().stream().map(f -> f.getSimpleName()).collect(Collectors.toList()));
		DialogData data = dialog.show("Add Filter", "Select the type of filter you want to add");

		Filter filter;
		try {
			filter = filterClasses.get(filterNames.indexOf(data.get("filterType"))).getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new FilterException("Failed to instantiate filter. Make sure the filter has a public default constructor", e);
		}

		filter.applyParameters();
		Filters.showFilterDialog(filter);

		connection.getFilters().add(filter);
		connection.applyFilters();
		updateFiltersList();
	}

}
