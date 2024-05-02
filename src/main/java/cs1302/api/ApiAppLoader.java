package cs1302.api;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import javafx.scene.image.Image;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.google.gson.Gson;
import javafx.scene.control.Alert;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import cs1302.api.Release;
import java.util.List;
import javafx.geometry.Insets;

public class ApiAppLoader extends VBox {

    HBox search;
    Label searchLabel;
    TextField searchEngine;
    Button searchButton;

    HBox artistInfo;
    ImageView artistImage;
    Image setUp;
    VBox releaseBox;
    TextFlow releaseInfo;
    ScrollPane releasePane;

    HBox otherArtist;
    Button artist1; // add label later
    ImageView artistImage1;
    Button artist2;
    ImageView artistImage2;
    Button artist3;
    ImageView artistImage3;

    Artist artist;
    Release[] releases;
    Release[] releaseLabel;

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static final String DEFAULT_IMG = "file:resources/default.png";

    public ApiAppLoader() {
        super();

        this.search = new HBox(8);
        this.searchLabel = new Label("Search: ");
        this.searchEngine = new TextField("Enter artist name");
        this.searchButton = new Button("Go");
        this.search.getChildren().addAll(searchLabel, searchEngine, searchButton);

        this.artistInfo = new HBox(8);
        this.artistImage = new ImageView();
        this.setUp = new Image(DEFAULT_IMG);
        this.artistImage.setImage(setUp);
        artistImage.setFitWidth(500);
        artistImage.setFitHeight(200); // 200 + 150 + 50 for the heights
        artistImage.setPreserveRatio(true);
        this.releaseInfo = new TextFlow(); // create loadPage method
        this.releaseBox = new VBox(8);
        this.releasePane = new ScrollPane(releaseInfo);
        this.artistInfo.getChildren().addAll(artistImage, releaseBox);

        this.otherArtist = new HBox(8);
        this.artistImage1 = new ImageView(); // create loadImage method
        this.artist1 = new Button();
        this.artistImage2 = new ImageView();
        this.artist2 = new Button();
        this.artistImage3 = new ImageView();
        this.artist3 = new Button();
        this.otherArtist.getChildren().addAll(artist1, artist2, artist3);

        this.releases = new Release[5];
        this.releaseLabel = new Release[5];

        this.getChildren().addAll(search, artistInfo, otherArtist);

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        searchButton.setOnAction((event) -> {
            loadReleases();
        });

        artist1.setOnAction((event) -> {
            // add method call here
        });

        artist2.setOnAction((event) -> {
            // add method call here
        });

        artist3.setOnAction((event) -> {
            // add method call here
        });

    }

    public void loadReleases() {
        searchButton.setDisable(true);
        if (searchEngine.getText().isEmpty()) {
            System.out.println("Empty Search Alert here:");
            //emptySearchAlert();
        } else {
            String term = URLEncoder.encode(searchEngine.getText(), StandardCharsets.UTF_8);
            String query = "query=artist:" + term + "&limit=1&fmt=json"; // change limit?
            String createUrl = "https://musicbrainz.org/ws/2/artist?" + query;
            try {
                HttpRequest requestID = HttpRequest.newBuilder()
                    .uri(URI.create(createUrl))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(requestID, BodyHandlers.ofString());
                Gson gson = new Gson();
                int statusCode = response.statusCode();
                if (statusCode == 200) {
                    String json = response.body();
                    //System.out.println(json);
                    ArtistOutput artistList  = gson.fromJson(json, ArtistOutput.class);
                    if (artistList != null && artistList.artists != null) {
                        Artist artist = artistList.artists.get(0); // gets first artist
                        System.out.println("Name: " + artist.name);
                        System.out.println("Country: " + artist.country);
                        System.out.println("Tags: ");

                        for (int i = 0; i < 5; i++) { // arrange in order of count/popularity???
                            if (artist.tags.get(i).toString() == null) {
                                i = 5;
                            }
                            loadPage();
                            System.out.println("\t+ " + artist.tags.get(i).toString());
                        }
                    }
                } else {
                    System.out.println("Failed to retrieve artist: " + statusCode);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        searchButton.setDisable(false);
    }


    /** public void findReleases(Artist artistName) {
        try {
            String urlID = "https://musicbrainz.org/ws/2/release-group?artist="
                + artistName.id // gets id from Artist class
                + "&fmt=json";
            HttpRequest requestRelease = HttpRequest.newBuilder()
                .uri(URI.create(urlID))
                .build();
            HttpResponse<String> responseRelease = HTTP_CLIENT
                .send(requestRelease, BodyHandlers.ofString());
            System.out.println(responseRelease.body());
            int statusCode2 = responseRelease.statusCode();
            if (statusCode2 == 200) {
                Gson gson = new Gson();
                Release[] releases = gson // creates array of releases (5)
                    .fromJson(responseRelease.body(), Release[].class);
                if (releases != null && releases.length > 0) {
                    System.out.println(releases[1].toString()); // toString()?
                } else {
                    System.out.println("No releases found for the artist"); // code to move to next artist on list?
                }
            } else {
                System.out.println("Failed to retrieve releases" + statusCode2);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error when finding releases: ");
            e.printStackTrace();
        }

        }*/
}
