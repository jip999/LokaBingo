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
     * Býr til random tölur shuffle'aðar fyrir bingospjald.
     * Shuffle'ar 1-15 saman, svo 16-30, ..., 61, 75.
     *
     * @return skilar fylki með 75 shuffled tölum ready fyrir bingospjald
     */
    public int[] handahofsTolur()
    {
        Random rand = new Random();
        int r = 75;
        int[] randFylki = new int[r];

        for (int i = 0; i < r; i++) randFylki[i] = i + 1;

        // Shuffle (1-15 saman, 16-30 saman, o.s.frv)
        for (int i = 0; i < r; i++)
        {
            int xx = rand.nextInt(15);

            switch (i / 15)
            {
                case 1 -> xx += 15;
                case 2 -> xx += 30;
                case 3 -> xx += 45;
                case 4 -> xx += 60;
            }

            int temp = randFylki[xx];
            randFylki[xx] = randFylki[i];
            randFylki[i] = temp;
        }
        return randFylki;
    }

    /**
     * Skilar litapalletu fyrir litaþemu leiks
     *
     * @param s litaþema
     * @return Skilar litapalletu fyrir litaþemu leiks
     */
    public String getThemuLitirFrom(String s)
    {
        String BGC,BC,TF,L1,L2,L3,L4,TL1,TL2,TL3;

        BGC = "-fx-background-color: ";
        BC = "-fx-border-color: ";
        TF = "-fx-text-fill: ";
        L1 = "white;";   // ljós Hvítur
        L2 = "#e8e8e8;"; // dökk Hvítur
        L3 = "#424242;"; // ljós Grár
        L4 = "#333333;"; // dökk Grár
        TL1 = "#00ff00;"; // Grænn
        TL2 = "#ff0000;"; // Rauður
        TL3 = "#ff7000;"; // AppGulur

        if (s.equals("Dökk"))
            return BGC+L3+ "," +BGC+L4+ "," +BC+L2+ "," +TF+L2+
                    "," +BGC+TL1+  "," +TF+L4+ "," +BGC+TL3;
        else
            return BGC+L1+ "," +BGC+L2+ "," +BC+L4+ "," +TF+L4+
                    "," +BGC+TL2+ "," +TF+L1+ "," +BGC+TL3;
    }

    /**
     * Skilar spjaldi
     *
     * @return skilar spjaldi
     */
    public int[][] getSpjaldFylki()
    {
        return spjald;
    }

    /**
     * Býr til, frumstillir og skilar tölum fyrir nýtt spjald
     *
     * @return skilar tölum fyrir nýtt spjald
     */
    @Override
    public int[][] nyttSpjald()
    {
        int[] tempFylki = handahofsTolur();

        for (int i = 0; i < (x * y); i++)
        {
            int xx = i / 5;

            switch (i % 5)
            {
                case 1 -> xx += 15;
                case 2 -> xx += 30;
                case 3 -> xx += 45;
                case 4 -> xx += 60;
            }
            spjald[i / x][i % x] = tempFylki[xx];
        }
        return spjald;
    }

    /**
     * Athugar hvort það er bingo út frá strengi 's'
     *
     * @param s tegund bingo's
     * @return skilar True ef bingo, annars false
     */
    @Override
    public boolean erBingo(String s)
    {
        for (String b : s.split("/"))
            for (int i = 0; i < y; i++)
            {
                // Nota ef það er bingo til að lita bingo rununa.
                boolean[][] L = new boolean[x][y];

                // ef tala < 0 í reit á bingo sem er verið að leita eftir þá set ég temp -2 í lita arr.
                for (int j = 0; j < x; j++)
                {
                    if      (b.equals("Lárétt" ) && spjald[i][j] < 0) L[i][j] = true;
                    else if (b.equals("Lóðrétt") && spjald[j][i] < 0) L[j][i] = true;
                    else if (b.equals("Hornalína") && spjald[j][j] < 0 && (i == 0)) L[j][j] = true;
                    else if (b.equals("Hornalína") && spjald[j][x-1-j] < 0 && (i == 1)) L[j][x-1-j] = true;
                    else break;

                    // ef j kemst upp í 4 (lengd bingo runu) þá set ég -2 á spjald fyrir lit
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