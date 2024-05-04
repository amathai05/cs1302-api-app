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
import javafx.scene.control.TextArea;

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
    private static final String TRANSLATOR_API = "https://libretranslate.com/translate";
    private static final String KEY = "91d414d0-9d15-4106-8d8d-d3a2c36751e1";

    Stage stage;
    Scene scene;
    VBox root;

    TopBar bar;
    TextArea  verse;
    TextArea transText;

    Translator transBox;

    BibleResponse bRes;
    TranslatorResponse tRes;

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

        verse = new TextArea();
        verse.setWrapText(true);
        verse.setPrefHeight(140);
        verse.setEditable(false);

        transText = new TextArea();
        transText.setWrapText(true);
        transText.setEditable(false);

        transBox = new Translator();

    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        bar.search.setOnAction(event -> {
            vers = bar.getVersion();
            reference = bar.getReference();

            retrieveBibleResponse();

            bibleVerse = this.bRes.text;

            System.out.println(bibleVerse);

            verse.setText(this.bRes.text);
        });

        transBox.translate.setOnAction(event -> {

            System.out.println(bibleVerse);

            retrieveTranslatorResponse();

            System.out.println(this.tRes.translatedText);

            transText.setText(this.tRes.translatedText);

        });


        this.stage = stage;

        // demonstrate how to load local asset using "file:resources/"
        Image bannerImage = new Image("file:resources/readme-banner.png");
        ImageView banner = new ImageView(bannerImage);
        banner.setPreserveRatio(true);
        banner.setFitWidth(640);

        // some labels to display information
        Label howToUse = new Label("Enter a reference! Example: \"1 John 3:16\" or \"Psalm 23\"");
        Label notice = new Label("Thank you to LibreTranslate & bible-api.com!");
        // setup scene
        root.getChildren().addAll(bar, howToUse, verse, transBox, transText, notice);
        scene = new Scene(root);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start

    public void retrieveBibleResponse() {
        try {
            //form uri
            String term = URLEncoder.encode(reference, StandardCharsets.UTF_8);
            String bibleVersion = URLEncoder.encode(this.vers, StandardCharsets.UTF_8);
            String query = String.format("%s?translation=%s", term, bibleVersion);
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
            this.bRes = GSON
                .fromJson(jsonString, BibleResponse.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Hi");
        }
    }


    public void retrieveTranslatorResponse() {
        try {

            //form uri

            String language = "";

            if (transBox.getLanguage().equals("English")) {
                language = "en";
            } else if (transBox.getLanguage().equals("Spanish")) {
                language = "es";
            } else if (transBox.getLanguage().equals("Chinese")) {
                language = "zh";
            } else if (transBox.getLanguage().equals("French")) {
                language = "fr";
            } else if (transBox.getLanguage().equals("Hindi")) {
                language = "hi";
            } else {
                language = "ar";
            }

            bibleVerse = bibleVerse.replace("\n", " ");

            String query = String.format
                ("{\"q\":\"%s\",\"source\":\"en\",\"target\":\"%s\",\"format\":\"text\",\"api_key\":\"%s\"}",
                bibleVerse, language, KEY);

            System.out.print(query);

            //build request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TRANSLATOR_API))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(query))
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
            this.tRes = GSON
                .fromJson(jsonString, TranslatorResponse.class);
        } catch (IOException | InterruptedException e) {
            System.out.println("Hi");
        }
    }


} // ApiApp
