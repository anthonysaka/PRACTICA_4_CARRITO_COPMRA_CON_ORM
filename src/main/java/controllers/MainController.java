package controllers;

import encapsulations.*;
import io.javalin.Javalin;
import org.jasypt.util.text.StrongTextEncryptor;
import services.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MainController {
    Javalin app;
    static String tempURI = "";
    static int idInvoice = 0;

    public MainController(Javalin app) throws SQLException {
        this.app = app;

        User admin = new User("sakamoto","admin","admin");
        UserServices.getInstancia().modify(admin);
        /*for(int i=0;i<11;i++){
            ProductServices.getInstancia().create(new Product("TEST"+i,420.20,"TEST PAGINATION TABLE - ORM LAZY FETCH"));
        }*/
    }


    public void routesControl(){
        app.get("/", ctx -> ctx.redirect("/products/1"));

        app.routes(() -> {

            before( ctx -> {
                    if (ctx.cookie("user_remember") != null){
                        StrongTextEncryptor stE = new StrongTextEncryptor();
                        stE.setPassword("myEncryptionPassword");
                        ctx.sessionAttribute("user",stE.decrypt(ctx.cookie("user_remember")));
                    }

                }

            );


            path("/products", () -> {

                before("/", ctx -> {
                   ctx.redirect("/products/1");
                });


                get("/:page", ctx -> {
                    String page = ctx.pathParam("page",String.class).get();
                    List<Product> listProduct = ProductServices.getInstancia().getListProductByPage(page);
                    EntityManager em = ProductServices.getInstancia().getEntityManager();

                    int pageSize = 10;
                    String countQ = "Select count (p.id) from Product p";
                    Query countQuery = em.createQuery(countQ);
                    Long countResults = (Long) countQuery.getSingleResult();
                    int PageTotalNumber = (int) (Math.ceil(countResults / pageSize));

                    Map<String, Object> model = new HashMap<>();
                    model.put("listProduct",listProduct);
                    model.put("pageCant",PageTotalNumber);

                    if (ctx.sessionAttribute("cart") == null){
                        model.put("itemCant",0);
                    } else {
                        int x = 0;
                        List<ShoppingCart> pl = ctx.sessionAttribute("cart");
                        for (ShoppingCart p: pl) {
                            x += p.getCant();
                        }

                        model.put("itemCant",x);
                    }
                    ctx.render("/templates/products.html",model);
                });

            });

            path("/productsadmin", () -> {

                before("/", ctx -> {
                    String auxUser = ctx.sessionAttribute("user");
                    if (auxUser == null){
                        tempURI = ctx.req.getRequestURI();
                        ctx.redirect("/login.html");
                    }
                });

                get("/", ctx -> {
                    List<Product> listProduct = ProductServices.getInstancia().findAll();
                    Map<String, Object> model = new HashMap<>();
                    model.put("listProduct", listProduct);

                    if (ctx.sessionAttribute("cart") == null) {
                        model.put("itemCant", 0);
                    } else {
                        int x = 0;
                        List<ShoppingCart> pl = ctx.sessionAttribute("cart");
                        for (ShoppingCart p : pl) {
                            x += p.getCant();
                        }

                        model.put("itemCant", x);
                    }
                    ctx.render("/templates/adminProducts.html",model);
                });

                before("/saleshistory", ctx -> {
                    String auxUser = ctx.sessionAttribute("user");
                    if (auxUser == null){
                        tempURI = ctx.req.getRequestURI();
                        ctx.redirect("/login.html");
                    }
                });

                get("/saleshistory", ctx -> {
                    List<InvoiceProduct> invp = InvoiceProductServices.getInstancia().findAll();
                    Map<String, Object> model = new HashMap<>();
                    model.put("listInvoice", invp);

                    if (ctx.sessionAttribute("cart") == null){
                        model.put("itemCant",0);
                    } else {
                        int x = 0;
                        List<ShoppingCart> pl = ctx.sessionAttribute("cart");
                        for (ShoppingCart p: pl) {
                            x += p.getCant();
                        }

                        model.put("itemCant",x);
                    }
                    System.out.println("LEGUEEE");
                    ctx.render("/templates/historialProduct.html",model);
                });

                get("/delete/:id", ctx -> {
                    Long id = ctx.pathParam("id",Long.class).get();
                    ProductServices.getInstancia().delete(id);
                    ctx.redirect("/productsadmin");
                });

                get("/edit/:id", ctx -> {
                    Long id = ctx.pathParam("id",Long.class).get();
                    Product auxProd = ProductServices.getInstancia().find(id);
                    Map<String, Object> model = new HashMap<>();
                    model.put("auxProd",auxProd);

                    ctx.render("/templates/editProduct.html",model);

                });

                post("/edit", ctx -> {
                   Long id = ctx.formParam("idProduct", Long.class).get();
                   String name = ctx.formParam("nameProduct");
                   Double price = ctx.formParam("priceProduct",Double.class).get();
                   String descrip = ctx.formParam("descriptionProduct");
                   Product p = new Product(id,name,price,descrip);
                    ProductServices.getInstancia().modify(p);

                    ctx.redirect("/productsadmin");

                });

                post("/add", ctx -> {
                    String name = ctx.formParam("nameProduct");
                    Double price = ctx.formParam("priceProduct",Double.class).get();
                    String descrip = ctx.formParam("descriptionProduct");

                    Product p = new Product(name,price,descrip);
                    ProductServices.getInstancia().create(p);

                    ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                        try {
                            byte[] bytes = uploadedFile.getContent().readAllBytes();
                            String encodedString = Base64.getEncoder().encodeToString(bytes);
                            Foto foto = new Foto(uploadedFile.getFilename(), uploadedFile.getContentType(), encodedString, p);
                            FotoServices.getInstancia().create(foto);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    ctx.redirect("/productsadmin");
                });

            });


            path("/addtocart", () -> {

                post("/", ctx -> {
                    Long id = ctx.formParam("id", Long.class).get();
                    Integer cant = ctx.formParam("cant", Integer.class).get();
                    Product auxProd = ProductServices.getInstancia().find(id);


                    if (ctx.sessionAttribute("cart") == null){
                        ArrayList<ShoppingCart> p = new ArrayList<>();
                        p.add(new ShoppingCart(auxProd,cant));
                        ctx.sessionAttribute("cart",p);
                    } else {
                        ArrayList<ShoppingCart> p = ctx.sessionAttribute("cart");
                        p.add(new ShoppingCart(auxProd,cant));
                        ctx.sessionAttribute("cart",p);
                    }
                    ctx.redirect("/products/1");
                });

            });

            path("/cart", () -> {

                get("/", ctx -> {
                    List<ShoppingCart> listProductCart = ctx.sessionAttribute("cart");
                    Map<String, Object> model = new HashMap<>();
                    model.put("listProductCart",listProductCart);

                    if (ctx.sessionAttribute("cart") == null){
                        model.put("itemCant",0);
                    } else {
                        int x = 0;
                        List<ShoppingCart> pl = ctx.sessionAttribute("cart");
                        for (ShoppingCart p: pl) {
                            x += p.getCant();
                        }

                        model.put("itemCant",x);
                    }
                    Double total = 0.0;
                    if (listProductCart !=null){
                        for (ShoppingCart p: listProductCart) {
                            total += (p.getProduct().getPrice()*p.getCant());
                        }
                        model.put("total",total);
                    }


                    ctx.render("/templates/shoppingCart.html",model);
                });

                post("/delete",ctx -> {
                    Long id = ctx.formParam("id",Long.class).get();
                    Integer cant = ctx.formParam("cant",Integer.class).get();
                    List<ShoppingCart> listProductCart = ctx.sessionAttribute("cart");
                    System.out.println(id);
                    System.out.println(cant);
                    System.out.println(listProductCart.get(0).getProduct().getId());
                    System.out.println(listProductCart.get(0).getCant());

                    for (int x = 0;x<listProductCart.size();x++){
                        if (listProductCart.get(x).getCant().equals(cant) && listProductCart.get(x).getProduct().getId().equals(id)){
                            listProductCart.remove(x);
                            break;
                        }
                    }

                    ctx.redirect("/cart");
                });

                post("/pay", ctx -> {
                    String name = ctx.formParam("name");
                    List<ShoppingCart> listProductCart = ctx.sessionAttribute("cart");

                    if (listProductCart != null){
                        Date date1 = new Date();
                        java.sql.Date date = new java.sql.Date(date1.getTime());
                        float pr = 0.0f;
                        for (ShoppingCart p : listProductCart) {
                            pr += p.getProduct().getPrice()*p.getCant();

                        }
                        InvoiceProduct invp = new InvoiceProduct(name,listProductCart,date,pr);
                        InvoiceProductServices.getInstancia().create(invp);

                        for (ShoppingCart p : listProductCart) {
                            ShoppingCart x = new ShoppingCart(p.getProduct(),p.getCant(),invp);
                            ShoppingCartServices.getInstancia().create(x);
                        }

                        ctx.sessionAttribute("cart",null);

                        ctx.redirect("/products");

                    }

                });

            });


            path("/authenticate", () -> {

                before("/", ctx -> {
                    String username = ctx.formParam("username");
                    String password = ctx.formParam("password");
                    User auxUser = UserServices.getInstancia().checkLoginUser(username,password);

                    if (auxUser == null){
                        ctx.redirect("/401.html");

                    }else{
                        ctx.attribute("userFound", auxUser.getUsername());

                        if (ctx.formParam("chkRemember") != null){
                            StrongTextEncryptor stE = new StrongTextEncryptor();
                            stE.setPassword("myEncryptionPassword");
                            String userEncryp = stE.encrypt(auxUser.getUsername());
                            ctx.cookie("user_remember", userEncryp,604800);
                        }
                    }
                });

                post("/", ctx -> {
                    ctx.sessionAttribute("user",ctx.attribute("userFound"));
                    ctx.redirect(tempURI);
                });

            });

            path("/logout", () -> {
                get("/",ctx -> {
                    if (ctx.sessionAttribute("user") != null){
                        ctx.sessionAttribute("user",null);
                        ctx.req.getSession().invalidate();
                    }
                    if (ctx.cookie("user_remember") != null){
                        ctx.removeCookie("user_remember");
                    }
                    ctx.redirect("/");
                });

            });

            path("/productsadmin/addproduct", () -> {

                before("/", ctx -> {
                    String auxUser = ctx.sessionAttribute("user");
                    if (auxUser == null){
                        tempURI = ctx.req.getRequestURI();
                        ctx.redirect("/login.html");
                    }
                });

                get("/", ctx -> {
                    ctx.render("/templates/addProduct.html");
                });

            });

            path("/productDetails/:id", () -> {

                get("/", ctx -> {
                    Long id = ctx.pathParam("id",Long.class).get();
                    Map<String, Object> model = new HashMap<>();

                    Product p = ProductServices.getInstancia().find(id);
                    model.put("prod",p);

                    List<Comment> comment = CommentServices.getInstancia().getListComments(id);
                    model.put("comment",comment);
                    model.put("user",ctx.sessionAttribute("user"));

                    ctx.render("/templates/productDetails.html",model);
                });


            });

            path("/login",()->{
                before("/",ctx -> {
                    tempURI = "/products/1";
                });
                get("/", ctx -> {
                    ctx.redirect("/login.html");
                });
            });

            path("/comment",()->{
                post("/add", ctx -> {
                    Long id = ctx.formParam("id", Long.class).get();
                    Product p = ProductServices.getInstancia().find(id);
                    String co = ctx.formParam("content",String.class).get();

                    Comment c = new Comment(p,co);
                    CommentServices.getInstancia().create(c);

                    ctx.redirect("/productDetails/"+id);
                });

                post("/delete/:id", ctx -> {
                    Long id = ctx.formParam("id", Long.class).get();
                    Long idProd = ctx.formParam("id", Long.class).get();

                    CommentServices.getInstancia().delete(id);

                    ctx.redirect("/productDetails/"+idProd);
                });
            });


        });



    }
}
