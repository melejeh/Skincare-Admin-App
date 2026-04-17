package PlanetMel;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Objects;


public final class SceneNavigator {
    private SceneNavigator() {}
    private static ConfigurableApplicationContext springContext;
    public static void setRoot(Scene scene, String fxmlResource, String title, double width, double height) {



        try {
            Objects.requireNonNull(scene);
            ApplicationContext ctx = PlanetMelApplication.getSpringContext();

            FXMLLoader loader = new FXMLLoader(
                    SceneNavigator.class.getClassLoader().getResource(
                            fxmlResource.startsWith("/") ? fxmlResource.substring(1) : fxmlResource));
            loader.setControllerFactory(ctx::getBean);

            Parent root = loader.load();

            scene.setRoot(root);

            Stage stage = (Stage) scene.getWindow();
            stage.setMinWidth(width);
            stage.setMinHeight(height);
            stage.setMaxWidth(width);
            stage.setMaxHeight(height);

        } catch (IOException e) {
            System.out.println("Unable to load FXML: " + fxmlResource);
            throw new RuntimeException("Unable to load FXML: " + fxmlResource, e);
        }
    }
}
