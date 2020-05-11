import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class SimpleTest {
    protected static WebDriver driver;
    private final static Logger logger = LogManager.getLogger("JUnit tests");
    TestConfig cfg = ConfigFactory.create(TestConfig.class);

    @Before
    public void setUp() {
        String browserType = System.getProperty("browser");
        if (browserType == null) browserType = "chrome"; //default browser (without argument)
        driver = WebDriverFactory.createNewDriver(browserType);
        logger.info("Driver is up");
    }

    @Test
    public void openPage(){
        driver.get(cfg.url());
        logger.info("page open");

    }

    @After
    public void setDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
