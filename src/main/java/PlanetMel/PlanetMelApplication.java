package PlanetMel;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import PlanetMel.controller.InitializeISkyController;

public class PlanetMelApplication extends Application {

    private static ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(JavaFXSpringApplication.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void start(Stage stage) throws Exception {
        var initResource = getClass().getResource("/PlanetMel/views/InitializePlanetMel-view.fxml");
        if (initResource == null) {
            throw new IllegalStateException("Cannot find /PlanetMel/views/InitializePlanetMel-view.fxml");
        }

        FXMLLoader loader = new FXMLLoader(initResource);
        loader.setControllerFactory(springContext::getBean);
        Parent root = loader.load();
        InitializeISkyController controller = loader.getController();

        Scene scene;

        if (controller.isMasterExist()) {
            var loginResource = getClass().getResource("/PlanetMel/views/Login.fxml");
            if (loginResource == null) {
                throw new IllegalStateException("Cannot find /PlanetMel/views/Login.fxml");
            }

            FXMLLoader loginLoader = new FXMLLoader(loginResource);
            loginLoader.setControllerFactory(springContext::getBean);
            Parent loginRoot = loginLoader.load();
            scene = new Scene(loginRoot);
            stage.setScene(scene);
            stage.setTitle("PlanetMel Authentication");
            stage.setMinWidth(400.0);
            stage.setMinHeight(500.0);
            stage.setMaxWidth(Double.MAX_VALUE);
            stage.setMaxHeight(Double.MAX_VALUE);
            stage.setResizable(true);
        } else {
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Initialize PlanetMel");
            stage.setMinWidth(1000.0);
            stage.setMinHeight(740.0);
            stage.setMaxWidth(Double.MAX_VALUE);
            stage.setMaxHeight(Double.MAX_VALUE);
            stage.setResizable(true);
        }

        stage.show();
    }
}