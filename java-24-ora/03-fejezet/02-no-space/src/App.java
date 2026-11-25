public class App {
    public static void main(String[] args) throws Exception {
        String mostFamous = "Rudolp the Red-Nosed Reindeer";
        char[] mfl = mostFamous.toCharArray();

        for (int dex = 0; dex < mfl.length; dex++) {
            char current = mfl[dex];
            System.out.print((current != ' ') ? current : '.');
        }
        System.out.println();
    }
}
