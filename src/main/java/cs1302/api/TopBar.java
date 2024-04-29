package cs1302.api;

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Priority;

/**
 * Make a HBox, Topbar, that acts as a component class
 * by holding search button, textfield and bible version.
 */
public class TopBar extends HBox {

    //instance variables
    Button search;
    private TextField location;
    private ComboBox<String> version;

    /**
     * Instantiates an ActionBar objects.
     */
    public TopBar() {
        super();

        //instantiate
        search = new Button("Search");
        location = new TextField("");
        version = new ComboBox<String>();

        //combobox
        version.getItems().addAll(
            "WEB",
            "ASV",
            "KJV"
        );

        version.setValue("WEB");

        //set children
        this.getChildren().addAll(search, location, version);

        //grow textfield
        this.setHgrow(location, Priority.ALWAYS);
    }

    /**
     * Returns the bible version.
     *
     * @return returns the version selected in ComboBox.
     */
    public String getVersion() {
        return (version.getValue());
    }

}
