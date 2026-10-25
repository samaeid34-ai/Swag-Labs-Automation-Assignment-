package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InventoryPage {
    private WebDriver driver;

    private By shoppingCartContainer = By.id("shopping_cart_container");
    private By inventoryItems = By.className("inventory_item");
    private By socialLinkedIn = By.cssSelector(".social_linkedin a");
    private By socialFacebook = By.cssSelector(".social_facebook a");
    private By socialTwitter = By.cssSelector(".social_twitter a");
    private By burgerMenuBtn = By.id("react-burger-menu-btn");
    private By logoutBtn = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isCartIconDisplayed() {
        return driver.findElement(shoppingCartContainer).isDisplayed();
    }

    public int getProductsCount() {
        List<WebElement> items = driver.findElements(inventoryItems);
        return items.size();
    }


    public void clickLinkedIn() {
        driver.findElement(socialLinkedIn).click();
    }

    public void clickFacebook() {
        driver.findElement(socialFacebook).click();
    }

    public void clickTwitter() {
        driver.findElement(socialTwitter).click();
    }


    public CartPage openCart() {
        driver.findElement(shoppingCartContainer).click();
        return new CartPage(driver);
    }


    public void addProductByName(String productName) {
        List<WebElement> items = driver.findElements(inventoryItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.tagName("button")).click();
                break;
            }
        }
    }


    public double getProductPriceByName(String productName) {
        List<WebElement> items = driver.findElements(inventoryItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                String priceText = item.findElement(By.className("inventory_item_price")).getText();

                return Double.parseDouble(priceText.replace("$", ""));
            }
        }
        return 0.0;
    }


    public String getProductButtonText(String productName) {
        List<WebElement> items = driver.findElements(inventoryItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                return item.findElement(By.tagName("button")).getText();
            }
        }
        return "";
    }


    public void logout() {
        driver.findElement(burgerMenuBtn).click();

        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        driver.findElement(logoutBtn).click();
    }
}