package services;

import encapsulations.Foto;

public class FotoServices extends ORMDBManage<Foto>{

    private static FotoServices instancia;

    private FotoServices() { super(Foto.class); }

    public static FotoServices getInstancia(){
        if(instancia==null){
            instancia = new FotoServices();
        }

        return instancia;
    }

    /**/



}