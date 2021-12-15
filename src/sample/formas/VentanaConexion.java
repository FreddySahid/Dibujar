package sample.formas;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Conexion;

import java.util.Hashtable;


public class VentanaConexion extends Stage {
    private Label lblServidor, lblPuerto, lblApodo,  lblImagen;
    private TextField txtServidor, txtPuerto, txtApodo;
    private Button btnConectar;
    private Conexion conexion;
    private ComboBox<String> cbImagenes;
    private final Image IMAGEN_CHICA  = new Image("https://w7.pngwing.com/pngs/129/94/png-transparent-computer-icons-avatar-icon-design-male-teacher-face-heroes-logo.png", 100, 100, true, true);
    private final Image IMAGEN_CHICO  = new Image("https://img2.freepng.es/20180920/yko/kisspng-computer-icons-portable-network-graphics-avatar-ic-5ba3c66df14d32.3051789815374598219884.jpg", 100, 100, true, true);
    private final Image IMAGEN_HOMBRE = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPtENZS1gdjgiNbiX6gLEZPDCiKUXSkSo9SY4ZnWPxGwESsCxZeoXUOIQiFsD8ph-WmAc&usqp=CAU", 100, 100, true, true);
    private final Image IMAGEN_MUJER  = new Image("https://img2.freepng.es/20180612/hv/kisspng-computer-icons-designer-avatar-5b207ebb279901.8233901115288562511622.jpg", 100, 100, true, true);
    private ImageView foto;
    public VentanaConexion(Conexion conexion){
        VBox root = new VBox();
        Scene escena = new Scene(root, 400, 200);
        setScene(escena);
        setTitle("Datos conexión");
        this.conexion = conexion;

        lblServidor = new Label("Servidor");
        lblServidor.setMinWidth(100);
        lblPuerto = new Label(("Puerto"));
        lblPuerto.setMinWidth(100);
        lblApodo = new Label("Usuario");
        lblApodo.setMinWidth(100);
        txtServidor = new TextField("localhost");
        txtPuerto = new TextField("4500");
        txtApodo = new TextField("");
        btnConectar = new Button("Conectar!");
        btnConectar.setOnAction(evt ->{
            if(txtApodo.getText().trim().length() > 0){
                conexion.setServidor(txtServidor.getText());
                conexion.setPuerto(Integer.parseInt(txtPuerto.getText()));
                conexion.setApodo(txtApodo.getText());
                if(conexion.conectar()){
                    this.close();
                }else{
                    Alert errorConexion = new Alert(Alert.AlertType.ERROR);
                    errorConexion.setTitle("Error");
                    errorConexion.setHeaderText("Hubo un error rey");
                    errorConexion.setContentText("No pudo realizar la conexión al servidor");
                    errorConexion.showAndWait();
                }
            }else{
                Alert dialogoError = new Alert(Alert.AlertType.ERROR);
                dialogoError.setTitle("Error");
                dialogoError.setHeaderText("Datos insuficientes");
                dialogoError.setContentText("Debes poner tu nombre puñetas");
                dialogoError.showAndWait();
            }

        });
        txtServidor.setEditable(false);
        txtPuerto.setEditable(false);


        lblImagen = new Label("Imagen:");
        lblImagen.setMinWidth(100);
        cbImagenes = new ComboBox();
        ObservableList<String> items = FXCollections.observableArrayList (
                "CHICA", "CHICO", "HOMBRE", "MUJER");
        Hashtable<String, Image> listaImagenes = new Hashtable();
        listaImagenes.put(items.get(0), IMAGEN_CHICA);
        listaImagenes.put(items.get(1), IMAGEN_CHICO);
        listaImagenes.put(items.get(2), IMAGEN_HOMBRE);
        listaImagenes.put(items.get(3), IMAGEN_MUJER);
        cbImagenes.setItems(items);
        cbImagenes.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String nombre, boolean vacio) {
                super.updateItem(nombre, vacio);
                if (vacio) {
                    setText(null);
                    setGraphic(null);
                } else {
                    imageView.setImage(listaImagenes.get(nombre));
                    setText(nombre);
                    setGraphic(imageView);
                }
            }
        });
        cbImagenes.setOnAction(evt ->{
            String nombre = cbImagenes.getValue();
            Image imagen = listaImagenes.get(nombre);
            //foto.setImage(new Image(imagen.getUrl(),50,50,true,true));
            foto.setImage(imagen);
        });
        foto = new ImageView();
        foto.setFitHeight(50);
        foto.setFitWidth(50);
        foto.setPreserveRatio(true);
        foto.setSmooth(true);

        HBox filaServidor = new HBox();
        filaServidor.getChildren().addAll(lblServidor, txtServidor);
        filaServidor.setSpacing(10);
        filaServidor.setPadding(new Insets(5));
        HBox filaPuerto = new HBox();
        filaPuerto.getChildren().addAll(lblPuerto, txtPuerto);
        filaPuerto.setSpacing(10);
        filaPuerto.setPadding(new Insets(5));
        HBox filaUsuario = new HBox();
        filaUsuario.getChildren().addAll(lblApodo, txtApodo);
        filaUsuario.setSpacing(10);
        filaUsuario.setPadding(new Insets(5));
        HBox filaFoto = new HBox();
        filaFoto.getChildren().addAll(lblImagen, cbImagenes, foto);
        filaFoto.setSpacing(10);
        filaFoto.setPadding(new Insets(5));
        root.setPadding(new Insets(10));
        root.getChildren().addAll(filaServidor,filaPuerto,filaUsuario, filaFoto, btnConectar);

    }
}
