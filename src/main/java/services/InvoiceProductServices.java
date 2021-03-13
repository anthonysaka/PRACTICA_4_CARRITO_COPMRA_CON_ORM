package services;


import encapsulations.InvoiceProduct;

public class InvoiceProductServices extends  ORMDBManage<InvoiceProduct>{

    private static InvoiceProductServices instancia;

    private InvoiceProductServices() { super(InvoiceProduct.class); }

    public static InvoiceProductServices getInstancia(){
        if(instancia==null){
            instancia = new InvoiceProductServices();
        }

        return instancia;
    }
}
