package com.bsuir;

import com.bsuir.controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class FxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
            .sources(BootApplication.class)
            .run(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(MainController.class);
        Scene scene = new Scene(root);
        stage.setTitle("Email Client");
        stage.setScene(scene);
        stage.show();
    }

    public void traverse(Node node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print(" ");
        }
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            parent.getChildrenUnmodifiable().forEach(n -> traverse(n, level + 1));
        }
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }
}
