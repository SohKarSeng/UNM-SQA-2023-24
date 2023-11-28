import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class VideoCountTest {

    private WebDriver driver;
    private WebDriverWait wait;


    @Before
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");

        // Wait for the videos to load
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    public void testVideoCount() throws InterruptedException {
        // Verify that there are exactly 12 videos on the initial page load
        List<WebElement> iframeElements = driver.findElements(By.tagName("iframe"));
        Assert.assertEquals(12, iframeElements.size());

        // Verify that the video count remains 12 after entering search keywords
        WebElement keywordSearchInput = driver.findElement(By.id("keyword-search"));
        keywordSearchInput.sendKeys("Software Testing");

        WebElement keywordSearchButton = driver.findElement(By.id("keyword-search-button"));
        keywordSearchButton.click();

        waitForOverlayToDisappear();
        TimeUnit.SECONDS.sleep(10);

        List<WebElement> filteredVideoContainers = driver.findElements(By.tagName("iframe"));
        Assert.assertEquals(12, filteredVideoContainers.size());

        WebElement categoryElement = driver.findElement(By.className("select2"));

        Select select1 = new Select(categoryElement);

         // Select option with value "Music"
        select1.selectByValue("Music");
        TimeUnit.SECONDS.sleep(3);
        select1.selectByValue("Classics");
        TimeUnit.SECONDS.sleep(3);

         waitForOverlayToDisappear();
        List<WebElement> filteredVideosAfterCategorySelection = driver.findElements(By.tagName("iframe"));
        Assert.assertEquals(12, filteredVideosAfterCategorySelection.size());
    }

    private void waitForOverlayToDisappear() {
        // Wait for the overlay to disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("blockUI")));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
