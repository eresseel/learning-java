public class App {
    public static void main(String[] args) throws Exception {
        int testscore = 76;
        int grade;

        if (testscore >= 90) {
            grade = 5;
        } else if (testscore >= 80) {
            grade = 4;
        } else if (testscore >= 70) {
            grade = 3;
        } else if (testscore >= 60) {
            grade = 2;
        } else {
            grade = 1;
        }
        
        System.out.println("Grade = " + grade);
    }
}
