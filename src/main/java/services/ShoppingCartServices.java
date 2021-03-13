package services;

import encapsulations.Product;
import encapsulations.ShoppingCart;

public class ShoppingCartServices extends ORMDBManage<ShoppingCart>{

    private static ShoppingCartServices instancia;

    private ShoppingCartServices() {
        super(ShoppingCart.class);
    }

    public static ShoppingCartServices getInstancia(){
        if(instancia==null){
            instancia = new ShoppingCartServices();
        }

        return instancia;
    }

    /**/
}
