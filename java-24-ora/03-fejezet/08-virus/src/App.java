public class App {
    public static void main(String[] args) throws Exception {
        short numViruses = Short.parseShort("1");
        if (numViruses > 0) {
            Virus virii[] = new Virus[numViruses];
            for(short i = 0; i < numViruses; i++) {
                virii[i] = new Virus();
                virii[i].setNewSeconds(61);
                System.out.println(virii[i].getNewSeconds());
            }
            System.out.println("There are " + Virus.getVirusCount() + " viruses.");
        }
    }
}
