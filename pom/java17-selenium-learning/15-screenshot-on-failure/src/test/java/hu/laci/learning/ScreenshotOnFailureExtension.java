package hu.laci.learning;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * JUnit 5 kiegészítő (extension): ha egy UI teszt elbukik, képernyőképet és oldalforrást ment.
 * <p>
 * Miért {@link AfterTestExecutionCallback} és nem TestWatcher? A TestWatcher akkor értesít a hibáról,
 * amikor az {@code @AfterEach} már lefutott, vagyis a böngésző már be van zárva. Az
 * AfterTestExecutionCallback a tesztmetódus UTÁN, de az {@code @AfterEach} ELŐTT fut le,
 * ezért ekkor a böngésző még él, és lehet róla képet készíteni.
 * <p>
 * A fájlok a {@code target/screenshots} mappába kerülnek.
 */
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Path OUTPUT_DIR = Path.of("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        // Csak elbukott (kivétellel végződött) tesztnél van teendő.
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        // A teszt példányából kérjük el a böngészőt. Ez csak a BaseTest-ből származó tesztekre működik.
        Object testInstance = context.getRequiredTestInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            return;
        }

        WebDriver driver = baseTest.getDriver();
        // A ChromeDriver megvalósítja a TakesScreenshot felületet. A null/típus-ellenőrzés védi a kiegészítőt.
        if (driver == null || !(driver instanceof TakesScreenshot screenshotDriver)) {
            return;
        }

        Files.createDirectories(OUTPUT_DIR);

        // Beszédes fájlnév: osztály_metódus_időbélyeg.
        String baseName = context.getRequiredTestClass().getSimpleName()
                + "_" + context.getRequiredTestMethod().getName()
                + "_" + LocalDateTime.now().format(TIMESTAMP);

        // 1. Képernyőkép (PNG).
        File screenshot = screenshotDriver.getScreenshotAs(OutputType.FILE);
        Path pngPath = OUTPUT_DIR.resolve(baseName + ".png");
        Files.copy(screenshot.toPath(), pngPath, StandardCopyOption.REPLACE_EXISTING);

        // 2. Az oldal HTML forrása: a hibakereséshez sokszor többet mond, mint a kép.
        Path htmlPath = OUTPUT_DIR.resolve(baseName + ".html");
        Files.writeString(htmlPath, driver.getPageSource(), StandardCharsets.UTF_8);

        System.out.println("Test failed, screenshot saved: " + pngPath.toAbsolutePath());
    }
}
