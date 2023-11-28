import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class HomeButtonTest {

    private WebDriver driver;

    @Before
    public void setUp() throws InterruptedException, IOException {
        // Set up the WebDriver (Assuming you have ChromeDriver installed)
        driver = new ChromeDriver();
        // Open the website with the search functionality
        driver.get("https://sohkarseng.github.io/UNM-SQA-2023-24/dashboard.html");

        driver.manage().window().fullscreen();
        // Add a delay (in milliseconds) to wait for the page to load
        Thread.sleep(5000);  // Adjust the time as needed
        // Find the search bar element
        WebElement searchBar = driver.findElement(By.id("keyword-search"));

        // Enter a search keyword (replace "test" with the actual keyword)
        searchBar.sendKeys("Worlds 2023");

        // Click on thr search button to perform the search
        WebElement searchButton = driver.findElement(By.id("keyword-search-button"));
        searchButton.click();
    }

    @Test
    public void testHomeButtonFunctionality() throws InterruptedException {


        List<WebElement> anchors = driver.findElements(By.tagName("a"));
        Iterator<WebElement> i = anchors.iterator();

        while(i.hasNext()) {
            WebElement anchor = i.next();
            if (anchor.getAttribute("href").contains("dashboard")) {
                anchor.click();
                break;
            }
        }
        Thread.sleep(10000);
        for (int j = 0; j < 11; j++) {
            WebElement card = driver.findElement(By.id("videoTitle" + j));


            String videoTitle = card.getText();
            System.out.println("videoTitle" + j);

            System.out.println(videoTitle);


            assertTrue(
            videoTitle.toLowerCase().contains("software") ||
                    videoTitle.toLowerCase().contains("quality") ||
                    videoTitle.toLowerCase().contains("assurance")||
                    videoTitle.toLowerCase().contains("qa") ||
                    videoTitle.toLowerCase().contains("it")
            );


        }
    }
    // Additional test cases can be added for different scenarios

    @After
    public void tearDown() {
        // Close the WebDriver after the test
        if (driver != null) {
            driver.quit();
        }
    }
}


