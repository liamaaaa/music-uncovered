package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        this.stage = stage;

        content.searchEngine.setPrefWidth(300);
        content.searchButton.setPrefWidth(50);
        content.artist1.setPrefWidth(100);
        content.artist2.setPrefWidth(100);
        content.artist3.setPrefWidth(100);
        // demonstrate how to load local asset using "file:resources/"
        //Image bannerImage = new Image("file:resources/readme-banner.png");
        //ImageView banner = new ImageView(bannerImage);
        //banner.setPreserveRatio(true);
        //banner.setFitWidth(640);

        // some labels to display information
        //Label notice = new Label("Modify the starter code to suit your needs.");

        // setup scene
        //root.getChildren().addAll(banner, notice);
            scene = new Scene(root, 600, 400);

        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        content.setPrefSize(scene.getWidth(), scene.getHeight());
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
        Platform.runLater(() -> this.stage.setResizable(false));

    } // start

} // ApiApp
