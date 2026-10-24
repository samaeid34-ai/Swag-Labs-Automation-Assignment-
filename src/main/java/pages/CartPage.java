package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage {
    private WebDriver driver;

    private By cartItems = By.className("cart_item");
    private By checkoutBtn = By.id("checkout");
    private By itemTotalLabel = By.className("summary_subtotal_label");
    private By errorMessage = By.cssSelector("h3[data-test='error']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getCartItemNames() {
        List<WebElement> items = driver.findElements(cartItems);
        List<String> names = new ArrayList<>();
        for (WebElement item : items) {
            names.add(item.findElement(By.className("inventory_item_name")).getText());
        }
        return names;
    }

    public int getCartItemsCount() {
        return driver.findElements(cartItems).size();
    }

    public void removeProductByName(String productName) {
        List<WebElement> items = driver.findElements(cartItems);
        for (WebElement item : items) {
            String name = item.findElement(By.className("inventory_item_name")).getText();
            if (name.equals(productName)) {
                item.findElement(By.tagName("button")).click();
                break;
            }
        }
    }

    public void clickCheckout() {
        driver.findElement(checkoutBtn).click();
    }

    public double getItemTotal() {

        String totalText = driver.findElement(itemTotalLabel).getText();
        String numericVal = totalText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericVal);
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }
}