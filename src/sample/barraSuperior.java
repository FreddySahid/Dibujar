package sample;

import com.sun.webkit.ColorChooser;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Optional;

public class barraSuperior extends VBox {

    public static final int SELECCIONAR = 0;
    public static final int LINEA = 1;
    public static final int CIRCULO = 2;
    public static final int CUADRADO = 3;
    public static final int RECTANGULO = 4;
    public static final int POLIGONO = 5;
    public static final int ELIPSE = 6;
    public static final int TRIANGULO = 7;
    public static final int CURVA1 = 8;
    public static final int CURVA2 = 9;
    public static final int ARCO = 10;
    public static final int POLILINEA = 11;
//Es independiente de las de arriba
    public static final int TRASLADAR = 1;
    public static final int ROTAR = 2;
    public static final int ESCALAR = 3;


    private int herramientaSeleccionada = SELECCIONAR;
    private int transformaciónSeleccionada = TRASLADAR;

    private Paint colorBordeSeleccionado = Color.BLACK;
    private Paint colorRellenoSeleccionado = Color.WHITE;
    private int grosorSeleccionado = 1;

    private MenuBar barraMenu;
    private Menu menuArchivo, menuEdicion, menuConexion;
    private MenuItem opNuevo, opSalir, opCopiar, opPegar, opCortar;
    private MenuItem opcionAbrir, opcionGuardar;
    private MenuItem opcionConectar, opcionDesconectar, opcionListaUsuarios;
    private ToolBar barraHerramientas;
    private AreaDibujo areaDibujo;
    private ColorPicker colorBorde, colorRelleno;
    private ComboBox<Integer> cbGrosor;

    public barraSuperior(){
        crearBarraMenu();
        crearBarraHerramientas();
       getChildren().addAll(barraMenu,barraHerramientas);
    }
    public void setAreaDibujo(AreaDibujo areaDibujo){
        this.areaDibujo = areaDibujo;
    }
    public void cambiarColorBorde(Color paint){
        colorBordeSeleccionado = paint;
        colorBorde.setValue( paint);

    }
    public void cambiarColorRelleno(Color paint){
        colorRellenoSeleccionado = paint;
        colorRelleno.setValue( paint);

    }
    public void cambiarGrosorBorde(int grosor){
        grosorSeleccionado = grosor;
        cbGrosor.setValue(grosor);
    }

    private void crearBarraMenu(){
        barraMenu = new MenuBar();
        menuArchivo = new Menu("Archivo");
        menuEdicion = new Menu("Edición");
        opcionAbrir = new MenuItem("Abrir");
        opcionGuardar = new MenuItem("Guardar");
        opNuevo = new MenuItem("Nuevo");
        //ImageView img = new ImageView(new Image("salir",16,16,false,false));
        //img.setFitWidth(8);
        //img.setFitHeight(8);
        opSalir = new MenuItem("Salir", new ImageView(new Image("salir.png")));
        opSalir.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        opSalir.setOnAction(evt -> {
            salirApp();
        });
        opNuevo.setOnAction(evt ->{
            areaDibujo.nuevoDibujo();
        });
        opcionAbrir.setOnAction(evt ->{
            areaDibujo.abrirDibujo();
        });
        opcionGuardar.setOnAction(evt ->{
            areaDibujo.guardarDibujo();
        });
        menuArchivo.getItems().addAll(opNuevo,new SeparatorMenuItem(), opSalir, new SeparatorMenuItem(), opcionAbrir, new SeparatorMenuItem(),opcionGuardar);
        opCortar = new MenuItem("Cortar");
        opCopiar = new MenuItem("Copiar");
        opPegar = new MenuItem("Pegar");
        menuEdicion.getItems().addAll(opCopiar,opCortar,opPegar);

        menuConexion = new Menu("Conexión");
        opcionConectar = new MenuItem("Conectar");
        opcionDesconectar = new MenuItem("Desconectar");
        opcionListaUsuarios = new MenuItem("Lista de Ususarios");
        opcionConectar.setAccelerator(KeyCombination.keyCombination("Ctrl+C"));
        opcionConectar.setOnAction(evt ->{
            areaDibujo.ConectarServidor();
        });
        //opcionDesconectar.setDisable(true);
        //opcionListaUsuarios.setDisable(true);
        opcionListaUsuarios.setOnAction(evt -> {
            areaDibujo.solicitarListaUsuarios();
        });
        menuConexion.getItems().addAll(opcionConectar, opcionDesconectar, new SeparatorMenuItem(), opcionListaUsuarios);
        barraMenu.getMenus().addAll(menuArchivo,menuEdicion, menuConexion);

    }
    private void conectarServidor(){

    }

    private void crearBarraHerramientas(){
        barraHerramientas = new ToolBar();
        Button seleccionar = new Button("", new ImageView(new Image("arrow.png",16,16,true,true)) );
        seleccionar.setOnAction(evt -> {herramientaSeleccionada= SELECCIONAR;});
        seleccionar.setTooltip(new Tooltip("Seleccionar"));
        Button linea = new Button("", new ImageView(new Image("linea.png",16,16,true,true)) );
        linea.setOnAction(evt -> {herramientaSeleccionada= LINEA;});
        linea.setTooltip(new Tooltip("Linea"));
        Button cuadrado = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        cuadrado.setOnAction(evt -> {herramientaSeleccionada= CUADRADO;});
        cuadrado.setTooltip(new Tooltip("Cuadrado"));
        Button rectangulo = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        rectangulo.setOnAction(evt -> {herramientaSeleccionada= RECTANGULO;});
        rectangulo.setTooltip(new Tooltip("Rectangulo"));
        Button circulo = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        circulo.setOnAction(evt -> {herramientaSeleccionada= CIRCULO;});
        circulo.setTooltip(new Tooltip("Circulo"));
        Button elipse = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        elipse.setOnAction(evt -> {herramientaSeleccionada= ELIPSE;});
        elipse.setTooltip(new Tooltip("Elipse"));
        Button triangulo = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        triangulo.setOnAction(evt -> {herramientaSeleccionada= TRIANGULO;});
        triangulo.setTooltip(new Tooltip("Triangulo"));
        Button poligono = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        poligono.setOnAction(evt -> {herramientaSeleccionada= POLIGONO;});
        poligono.setTooltip(new Tooltip("Polígono"));
        Button curva2D = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        curva2D.setOnAction(evt -> {herramientaSeleccionada= CURVA1;});
        curva2D.setTooltip(new Tooltip("Curva cuadratica"));
        Button curva3D = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        curva3D.setOnAction(evt -> {herramientaSeleccionada= CURVA2;});
        curva3D.setTooltip(new Tooltip("Curva cubica"));
        Button arco = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        arco.setOnAction(evt -> {herramientaSeleccionada= ARCO;});
        arco.setTooltip(new Tooltip("Arco"));
        Button Polilinea = new Button("", new ImageView(new Image("cuadrado.png",16,16,true,true)) );
        Polilinea.setOnAction(evt -> {herramientaSeleccionada= POLILINEA;});
        Polilinea.setTooltip(new Tooltip("Polilínea"));

        barraHerramientas.getItems().addAll(seleccionar,linea,cuadrado, rectangulo,circulo,elipse,triangulo,poligono, curva2D,curva3D,arco, Polilinea);

        Label etiColorBorde = new Label("Borde");
        colorBorde = new ColorPicker();
        colorBorde.setOnAction(evt ->{
            colorBordeSeleccionado = colorBorde.getValue();
            if(areaDibujo != null) {
                areaDibujo.cambiarColorBorde(colorBordeSeleccionado);
            }
        });
        colorBorde.setValue((Color) colorBordeSeleccionado);

        Label etiColorRelleno = new Label("Relleno");
        colorRelleno = new ColorPicker();
        colorRelleno.setOnAction(evt ->{
            colorRellenoSeleccionado = colorRelleno.getValue();
            if(areaDibujo != null) {
                areaDibujo.cambiarColorRelleno(colorRellenoSeleccionado);
            }

        });
        colorRelleno.setValue((Color) colorRellenoSeleccionado);

        Label etiGrosor = new Label("Grosor");
        cbGrosor = new ComboBox<Integer>();
        for(int i=1; i<=20; i++) cbGrosor.getItems().add(i);
        cbGrosor.setValue(grosorSeleccionado);
        cbGrosor.setOnAction(evt ->{
            grosorSeleccionado = cbGrosor.getValue();
            if(areaDibujo != null) {
                areaDibujo.cambiarGrosorBorde(grosorSeleccionado);
            }
        });

        barraHerramientas.getItems().addAll(etiColorBorde,colorBorde, etiColorRelleno,colorRelleno,
                etiGrosor,cbGrosor);

        Button trasladar = new Button("", new ImageView(new Image("arrow.png",16,16,true,true)) );
        trasladar.setTooltip(new Tooltip("Trasladar"));
        trasladar.setOnAction(evt -> {transformaciónSeleccionada= TRASLADAR;});
        Button rotar = new Button("", new ImageView(new Image("arrow.png",16,16,true,true)) );
        rotar.setTooltip(new Tooltip("Rotar"));
        rotar.setOnAction(evt -> {transformaciónSeleccionada= ROTAR;});
        Button escalar = new Button("", new ImageView(new Image("arrow.png",16,16,true,true)) );
        escalar.setTooltip(new Tooltip("Escalar"));
        escalar.setOnAction(evt -> {transformaciónSeleccionada= ESCALAR;});
        barraHerramientas.getItems().addAll(trasladar,rotar,escalar);
    }

    public int getHerramientaSeleccionada(){ return herramientaSeleccionada; }
    public int getTransformaciónSeleccionada(){ return transformaciónSeleccionada; }

    public Paint getColorBordeSeleccionado(){ return colorBordeSeleccionado; }

    public Paint getColorRellenoSeleccionado(){ return colorRellenoSeleccionado; }

    public int getGrosorSeleccionado(){ return grosorSeleccionado; }

    private void salirApp(){
        Alert dialogSalir = new Alert(Alert.AlertType.CONFIRMATION);
        dialogSalir.setTitle("Advertencia");
        dialogSalir.setHeaderText("Salir");
        dialogSalir.setContentText("¿Está seguro de querer salir?");
        Optional<ButtonType> result = dialogSalir.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.exit(0);
        }
    }
}
