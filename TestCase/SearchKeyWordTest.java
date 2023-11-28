import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;


public class SearchKeyWordTest {
    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");
    }

    @Test
    public void SearchTest() throws Exception {

        sleep(6000);
        WebElement noteInput = driver.findElement(By.id("keyword-search"));
        noteInput.sendKeys("Software");

        WebElement buttonSave = driver.findElement(By.id("keyword-search-button"));
        buttonSave.click();
        sleep(6000);

        List<WebElement> iframeElements = driver.findElements(By.tagName("iframe"));
        assertEquals(12, iframeElements.size());
        sleep(1000);
    }

    @After
    public void finish() {
        if (driver != null) {
            driver.quit();
        }
    }
}
