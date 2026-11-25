public class App {
    public static void main(String[] args) throws Exception {
        String favorite = "piano";
        String guess = "piano";
        System.out.println("Is Ada's favorite instrument a " + guess + "?");
        System.out.println("Answer: " + favorite.equals(guess));
    }
}
