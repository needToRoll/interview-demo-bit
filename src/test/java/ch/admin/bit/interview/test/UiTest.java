package ch.admin.bit.interview.test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class UiTest {
    private static WebDriver browser;
    private static ChromeOptions chromeOptions = new ChromeOptions();

    @BeforeClass
    public static void prepareClass() {
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        System.setProperty("webdriver.chrome.driver", "chromedriver");
    }

    @Test
    public void test(){
        browser = new ChromeDriver(chromeOptions);
        browser.get("https://seleniumbase.io/demo_page");
        final By myButton = By.id("myButton");
        browser.findElement(myButton).click();
        final String buttonText = browser.findElement(myButton).getText();
        Assert.assertEquals("Click Me (Purple)", buttonText);
    }


}
