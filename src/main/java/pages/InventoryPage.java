package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InventoryPage {
    private WebDriver driver;
    private By shoppingCartContainer = By.id("shopping_cart_container");
    private By inventoryItems = By.className("inventory_item");


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
}
