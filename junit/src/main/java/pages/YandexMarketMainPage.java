package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

public class YandexMarketMainPage {

    private WebDriver driver;

    public YandexMarketMainPage(WebDriver driver) {
        this.driver = driver;
    }

    public By popupHelp = By.cssSelector(".b-spy-visible");
    public By allCategoriesButton = By.cssSelector("div[data-zone-name=all-categories] button");
    public By verticalMenu = By.cssSelector("div[role=tablist][aria-orientation=vertical]");
    public By expandedMenu = By.cssSelector("div[role=tablist][aria-orientation=vertical] ~ div[aria-expanded=true]");
    public By electroLink = By.cssSelector("a[href=\"/catalog--elektronika/54440\"]");
    public By mobileLink = By.cssSelector("a[href^=\"/catalog--mobilnye-telefony/54726\"]");

    public void clickOnExpandedMenuLink (Actions action, By moveToLink, By targetLink) {
        action.moveToElement(driver.findElement(verticalMenu).findElement(moveToLink)).build().perform();
        driver.findElement(expandedMenu).findElement(targetLink).click();
    }
}
