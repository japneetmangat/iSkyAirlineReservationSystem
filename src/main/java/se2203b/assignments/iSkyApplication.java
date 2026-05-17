package se2203b.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import se2203b.assignments.controller.InitializeISkyController;
import se2203b.assignments.domain.Role;
import se2203b.assignments.domain.UserAccount;
import se2203b.assignments.repo.UserAccountRepository;
import se2203b.assignments.service.UserAccountService;


import java.util.Arrays;
import java.util.List;

public class iSkyApplication extends Application {
    private static ConfigurableApplicationContext springContext;

    @Override
    public void init() {


        // Start Spring here and keep the context in the static field
        springContext =
                new SpringApplicationBuilder(JavaFXSpringApplication.class)
                        .run(getParameters().getRaw().toArray(new String[0]));
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader;
        Scene scene;
        Parent root;
        loader = new FXMLLoader(
                getClass().getResource("/se2203b/assignments/views/InitializeISky-view.fxml"));
        loader.setControllerFactory(springContext::getBean); // <-- uses springContext
        root = loader.load();
        InitializeISkyController controller = loader.getController();

        // Check if already initialized, bounce to login immediately.
        if (controller.isMasterExist()) {
            loader = new FXMLLoader(
                    getClass().getResource("/se2203b/assignments/views/Login.fxml"));
            loader.setControllerFactory(springContext::getBean); // <-- uses springContext
            root = loader.load();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("iSky Authentication");
            stage.setMinWidth(400.0);
            stage.setMinHeight(500.0);
            stage.setMaxWidth(400.0);
            stage.setMaxHeight(500.0);
        } else {

            scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Initialize iSky (one-time)");

            stage.setMinWidth(1000.0);
            stage.setMinHeight(740.0);
            stage.setMaxWidth(1000.0);
            stage.setMaxHeight(740.0);
        }


        //     controller.setScene(scene);


        stage.getIcons().add(new Image("file:src/main/resources/se2203b/assignments/images/WesternLogo.png"));
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
    }
}
