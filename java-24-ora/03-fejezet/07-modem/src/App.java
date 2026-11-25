public class App {
    public static void main(String[] args) throws Exception {
        CableModem roadRunner = new CableModem();
        DslModem bellSouth = new DslModem();
        AcousticModem acModem = new AcousticModem();

        roadRunner.speed = 500000;
        bellSouth.speed = 400000;
        acModem.speed = 300;

        System.out.println("Trying the cable modem:");
        roadRunner.displaySpeed();
        roadRunner.connect();
        roadRunner.disconnent();

        System.out.println("Trying the DSL modem:");
        bellSouth.displaySpeed();
        bellSouth.connect();
        bellSouth.disconnent();

        System.out.println("Trying the cable modem:");
        acModem.displaySpeed();
        acModem.connect();
        acModem.disconnent();
    }
}
