import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.Assert.*;

public class ForwardSeekTest {
    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        // Set up the WebDriver (Assuming you have ChromeDriver installed)
        driver = new ChromeDriver();
        // Open the website with the search functionality
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");
        driver.manage().window().fullscreen();
        // Add a delay (in milliseconds) to wait for the page to load
        Thread.sleep(5000);  // Adjust the time as needed

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

    }

    @Test
    public void testForwardSeekFunctionality() throws InterruptedException {

        WebElement progressBar = driver.findElement(By.cssSelector("input[data-plyr='seek']"));

        // Scroll down to the progress bar
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ behavior: 'auto', block: 'center', inline: 'center' });", progressBar);
        Thread.sleep(5000);
        // Get the width of the progress bar
        int progressBarWidth = progressBar.getSize().getWidth();

        // Calculate the position to click for the timestamp '02:30'
        double totalDuration = 239.0;
        double timestampToSeek = 30.0;

        double percentToClick = (timestampToSeek / totalDuration) * 100;
        int xOffset = (int) (progressBarWidth * percentToClick / 100) - 548;

        // Find the video player element
        WebElement videoPlayer = driver.findElement(By.cssSelector(".plyr"));
        Actions actions = new Actions(driver);
        actions.moveToElement(videoPlayer).perform();
        // Use Actions to move to the progress bar and perform a click

        actions.moveToElement(progressBar, xOffset, 0).click().build().perform();

        Thread.sleep(1000);
        // Wait for the poster to become invisible
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".plyr__poster")));

// Use JavaScript to click the iframe
        WebElement video = driver.findElement(By.cssSelector(".plyr__video-embed iframe"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", video);

        Thread.sleep(1000);
        String ariaValueText = progressBar.getAttribute("aria-valuetext");

        System.out.println(ariaValueText);
        assertTrue("The timestamp is not 0:30", ariaValueText.contains("00:30"));
    }


    @After
    public void tearDown() {
        // Close the WebDriver after the test
        if (driver != null) {
            driver.quit();
        }
    }
}