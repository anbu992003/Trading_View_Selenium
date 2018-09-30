import static java.util.concurrent.TimeUnit.SECONDS;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

//import io.github.bonigarcia.wdm.WebDriverManager;


public class ChromeAndFirefoxTest {



	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver", "/Users/anbu/Downloads/chromedriver");
		System.setProperty("webdriver.gecko.driver", "/Users/anbu/Downloads/geckodriver");

		
		int timeout = 30;
		String sutUrl = "https://en.wikipedia.org/wiki/Main_Page";
		
		WebDriver chrome = new ChromeDriver();
		chrome.manage().timeouts().implicitlyWait(timeout, SECONDS);
		chrome.get(sutUrl);
		System.out.println(chrome.getTitle());
		
//		WebDriver firefox = new FirefoxDriver();
//		firefox.manage().timeouts().implicitlyWait(timeout, SECONDS);
//		firefox.get(sutUrl);
//		System.out.println(firefox.getTitle());
		

		// Implicit timeout
		
		

		// Open page in different browsers
		

		
		
		

	}
}