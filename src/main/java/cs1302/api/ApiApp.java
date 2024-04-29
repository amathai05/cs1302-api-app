package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import cs1302.api.TopBar;
import cs1302.api.Translator;
import javafx.scene.control.TextField;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;

    TopBar bar;
    TextField verse;
    TextField transText;
    Translator transBox;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        bar = new TopBar();
        verse = new TextField();
        transText = new TextField();
        transBox = new Translator();

    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        // demonstrate how to load local asset using "file:resources/"
        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        root.getChildren().addAll(bar, verse, transBox, transText, banner, notice);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

} // ApiApp
