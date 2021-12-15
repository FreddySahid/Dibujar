package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));


        ContenedorPrincipal contenido = new ContenedorPrincipal();
        ScrollPane root = new ScrollPane();
        root.setContent(contenido);
        root.setFitToHeight(true);
        root.setFitToWidth(true);


        Scene escena = new Scene(root, 1350, 700);
        escena.setOnKeyPressed(evt -> {

                if(evt.getCode() == KeyCode.ESCAPE){
                    contenido.TeclaEscape();
                }

        });

        primaryStage.setTitle("Paint");
        primaryStage.setScene(escena);
        //primaryStage.setResizable(true);
        //primaryStage.setMinWidth(300);
        //primaryStage.setMinHeight(300);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
