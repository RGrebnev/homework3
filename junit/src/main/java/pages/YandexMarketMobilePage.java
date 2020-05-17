package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YandexMarketMobilePage {

    private WebDriver driver;

    public YandexMarketMobilePage(WebDriver driver) {
        this.driver = driver;
    }

    public By filterManufacturer = By.xpath("//div[@data-zone-name=\"search-filter\"]//legend[text()=\"Производитель\"]/..");
    public By priceSort = By.linkText("по цене");
    public By preloaderOnItems = By.cssSelector(".n-filter-applied-results__content .preloadable__preloader_visibility_visible");
    public By itemLink = By.cssSelector("div.n-snippet-cell2__header a");
    public By itemCompareButton = By.cssSelector(".n-user-lists_type_compare");
    public By popupInformer = By.cssSelector(".popup-informer");
    public By popupInformerText = By.cssSelector(".popup-informer .popup-informer__title");
    public By popupInformerClose = By.cssSelector(".popup-informer .popup-informer__close");
    public By popupInformerCompareButton = By.cssSelector(".popup-informer .button");

    public void selectManufacturer(String manufacturerName) {
        driver
                .findElement(filterManufacturer)
                .findElement(manufacturerCheckbox(manufacturerName))
                .click();
    }

    public void addFirstItemToCompare(String manufacturerName) {
        driver
                .findElement(manufacturerItems(manufacturerName))
                .findElement(itemCompareButton)
                .click();
    }

    public String getFirstItemFullName(String manufacturerName) {
        return driver
                .findElement(manufacturerItems(manufacturerName))
                .findElement(itemLink)
                .getAttribute("title");
    }

    private By manufacturerCheckbox(String manufacturerName) {
        return By.xpath(String.format("//span[text()=\"%s\"]/ancestor::label/div", manufacturerName));
    }

    private By manufacturerItems(String manufacturerName) {
        return By.xpath(String.format("//div[text()=\"%s\"]/ancestor::div[contains(@data-id, \"model\")]", manufacturerName));
    }

}
