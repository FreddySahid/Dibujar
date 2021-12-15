package sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.formas.FormaGeometrica;
import sample.formas.VentanaConexion;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class AreaDibujo extends Pane {
    private List<Shape> formas;
    private barraSuperior barraSuperior;
    private boolean dibujando = false;
    private Rectangle area;
    private Shape forma;
    private boolean moviendo = false;
    private boolean rotando = false;
    private boolean escalando = false;
    private boolean agregandoPuntosPoligonos = false;
    private boolean dibujandoCurva2D = false;
    private boolean dibujandoCurva3D = false;
    private boolean dibujandoarco = false;
    private boolean dibujandoCurva3D2 = false;
    private boolean dibujoModificado = false;
    private Circle []puntosLinea;
    private Line []Lineas;
    private double xA, yA;
    private  Conexion conexion;
    private String nombreArchivo="";
    private Thread hormiga;
    public AreaDibujo(barraSuperior barraSuperior){
        this.barraSuperior = barraSuperior;
        this.formas = new ArrayList<Shape>();




        this.puntosLinea  = new Circle[] {new Circle(), new Circle(), new Circle(), new Circle()};
        this.Lineas = new Line[]{new Line(), new Line(), new Line(), new Line()};
        this.area = new Rectangle();
        area.setStroke(Color.BLUE);
        area.setFill(null);
        area.getStrokeDashArray().addAll(3.0,6.0);
        hormiga = new Thread(new HormigasT());
        hormiga.setDaemon(true);
        hormiga.start();
        hormiga.suspend();
        //area.strokeDashOffsetProperty().bind();

        for (int i=0; i<2; i++){
            Lineas[i].setStroke(Color.BLUE);
            Lineas[i].getStrokeDashArray().addAll(4.0,6.0);
        }
        for (int i=0; i<4; i++){
            puntosLinea[i].setRadius(4);
        }
       this.setOnMouseClicked(evt ->{
            double x = evt.getX();
            double y = evt.getY();
            getChildren().removeAll(puntosLinea);
            getChildren().remove(area);
            int herramienta = barraSuperior.getHerramientaSeleccionada();
            if(herramienta == barraSuperior.SELECCIONAR){
                int tam = formas.size();
                for(int i =tam-1; i>=0; i--){
                    Shape f  = formas.get(i);
                    if(f.contains(x,y)){
                        forma = f;
                        Bounds b = forma.getBoundsInParent();
                        puntosLinea[0].setCenterX(b.getMinX()-5);
                        puntosLinea[0].setCenterY(b.getMinY()-5);
                        puntosLinea[1].setCenterX(b.getMaxX()+5);
                        puntosLinea[1].setCenterY(b.getMinY()-5);
                        puntosLinea[2].setCenterX(b.getMaxX()+5);
                        puntosLinea[2].setCenterY(b.getMaxY()+5);
                        puntosLinea[3].setCenterX(b.getMinX()-5);
                        puntosLinea[3].setCenterY(b.getMaxY()+5);

                        area.setX(b.getMinX()-5);
                        area.setY(b.getMinY()-5);
                        area.setWidth(b.getWidth()+10);
                        area.setHeight(b.getHeight()+10);
                        getChildren().add(area);
                        getChildren().addAll(puntosLinea);

                        barraSuperior.cambiarColorBorde((Color) forma.getStroke());
                        barraSuperior.cambiarColorRelleno((Color) forma.getFill());
                        barraSuperior.cambiarGrosorBorde((int) forma.getStrokeWidth());

                        break;

                    }
                }
            }
        });

        this.setOnMousePressed(evt -> {
            double x = evt.getX();
            double y = evt.getY();
            if (evt.isPrimaryButtonDown()) {
                if (y >= 0 && !dibujando) {
                    int herramienta = barraSuperior.getHerramientaSeleccionada();
                    if (herramienta == barraSuperior.SELECCIONAR) {
                        dibujando = false;
                        forma = null;
                    } else if (herramienta == barraSuperior.LINEA) {
                        forma = new Line(x, y, x, y);

                    } else if (herramienta == barraSuperior.CUADRADO) {
                        forma = new Rectangle(x, y, 0, 0);
                    } else if (herramienta == barraSuperior.RECTANGULO) {
                        forma = new Rectangle(x, y, 0, 0);
                    } else if (herramienta == barraSuperior.CIRCULO) {
                        forma = new Circle(x, y, 0);
                    } else if (herramienta == barraSuperior.ELIPSE) {
                        forma = new Ellipse(x, y, 0, 0);
                    } else if (herramienta == barraSuperior.TRIANGULO) {
                        forma = new Polygon(x, y, x, y, x, y);

                    }else if (herramienta == barraSuperior.POLIGONO) {
                        if(!agregandoPuntosPoligonos) {
                            forma = new Polygon(x, y);
                            agregandoPuntosPoligonos = true;
                        }else {
                            Polygon poli = (Polygon) forma;
                            poli.getPoints().addAll(x,y);
                        }
                    }else if(herramienta==barraSuperior.CURVA1){
                        forma = new QuadCurve(x,y,x,y,x,y);
                        //dibujandoCurva2D = true;
                        for(int i=0; i<3; i++){
                            puntosLinea[i].setCenterX(x);
                            puntosLinea[i].setCenterY(y);
                            puntosLinea[i].setRadius(5);
                        }
                        for(int i=0; i<2; i++){
                            Lineas[i].setStartX(x);
                            Lineas[i].setStartY(y);
                            Lineas[i].setEndX(x);
                            Lineas[i].setEndY(y);
                        }
                        if(!getChildren().contains(puntosLinea[0])) {
                            getChildren().add(puntosLinea[0]);
                        }
                        if(!getChildren().contains(puntosLinea[1])) {
                            getChildren().add(puntosLinea[1]);
                        }
                        if(!getChildren().contains(puntosLinea[2])) {
                            getChildren().add(puntosLinea[2]);
                        }
                        if(!getChildren().contains(Lineas[0])) {
                            getChildren().add(Lineas[0]);
                        }
                        if(!getChildren().contains(Lineas[1])) {
                            getChildren().add(Lineas[1]);
                        }

                    }//Aquí se crea la curva
                    else if(herramienta==barraSuperior.CURVA2){
                        forma = new CubicCurve(x,y,x,y, x,y, x, y);
                    }
                    else if (herramienta == barraSuperior.ARCO) {
                        forma = new Arc();
                    }
                    else if(herramienta==barraSuperior.POLILINEA){
                        if(!agregandoPuntosPoligonos) {
                            forma = new Polyline(x, y);
                            agregandoPuntosPoligonos = true;
                        }else {
                            Polyline poli = (Polyline) forma;
                            poli.getPoints().addAll(x,y);
                        }
                    }
                    xA = x;
                    yA = y;
                    if (forma != null) {
                        dibujoModificado = true;
                        forma.setStroke(barraSuperior.getColorBordeSeleccionado());
                        //forma.setFill(barraSuperior.getColorRellenoSeleccionado());
                        Stop [] colores = new Stop[]{
                                new Stop(1, Color.BLACK),
                                new Stop(0, Color.WHITE),
                                //new Stop(6, Color.DARKGRAY),
                               // new Stop(1, Color.BLACK)
                        };
                        //LinearGradient gradiente = new LinearGradient(0.33,0.33,1,0.33, true, CycleMethod.REFLECT, colores);
                        RadialGradient radial = new RadialGradient(0,0,0.5,0.5,1,true, CycleMethod.NO_CYCLE, colores);
                        //forma.setFill(gradiente);
                        Image imagenChida = new Image("arrow.png");
                        ImagePattern patron = new ImagePattern(imagenChida,
                                .3,.3,.8,.8, true);
                        forma.setFill(patron);
                        forma.setStrokeWidth(barraSuperior.getGrosorSeleccionado());


                        if(!getChildren().contains(forma)) {
                            getChildren().add(forma);
                            formas.add(forma);
                        }
                        dibujando = true;
                    }
                }
            }
            else if(evt.isSecondaryButtonDown()){
                xA = x;
                yA = y;
            }
        });

        this.setOnMouseDragged(evt ->{
            double x = evt.getX();
            double y = evt.getY();
            double difX = x - xA;
            double difY = y - yA;
            if(evt.isPrimaryButtonDown()) {
                if (y >= 0) {
                    if (forma != null) {
                        dibujoModificado = true;
                        int herramienta = barraSuperior.getHerramientaSeleccionada();
                        if (herramienta == barraSuperior.SELECCIONAR) {

                        } else if (herramienta == barraSuperior.LINEA && dibujando) {

                            Line linea = (Line) forma;

                            linea.setEndX(x);
                            linea.setEndY(y);

                        } else if (herramienta == barraSuperior.CUADRADO && dibujando) {
                            Rectangle rect = (Rectangle) forma;

                            if (difX < 0) {
                                difX = xA - x;
                                if (difX < difY) {
                                    rect.setX(xA - difY);
                                    rect.setWidth(difY);
                                } else {
                                    rect.setX(xA - difX);
                                    rect.setWidth(difX);
                                }
                            } else {
                                rect.setX(xA);
                                if (difX < difY) {
                                    rect.setWidth(difY);
                                } else {
                                    rect.setWidth(difX);
                                }
                            }
                            if (difY < 0) {
                                difY = yA - y;
                                if (difX < difY) {
                                    rect.setY(yA - difY);
                                    rect.setHeight(difY);
                                } else {
                                    rect.setY(yA - difX);
                                    rect.setHeight(difX);
                                }
                            } else {
                                rect.setY(yA);
                                if (difX < difY) {
                                    rect.setHeight(difY);
                                } else {
                                    rect.setHeight(difX);
                                }
                            }
                        } else if (herramienta == barraSuperior.RECTANGULO && dibujando) {
                            Rectangle rect = (Rectangle) forma;
                            if (difX < 0) {
                                difX = xA - x;
                                rect.setX(xA - difX);
                                rect.setWidth(difX);

                            } else {
                                rect.setX(xA);
                                rect.setWidth(difX);

                            }
                            if (difY < 0) {

                                difY = yA - y;
                                rect.setY(yA - difY);
                                rect.setHeight(difY);

                            } else {

                                rect.setY(yA);
                                rect.setHeight(difY);

                            }
                        } else if (herramienta == barraSuperior.CIRCULO && dibujando) {
                            Circle circulo = (Circle) forma;

                            circulo.setCenterX(xA + (difX / 2.0));
                            circulo.setCenterY(yA + (difY / 2.0));
                            circulo.setRadius(Math.abs(difY) / 2.0);

                        } else if (herramienta == barraSuperior.ELIPSE && dibujando) {
                            Ellipse elipse = (Ellipse) forma;

                            elipse.setCenterX(xA + (difX / 2.0));
                            elipse.setCenterY(yA + (difY / 2.0));
                            elipse.setRadiusX(Math.abs(difX) / 2.0);
                            elipse.setRadiusY(Math.abs(difY) / 2.0);


                        } else if (herramienta == barraSuperior.TRIANGULO && dibujando) {
                            Polygon triangulo = (Polygon) forma;
                            triangulo.getPoints().clear();
                            triangulo.getPoints().addAll(xA, y, xA + (difX / 2), yA, x, y);

                        }else if(herramienta==barraSuperior.CURVA1 && dibujando){
                            QuadCurve Linea2D =(QuadCurve) forma;
                            Linea2D.setControlX(xA +(difX/1.8));
                            Linea2D.setControlY(yA +(difY/1.8));
                            Linea2D.setEndX(x);
                            Linea2D.setEndY(y);

                            puntosLinea[1].setCenterX(xA +(difX/1.8));
                            puntosLinea[1].setCenterY(yA +(difY/1.8));

                            puntosLinea[2].setCenterX(x);
                            puntosLinea[2].setCenterY(y);

                            Lineas[0].setStartX(x);
                            Lineas[0].setStartY(y);

                            Lineas[0].setStartX(x);
                            Lineas[0].setStartY(y);
                            Lineas[1].setEndX(xA +(difX/2));
                            Lineas[1].setEndY(yA +(difY/2));

                        }//Control para dibujar la curva.
                        else if(herramienta == barraSuperior.CURVA2 && dibujando){
                            CubicCurve cubic =  (CubicCurve) forma;
                            //Cordenadas de la primer parte de la curva
                            cubic.setControlX1(xA +(difX/2));
                            cubic.setControlY1(yA +(difY/2));
                            //Cordenadas de la segunda parte de la curva
                            cubic.setControlX2(xA +(difX/2));
                            cubic.setControlY2(yA +(difY/2));
                            cubic.setEndX(x);
                            cubic.setEndY(y);

                        } else if (herramienta == barraSuperior.ARCO && dibujando) {
                            Arc arco = (Arc) forma;

                            arco.setCenterX(xA + (difX / 2.0));
                            arco.setCenterY(yA + (difY / 2.0));
                            arco.setRadiusX(Math.abs(difX) / 2.0);
                            arco.setRadiusY(Math.abs(difY) / 2.0);
                            arco.setStartAngle(45.0f);
                            arco.setLength(y);
                            arco.setType(ArcType.ROUND);



                        }

                    }
                }
            }
            else if(evt.isSecondaryButtonDown()){
                int transformacion = barraSuperior.getTransformaciónSeleccionada();
                if(forma != null){
                    dibujoModificado=true;
                    forma.setStroke(barraSuperior.getColorBordeSeleccionado());
                    forma.setFill(barraSuperior.getColorRellenoSeleccionado());
                    forma.setStrokeWidth(barraSuperior.getGrosorSeleccionado());
                    if(transformacion == barraSuperior.TRASLADAR) {
                        //forma.setRotate(difX/2);
                        forma.setTranslateX(difX);
                        forma.setTranslateY(difY);
                        moviendo = true;
                    }else if(transformacion == barraSuperior.ROTAR){
                        forma.setRotate(difX/2);
                        rotando = true;
                    }else if(transformacion == barraSuperior.ESCALAR){
                        forma.setScaleX(difX/100.0);
                        forma.setScaleY(difY/100.0);
                        escalando=true;
                    }
                }
            }
        });

        this.setOnMouseReleased(evt -> {
            int herramienta = barraSuperior.getHerramientaSeleccionada();
            double x = evt.getX();
            double y = evt.getY();
            double difX = x - xA;
            double difY = y - yA;

            if(forma != null && dibujando && conexion != null &&conexion.estaConectado()){
                Color borde = (Color)forma.getStroke();
                Color relleno = (Color)forma.getFill();
                String tipo = forma.getClass().getName();
                StringBuffer sb = new StringBuffer();
                if (tipo.contains("Line")) {
                    Line l = (Line) forma;
                    sb.append("LINEA,");
                    sb.append(borde.toString());
                    sb.append(",");
                    sb.append(relleno.toString());
                    sb.append(",");
                    sb.append(l.getStrokeWidth());
                    sb.append(",");
                    sb.append(l.getStartX());
                    sb.append(",");
                    sb.append(l.getStartY());
                    sb.append(",");
                    sb.append(l.getEndX());
                    sb.append(",");
                    sb.append(l.getEndY());
                    String cad = sb.toString();
                    conexion.enviarforma(cad);
                }
            }

            if ( moviendo && forma != null){

                if (herramienta == barraSuperior.TRIANGULO) {
                    Polygon triangulo = (Polygon) forma;
                    double x1 = triangulo.getPoints().get(0)+difX;
                    double y1 = triangulo.getPoints().get(1)+difY;
                    double x2 = triangulo.getPoints().get(2)+difX;
                    double y2 = triangulo.getPoints().get(3)+difY;
                    double x3 = triangulo.getPoints().get(4)+difX;
                    double y3 = triangulo.getPoints().get(5)+difY;
                    triangulo.getPoints().clear();
                    triangulo.getPoints().addAll(x1,y1,x2,y2,x3,y3);
                    triangulo.setTranslateX(0);
                    triangulo.setTranslateY(0);
                }else if (herramienta == barraSuperior.CUADRADO) {
                    Rectangle cuadrado = (Rectangle) forma;

                }

            }//Si dejo estos else if no puedo hacer la curva cubica ni la cuadratica
            // Si quito los de la curva cubica ya puedo hacer la cuadratica


            //Si quito estos dos puedo hacer la curva cubica, pero no la puedo dividir en 2.
            else if(forma != null && herramienta != barraSuperior.CURVA1 && herramienta != barraSuperior.CURVA2 && herramienta != barraSuperior.ARCO) {
                dibujando = false;
            }else if(forma != null && dibujando && dibujandoCurva2D){
                dibujando = false;
                dibujandoCurva2D = false;
                getChildren().remove(puntosLinea[0]);
                getChildren().remove(puntosLinea[1]);
                getChildren().remove(puntosLinea[2]);

                getChildren().remove(Lineas[0]);
                getChildren().remove(Lineas[0]);
                getChildren().remove(Lineas[1]);
                getChildren().remove(Lineas[1]);
            } else if(forma != null && dibujando && dibujandoCurva3D){
                dibujando = false;
                dibujandoCurva3D = false;
            } else if(forma != null && dibujando && dibujandoarco){
                dibujando=false;
                dibujandoarco=false;
            }





            moviendo = false;
        });

        this.setOnMouseMoved(evt-> {
            double x = evt.getX();
            double y = evt.getY();
            double difX = x - xA;
            double difY = y - yA;
            int herramienta = barraSuperior.getHerramientaSeleccionada();
            if(dibujando && forma != null && herramienta== barraSuperior.CURVA1){
                QuadCurve Linea2D = (QuadCurve) forma;
                Linea2D.setControlY(x);
                Linea2D.setControlY(y);

                puntosLinea[1].setCenterX(x);
                puntosLinea[1].setCenterY(y);

                Lineas[0].setEndX(x);
                Lineas[0].setEndY(y);
                Lineas[1].setEndX(x);
                Lineas[1].setEndY(y);
                dibujandoCurva2D = true;
            }
            if(dibujando && forma != null && herramienta== barraSuperior.CURVA2){
                CubicCurve cubic = (CubicCurve) forma;
                cubic.setControlX1(x);
                cubic.setControlY1(y);
                cubic.setControlX2(x + (xA / 2));
                cubic.setControlY2(y + (yA / 2));
                dibujandoCurva3D = true;
            }
            if(dibujando && forma != null && herramienta == barraSuperior.ARCO){
                Arc arco = (Arc) forma;
                arco.setLength(y);
                dibujandoarco = true;
            }


        });

    }
    class Hormiga extends Thread{
        public void run(){
            int ii=0;
            while(true){
                area.setStrokeDashOffset(ii++);
                try{
                    sleep(60);
                }catch(Exception e){

                }
            }
        }
    }

    class HormigasT extends Task<Double> {
        @Override
        protected Double call(){
            int ii=0;
            while(true){
                this.updateValue((double)ii++);
                try{
                    Thread.sleep(60);
                }catch(Exception e){

                }
            }
        }
    }
    public void cambiarColorBorde(Paint paint){
        if(forma != null){
            forma.setStroke(paint);
        }

    }
    public void cambiarColorRelleno(Paint paint){
        if(forma != null){
            forma.setFill(paint);
        }

    }
    public void cambiarGrosorBorde(double grosor){
        if(forma != null){
            forma.setStrokeWidth(grosor);
        }
    }
    public void TeclaEscape(){
        agregandoPuntosPoligonos = false;
    }
    public void nuevoDibujo(){
        if(dibujoModificado) {
            Alert dialogSalir = new Alert(Alert.AlertType.CONFIRMATION);
            dialogSalir.setTitle("Advertencia");
            dialogSalir.setHeaderText("Guardar");
            dialogSalir.setContentText("¿Desea guardar los cambios en el dibujo?");
            Optional<ButtonType> result = dialogSalir.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if(guardarDibujo()){
                    limpiar();
                }
            }
        }else {
            limpiar();
        }

    }
    private void limpiar(){
        dibujoModificado = false;
        nombreArchivo = "";
        forma=null;
        formas.clear();
        getChildren().clear();
    }
    public boolean abrirDibujo() {
        boolean abrir = true;
        if (dibujoModificado) {
            Alert dialogoSalir = new Alert(Alert.AlertType.CONFIRMATION);
            dialogoSalir.setTitle("Advertencia");
            dialogoSalir.setHeaderText("Guardar el trabajo actual");
            dialogoSalir.setContentText("¿Desea guardar los cambios en el dibujo?");
            Optional<ButtonType> result = dialogoSalir.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!guardarDibujo())
                    abrir = false;
            }
        }

        if (abrir) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Abrir archivos de dibujo FEI");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Archivos Dibujo", "*.draw",
                            "*.fei"),
                    new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
            Window ventana = getScene().getWindow();
            File archivo = fileChooser.showOpenDialog(ventana);
            if (archivo != null) {
                nombreArchivo = archivo.getPath();
                return abrir();
            } else
                return false;
        }
        return false;
    }
    public boolean abrir() {
        try {
            FileReader fr = new FileReader(nombreArchivo);
            BufferedReader bfr = new BufferedReader(fr);
            String cad = bfr.readLine();
            limpiar();
            while (cad != null && cad.length() > 0) {
                Shape f=null;
                StringTokenizer st = new StringTokenizer(cad, ",");
                String tipoForma = st.nextToken();
                String borde = st.nextToken();
                String relleno = st.nextToken();
                String grosor = st.nextToken();
                if (tipoForma.equals("LINEA")) {
                    double x1 = Double.parseDouble(st.nextToken());
                    double y1 = Double.parseDouble(st.nextToken());
                    double x2 = Double.parseDouble(st.nextToken());
                    double y2 = Double.parseDouble(st.nextToken());
                    f = new Line(x1, y1, x2, y2);
                } else if (tipoForma.equals("CIRCULO")) {
                    double x = Double.parseDouble(st.nextToken());
                    double y = Double.parseDouble(st.nextToken());
                    double radio = Double.parseDouble(st.nextToken());
                    f = new Circle(x, y, radio);
                } else if (tipoForma.equals("ELIPSE")) {
                    double x = Double.parseDouble(st.nextToken());
                    double y = Double.parseDouble(st.nextToken());
                    double radiox = Double.parseDouble(st.nextToken());
                    double radioy = Double.parseDouble(st.nextToken());
                    f = new Ellipse(x, y, radiox, radioy);
                }else if(tipoForma.equals("ARCO")){
                    double x = Double.parseDouble(st.nextToken());
                    double y = Double.parseDouble(st.nextToken());
                    double radiox = Double.parseDouble(st.nextToken());
                    double radioy = Double.parseDouble(st.nextToken());
                    double angulo = Double.parseDouble(st.nextToken());
                    double largo = Double.parseDouble(st.nextToken());
                    f = new Arc(x, y, radiox, radioy, angulo, largo);
                    //Profesor, no encontre la manera de incluir el tipo de arco al abrilo
                    //por eso comente en gusardar dibujo la parte donde se obtiene su tipo.

                }else if(tipoForma.equals("CURVA1")){
                    double Sx = Double.parseDouble(st.nextToken());
                    double Sy = Double.parseDouble(st.nextToken());
                    double Cx = Double.parseDouble(st.nextToken());
                    double Cy = Double.parseDouble(st.nextToken());
                    double Fx = Double.parseDouble(st.nextToken());
                    double Fy = Double.parseDouble(st.nextToken());

                    f = new QuadCurve (Sx,Sy,Cx,Cy,Fx,Fy);
                }else if(tipoForma.equals("CURVA2")){

                    double Sx = Double.parseDouble(st.nextToken());
                    double Sy = Double.parseDouble(st.nextToken());
                    double Cx1 = Double.parseDouble(st.nextToken());
                    double Cy1 = Double.parseDouble(st.nextToken());
                    double Cx2 = Double.parseDouble(st.nextToken());
                    double Cy2 = Double.parseDouble(st.nextToken());
                    double Fx = Double.parseDouble(st.nextToken());
                    double Fy = Double.parseDouble(st.nextToken());

                    f = new CubicCurve (Sx,Sy,Cx1,Cy1,Cx2,Cy2,Fx,Fy);
                }else if (tipoForma.equals("TRIANGULO") || tipoForma.equals("POLIGONO")) {
                    f = new Polygon();
                    while(st.hasMoreTokens())
                        ((Polygon)f).getPoints().add(Double.parseDouble(st.nextToken()));
                }else if(tipoForma.equals("POLILINEA")){
                    f = new Polyline();
                    while(st.hasMoreTokens())
                        ((Polyline)f).getPoints().add(Double.parseDouble(st.nextToken()));
                }
                if (f != null) {
                    f.setStroke(Color.web(borde));
                    f.setFill(Color.web(relleno));
                    f.setStrokeWidth(Double.parseDouble(grosor));
                    formas.add(f);
                    getChildren().add(f);
                }

                cad = bfr.readLine();
            }

            dibujoModificado = false;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean abrirJSON() {
        try {
            FileReader fr = new FileReader(nombreArchivo);
            BufferedReader bfr = new BufferedReader(fr);
            StringBuffer sb = new StringBuffer();
            String cad = bfr.readLine();
            sb.append(cad);
            limpiar();
            while (cad != null && cad.length() > 0) {
                cad = bfr.readLine();
                if (cad != null)
                    sb.append(cad);
            }
            Gson gson = new Gson();
            cad = sb.toString();
            Type tipoListaLineas = new TypeToken<List<FormaGeometrica>>(){}.getType();
            List<FormaGeometrica> lista = gson.fromJson(cad, tipoListaLineas);
            for(FormaGeometrica formaGeometrica : lista) {
                Shape f=null;
                if (formaGeometrica.getTipo().equals("Linea")) {
                    f = new Line(formaGeometrica.getX1(), formaGeometrica.getY1(), formaGeometrica.getX2(),
                            formaGeometrica.getY2());
                }else  if (formaGeometrica.getTipo().equals("Cuadrado")) {
                    f = new Rectangle(formaGeometrica.getX1(), formaGeometrica.getY1(),
                            formaGeometrica.getAncho(), formaGeometrica.getAlto());
                }
                if (f != null) {
                    f.setStrokeWidth(formaGeometrica.getGrosor());
                    f.setStroke(Color.web(formaGeometrica.getColorBorde()));
                    f.setFill(Color.web(formaGeometrica.getColorRelleno()));
                    getChildren().add(f);
                    formas.add(f);
                }
            }
            dibujoModificado = false;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public void solicitarListaUsuarios(){
        final Image IMAGEN_CHICA  = new Image("https://w7.pngwing.com/pngs/129/94/png-transparent-computer-icons-avatar-icon-design-male-teacher-face-heroes-logo.png", 100, 100, true, true);
        final Image IMAGEN_CHICO  = new Image("https://img2.freepng.es/20180920/yko/kisspng-computer-icons-portable-network-graphics-avatar-ic-5ba3c66df14d32.3051789815374598219884.jpg", 100, 100, true, true);
        final Image IMAGEN_HOMBRE = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPtENZS1gdjgiNbiX6gLEZPDCiKUXSkSo9SY4ZnWPxGwESsCxZeoXUOIQiFsD8ph-WmAc&usqp=CAU", 100, 100, true, true);
        final Image IMAGEN_MUJER  = new Image("https://img2.freepng.es/20180612/hv/kisspng-computer-icons-designer-avatar-5b207ebb279901.8233901115288562511622.jpg", 100, 100, true, true);
        ObservableList<String> items = FXCollections.observableArrayList (
                "CHICA", "CHICO", "HOMBRE", "MUJER");
        Hashtable<String, Image> listaImagenes = new Hashtable();
        listaImagenes.put(items.get(0), IMAGEN_CHICA);
        listaImagenes.put(items.get(1), IMAGEN_CHICO);
        listaImagenes.put(items.get(2), IMAGEN_HOMBRE);
        listaImagenes.put(items.get(3), IMAGEN_MUJER);

        ObservableList<Usuario> usuarios = FXCollections.observableArrayList(
                new Usuario("Barbie", "CHICA"),
                new Usuario("Ana", "CHICA"),
                new Usuario("Donald", "HOMBRE"),
                new Usuario("HUGO", "CHICO"),
                new Usuario("LAURA", "MUJER"),
                new Usuario("DIANA", "MUJER"),
                new Usuario("PEDRO", "HOMBRE")

        );

        Stage ventana = new Stage();
        ventana.setTitle("Lista de Usuarios");
        TilePane ListaUsuarios = new TilePane();
        for(Usuario usuario : usuarios) {
            Image imagen = listaImagenes.get(usuario.getFoto());
            ImageView view = new ImageView(imagen);
            Label lbl = new Label(usuario.getApodo());
            lbl.setMinWidth(100);
            //lbl.setMinHeight(10);
            lbl.setAlignment(Pos.CENTER);
            VBox cont = new VBox();
            cont.getChildren().addAll(view,lbl);

            ListaUsuarios.getChildren().add(cont);
        }
        Scene escena= new Scene(ListaUsuarios, 400, 600);
        ventana.setScene(escena);
        ventana.show();
    }

    class Usuario{
        private String Apodo;
        private String foto;
        public Usuario(String Apodo, String foto){
            this.Apodo = Apodo;
            this.foto  =foto;
        }

        public String getApodo() {
            return Apodo;
        }

        public void setApodo(String apodo) {
            Apodo = apodo;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }
    }
    public boolean guardarDibujo() {
        if (nombreArchivo.equals("")) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivos de dibujo FEI");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Archivos Dibujo", "*.draw",
                            "*.fei"),
                    new FileChooser.ExtensionFilter("Todos los archivos", "*.*"));
            Window ventana = getScene().getWindow();
            File archivo = fileChooser.showSaveDialog(ventana);
            if (archivo != null) {
                nombreArchivo = archivo.getPath();
                return guardar();
            } else
                return false;
        } else {
            return guardar();
        }
    }
    public boolean guardarJSON() {
        Gson gson = new Gson();
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append("\n");
        int tamT = formas.size();
        int c=1;
        for(Shape f : formas) {
            Color borde = (Color)f.getStroke();
            Color relleno = (Color)f.getFill();
            String tipo = f.getClass().getName();
            if (tipo.contains("Line")) {
                Line l = (Line) f;
                FormaGeometrica formaGeometrica = new FormaGeometrica(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(),
                        borde.toString(), relleno.toString(), l.getStrokeWidth());
                String cadJSON = gson.toJson(formaGeometrica);
                sb.append(cadJSON);
            }else  if (tipo.contains("Rectangle")) {
                Rectangle r = (Rectangle) f;
                if (r.getWidth() == r.getHeight()) {
                    FormaGeometrica formaGeometrica = new FormaGeometrica(r.getX(), r.getY(),r.getWidth(),
                            borde.toString(), relleno.toString(), r.getStrokeWidth());
                    String cadJSON = gson.toJson(formaGeometrica);
                    sb.append(cadJSON);
                }
            }
            if (c < tamT)
                sb.append(",\n");
            c++;
        }
        sb.append("\n");
        sb.append("]");
        try {
            FileWriter f = new FileWriter(nombreArchivo);
            BufferedWriter bw = new BufferedWriter(f);
            bw.write(sb.toString());
            bw.close();
            f.close();

            dibujoModificado = false;
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean guardar() {
        StringBuffer sb = new StringBuffer();
        for(Shape f : formas) {
            Color borde = (Color)f.getStroke();
            Color relleno = (Color)f.getFill();
            String tipo = f.getClass().getName();
            if (tipo.contains("Line")) {
                Line l = (Line) f;
                sb.append("LINEA,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(l.getStrokeWidth());
                sb.append(",");
                sb.append(l.getStartX());
                sb.append(",");
                sb.append(l.getStartY());
                sb.append(",");
                sb.append(l.getEndX());
                sb.append(",");
                sb.append(l.getEndY());
            } else if (tipo.contains("Circle")) {
                Circle c = (Circle) f;
                sb.append("CIRCULO,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                sb.append(c.getCenterX());
                sb.append(",");
                sb.append(c.getCenterY());
                sb.append(",");
                sb.append(c.getRadius());
            } else if (tipo.contains("Ellipse")) {
                Ellipse e = (Ellipse) f;
                sb.append("ELIPSE,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                sb.append(e.getCenterX());
                sb.append(",");
                sb.append(e.getCenterY());
                sb.append(",");
                sb.append(e.getRadiusX());
                sb.append(",");
                sb.append(e.getRadiusY());
            }else if(tipo.contains("Arc")){
                Arc arco = (Arc) f;
                sb.append("ARCO,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                sb.append(arco.getCenterX());
                sb.append(",");
                sb.append(arco.getCenterY());
                sb.append(",");
                sb.append(arco.getRadiusX());
                sb.append(",");
                sb.append(arco.getRadiusY());
                sb.append(",");
                sb.append(arco.getStartAngle());
                sb.append(",");
                sb.append(arco.getLength());
                //sb.append(",");
                //sb.append(arco.getType());
            }
            else if (tipo.contains("Polygon")) {
                Polygon p = (Polygon) f;
                if (p.getPoints().size() == 6)
                    sb.append("TRIANGULO,");
                else
                    sb.append("POLIGONO,");

                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                int c=1;
                int tp = p.getPoints().size();
                for(double v : p.getPoints()) {
                    sb.append(v);
                    if (c < tp)
                        sb.append(",");
                    c++;
                }
            }else if(tipo.contains("Polyline")){
                Polyline p = (Polyline) f;
                sb.append("POLILINEA,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                int c=1;
                int tp = p.getPoints().size();
                for(double v : p.getPoints()) {
                    sb.append(v);
                    if (c < tp)
                        sb.append(",");
                    c++;
                }
            }else if(tipo.contains("QuadCurve")){
                QuadCurve c =(QuadCurve) f;
                sb.append("CURVA1,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                sb.append(c.getStartX());
                sb.append(",");
                sb.append(c.getStartY());
                sb.append(",");
                sb.append(c.getControlX());
                sb.append(",");
                sb.append(c.getControlY());
                sb.append(",");
                sb.append(c.getEndX());
                sb.append(",");
                sb.append(c.getEndY());
            }else if(tipo.contains("CubicCurve")){
                CubicCurve c = (CubicCurve) f;
                sb.append("CURVA2,");
                sb.append(borde.toString());
                sb.append(",");
                sb.append(relleno.toString());
                sb.append(",");
                sb.append(f.getStrokeWidth());
                sb.append(",");
                sb.append(c.getStartX());
                sb.append(",");
                sb.append(c.getStartY());
                sb.append(",");
                sb.append(c.getControlX1());
                sb.append(",");
                sb.append(c.getControlY1());
                sb.append(",");
                sb.append(c.getControlX2());
                sb.append(",");
                sb.append(c.getControlY2());
                sb.append(",");
                sb.append(c.getEndX());
                sb.append(",");
                sb.append(c.getEndY());
            }

            sb.append("\n");
        }
        try {
            FileWriter f = new FileWriter(nombreArchivo);
            BufferedWriter bw = new BufferedWriter(f);
            bw.write(sb.toString());
            bw.close();
            f.close();

            dibujoModificado = false;
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    private Shape f = null;
   private escuchaServidor hilo = null;

    public void ConectarServidor(){
        conexion = new Conexion();
        VentanaConexion ventanaConexion = new VentanaConexion(conexion);

        ventanaConexion.setOnHiding(evt -> {
            if(conexion != null && conexion.estaConectado()){
                hilo = new escuchaServidor();
                hilo.start();
            }
        });
        ventanaConexion.show();

    }
    class escuchaServidor extends Thread{

        public void run(){
            BufferedReader lector = conexion.getLector();
            while(true){
                try{
                    String cad = lector.readLine();
                    StringTokenizer st = new StringTokenizer(cad, ",>");
                    String id = st.nextToken();
                    String usuario = st.nextToken();
                    String tipo = st.nextToken();
                    String borde = st.nextToken();
                    String relleno = st.nextToken();
                    String grosor = st.nextToken();

                    if(tipo.equals("LINEA")){
                        double x1 = Double.parseDouble(st.nextToken());
                        double y1 = Double.parseDouble(st.nextToken());
                        double x2 = Double.parseDouble(st.nextToken());
                        double y2 = Double.parseDouble(st.nextToken());
                        f = new Line(x1, y1, x2, y2);
                    }
                    if (f != null){
                        f.setStroke(Color.web(borde));
                        f.setFill(Color.web(relleno));
                        f.setStrokeWidth(Double.parseDouble(grosor));
                        Platform.runLater(new Runnable() {
                            public void run() {
                                formas.add(f);
                                getChildren().add(f);
                            }
                        });
                        formas.add(f);
                        getChildren().add(f);
                    }
                }catch (Exception e){

                }
            }
        }
    }


}
