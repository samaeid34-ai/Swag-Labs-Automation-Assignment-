package tests;

import base.BaseTest;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.DataDriven;

public class LoginTest extends BaseTest {

    @Test
    public void testSuccessfulLogin() {
        JSONObject validUser = DataDriven.jsonReader("validUser");
        String username = (String) validUser.get("username");
        String password = (String) validUser.get("password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        Assert.assertTrue(driver.getCurrentUrl().contains("/inventory.html"), "URL does not contain /inventory.html");
    }


    @Test
    public void testInvalidLogin() {
        JSONObject invalidUser = DataDriven.jsonReader("invalidUser");
        String username = (String) invalidUser.get("username");
        String password = (String) invalidUser.get("password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains("Username and password do not match"), "Error message mismatch!");
    }


    @Test
    public void testLoginWithoutPassword() {
        JSONObject noPasswordUser = DataDriven.jsonReader("noPasswordUser");
        String username = (String) noPasswordUser.get("username");
        String password = (String) noPasswordUser.get("password");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);


        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains("Password is required"), "Error message mismatch!");
    }
}
