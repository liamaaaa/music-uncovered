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
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.layout.Priority;
import javafx.application.Platform;
import javafx.scene.control.OverrunStyle;

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

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static final String API_KEY = "ac27f5d94abeff5961ce56b3d823e86d";

    public ApiAppLoader() {
        super();

        this.search = new HBox(8);
        this.searchLabel = new Label("Enter Artist Name: ");
        this.searchEngine = new TextField("Search");
        this.searchButton = new Button("Go");
        this.search.getChildren().addAll(searchLabel, searchEngine, searchButton);

        this.artistInfo = new HBox(8);
        this.artistImage = new ImageView();
        this.artistImage.setFitWidth(550);
        this.artistImage.setFitHeight(200); // 200 + 150 + 50 for the heights
        this.artistImage.setPreserveRatio(true);

        this.releaseInfo = new TextFlow(); // create loadPage method
        this.releasePane = new ScrollPane(releaseInfo);
        this.releasePane.setPrefSize(200, 200);
        this.artistInfo.getChildren().addAll(artistImage, releasePane);
        HBox.setHgrow(releasePane, Priority.ALWAYS);
        HBox.setHgrow(artistImage, Priority.ALWAYS);

        this.otherArtist = new HBox(8);
        this.artistImage1 = new ImageView(); // create loadImage method
        this.artist1 = new Button("Top Tracks");
        this.artistImage2 = new ImageView();
        this.artist2 = new Button("Top Albums");
        this.artistImage3 = new ImageView();
        this.artist3 = new Button("Similar Artists");
        this.otherArtist.getChildren().addAll(artist1, artist2, artist3);
        HBox.setHgrow(artist1, Priority.NEVER);
        otherArtist.setSpacing(10);

        artist1.setTextOverrun(OverrunStyle.CLIP);
        artist2.setTextOverrun(OverrunStyle.CLIP);
        artist3.setTextOverrun(OverrunStyle.CLIP);


        this.getChildren().addAll(search, artistInfo, otherArtist);

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        searchButton.setOnAction((event) -> {
            loadReleases();
        });

        artist1.setOnAction((event) -> {
            topTracks();
        });

        artist2.setOnAction((event) -> {
            topAlbums();
        });

        artist3.setOnAction((event) -> {
            similarArtists();
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
                    ArtistOutput artistList  = gson.fromJson(json, ArtistOutput.class);
                    if (artistList != null && artistList.artists != null) {
                        artist = artistList.artists.get(0); // gets first artist
                        for (int i = 0; i < 5; i++) {
                            if (artist.tags.size() < i + 1
                                || artist.tags.get(i).toString() == null) {
                                i = 5;
                            }
                        }
                        loadPage(artist);
                        loadImage(artist);
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

    public void topTracks() {
        String term = URLEncoder.encode(searchEngine.getText(), StandardCharsets.UTF_8);
        String query = "?method=artist.gettoptracks&artist=" + term + "&api_key=" + API_KEY
            + "&format=json";
        String createUrl = "http://ws.audioscrobbler.com/2.0/" + query;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(createUrl))
                .build();
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            Gson gson = new Gson();
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                String json = response.body();
                //System.out.println(json);
                TopTracksResponse topTracksResp = gson.fromJson(json, TopTracksResponse.class);
                if (topTracksResp != null && topTracksResp.toptracks != null &&
                    topTracksResp.toptracks.track != null) {
                    releaseInfo.getChildren().add(new Text("\nTop Tracks: "));
                    Text trackList;
                    for (int i = 0; i < 5; i++) {
                        //if (artist.tracks.size() - 1 < i) {
                        //    i = 5;
                        //} else {
                        trackList = new Text("\n\t+" + topTracksResp.toptracks.track.get(i).name);
                        releaseInfo.getChildren().add(trackList);
                        //}
                    }
                    releasePane.layout();
                } else {
                    System.out.println("Cannot retrieve track list");
                }
            } else {
                System.out.println("Status Code error: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Cannot retrieve tracks");
        }
    }

    public void topAlbums() {
        String term = URLEncoder.encode(searchEngine.getText(), StandardCharsets.UTF_8);
        String query = "?method=artist.gettopalbums&artist=" + term + "&api_key=" + API_KEY
             + "&format=json";
        String createUrl = "http://ws.audioscrobbler.com/2.0/" + query;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(createUrl))
                .build();
            HttpResponse<String> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofString());
            Gson gson = new Gson();
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                String json = response.body();
                TopAlbumsResponse topAlbumsResp = gson.fromJson(json, TopAlbumsResponse.class);
                if (topAlbumsResp != null && topAlbumsResp.topalbums != null
                    && topAlbumsResp.topalbums.album != null) {
                    releaseInfo.getChildren().add(new Text("\nTop Albums: "));
                    Text trackList;
                    for (int i = 0; i < 3; i++) {
                        trackList = new Text("\n\t+" + topAlbumsResp.topalbums
                            .album.get(i).name);
                        releaseInfo.getChildren().add(trackList);
                    }
                } else {
                    System.out.println("Cannot retrieve album list");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Cannot retrieve albums");
        }

    }

    public void similarArtists() {
    }

    public void loadPage(Artist artistName) {
        Text name = new Text("\n Name: " + artistName.name);
        Text country = new Text("\n Country: " + artistName.country);
        this.releaseInfo.getChildren().addAll(name, country);
        this.releaseInfo.getChildren().add(new Text("\n Tags: "));
        for (int i = 0; i < 5; i++) {
            if (artistName.tags.size() < i + 1
                || artistName.tags.get(i).toString() == null) { // add try catch for this
                i = 5;

            } else {
                releaseInfo.getChildren().add(new Text("\n\t+ "
                    + artistName.tags.get(i).toString()));
            }
        }
    }

    public void loadImage(Artist artistName) {
        new Thread(() ->
        {
            String term = URLEncoder.encode(searchEngine.getText(), StandardCharsets.UTF_8);
            String query = "?method=artist.getinfo&artist=" + term + "&api_key=" + API_KEY
                + "&format=json";
            String createUrl = "http://ws.audioscrobbler.com/2.0/" + query;
            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(createUrl))
                    .build();
                HttpResponse<String> response = HTTP_CLIENT
                    .send(request, BodyHandlers.ofString());
                Gson gson = new Gson();
                int statusCode = response.statusCode();
                if (statusCode == 200) {
                    String json = response.body();
                    String imageUrl = parseJson(json);
                    if (imageUrl != null) {
                        Platform.runLater(() -> {
                            artistImage.setFitHeight(200);
                            artistImage.setFitWidth(250);
                            artistImage.setImage(new Image(imageUrl));
                        });
                    } else {
                        System.out.println("Cannot retrieve artist image url.");
                    }
                } else {
                    System.out.println("2. Cannot retrieve artist image.");
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("1. Cannot retrieve artist image.");
            }
        }).start();
    }

    public void setButtonImages(String imageUrl) {
        artist1.setPrefWidth(205);
        artist2.setPrefWidth(205);
        artist3.setPrefWidth(205);

        Image image = new Image(imageUrl);

        artistImage1.setFitHeight(100);
        artistImage1.setFitWidth(100);
        artistImage1.setImage(image);
        artist1.setGraphic(artistImage1);

        artistImage2.setFitHeight(100);
        artistImage2.setFitWidth(100);
        artistImage2.setImage(image);
        artist2.setGraphic(artistImage2);

        artistImage3.setFitHeight(100);
        artistImage3.setFitWidth(100);
        artistImage3.setImage(image);
        artist3.setGraphic(artistImage3);
    }

    public String parseJson(String toParse) {
        int begin = 8 + toParse.indexOf("#text");
        int end = 3 + toParse.indexOf("png");
        System.out.println(toParse.substring(begin, end));
        return toParse.substring(begin, end);
    }
}
