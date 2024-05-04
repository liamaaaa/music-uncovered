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
import javafx.scene.control.ContentDisplay;

/**
 * Custom component class to store objects of the scene.
 */
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
    Button artist1;
    ImageView artistImage1;
    Button artist2;
    ImageView artistImage2;
    Button artist3;
    ImageView artistImage3;

    Artist artist;
    Button clearButton;

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static final String API_KEY = "ac27f5d94abeff5961ce56b3d823e86d";

    /**
     * Initializes objects of this ApiAppLoader class.
     * Creates ActionEvents for buttons of the class.
     */
    public ApiAppLoader() {
        super();

        this.search = new HBox(8);
        this.searchLabel = new Label("Enter Artist Name: ");
        this.searchEngine = new TextField("Search");
        this.searchButton = new Button("Go");
        this.clearButton = new Button("Clear");
        this.search.getChildren().addAll(searchLabel, searchEngine, searchButton, clearButton);

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
        this.artist1 = new Button("Top Tracks", artistImage1);
        this.artistImage2 = new ImageView();
        this.artist2 = new Button("Top Albums", artistImage2);
        this.artistImage3 = new ImageView();
        this.artist3 = new Button("Similar Artists", artistImage2);
        this.otherArtist.getChildren().addAll(artist1, artist2, artist3);
        HBox.setHgrow(artist1, Priority.NEVER);
        otherArtist.setSpacing(10);

        this.getChildren().addAll(search, artistInfo, otherArtist);

        this.setPadding(new Insets(10));
        this.setSpacing(10);

        searchButton.setOnAction((event) -> {
            loadReleases();
        });

        clearButton.setOnAction((event) -> releaseInfo.getChildren().clear());

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

    /**
     * Searches MusicBrainz API to store information in {@code Artist} class.
     * Stores name of artist, country of origin, and tags related to them.
     */
    public void loadReleases() {
        searchButton.setDisable(true);
        if (searchEngine.getText().isEmpty()) {
            System.out.println("Empty Search Alert here:");
            emptySearchAlert();
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
                    if (artistList != null && artistList.artists != null
                        && !artistList.artists.isEmpty()) {
                        artist = artistList.artists.get(0); // use artist.name for other methods
                        loadPage();
                        loadImage();
                    } else {
                        nullArtistAlert();
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

    /**
     * Uses Last.fm API to find  top 5 tracks from an artist searched in MusicBrainz API.
     * Adds these tracks to {@code releaseInfo}.
     */
    public void topTracks() {
        String term = URLEncoder.encode(artist.name, StandardCharsets.UTF_8);
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
                TopTracksResponse topTracksResp = gson.fromJson(json, TopTracksResponse.class);
                if (topTracksResp != null && topTracksResp.toptracks != null &&
                    topTracksResp.toptracks.track != null) {
                    releaseInfo.getChildren().add(new Text("\nTop Tracks: "));
                    Text trackList;
                    for (int i = 0; i < 5; i++) {
                        if (topTracksResp.toptracks.track.size() - 1 < i) {
                            i = 5;
                        } else if (topTracksResp.toptracks.track
                            .get(i).name != null) {
                            trackList = new Text("\n\t+"
                            + topTracksResp.toptracks.track.get(i).name);
                            releaseInfo.getChildren().add(trackList);
                        }
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

     /**
      * Uses Last.fm API to find  top 3 albums/EPs/singles from an artist
      *     searched in MusicBrainz API.
      * Adds these tracks to {@code releaseInfo}.
      */
    public void topAlbums() {
        String term = URLEncoder.encode(artist.name, StandardCharsets.UTF_8);
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
                    releaseInfo.getChildren().add(new Text("\nTop Albums, EPs, Singles: "));
                    Text trackList;
                    for (int i = 0; i < 3; i++) {
                        if (topAlbumsResp.topalbums.album.size() - 1 < i) {
                            i = 3;
                        } else if (!topAlbumsResp.topalbums.album.get(i).name.equals("(null)")) {
                            trackList = new Text("\n\t+" + topAlbumsResp.topalbums
                                .album.get(i).name);
                            releaseInfo.getChildren().add(trackList);
                        }
                    }
                } else {
                    System.out.println("Cannot retrieve album list");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Cannot retrieve albums");
        }
    }

     /**
      * Uses Last.fm API to find  top 3 related artists from an artist searched in MusicBrainz API.
      * Adds these tracks to {@code releaseInfo}.
      */
    public void similarArtists() {
        String term = URLEncoder.encode(artist.name, StandardCharsets.UTF_8);
        String query = "?method=artist.getsimilar&artist=" + term + "&api_key=" + API_KEY
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
                TopSimilarResponse topSimilarResp = gson.fromJson(json, TopSimilarResponse.class);
                if (topSimilarResp != null && topSimilarResp.similarartists != null
                    && topSimilarResp.similarartists.artist != null) {
                    releaseInfo.getChildren().add(new Text("\nRelated Acts/Artists: "));
                    Text otherList;
                    for (int i = 0; i < 3; i++) {
                        if (topSimilarResp.similarartists.artist.size() - 1 < i) {
                            i = 3;
                        } else if (topSimilarResp.similarartists.artist.get(i).name != null) {
                            otherList = new Text("\n\t+" + topSimilarResp.similarartists
                                .artist.get(i).name);
                            releaseInfo.getChildren().add(otherList);
                        }
                    }
                } else {
                    System.out.println("Cannot retrieve similar artists list");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Cannot retrieve similar artists");
        }
    }

    /**
     * Loads info about the name, country of origin, and tags for an artist.
     */
    public void loadPage() {
        if (artist != null) {
            Text name = new Text("\n Name: " + artist.name);
            Text country = new Text("\n Country: " + artist.country);
            this.releaseInfo.getChildren().addAll(name, country);
            this.releaseInfo.getChildren().add(new Text("\n Tags: "));
            if (artist != null && artist.tags != null) {
                for (int i = 0; i < 5; i++) {
                    if (artist.tags.size() < i + 1
                        || artist.tags.get(i).toString() == null) { // add try catch for this
                        i = 5;
                    } else {
                        releaseInfo.getChildren().add(new Text("\n\t+ "
                            + artist.tags.get(i).toString()));
                    }
                }
            } else {
                releaseInfo.getChildren().add(new Text(" N/A "));
            }
        } else {
            nullArtistAlert();
        }
    }

    /**
     * Loads related album image to artist from iTunes API.
     */
    public void loadImage() {
        String term = URLEncoder.encode(artist.name, StandardCharsets.UTF_8);
        String limit = URLEncoder.encode("200", StandardCharsets.UTF_8);
        String query = String.format("?term=%s&limit=%s", term, limit);
        String media = "music";
        String createUrl = "https://itunes.apple.com/search" + query
            + "&media=" + media;

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
                ItunesResponse itunesResp = gson.fromJson(json, ItunesResponse.class);
                if (itunesResp != null && itunesResp.results.get(1).artworkUrl100 != null) {
                    artistImage.setImage(new Image(itunesResp.results
                        .get(1).artworkUrl100));
                }
            } else {
                System.out.println("Status Code error: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Cannot retrieve album image");
        }
    }

    /**
     * Sets images for 3 bottom buttons.
     * @param imageUrl Defines that url to set the graphics
     *     of the buttons to.
     */
    public void setButtonImages(String imageUrl) {
        artist1.setPrefWidth(205);
        artist2.setPrefWidth(205);
        artist3.setPrefWidth(205);

        Image image = new Image(imageUrl);

        artistImage1.setFitHeight(100);
        artistImage1.setFitWidth(150);
        artistImage1.setImage(image);
        artist1.setGraphic(artistImage1);
        artist1.setContentDisplay(ContentDisplay.TOP);

        artistImage2.setFitHeight(100);
        artistImage2.setFitWidth(150);
        artistImage2.setImage(image);
        artist2.setGraphic(artistImage2);
        artist2.setContentDisplay(ContentDisplay.TOP);

        artistImage3.setFitHeight(100);
        artistImage3.setFitWidth(150);
        artistImage3.setImage(image);
        artist3.setGraphic(artistImage3);
        artist3.setContentDisplay(ContentDisplay.TOP);
    }

    /**
     * Displays error alert if no search results are found
     *     for an artist.
     */
    private void nullArtistAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("URI: 0 distinct results found.");
        alert.showAndWait();
        searchButton.setDisable(false);
    }

    /**
     * Displays error alert if search bar is empty.
     */
    private void emptySearchAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("URI: Search bar is empty.");
        alert.showAndWait();
        searchButton.setDisable(false);
    }

}
