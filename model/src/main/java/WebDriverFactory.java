import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {
    private static Logger logger = LogManager.getLogger(WebDriverFactory.class);

    // create driver without options
    public static WebDriver createNewDriver(String name) {
        WebDriver driver = null;
        BrowserName browserName = getBrowserName(name);
        switch (browserName) {
            case FIREFOX:
                driver = getFirefoxDriver();
                break;
            case CHROME:
                driver = getChromeDriver();
                break;
        }
        return driver;
    }

    //create driver with options
    public static WebDriver createNewDriver(String name, MutableCapabilities capabilities) {
        WebDriver driver = null;
        BrowserName browserName = getBrowserName(name);
        switch (browserName) {
            case FIREFOX:
                if (capabilities instanceof FirefoxOptions) {
                    driver = getFirefoxDriver((FirefoxOptions) capabilities);
                } else {
                    logger.warn("Invalid browser options! No options selected.");
                    driver = getFirefoxDriver();
                }
                break;
            case CHROME:
                if (capabilities instanceof ChromeOptions) {
                    driver = getChromeDriver((ChromeOptions) capabilities);
                } else {
                    logger.warn("Invalid browser options! No options selected.");
                    driver = getChromeDriver();
                }
                break;
        }
        return driver;
    }

    private static BrowserName getBrowserName(String name) {
        BrowserName browserName;
        try {
            browserName = BrowserName.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            logger.warn("Invalid argument. Default browser selected: Chrome");
            browserName = BrowserName.CHROME;
        }
        return browserName;
    }

    private static WebDriver getChromeDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    private static WebDriver getChromeDriver(ChromeOptions options) {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private static WebDriver getFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver();
    }

    private static WebDriver getFirefoxDriver(FirefoxOptions options) {
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }
}
