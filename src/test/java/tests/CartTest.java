package tests;

import base.BaseTest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DataDriven;

import java.io.FileReader;
import java.util.List;
import java.util.Set;

public class CartTest extends BaseTest {

    private InventoryPage loginAndGetInventory() {
        JSONObject validUser = DataDriven.jsonReader("validUser");
        LoginPage loginPage = new LoginPage(driver);
        return loginPage.login((String) validUser.get("username"), (String) validUser.get("password"));
    }

    @Test
    public void testSocialLinks() {
        InventoryPage inventoryPage = loginAndGetInventory();
        String mainWindow = driver.getWindowHandle();


        inventoryPage.clickLinkedIn();
        switchToNewWindow(mainWindow);
        Assert.assertTrue(driver.getCurrentUrl().contains("linkedin"), "LinkedIn URL mismatch!");
        driver.close();
        driver.switchTo().window(mainWindow);
        inventoryPage.clickFacebook();
        switchToNewWindow(mainWindow);
        Assert.assertTrue(driver.getCurrentUrl().contains("facebook"), "Facebook URL mismatch!");
        driver.close();
        driver.switchTo().window(mainWindow);


        inventoryPage.clickTwitter();
        switchToNewWindow(mainWindow);
        Assert.assertTrue(driver.getCurrentUrl().contains("x.com") || driver.getCurrentUrl().contains("twitter"), "Twitter/X URL mismatch!");
        driver.close();
        driver.switchTo().window(mainWindow);
    }

    private void switchToNewWindow(String mainWindow) {
        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(mainWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
    }


    @Test
    public void testCartIsEmpty() {
        InventoryPage inventoryPage = loginAndGetInventory();
        CartPage cartPage = inventoryPage.openCart();
        Assert.assertEquals(cartPage.getCartItemsCount(), 0, "Cart is not empty!");
    }


    @Test
    public void testAddSpecificProducts() {
        InventoryPage inventoryPage = loginAndGetInventory();
        JSONArray productsList = getCartProductsFromJson();
        for (Object prod : productsList) {
            inventoryPage.addProductByName((String) prod);
        }

        CartPage cartPage = inventoryPage.openCart();
        List<String> cartItems = cartPage.getCartItemNames();

        Assert.assertEquals(cartItems.size(), productsList.size(), "Items count mismatch!");
        for (int i = 0; i < productsList.size(); i++) {
            Assert.assertEquals(cartItems.get(i), productsList.get(i), "Product order or name mismatch!");
        }
    }


    @Test
    public void testRemoveOneProduct() {
        InventoryPage inventoryPage = loginAndGetInventory();
        JSONArray productsList = getCartProductsFromJson();

        String productToRemove = (String) productsList.get(1);
        for (Object prod : productsList) {
            inventoryPage.addProductByName((String) prod);
        }

        CartPage cartPage = inventoryPage.openCart();
        cartPage.removeProductByName(productToRemove);


        driver.navigate().back();


        Assert.assertEquals(inventoryPage.getProductButtonText(productToRemove), "Add to cart", "Button did not change to Add to cart!");


        Assert.assertEquals(inventoryPage.getProductButtonText((String) productsList.get(0)), "Remove", "Backpack button changed incorrectly!");
        Assert.assertEquals(inventoryPage.getProductButtonText((String) productsList.get(2)), "Remove", "Onesie button changed incorrectly!");
    }


    @Test
    public void testCartTotalPrice() {
        InventoryPage inventoryPage = loginAndGetInventory();
        JSONArray productsList = getCartProductsFromJson();

        double sumPrices = 0.0;
        for (Object prod : productsList) {
            String pName = (String) prod;
            sumPrices += inventoryPage.getProductPriceByName(pName);
            inventoryPage.addProductByName(pName);
        }

        CartPage cartPage = inventoryPage.openCart();
        cartPage.clickCheckout();


        driver.findElement(By.id("first-name")).sendKeys("Test");
        driver.findElement(By.id("last-name")).sendKeys("User");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        double itemTotal = cartPage.getItemTotal();
        Assert.assertEquals(itemTotal, sumPrices, 0.01, "Calculated sum does not match checkout Item Total!");
    }


    @Test
    public void testCheckoutWithEmptyCart() {
        InventoryPage inventoryPage = loginAndGetInventory();
        CartPage cartPage = inventoryPage.openCart();
        cartPage.clickCheckout();


        boolean isBlockedOrError = driver.getCurrentUrl().contains("cart.html") || !driver.findElements(By.cssSelector("h3[data-test='error']")).isEmpty();
        Assert.assertTrue(isBlockedOrError, "Site allowed checkout with empty cart without restriction!");
    }


    @Test
    public void testCartStateAfterLogoutLogin() {
        InventoryPage inventoryPage = loginAndGetInventory();
        JSONArray productsList = getCartProductsFromJson();


        inventoryPage.addProductByName((String) productsList.get(0));
        inventoryPage.addProductByName((String) productsList.get(1));


        inventoryPage.logout();


        loginAndGetInventory();
        CartPage cartPage = inventoryPage.openCart();


        int itemsCount = cartPage.getCartItemsCount();
        Assert.assertEquals(itemsCount, 2, "Cart items were lost after logout/login!");
    }


    private JSONArray getCartProductsFromJson() {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("testData/testData.json");
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            return (JSONArray) jsonObject.get("cartProducts");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
