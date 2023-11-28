import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import java.util.List;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;


public class NoteTakingTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");
    }

    @Test
    public void noteTaking () throws Exception {
        sleep(1000);

        WebElement inputText = driver.findElement(By.id("note-content-0"));
        inputText.sendKeys("Test");

        WebElement inputButton = driver.findElement(By.id("saveNoteButton0"));
        inputButton.click();

        sleep(2000);

        WebElement noteSaved = driver.findElement(By.id("note-dropdown-0"));
        Select select2 = new Select(noteSaved);
        select2.selectByIndex(1);

        inputText = driver.findElement(By.id("note-content-0"));
        String expectedResult = "Test";
        String actualText = inputText.getAttribute("value");

        assertEquals(expectedResult, actualText);
        
    }

    @After
    public void finish() {
        if (driver != null) {
            driver.quit();
        }
    }

}
