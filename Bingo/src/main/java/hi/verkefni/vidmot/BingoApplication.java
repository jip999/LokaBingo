package hi.verkefni.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/******************************************************************************
 *  Nafn    : Jakub Ingvar Pitak
 *  T-póstur: jip2@hi.is
 *
 *  Lýsing  : Viðmótsklasi sem býr til og keyrir Bingoappið í nýjum glugga.
 *
 *****************************************************************************/

public class BingoApplication extends Application {
    /**
     * Fall sem býr til gluggaútlit fyrir bingó appið.
     *
     * @param stage Grunnurinn sem glugginn er byggður á
     * @throws IOException Ef það fer eitthvað úrskeyðis
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BingoApplication.class.getResource("bingo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bingo!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Keyrir Bingoappið.
     *
     * @param args ónotað
     */
    public static void main(String[] args)
    {
        launch();
    }
}