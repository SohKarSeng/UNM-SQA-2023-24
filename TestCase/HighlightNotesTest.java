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

public class HighlightNotesTest {

    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");

        // Wait for the video to load
        TimeUnit.SECONDS.sleep(15);

        // Play the video
        WebElement playPauseButton = driver.findElement(By.cssSelector(".plyr__controls__item[data-plyr='play']"));
        playPauseButton.click();

        TimeUnit.SECONDS.sleep(6);

        WebElement note = driver.findElement(By.id("note-content-0"));
        note.sendKeys("JUNIT TEST NOTE");

        WebElement saveNote = driver.findElement(By.id("saveNoteButton0"));
        saveNote.click();

    }

    @Test
    public void testVideoPlaybackAndNoteHighlight() throws InterruptedException {
    
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
   // Wait for the seekInput to be visible and enabled before clearing it
    WebElement seekInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[data-plyr='seek']")));
    wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[data-plyr='seek']")));
    seekInput.clear();
    seekInput.sendKeys("0");
    
    WebElement playPauseButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".plyr__controls__item[data-plyr='play']")));
    playPauseButton.click();

    // Wait until the video reaches 0:05
    TimeUnit.SECONDS.sleep(5);

     // Pause the video using JavaScript
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("document.querySelector('.plyr__controls__item[data-plyr=\"play\"]').click();");

    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
    Thread.sleep(1000);
    
    // Verify that the note and timestamp are highlighted
    WebElement fullNotesSection = driver.findElement(By.id("description-list-0"));
    WebElement highlightedNote = fullNotesSection.findElement(By.xpath("//dt[text()='0:05']"));
    WebElement noteContent = fullNotesSection.findElement(By.xpath("//dd[text()='JUNIT TEST NOTE']"));

    String backgroundColor = highlightedNote.getCssValue("background-color");
    String contentBackgroundColor = noteContent.getCssValue("background-color");

    // Assuming the highlighted color is yellow
    Assert.assertEquals("rgba(255, 255, 204, 1)", backgroundColor);
    Assert.assertEquals("rgba(255, 255, 204, 1)", contentBackgroundColor);

    TimeUnit.SECONDS.sleep(5);
    }


    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
