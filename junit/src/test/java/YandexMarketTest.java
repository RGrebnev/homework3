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
        logger.info("Driver is up");
    }

    @Test
    public void openPage() throws InterruptedException {

        By popupHelp = By.cssSelector(".b-spy-visible");
        By allCategories = By.cssSelector("div[data-zone-name=all-categories] button");
        By verticalMenu = By.cssSelector("div[role=tablist][aria-orientation=vertical]");
        By expandedMenu = By.cssSelector("div[role=tablist][aria-orientation=vertical] ~ div[aria-expanded=true]");
        By electroLink = By.cssSelector("a[href=\"/catalog--elektronika/54440\"]");
        By mobileLink = By.cssSelector("a[href^=\"/catalog--mobilnye-telefony/54726\"]");
        By meizuManufacturer = By.cssSelector("input[name=\"Производитель Meizu\"] + div");
        By realmeManufacturer = By.cssSelector("input[name=\"Производитель realme\"] + div");

        WebDriverWait wait = new WebDriverWait(driver, 10L);
        Actions action = new Actions(driver);

        driver.get(cfg.yandex());
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
        //отметить чекбоксы meizu и realmi
        driver.findElement(meizuManufacturer).click();
        driver.findElement(realmeManufacturer).click();

    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
