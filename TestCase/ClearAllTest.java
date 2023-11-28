import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.List;

import static org.junit.Assert.*;

public class ClearAllTest {
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

        WebElement note = driver.findElement(By.id("note-content-0"));
        note.sendKeys("Note Test 1");

        Thread.sleep(1000);

        WebElement saveNote = driver.findElement(By.id("saveNoteButton0"));
        saveNote.click();

        WebElement playButton = driver.findElement(By.className("plyr__control--overlaid"));
        playButton.click();

        // let the video play for 5 seconds
        Thread.sleep(5000);

        note.sendKeys("Note Test 2");
        saveNote.click();

        Thread.sleep(1000);

        // Find the span element to click on
        WebElement spanElement = driver.findElement(By.id("select2-note-dropdown-0-container"));

        // Click on the span element to open the dropdown
        spanElement.click();

        WebElement selectNoteDropdown = driver.findElement(By.id("note-dropdown-0"));
        List<WebElement> dropdownOptions = selectNoteDropdown.findElements(By.cssSelector("option"));
        assertEquals("Dropdown should have two options", 3, dropdownOptions.size());
        assertEquals("First option should be 'Select a note'", "Select a note", dropdownOptions.get(0).getText().trim());
        assertEquals("Second option should be '0:00'", "0:00", dropdownOptions.get(1).getText().trim());


        assertTrue(
                dropdownOptions.get(2).getText().trim().equals("0:04") ||
                        dropdownOptions.get(2).getText().trim().equals("0:05")
        );



        WebElement fullNotesSection = driver.findElement(By.id("description-list-0"));
        List<WebElement> fullNotesTimestamp = fullNotesSection.findElements(By.className("col-3"));
        assertEquals("Should only have 2 timestamps", 2, fullNotesTimestamp.size());
        assertEquals("0:00", fullNotesTimestamp.get(0).getText().trim());

        assertTrue(
                fullNotesTimestamp.get(1).getText().trim().equals("0:04") ||
                        fullNotesTimestamp.get(1).getText().trim().equals("0:05")
        );
        List<WebElement> fullNotesContent = fullNotesSection.findElements(By.className("col-9"));
        assertEquals("Should only have 2 notes", 2, fullNotesContent.size());
        assertEquals("Note Test 1", fullNotesContent.get(0).getText().trim());
        assertEquals("Note Test 2", fullNotesContent.get(1).getText().trim());

    }

    @Test
    public void testClearAllFunctionality() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        Thread.sleep(1000);
        WebElement clearAllButton = driver.findElement(By.id("clearallNoteButton-0"));
        clearAllButton.click();
        Alert alert = driver.switchTo().alert();

        // Perform actions on the alert
        System.out.println("Alert Text: " + alert.getText());
        alert.accept(); // Click OK or confirm
        WebElement dlElement = driver.findElement(By.id("description-list-0"));

        // Get a list of inner elements
        java.util.List<WebElement> innerElements = dlElement.findElements(By.xpath("./*")); // Using XPath to select all child elements

        // Assert that there are no inner elements
        assertEquals("Expected no inner elements", 0, innerElements.size());
    }

    @After
    public void tearDown() {
        // Close the WebDriver after the test
        if (driver != null) {
            driver.quit();
        }
    }
}
