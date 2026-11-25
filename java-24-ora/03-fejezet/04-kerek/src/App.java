public class App {
    public static void main(String[] args) throws Exception {
        String phrase[] = {
            "A STITCH IN THE TIME SAVES NINE",
            "JUST DO IT",
            "I WANT MY MTV"
        };
        int[] letterCount = new int[26];

        for(int count = 0; count < phrase.length; count++) {
            String current = phrase[count];
            char[] letters = current.toCharArray();

            for(int count2 = 0; count2 < letters.length; count2++) {
                char lett = letters[count2];
                if ((lett >= 'A') && (lett <= 'Z')) {
                    letterCount[lett - 'A']++;
                }
            }
        }

        for(char count = 'A'; count <= 'Z'; count++) {
            System.out.println(count + ": " + letterCount[count - 'A'] + "\t");
        }
        System.out.println();
    }
}
