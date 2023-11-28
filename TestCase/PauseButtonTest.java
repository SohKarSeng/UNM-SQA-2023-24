import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.*;

public class PauseButtonTest {
    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        // Set up the WebDriver (Assuming you have ChromeDriver installed)
        driver = new ChromeDriver();
        // Open the website with the search functionality
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");
        driver.manage().window().fullscreen();
        // Add a delay (in milliseconds) to wait for the page to load
        Thread.sleep(10000);  // Adjust the time as needed

    }

    @Test
    public void testPauseFunctionality() throws InterruptedException {

        // Now you should be able to interact with elements inside the iframe
        // For example, locate the play button and click it
        WebElement playButton = driver.findElement(By.className("plyr__control--overlaid"));
        playButton.click();


        // Wait for the video to play (you might need to adjust the wait time)
        try {
            Thread.sleep(5000); // Wait for 5 seconds (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


// Wait for the poster to become invisible
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".plyr__poster")));

// Use JavaScript to click the iframe
        WebElement video = driver.findElement(By.cssSelector(".plyr__video-embed iframe"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", video);

        try {
            Thread.sleep(1000); // Wait for 5 seconds (adjust as needed)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Find the video player element
        WebElement videoPlayer = driver.findElement(By.cssSelector(".plyr"));
        assertTrue("The video is playing", videoPlayer.getAttribute("class").contains("plyr--paused"));

    }


    @After
    public void tearDown() {
        // Close the WebDriver after the test
        if (driver != null) {
            driver.quit();
        }
    }
}

