package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import java.io.FileReader;

/**
 * REPLACE WITH NON-SHOUTING DESCRIPTION OF YOUR APP.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;
    ApiAppLoader content;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        root = new VBox();
        content = new ApiAppLoader();
        root.getChildren().add(content);
    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        String homeUrl = "file:resources/homescreen.jpg";
        String button = "file:resources/button.jpg";

        this.stage = stage;

        Image home = new Image(homeUrl);
        content.artistImage.setImage(home);

        content.searchEngine.setPrefWidth(300);
        content.searchButton.setPrefWidth(50);

        content.artist1.setPrefSize(100, 100);
        content.artist2.setPrefSize(100,100);
        content.artist3.setPrefSize(100, 100);
        content.setButtonImages(button);
        content.otherArtist.setAlignment(Pos.CENTER);

        scene = new Scene(root, 625, 400);
        root.setStyle("-fx-background-color: lightblue;");
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        content.setPrefSize(scene.getWidth(), scene.getHeight());
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));

    } // start

} // ApiApp
