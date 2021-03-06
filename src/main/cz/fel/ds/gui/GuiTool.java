package cz.fel.ds.gui;

import cz.fel.ds.gui.mainPage.MainPageController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by Tom on 16. 5. 2015.
 */
public class GuiTool extends Application {

    public void start(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage/main_page.fxml"));
        Parent root=loader.load();
        primaryStage.setTitle("NutriOffice");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root,800,600));
        primaryStage.show();
        MainPageController c = loader.getController();
        c.init();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void closeDialog(Control c) {
        Stage stage = (Stage) c.getScene().getWindow();
        stage.close();
    }

    public static LocalDate dateToLocalDate(Date d){
        return new Date(d.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date localDateToDate(LocalDate d){
        return Date.from(Instant.from(d.atStartOfDay(ZoneId.systemDefault())));
    }

    public static Double stringToDouble(String s) throws Exception{
        if(s.equalsIgnoreCase(""))throw new Exception();
        return Double.valueOf(s);
    }
}
