package hi.verkefni.vinnsla;

import java.util.Random;

/******************************************************************************
 *  Nafn    : Jakub Ingvar Pitak
 *  T-póstur: jip2@hi.is
 *
 *  Lýsing  : Vinnsluklasi sem geymir bingóspjalds föll fyrir
 *  viðmótsklasann BingoController.
 *
 *****************************************************************************/

public class Bingospjald implements BingospjaldInterface
{
    private final int x = 5;
    private final int y = 5;
    private final int[][] spjald = new int[this.x][this.y];

    /**
     * Stillir (i, j) reit sem ýttan á með (-1).
     *
     * @param i x-staðsetning
     * @param j y-staðsetning
     */
    @Override
    public void aReit(int i, int j)
    {
        spjald[i][j] = -1;
    }

    /**
     * Býr til random tölur fyrir nyttSpjald() klasan.
     *
     * @return skilar 'r' mörgun random tölum á bilinu 'r'
     */
    private int[] handahofsTolur()
    {
        Random rand = new Random();
        int r = 75;
        int[] randFylki = new int[r];

        // Temp array með tölum (1,2,3...,r)
        for (int i = 0; i < r; i++)
            randFylki[i] = i + 1;

        for (int i = 0; i < r; i++) // Shuffle
        {
            int randVisitala = rand.nextInt(r);

            int temp = randFylki[randVisitala];
            randFylki[randVisitala] = randFylki[i];
            randFylki[i] = temp;
        }
        return randFylki;
    }

    public String getThemuLitirFrom(String s)
    {
        String BGC = "-fx-background-color: ";
        String BC = "-fx-border-color: ";
        String TF = "-fx-text-fill: ";
        String L1 = "white;";   // ljós Hvítur
        String L2 = "#e8e8e8;"; // dökk Hvítur
        String L3 = "#424242;"; // ljós Grár
        String L4 = "#333333;"; // dökk Grár
        String TL1 = "#00ff00;"; // Grænn
        String TL2 = "#ff0000;"; // Rauður
        String TL3 = "#ff7000;"; // AppGulur

        if (s.equals("Dökk"))
            return BGC+L3+ "," +BGC+L4+ "," +BC+L2+ "," +TF+L2+
                    "," +BGC+TL1+  "," +TF+L4+ "," +BGC+TL3;
        else
            return BGC+L1+ "," +BGC+L2+ "," +BC+L4+ "," +TF+L4+
                    "," +BGC+TL2+ "," +TF+L1+ "," +BGC+TL3;
    }

    public int[][] getSpjaldFylki()
    {
        return spjald;
    }

    @Override
    public int[][] nyttSpjald() // PRÓFA SET
    {
        int[] tempFylki = handahofsTolur();

        // Fyrstu 25 tölur í shuffled temp array fara í spjaldArr
        for (int i = 0; i < (x * y); i++)
            spjald[i / x][i % x] = tempFylki[i];

        return spjald;
    }

    @Override
    public boolean erBingo(String s)
    {
        for (String b : s.split("/"))
            for (int i = 0; i < y; i++)
            {
                // L arr nota ég ef það er bingo til að lita bingo rununa.
                boolean[][] L = new boolean[x][y];

                // ef tala er < 0 í reit á bingo sem er verið að leita eftir þá set ég temporary -2 í lita arr.
                for (int j = 0; j < x; j++)
                {
                    if      (b.equals("Lárétt" ) && spjald[i][j] < 0) L[i][j] = true;
                    else if (b.equals("Lóðrétt") && spjald[j][i] < 0) L[j][i] = true;
                    else if (b.equals("Í Kross") && spjald[j][j] < 0 && (i == 0)) L[j][j] = true;
                    else if (b.equals("Í Kross") && spjald[j][x-1-j] < 0 && (i == 1)) L[j][x-1-j] = true;
                    else break;

                    // ef j kemst upp í 4 (lengd bingo runu) þá set ég -2 á spjaldið fyrir vinnigstölur og enda fallið
                    if (j == (x - 1))
                    {
                        for (int k = 0; k < x; k++)
                            for (int l = 0; l < y; l++)
                                if (L[k][l]) spjald[k][l] = (-2);
                        return true;
                    }
                }
            }
        return false;
    }
}

/*
if (b.equals("Lárétt") && spjaldFylki[i][j] != (-1)) break;
if (b.equals("Lóðrétt") && spjaldFylki[j][i] != (-1)) break;
if (b.equals("Í Kross"))
{
    if ((i == 0) && spjaldFylki[j][j] != (-1)) break;
    if ((i == 1) && spjaldFylki[j][x - 1 - j] != (-1)) break;
    if (i > 1) break;
}

if (b.equals("Lárétt")) lita[i][j] = true;
if (b.equals("Lóðrétt")) lita[j][i] = true;
if (b.equals("Í Kross"))
{
    if (i == 0) lita[j][j] = true;
    if (i == 1) lita[j][x - 1 - j] = true;
}
*/