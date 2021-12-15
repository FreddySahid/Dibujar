package sample;

import javafx.scene.layout.BorderPane;



public class ContenedorPrincipal extends BorderPane {

    private AreaDibujo areaDibujo;
    private barraSuperior ContenedorSuperior;
    public void TeclaEscape(){
        areaDibujo.TeclaEscape();
    }
    public ContenedorPrincipal(){

        ContenedorSuperior = new barraSuperior();
        areaDibujo = new AreaDibujo(ContenedorSuperior) ;
       // crearFormas2(areaDibujo);
        ContenedorSuperior.setAreaDibujo(areaDibujo);

        setTop(ContenedorSuperior);
        setCenter(areaDibujo);
    }

}
