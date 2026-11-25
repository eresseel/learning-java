public class App {
    public static void main(String[] args) throws Exception {
        int tanulokAtlaga[][] = {
            {5,5},
            {4,3,3}
        };

        for (int i = 0; i < tanulokAtlaga.length; i++) {
            int atlag = 0;
            System.out.print("A(z) " + (i+1) + ". tanulo jegyeinek atlaga: ");
            for (int j = 0; j < tanulokAtlaga[i].length; j++) {
                atlag += tanulokAtlaga[i][j];
            }
            System.out.println((float)atlag / tanulokAtlaga[i].length);
        }
    }
}
