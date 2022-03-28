package hi.verkefni.vidmot;


import hi.verkefni.vinnsla.BingoDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Optional;

/******************************************************************************
 *  Nafn    : Jakub Ingvar Pitak
 *  T-póstur: jip2@hi.is
 *
 *  Lýsing  : Viðmótsklasi sem býr til Dialog glugga út frá dialog-view.fxml
 *  skránni.
 *
 *****************************************************************************/
public class BingoDialogPane extends DialogPane
{
    @FXML
    private DialogPane fxDialogPane;
    @FXML
    private Text fxText;
    @FXML
    private VBox fxVBox;
    @FXML
    private ToggleGroup thema, draga, midja;
    @FXML
    private CheckBox fxKross, fxLarett, fxLodrett;

    private final BingoDialog vinnsluTilvisun;

    /**
     * Býr til Dialog glugga út frá dialog-view.fxml skránni
     */
    public BingoDialogPane()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                getResource("dialog-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException exception)
        {
            throw new RuntimeException(exception);
        }
        vinnsluTilvisun = new BingoDialog();
        setDialogTheme();
    }

    /**
     * Skilar stillingu á þemunni
     *
     * @return ljós/dökk þema
     */
    public String getThema()
    {
        RadioButton rb = (RadioButton) thema.getSelectedToggle();
        return rb.getText();
    }

    /**
     * Stillir þemu fyrir dialog
     */
    public void setDialogTheme()
    {
        RadioButton rb = (RadioButton) thema.getSelectedToggle();
        String l = vinnsluTilvisun.getLitirFrom(rb.getText());
        String[] litir = l.split(",");

        fxDialogPane.setStyle(litir[0]);
        fxText.setStyle(litir[1]);

        for (Node n : fxVBox.getChildren())
            for (Node nn : ((HBox) n).getChildren())
                if (nn.getAccessibleRole() == AccessibleRole.TEXT)
                    nn.setStyle(litir[2]);
                else
                    for (Node nnn : ((VBox) nn).getChildren())
                        nnn.setStyle(litir[2]);
    }

    /**
     * Skilar stillingu um hvort talvan eigi að draga
     * tölur eða ekki
     *
     * @return já / nei
     */
    public String getDraga()
    {
        RadioButton rb = (RadioButton) draga.getSelectedToggle();
        return rb.getText();
    }

    /**
     * Skilar stillingu um hvort að miðjureiturinn er
     * gefins
     *
     * @return já / nei
     */
    public String getMidja()
    {
        RadioButton rb = (RadioButton) midja.getSelectedToggle();
        return rb.getText();
    }

    /**
     * Skilar strengja stillingu á hvernig bingo gildir
     *
     * @return strengur lá/lóð og-eða í kross
     */
    public String getBingo()
    {
        String s = "";

        if (fxLarett.isSelected()) s += fxLarett.getText() + ",";
        if (fxLodrett.isSelected()) s += fxLodrett.getText()+ ",";
        if (fxKross.isSelected()) s += fxKross.getText();

        return s;
    }

    /**
     *
     */
    public void setStillingar()
    {
        Dialog<ButtonType> d = new Dialog<>();
        d.setDialogPane(this);
        Optional<ButtonType> utkoma = d.showAndWait();

        if (utkoma.isPresent())
        {
            ButtonBar.ButtonData bGet = utkoma.get().getButtonData();

            if (bGet == ButtonBar.ButtonData.OK_DONE)
                if (!fxLarett.isSelected() && !fxLodrett.isSelected() && !fxKross.isSelected())
                {
                    HBox n = (HBox)fxLarett.getParent().getParent();
                    Label l = (Label) n.getChildren().get(0);
                    l.setText("Bingo - Velja!!");

                    setStillingar();
                }
            if (bGet == ButtonBar.ButtonData.CANCEL_CLOSE) System.exit(0);
        }
    }
}
//Platform.exit();
