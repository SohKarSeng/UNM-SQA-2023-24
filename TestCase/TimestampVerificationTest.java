import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TimestampVerificationTest {

    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");
        TimeUnit.SECONDS.sleep(15);
        
    }

    @Test
    public void testTimestampVerification() throws InterruptedException {
        // Wait for the video to load
        TimeUnit.SECONDS.sleep(5);

        // Play the video
        WebElement playButton = driver.findElement(By.cssSelector(".plyr__control--overlaid[data-plyr='play']"));
        playButton.click();

        // Wait until the video progresses to 0.04 seconds
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement videoTimestamp = driver.findElement(By.id("video-timestamp-0"));
        wait.until(ExpectedConditions.textToBePresentInElement(videoTimestamp, "0:04"));

        // Get the initial timestamp
        String initialTimestamp = videoTimestamp.getText();

        WebElement noteInput = driver.findElement(By.id("note-content-0"));

        // Enter notes in the "Enter your notes here" section
        noteInput.sendKeys("Test notes for timestamp verification");

        WebElement saveButton = driver.findElement(By.id("saveNoteButton0"));
        saveButton.click();

         TimeUnit.SECONDS.sleep(5);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        Thread.sleep(1000);
        // Wait for the "Full Notes" section to be visible
        WebElement fullNotesSection = driver.findElement(By.id("description-list-0"));

        // Check the “Full Notes” section to ensure that the notes along with the timestamp are correct
        Assert.assertTrue("Full Notes section should contain the saved note", fullNotesSection.getText().contains("Test notes for timestamp verification"));

        // Get the final timestamp
        String finalTimestamp = videoTimestamp.getText();

        // Verify that the timestamp remains unchanged in the notes taking section
        Assert.assertEquals("Timestamp should remain unchanged in the notes taking section", initialTimestamp, finalTimestamp);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
