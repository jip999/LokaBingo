package hi.verkefni.vinnsla;

public class BingoDialog
{
    public String getLitirFrom(String s)
    {
        String BGC = "-fx-background-color: ";
        String F = "-fx-fill: ";
        String TF = "-fx-text-fill: ";
        String l1 = "#e8e8e8;"; // dökk Hvítur
        String l2 = "#333333;"; // dökk Grár

        if (s.equals("Dökk"))
            return BGC+l2 + "," +F+l1+ "," +TF+l1;
        else
            return BGC+l1+ "," +F+l2+ "," +TF+l2;
    }
}
