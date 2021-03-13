import controllers.MainController;
import controllers.StoreController;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import services.DataBaseH2Services;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {

        Javalin app = Javalin.create( config -> {
            //set configs
            config.addStaticFiles("/public");

        }).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");

        DataBaseH2Services.startDb();




        new MainController(app).routesControl();



    }
}
