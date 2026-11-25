public class Virus {
    static short virusCount = 0;
    private byte newSeconds = 0;

    public Virus() {
        virusCount++;
    }

    static int getVirusCount() {
        return virusCount;
    }

    public void setNewSeconds(int x) {
        if (x > 60 && x < 100) {
            newSeconds = (byte)x;
        }
    }
    public byte getNewSeconds() {
        return newSeconds;
    }
}
