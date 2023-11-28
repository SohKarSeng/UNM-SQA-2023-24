import java.util.List;
import java.util.concurrent.TimeUnit;

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

public class NotesValidationTest {

    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");
        TimeUnit.SECONDS.sleep(15);
    }

    @Test
    public void testNoteEntryAndSave() throws InterruptedException {
        // Find the note content input element
        WebElement noteInput = driver.findElement(By.id("note-content-0"));

        // Enter notes in the "Enter your notes here" section
        noteInput.sendKeys("Test notes for JUnit");

        // Find the Save button and click it
        WebElement saveButton = driver.findElement(By.id("saveNoteButton0"));
        saveButton.click();
        
        TimeUnit.SECONDS.sleep(5);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 500);");
        Thread.sleep(1000);
        
        // Wait for the "Full Notes" section to be visible
        WebElement fullNotesSection = driver.findElement(By.id("description-list-0"));

        // Check the “Full Notes” section to ensure that the notes along with the timestamp are correct
        Assert.assertTrue("Full Notes section should contain the saved note", fullNotesSection.getText().contains("Test notes for JUnit"));

        // Check the "Select a note" dropdown 
        WebElement selectNoteDropdown = driver.findElement(By.id("note-dropdown-0"));
        List<WebElement> dropdownOptions = selectNoteDropdown.findElements(By.cssSelector("option"));
        Assert.assertEquals("Dropdown should have two options", 2, dropdownOptions.size());
        Assert.assertEquals("First option should be 'Select a note'", "Select a note", dropdownOptions.get(0).getText().trim());
        Assert.assertEquals("Second option should be '0:00'", "0:00", dropdownOptions.get(1).getText().trim());

        dropdownOptions.get(1).click();

        // Wait for the note content input field to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-content-0")));

        // Verify that the corresponding note content is displayed
        WebElement selectedNote = driver.findElement(By.id("note-content-0"));
        Assert.assertTrue("Note content should be displayed when the corresponding timestamp is clicked", selectedNote.getAttribute("value").contains("Test notes for JUnit"));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
