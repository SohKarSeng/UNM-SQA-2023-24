import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoThumbnailAndTitleTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @Before
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");

        // Wait for the videos to load
        TimeUnit.SECONDS.sleep(15);
        
        // Verify that there are exactly 12 videos on the initial page load
        List<WebElement> iframeElements = driver.findElements(By.tagName("iframe"));
        Assert.assertEquals(12, iframeElements.size());
 
    }

    @Test
    public void testThumbnailAndTitle() {
        // Get a list of all video elements
        List<WebElement> videoElements = driver.findElements(By.cssSelector("section.floating-label-textarea"));

        // Verify the presence of a thumbnail and title for each video
        for (int i = 0; i < videoElements.size(); i++) {
            // Wait for the thumbnail to be present
            WebElement thumbnailElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.plyr__poster")));
            Assert.assertTrue("Thumbnail element not found for video " + i, thumbnailElement.isDisplayed());

            String thumbnailSrc = thumbnailElement.getCssValue("background-image");
            // Extract the image URL from the background-image property
            String imageUrl = thumbnailSrc.substring(5, thumbnailSrc.length() - 2);
            Assert.assertNotNull("Thumbnail source attribute is empty", imageUrl);
            Assert.assertTrue("Thumbnail source is not an image", imageUrl.endsWith(".jpg") || imageUrl.endsWith(".png") || imageUrl.endsWith(".gif"));

            // Wait for the title to be present
            WebElement titleElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h4.card-title")));
            Assert.assertTrue("Title element not found for video " + i, titleElement.isDisplayed());

            String titleText = titleElement.getText();
            Assert.assertNotNull("Title text is empty for video " + i, titleText);
            Assert.assertFalse("Title text is too short for video " + i, titleText.length() < 5);
            Assert.assertFalse("Title text is too long for video " + i, titleText.length() > 100);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
