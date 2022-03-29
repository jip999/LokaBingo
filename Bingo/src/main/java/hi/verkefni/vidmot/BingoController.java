package hi.verkefni.vidmot;

import hi.verkefni.vinnsla.Bingospjald;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/******************************************************************************
 *  Nafn    : Jakub Ingvar Pitak
 *  T-póstur: jip2@hi.is
 *
 *  Lýsing  : Viðmótsklasi sem stýrir og breytir útliti á FXML skránni þegar
 *  appið sjálft er í gangi.
 *
 *****************************************************************************/

public class BingoController implements Initializable
{
    private Bingospjald vinnsluTilvisun;
    //---------------------------------------------
    @FXML
    private GridPane fxGrid;
    @FXML
    private Label fxBingovinningur, fxLeikur, fxTala;
    @FXML
    private Button fxBingo, fxUmferd, fxNyrLeikur, fxHaettaLeik;
    @FXML
    private VBox fxVBox;
    //---------------------------------------------
    private String thema;
    private String draga;
    private String bingo;
    private String midja;

    private String[] litir;
    int[][] spjald;

    private int x;
    private int y;
    //---------------------------------------------

    /**
     * Handler fyrir bingo sem fer í gang þegar ýtt er á takka.
     * Breytir útliti á takka, fellur virkni á takka og athugar
     * ef hvort það er bingo.
     * Ef það er bingo þá fellur virkni á öllum spjaldartökkum
     * og vinningsrunan fær sér lit.
     *
     * @param actionEvent button-click(þeger ýtt er á takka)
     */
    @FXML
    public void bingoHandler(ActionEvent actionEvent)
    {
        // Breyti útliti og felli á virkni takka þegar ýtt er á hann
        Button b = (Button) actionEvent.getSource();
        int posX = GridPane.getRowIndex(b) - 1;
        int posY = GridPane.getColumnIndex(b);
        ytturTakki(b, posX, posY);

        // Ef það er bingo þá birti ég BINGO texta og felli virkni á tökkum
        if (vinnsluTilvisun.erBingo(bingo))
        {
            fxBingovinningur.setText("B I N G O !");
            int start = fxGrid.getRowCount() - 1;
            int size = fxGrid.getChildren().size();

            //Fyrir sigurlínu
            int[][]spjald = vinnsluTilvisun.getSpjaldFylki();

            for (int i = start; i < size; i++)
            {
                Node n = fxGrid.getChildren().get(i);
                n.setDisable(true);

                // Breyti lit á sigurlínu hér
                if (spjald[(i / 5) - 1][i % 5] == (-2))
                    n.setStyle(litir[6]);
            }
        }
    }

    public void haettaLeik(ActionEvent e)
    {
        System.exit(0);
    }

    public void nyrLeikur(ActionEvent e)
    {
        Stage stage = (Stage) fxVBox.getScene().getWindow();
        stage.hide();
        grunnur();
        stage.show();
    }

    public void ytturTakki(Button b, int px, int py)
    {
        b.setShape(new Circle(1.0, 1.0, 1.0));
        b.setStyle(litir[4] + litir[5]);
        vinnsluTilvisun.aReit(px, py);
        b.setDisable(true);
    }

    public void friMidja()
    {
        int midX = (x / 2);
        int midY = ((y - 1)/2);
        int midGrid = (fxGrid.getChildren().size() / 2) + midX;
        Button b = (Button) fxGrid.getChildren().get(midGrid);

        if (midja.equals("Já"))
        {
            ytturTakki(b, midX, midY);
            b.setText("FREE");
        }
        else
        {
            setTakka(midX, midY);
        }
    }

    public void setTakka(int i, int j)
    {
        Button b = (Button) fxGrid.getChildren().get((i * x) + j);
        String num = String.valueOf(spjald[i-1][j]);

        b.setText(num);
        b.setShape(null);
        b.setStyle(litir[0] + litir[2] + litir[3]);
        b.setDisable(false);
    }

    public void setLitiSpjald()
    {
        fxVBox.setStyle(litir[0]);
        fxGrid.setStyle(litir[1] + litir[2]);
        fxBingo.     setStyle(litir[1] + litir[2] + litir[3]);
        fxHaettaLeik.setStyle(litir[1] + litir[2] + litir[3]);
        fxNyrLeikur. setStyle(litir[1] + litir[2] + litir[3]);
        fxUmferd.    setStyle(litir[1] + litir[2] + litir[3]);
        fxBingovinningur.setStyle(litir[3]);
        fxLeikur.        setStyle(litir[3]);
        fxTala.          setStyle(litir[3]);
    }

    /**
     * Býr til spjald og setur random tölur á takka.
     * Grunnstillir útlit í bingospjaldinu.
     */
    public void grunnStillaSpjald()
    {
        // Sæki nýtt spjald
        vinnsluTilvisun = new Bingospjald();
        spjald = vinnsluTilvisun.nyttSpjald();

        // Sæki liti fyrir spjaldið og stilli
        String l = vinnsluTilvisun.getThemuLitirFrom(thema);
        litir = l.split(",");
        setLitiSpjald();

        // Set tölur á takkana og stilli lit á tökkum og label reitum
        for (int i = 0; i < y; i++)
            for (int j = 0; j < x; j++)
            {
                if (i != 0) setTakka(i, j);
                else
                {
                    Label t = (Label) fxGrid.getChildren().get(j);
                    t.setStyle(litir[3]);
                }
            }
        friMidja();// gef miðjutakkan frítt ef notandi vill

        // hvernig leikur
        StringBuilder sb= new StringBuilder(bingo);
        sb.deleteCharAt(sb.length() - 1);
        fxLeikur.setText(sb.toString());

        // ekki bingo
        fxBingovinningur.setText("");
    }

    public void grunnur()
    {
        BingoDialogPane bdg = new BingoDialogPane();
        bdg.setStillingar();

        thema = bdg.getThema();
        draga = bdg.getDraga();
        bingo = bdg.getBingo();
        midja = bdg.getMidja();

        x = fxGrid.getColumnCount();
        y = fxGrid.getRowCount();

        grunnStillaSpjald();
    }

    /**
     * Opnar dialog glugga og tekur stillingar fyrir þemu,
     * hvort talvan eigi að draga, og hvernig bingo verður
     * í leiknum.
     *
     * @param url ónotað
     * @param resourceBundle ónotað
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        grunnur();
    }
}
