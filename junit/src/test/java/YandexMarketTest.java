import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.YandexMarketComparePage;
import pages.YandexMarketMainPage;
import pages.YandexMarketMobilePage;

import java.util.concurrent.TimeUnit;

public class YandexMarketTest {
    protected static WebDriver driver;
    private final static Logger logger = LogManager.getLogger("JUnit tests");
    private TestConfig cfg = ConfigFactory.create(TestConfig.class);
    private WebDriverWait wait;
    private Actions action;

    @Before
    public void setUp() {
        String browserType = System.getProperty("browser");
        if (browserType == null) browserType = "chrome"; //default browser (without argument)
        driver = WebDriverFactory.createNewDriver(browserType);
        logger.info("driver is up");

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2L, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 20L);
        action = new Actions(driver);

        driver.get(cfg.yandexM());
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".i-global_js_inited")));
        logger.info("page is ready");

    }

    @Test
    public void longLongTest() {
        YandexMarketMainPage mainPage = new YandexMarketMainPage(driver);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(mainPage.popupHelp));
        driver.findElement(mainPage.allCategoriesButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.verticalMenu));
        logger.info("categories menu is open");
        mainPage.clickOnExpandedMenuLink(action, mainPage.electroLink, mainPage.mobileLink);
        Assert.assertTrue("wrong page: not mobile phones", driver.getTitle().contains("Мобильные телефоны"));
        logger.info("mobile phones page is open");

        YandexMarketMobilePage mobilePage = new YandexMarketMobilePage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(mobilePage.filterManufacturer));
        mobilePage.selectManufacturer("Xiaomi");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(mobilePage.preloaderOnItems));
        logger.info("xiaomi filter applied");
        mobilePage.selectManufacturer("realme");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(mobilePage.preloaderOnItems));
        logger.info("realme filter applied");
        driver.findElement(mobilePage.priceSort).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(mobilePage.preloaderOnItems));
        logger.info("sorted by price");

        String firstXiaomiName = mobilePage.getFirstItemFullName("Xiaomi");
        mobilePage.addFirstItemToCompare("Xiaomi");
        wait.until(ExpectedConditions.visibilityOfElementLocated(mobilePage.popupInformer));
        Assert.assertTrue("added item is not first xiaomi" ,driver.findElement(mobilePage.popupInformerText).getText().contains(firstXiaomiName));
        logger.info("first xiaomi item added to compare");

        wait.until(ExpectedConditions.elementToBeClickable(mobilePage.popupInformerClose));
        driver.findElement(mobilePage.popupInformerClose).click();

        String firstRealmeName = mobilePage.getFirstItemFullName("realme");
        mobilePage.addFirstItemToCompare("realme");
        wait.until(ExpectedConditions.visibilityOfElementLocated(mobilePage.popupInformer));
        Assert.assertTrue("added item is not first realme" ,driver.findElement(mobilePage.popupInformerText).getText().contains(firstRealmeName));
        logger.info("first realme item added to compare");

        wait.until(ExpectedConditions.elementToBeClickable(mobilePage.popupInformerCompareButton));
        driver.findElement(mobilePage.popupInformerCompareButton).click();

        Assert.assertTrue("wrong page: not compare page", driver.getTitle().contains("Сравнение товаров"));
        logger.info("compare page is open");

        YandexMarketComparePage comparePage = new YandexMarketComparePage(driver);
        Assert.assertTrue("number of compared elements != 2", driver.findElements(comparePage.comparedItems).size() == 2);

        driver.findElement(comparePage.allSpecsButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(comparePage.comparePreloader));
        Assert.assertTrue("fail: OS is not displayed", driver.findElement(comparePage.compareOS).isDisplayed());
        logger.info("spec OS is displayed");

        driver.findElement(comparePage.diffSpecsButton).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(comparePage.comparePreloader));
        Assert.assertFalse("fail: OS is displayed", driver.findElement(comparePage.compareOS).isDisplayed());
        logger.info("spec OS is not displayed");

    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
