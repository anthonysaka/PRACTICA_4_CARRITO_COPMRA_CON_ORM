package services;

import encapsulations.Product;
import encapsulations.User;

public class UserServices extends ORMDBManage<User>{

    private static UserServices instancia;

    private UserServices() {
        super(User.class);
    }

    public static UserServices getInstancia(){
        if(instancia==null){
            instancia = new UserServices();
        }
        return instancia;
    }

    /**/

    public User checkLoginUser(String username, String password) {
        User auxUser = find(username);//search by id object

        if (auxUser == null){
            return null;
        } else {
            auxUser = auxUser.getPassword().equalsIgnoreCase(password) ? auxUser : null;
            return  auxUser;
        }


    }


}
