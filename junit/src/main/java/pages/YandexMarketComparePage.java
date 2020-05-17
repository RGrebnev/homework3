package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YandexMarketComparePage {

    private WebDriver driver;

    public YandexMarketComparePage(WebDriver driver) {
        this.driver = driver;
    }

    public By comparedItems = By.cssSelector(".n-compare-head .n-compare-cell");
    public By allSpecsButton = By.cssSelector(".n-compare-show-controls__all");
    public By diffSpecsButton = By.cssSelector(".n-compare-show-controls__diff");
    public By compareOS = By.xpath("//div[text()=\"Операционная система\"]");
    public By comparePreloader = By.cssSelector(".spin2_progress_yes");

}
