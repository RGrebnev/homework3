import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.naming.ldap.LdapReferralException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class YandexMarketTest {
    protected static WebDriver driver;
    private final static Logger logger = LogManager.getLogger("JUnit tests");
    TestConfig cfg = ConfigFactory.create(TestConfig.class);

    @Before
    public void setUp() {
        String browserType = System.getProperty("browser");
        if (browserType == null) browserType = "chrome"; //default browser (without argument)
        driver = WebDriverFactory.createNewDriver(browserType);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(2L, TimeUnit.SECONDS);
        logger.info("Driver is up");
    }

    @Test
    public void longLongTest() {

        By popupHelp = By.cssSelector(".b-spy-visible");
        By allCategories = By.cssSelector("div[data-zone-name=all-categories] button");
        By verticalMenu = By.cssSelector("div[role=tablist][aria-orientation=vertical]");
        By expandedMenu = By.cssSelector("div[role=tablist][aria-orientation=vertical] ~ div[aria-expanded=true]");
        By electroLink = By.cssSelector("a[href=\"/catalog--elektronika/54440\"]");
        By mobileLink = By.cssSelector("a[href^=\"/catalog--mobilnye-telefony/54726\"]");
        By filterManufacturer = By.xpath("//div[@data-zone-name=\"search-filter\"]//legend[text()=\"Производитель\"]/..");
        By xiaomiManufacturer = By.cssSelector("input[name$=\"Xiaomi\"] + div");
        By realmeManufacturer = By.cssSelector("input[name$=\"realme\"] + div");
        By priceSort = By.linkText("по цене");
        By preloaderOnItems = By.cssSelector(".n-filter-applied-results__content .preloadable__preloader_visibility_visible");
        By xiaomiItems = By.xpath("//div[text()=\"Xiaomi\"]/ancestor::div[contains(@data-id, \"model\")]");
        By realmeItems = By.xpath("//div[text()=\"realme\"]/ancestor::div[contains(@data-id, \"model\")]");
        By itemCompareButton = By.cssSelector(".n-user-lists_type_compare");
        By itemLink = By.cssSelector("div.n-snippet-cell2__header a");
        By popupInformer = By.cssSelector(".popup-informer");
        By popupInformerText = By.cssSelector(".popup-informer .popup-informer__title");
        By popupInformerClose = By.cssSelector(".popup-informer .popup-informer__close");
        By popupInformerCompare = By.cssSelector(".popup-informer .button");
        By compareItems = By.cssSelector(".n-compare-head .n-compare-cell");
        By compareAllSpecs = By.cssSelector(".n-compare-show-controls__all");
        By compareDiff = By.cssSelector(".n-compare-show-controls__diff");
        By compareOS = By.xpath("//div[text()=\"Операционная система\"]");
        By comparePreloader = By.cssSelector(".spin2_progress_yes");

        WebDriverWait wait = new WebDriverWait(driver, 20L);
        Actions action = new Actions(driver);

        driver.get(cfg.yandexM());
        //ожидание загрузки скриптов
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".i-global_js_inited")));

        //при первом запуске отображается попап (подсказка) в строке поиска, которая перекрывает кнопку "Все категории"
        //если попап отобразился, то нужно подождать, пока попап не скроется
        //если попап не отобразился, то тест идет дальше
        wait.until(ExpectedConditions.invisibilityOfElementLocated(popupHelp));

        //нажимаем кнопку "Все категории" и ждем отображения меню категорий
        driver.findElement(allCategories).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(verticalMenu));
        //наводим курсор на элемент "Электроника"
        action.moveToElement(driver.findElement(verticalMenu).findElement(electroLink)).build().perform();
        //нажать на ссылку "мобильные телефоны" в расширенном меню
        driver.findElement(expandedMenu).findElement(mobileLink).click();

        //ожидание блока фильтра по производителю
        wait.until(ExpectedConditions.visibilityOfElementLocated(filterManufacturer));
        //отметить чекбоксы xiaomi и realme
        //после нажатия ждем обновления результатов - перестает отображаться прелоадер
        driver.findElement(xiaomiManufacturer).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloaderOnItems));
        driver.findElement(realmeManufacturer).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloaderOnItems));
        //упорядочить по цене (ждем обновление)
        driver.findElement(priceSort).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloaderOnItems));

        //добавить к сравнению первый найденный элемент xiaomi и realme
        driver.findElement(xiaomiItems).findElement(itemCompareButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(popupInformer));
        String xiaomiName = driver.findElement(xiaomiItems).findElement(itemLink).getAttribute("title");
        Assert.assertTrue(driver.findElement(popupInformerText).getText().contains(xiaomiName));
        driver.findElement(popupInformerClose).click();

        driver.findElement(realmeItems).findElement(itemCompareButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(popupInformer));
        String realmeName = driver.findElement(realmeItems).findElement(itemLink).getAttribute("title");
        Assert.assertTrue(driver.findElement(popupInformerText).getText().contains(realmeName));
        driver.findElement(popupInformerCompare).click();

        //проверка количества сравниваемых элементов
        Assert.assertTrue(driver.findElements(compareItems).size() == 2);

        //нажать "все характеристики" (подождать закрытие прелоадера)
        driver.findElement(compareAllSpecs).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(comparePreloader));
        //проверка наличия параметра "Операционная система"
        Assert.assertTrue(driver.findElement(compareOS).isDisplayed());

        //нажать "различающиеся характеристики" (подождать закрытие прелоадера)
        driver.findElement(compareDiff).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(comparePreloader));
        //проверка отсутствия параметра "Операционная система"
        Assert.assertFalse(driver.findElement(compareOS).isDisplayed());
    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
