package sample;

import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Conexion {

    private String servidor;
    private int puerto;
    private String apodo;
    private Socket conn;
    private BufferedReader lector;
    private BufferedWriter escritor;
    private boolean conectado;
    public Conexion(){
        servidor = "";
        puerto = 0;
        apodo = "";
        conectado = false;
    }
    public Conexion(String servidor, int puerto, String apodo){
        this.servidor = servidor;
        this.apodo = apodo;
        this.puerto = puerto;
        this.conectado = false;
    }

    public boolean conectar(){
        try{
            conn = new Socket(servidor, puerto);
            lector = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            escritor = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            enviarMensaje(apodo);
            conectado = true;

        }catch (Exception e){

        }
        return conectado;
    }

    public boolean estaConectado(){
        return conectado;
    }
    private void enviarMensaje(String mensaje){
        try{
            escritor.write(mensaje+"\n");
            escritor.flush();
        }catch (Exception e){

        }

    }
    public void desconectar(){

    }
    public void enviarforma(String forma){
        enviarMensaje(forma);
    }
    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getApodo() {
        return apodo;
    }

    public void setApodo(String apodo) {
        this.apodo = apodo;
    }

    public Socket getConn() {
        return conn;
    }

    public void setConn(Socket conn) {
        this.conn = conn;
    }

    public BufferedReader getLector() {
        return lector;
    }

    public void setLector(BufferedReader lector) {
        this.lector = lector;
    }

    public BufferedWriter getEscritor() {
        return escritor;
    }

    public void setEscritor(BufferedWriter escritor) {
        this.escritor = escritor;
    }

}
