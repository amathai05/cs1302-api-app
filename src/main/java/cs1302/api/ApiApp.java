package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.net.URI;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;

import cs1302.api.TopBar;
import cs1302.api.Translator;
import javafx.scene.control.TextField;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object



    private static final String BIBLE_API = "https://bible-api.com/";

    Stage stage;
    Scene scene;
    VBox root;

    TopBar bar;
    TextField verse;
    TextField transText;
    Translator transBox;

    BibleResponse res;

    String reference;
    String vers;

    String bibleVerse;

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

        bar.search.setOnAction(event -> {
            vers = bar.getVersion();
            reference = bar.getReference();
            retrieveResponse();

            System.out.print(this.res.text);

            verse.setText(this.res.text);
        });


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

    public void retrieveResponse() {
        try {
            //form uri
            String term = URLEncoder.encode(reference, StandardCharsets.UTF_8);
            String bibleVersion = URLEncoder.encode(this.vers, StandardCharsets.UTF_8);
            String query = String.format("%s?translaton=%s", term, bibleVersion);
            String uri = BIBLE_API + query;

            System.out.println(uri);

            //build request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            //response code 200
            if (response.statusCode() != 200) {
                throw new IOException(response.toString());
            }
            String jsonString = response.body();
            System.out.println("********** RAW JSON STRING: **********");
            System.out.println(jsonString.trim());
            // parse the JSON-formatted string using GSON
            this.res = GSON
                .fromJson(jsonString, BibleResponse.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Hi");
        }
    }

} // ApiApp
