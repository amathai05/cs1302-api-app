package cs1302.api;

import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
//import javafx.scene.layout.Priority;


/**
 * Make a Translator, HBox, that acts as a component class
 * by holding a translate button, textfield, and combobox with languages.
 */
public class Translator extends HBox {

    //instance variables
    Button translate;
    Label statement;
    ComboBox<String> languages;

    /**
     * Instantiates a new Translator object.
     */
    public Translator() {

        //instantiate
        translate = new Button("Translate");
        statement = new Label("Translate To:");
        languages = new ComboBox<String>();

        //combobox
        languages.getItems().addAll(
            "English",
            "Spanish",
            "Chinese",
            "French",
            "Hindi",
            "Arabic"
        );

        languages.setValue("English");

        //set children
        this.getChildren().addAll(translate, statement, languages);

        this.setSpacing(150);
        this.setAlignment(javafx.geometry.Pos.CENTER);
    }

    /**
     * Retrieve the language selected in the combobox.
     *
     * @return Language you would like to translate to.
     */
    public String getLanguage() {
        return (languages.getValue());
    }

}
