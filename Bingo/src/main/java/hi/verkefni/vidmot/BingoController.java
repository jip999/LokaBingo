package hi.verkefni.vidmot;

import hi.verkefni.vinnsla.Bingospjald;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
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
    private BingoDialogPane bdg;
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
    private String thema, draga, bingo, midja;

    private String[] litir;
    private int[][] spjald;
    private int[] randTolur;

    private int x, y, tel;
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
        ytturTakki(b, posX, posY, "");

        // Ef það er bingo þá birti ég BINGO texta og felli virkni á tökkum
        if (!draga.equals("Já")) efBingo();
    }

    public void haettaLeik()
    {
        System.exit(0);
    }

    public void nyrLeikur()
    {
        Stage stage = (Stage) fxVBox.getScene().getWindow();
        stage.hide();
        grunnur();
        stage.show();
    }

    public void naestaUmferd()
    {
        String talaText = "Tala: ";
        if (tel < 75)
            fxTala.setText(talaText + randTolur[tel++]);
        else
        {
            fxTala.setText(talaText + randTolur[tel - 1] + " F");
            fxUmferd.setDisable(true);
        }
    }

    public void ytturTakki(Button b, int px, int py, String midTala)
    {
        if (draga.equals("Nei"))
        {
            b.setShape(new Circle(1.0, 1.0, 1.0));
            b.setStyle(litir[4] + litir[5]);
            vinnsluTilvisun.aReit(px, py);
            b.setDisable(true);
        }
        else
            for (int i = 0; i < tel; i++)
                if (String.valueOf(randTolur[i]).equals(b.getText()))
                {
                    b.setShape(new Circle(1.0, 1.0, 1.0));
                    b.setStyle(litir[4] + litir[5]);
                    vinnsluTilvisun.aReit(px, py);
                    b.setDisable(true);
                }
    }

    public void efBingo()
    {
        if (vinnsluTilvisun.erBingo(bingo))
        {
            fxUmferd.setDisable(true);
            fxBingo.setDisable(true);

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

    public void friMidja()
    {
        int midX = (x / 2);
        int midY = ((y - 1)/2);
        int midGrid = (fxGrid.getChildren().size() / 2) + midX;
        Button b = (Button) fxGrid.getChildren().get(midGrid);

        if (midja.equals("Já"))
        {
            b.setText("FREE");
            b.setShape(new Circle(1.0, 1.0, 1.0));
            b.setStyle(litir[4] + litir[5]);
            vinnsluTilvisun.aReit(midX, midY);
            b.setDisable(true);
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

    public void setTolurSpjald()
    {
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
    }

    public void setFxUmferd()
    {
        tel = 0;
        if (draga.equals("Já"))
        {
            fxUmferd.setDisable(false);
            randTolur = vinnsluTilvisun.handahofsTolur();
            naestaUmferd();
        }
        else
        {
            fxUmferd.setDisable(true);
            randTolur = null;
            fxTala.setText("Ódregið");
        }
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

        // hvernig leikur
        StringBuilder sb= new StringBuilder(bingo);
        sb.deleteCharAt(sb.length() - 1);
        fxLeikur.setText(sb.toString());

        // Sæki liti fyrir spjaldið
        String l = vinnsluTilvisun.getThemuLitirFrom(thema);
        litir = l.split(",");

        setLitiSpjald();
        setTolurSpjald();
        setFxUmferd();
        friMidja();

        fxBingo.setDisable(!draga.equals("Já"));
        fxBingovinningur.setText("");
    }

    public void grunnur()
    {
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
        bdg = new BingoDialogPane();
        grunnur();
    }
}
