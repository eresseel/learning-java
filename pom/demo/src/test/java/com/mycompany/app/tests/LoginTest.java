package com.mycompany.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class LoginTest extends BaseTest {

    @Test
    public void testLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateTo();
        loginPage.enterUsername("testuser");
        loginPage.enterPassword("testpass");
        loginPage.clickLogin();

        String actualErrorMessage = loginPage.getErrorMessage();

        Assertions.assertTrue(actualErrorMessage.contains("Invalid credentials"),
            "A várt 'Invalid credentials' hibaüzenet nem jelent meg. A kapott üzenet: " + actualErrorMessage);
    }
}
