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
    //---------------------------------------------
    @FXML
    private GridPane fxGrid;
    @FXML
    private Label fxBingovinningur;
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

    public void ytturTakki(Button b, int px, int py)
    {
        b.setShape(new Circle(1.0, 1.0, 1.0));
        b.setStyle(litir[4] + litir[5]);
        vinnsluTilvisun.aReit(px, py);
        b.setDisable(true);
    }

    public void friMidja()
    {
        if (midja.equals("Já"))
        {
            int midX = (x / 2);
            int midY = ((y - 1)/2);
            int midGrid = (fxGrid.getChildren().size() / 2) + midX;

            Button b = (Button) fxGrid.getChildren().get(midGrid);
            ytturTakki(b, midX, midY);
            b.setText("FREE");
        }
    }

    public void setTakka(int i, int j)
    {
        Button b = (Button) fxGrid.getChildren().get((i * x) + j);
        String num = String.valueOf(spjald[i-1][j]);

        b.setText(num);
        b.setStyle(litir[0] + litir[2] + litir[3]);
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

        // Sæki liti fyrir spjaldið
        String l = vinnsluTilvisun.getThemuLitirFrom(thema);
        litir = l.split(",");

        // Stilli lit á spjaldið
        fxVBox.setStyle(litir[0]);
        fxGrid.setStyle(litir[1] + litir[2]);
        fxBingovinningur.setStyle(litir[3]);

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
        // gef miðjutakkan frítt ef notandi vill
        friMidja();
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
}
