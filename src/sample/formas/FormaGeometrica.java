package sample.formas;

public class FormaGeometrica {
    private String tipo;
    private double x1;
    private double x2;
    private double y1;
    private double y2;
    private double ancho;
    private double alto;
    private double radioX;
    private double radioY;
    private String colorBorde;
    private String colorRelleno;
    private double grosor;
    //Linea
    public FormaGeometrica(double x1, double y1, double x2, double y2,
                           String colorBorde , String colorRelleno, double grosor){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.colorBorde = colorBorde;
        this.colorRelleno = colorRelleno;
        this.grosor = grosor;
        tipo = "Linea";

    }
    //Rectangula
    public FormaGeometrica(double x1, double y1,
                           String colorBorde , String colorRelleno, double grosor,double ancho, double alto){
        this.x1 = x1;
        this.y1 = y1;
        this.ancho = ancho;
        this.alto = alto;
        this.colorBorde = colorBorde;
        this.colorRelleno = colorRelleno;
        this.grosor = grosor;
        tipo = "Rectangulo";

    }


    //Cuadrado
    public FormaGeometrica(double x, double y, double lado,
                           String colorBorde , String colorRelleno, double grosor){
        this.x1 = x;
        this.y1 = y;
        this.ancho = lado;
        this.alto = lado;
        this.colorBorde = colorBorde;
        this.colorRelleno = colorRelleno;
        this.grosor=grosor;
        tipo = "Cuadrado";

    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public String getColorBorde() {
        return colorBorde;
    }

    public void setColorBorde(String colorBorde) {
        this.colorBorde = colorBorde;
    }

    public String getColorRelleno() {
        return colorRelleno;
    }

    public void setColorRelleno(String colorRelleno) {
        this.colorRelleno = colorRelleno;
    }

    public double getGrosor() {
        return grosor;
    }

    public void setGrosor(double grosor) {
        this.grosor = grosor;
    }
    public double getAncho() {
        return ancho;
    }

    public void setAncho(double ancho) {
        this.ancho = ancho;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(double alto) {
        this.alto = alto;
    }

    public double getRadioX() {
        return radioX;
    }

    public void setRadioX(double radioX) {
        this.radioX = radioX;
    }

    public double getRadioY() {
        return radioY;
    }

    public void setRadioY(double radioY) {
        this.radioY = radioY;
    }

}
