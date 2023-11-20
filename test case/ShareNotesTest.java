import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ShareNotesTest {

    private WebDriver driver;

    @Before
    public void setUp()throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\leebo\\Documents\\Degree\\Degree Year 3\\Software Quality Assurance\\SeleniumTest\\TestCase\\src\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("file:///C:/Users/leebo/Documents/Degree/Degree%20Year%203/Software%20Quality%20Assurance/UNM-SQA-2023-24-main/UNM-SQA-2023-24-main/dashboard.html");

        // Wait for the video to load
        TimeUnit.SECONDS.sleep(9);

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
    public void testShareNotes() throws UnsupportedFlavorException, IOException {
        // Step 1: Verify that notes are present in the “Full Notes” section with corresponding timestamps.
        WebElement fullNotesList = driver.findElement(By.id("description-list-0"));
        List<WebElement> notes = fullNotesList.findElements(By.tagName("dt"));
        Assert.assertTrue(!notes.isEmpty());

        // Step 2: Confirm the presence of the Share button in the notes interface.
        WebElement shareButton = driver.findElement(By.id("shareNoteButton-0"));
        Assert.assertTrue(shareButton.isDisplayed());

        // Step 3: Click on the Share button.
        shareButton.click();

        // Step 4: Copy the contents of the clipboard.
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String sharedContent = (String) clipboard.getData(DataFlavor.stringFlavor);

        // Step 5: Verify the format and content of the shared notes.
        Assert.assertTrue(sharedContent.startsWith("Video URL : "));
        Assert.assertTrue(sharedContent.contains("Note Content :"));
        Assert.assertTrue(sharedContent.contains("JUNIT TEST NOTE"));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}