package tests;

import base.BaseTest;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;

public class InventoryTest extends BaseTest {

    @Test
    public void testInventoryPageElements() {
        JSONObject validUser = DataDriven.jsonReader("validUser");
        String username = (String) validUser.get("username");
        String password = (String) validUser.get("password");

        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = loginPage.login(username, password);


        Assert.assertEquals(inventoryPage.getPageTitle(), "Swag Labs", "Page title does not match!");


        Assert.assertTrue(inventoryPage.isCartIconDisplayed(), "Cart icon is not displayed!");


        Assert.assertEquals(inventoryPage.getProductsCount(), 6, "Products count is not 6!");
    }
}
