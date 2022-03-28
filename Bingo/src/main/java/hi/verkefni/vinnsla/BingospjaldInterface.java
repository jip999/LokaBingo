package hi.verkefni.vinnsla;

/******************************************************************************
 *  Nafn    : Jakub Ingvar Pitak
 *  T-póstur: jip2@hi.is
 *
 *  Lýsing  : Vinnsluklasi sem geymir viðmót á public föllum í
 *  Bingospjald klasanum.
 *
 *****************************************************************************/
public interface BingospjaldInterface {
    /**
     * Talan á reit (i,j) hefur verið lesin upp. Reiturinn er merktur sem -1 í fylkinu.
     * Forskilyrði - (i,j) er innan marka bingóspjaldsins - óþarfi að tékka sérstaklega
     *
     * @param i x-staðsetning
     * @param j y-staðsetning
     */
    void aReit(int i, int j);

    int[][] getSpjaldFylki();

    /**
     * Frumstillir bingóspjald með tölum af handahófi sem það fær frá handahofsTolur().
     *
     * @return skilar fylkinu með gögnum bingóspjaldsins
     */
    int[][] nyttSpjald();

    /**
     * Athugar hvort það er bingó í hornalínu spjaldsins
     *
     * @return true ef það er bingo annars false
     */
    boolean erBingo(String s);
}
