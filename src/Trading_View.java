import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Trading_View {

	private static boolean headless = true;
	public static boolean startCrawling = true;
	public static WebDriver chrome = null;
	private static List<NSEScript> records = null;
	private static String lastRecordSymbol = null;

	// Nifty 50
//	private static String inputFileName = "/Users/anbu/Downloads/ind_nifty50list.csv";
//	private static String outFilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/report.out";	
//	private static String retryfilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/report_retry.out";

	// Nifty 500
//	private static String inputFileName = "/Users/anbu/Downloads/ind_nifty500list.csv";
//	private static String outFilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/nifty500_report.out";
//	private static String retryfilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/nifty500_report_retry.out";

	// Portfolio
//	private static String inputFileName = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/portfolio.txt";
//	private static String outFilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/portfolio_report.out";
//	private static String retryfilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/portfolio_report_retry.out";

	// Watchlist N500 Buy
//	private static String inputFileName = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/Watchlist_Buy.txt";
//	private static String outFilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/Watchlist_Buy.out";
//	private static String retryfilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/Watchlist_Buy_retry.out";

	// Watchlist - General
	private static String inputFileName = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/Watchlist.txt";
	private static String outFilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/Watchlist_report.out";
	private static String retryfilename = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/Watchlist_report_retry.out";

	private static String chromeDriverLocation = "/Users/anbu/Downloads/chromedriver";
	private static String screenshotLocation = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/sel.png";
	private static String htmlLocation = "/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/report.html";
	private static boolean retriedFlag = false;
	private static List<NSEScript> retryRecords = new ArrayList<NSEScript>();
	private static List<NSEScript> outRecords = new ArrayList<NSEScript>();

	private static long sleepTime = 3000;
	private static String buySpanStr = "//span[contains(@class, 'counterNumber-3l14ys0C-') and contains(@class, 'brandColor-1WP1oBmS-')]";
	private static String sellSpanStr = "//span[contains(@class, 'counterNumber-3l14ys0C-') and contains(@class, 'redColor-Hpg7doOR-')]";
	private static String snrClssNm = "itemContent-OyUxIzTS-";
	private static String dySmCssStr = "div.summary-72Hk5lHE- span.speedometerSignal-pyzN--tL-";
	private static String dySmXpathStr = "//*[@id=\"technicals-root\"]/div/div/div[2]/div[2]/span[2]";

	private static class NSEScript {
		private String companyName;
		private String industry;
		private String symbol;
		private String series;
		private String isin;
		private String day;
		private String week;
		private String month;

		public NSEScript(String companyName, String industry, String symbol, String series, String isin) {
			this.companyName = companyName;
			this.industry = industry;
			this.symbol = symbol;
			this.series = series;
			this.isin = isin;
		}

		public String getCompanyName() {
			return companyName;
		}

		public String getIndustry() {
			return industry;
		}

		public String getSymbol() {
			return symbol;
		}

		public String getSeries() {
			return series;
		}

		public String getDay() {
			return day;
		}

		public String getWeek() {
			return week;
		}

		public String getMonth() {
			return month;
		}

		public String getIsin() {
			return isin;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public void setWeek(String week) {
			this.week = week;
		}

		public void setMonth(String month) {
			this.month = month;
		}
	}

	private static void config() throws Exception {
		System.setProperty("webdriver.chrome.driver", chromeDriverLocation);

		int timeout = 30;

		// Setting your Chrome options (Desired capabilities)
		ChromeOptions options = new ChromeOptions();
		if (headless) {
			options.addArguments("headless");
			options.addArguments("window-size=1200x600");
		} else {
			options.addArguments("--start-maximized");
			options.addArguments("--start-fullscreen");
		}

//		WebDriver chrome = new ChromeDriver();
		chrome = new ChromeDriver(options);
		chrome.manage().timeouts().implicitlyWait(timeout, SECONDS);

		System.out.println("Configurations Completed!!!");
	}

	private static void crawlMainPage(NSEScript script) throws Exception {
//		String strUrl = "https://in.tradingview.com/symbols/NSE-NRBBEARING/technicals/";
		String strUrl = "https://in.tradingview.com/symbols/NSE-" + script.getSymbol() + "/technicals/";

		System.out.println("URL: " + strUrl);

		chrome.get(strUrl);
		System.out.println("Title: " + chrome.getTitle());
		if(chrome.getTitle().contains("Page Not Found"))
		{			
			throw new Exception("Page Not Found");
		}
		
//		// Load page & take screenshot of full-screen page
//		File scrFile = ((TakesScreenshot) chrome).getScreenshotAs(OutputType.FILE);
//		FileUtils.copyFile(scrFile, new File("/Users/anbu/Downloads/phantomjs-2.1.1-macosx/work/trading_view/sel.png"));

		// div class
		// speedometerWrapper-1SNrYKXY- summary-72Hk5lHE-
		// span class
		// speedometerSignal-pyzN--tL- neutralColor-15OoMFX9-
		String daySummary = chrome.findElement(By.cssSelector(dySmCssStr)).getText();
		String daySummary1 = chrome.findElement(By.xpath(dySmXpathStr)).getText();

		System.out.println("Day Summary: " + daySummary);
		System.out.println("Day Summary1: " + daySummary1);
		script.setDay(daySummary1);

		// Buy
		// Spans
		// brandColor-1WP1oBmS-
		Integer buyVal = getBuyValue();

		// item-17wa4fow-
		// itemContent-OyUxIzTS-
		// 1 day
		// 1 week
		// 1 month
		retriedFlag = false;
		WebElement dayScenario = null;
		List<WebElement> scenario = chrome.findElements(By.className(snrClssNm));
		for (int i = 0; i < scenario.size(); i++) {

//			System.out.println(scenario.get(i).getText());

			if (scenario.get(i).getText().contains("month")) {
				System.out.println("Month: ");
				script.setMonth(crawlNextPage(scenario.get(i), buyVal));
			} else if (scenario.get(i).getText().contains("week")) {
				System.out.println("Week: ");
				script.setWeek(crawlNextPage(scenario.get(i), buyVal));
			} else if (scenario.get(i).getText().contains("day")) {
				dayScenario = scenario.get(i);
			}
		}

		if ((daySummary1 == null || daySummary1.trim().isEmpty())
				&& (dayScenario != null && dayScenario.getText().contains("day"))) {
			System.out.println("Day: ");
			script.setDay(crawlNextPage(dayScenario, buyVal));
		}

		System.out.println("OUT:\t" + script.getSymbol() + "," + script.getDay() + "," + script.getWeek() + ","
				+ script.getMonth() + "\n");

		if (retriedFlag) {
			retryRecords.add(script);
			outRecords.add(script);
			retriedFlag = false;
		} else {
			outRecords.add(script);
		}

	}

	private static void saveScreenshot() {
		// Load page & take screenshot of full-screen page

		try {

			File scrFile = ((TakesScreenshot) chrome).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenshotLocation));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void writeHtml() {
		try {
			String stored_report = chrome.getPageSource();

			File f = new File(htmlLocation);
			FileWriter writer = new FileWriter(f, true);
			writer.write(stored_report);
			System.out.println("Report Created is in Location : " + f.getAbsolutePath());
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static Integer getBuyValue() {
//		System.out.println("Inside getBuyValue!!!");
		JavascriptExecutor jse = (JavascriptExecutor) chrome;
		jse.executeScript("window.scrollBy(0,250)", "");
		jse.executeScript("window.scrollBy(0,250)", "");
		writeHtml();
		saveScreenshot();

		Integer buyVal = 0;
//		List<WebElement> buySpans = chrome.findElements(By.className("span.counterNumber-3l14ys0C-.brandColor-1WP1oBmS-"));
		List<WebElement> buySpans = chrome.findElements(By.xpath(buySpanStr));
		List<WebElement> sellSpans = chrome.findElements(By.xpath(sellSpanStr));

//		WebDriverWait wait = new WebDriverWait(chrome, 10);
//		List<WebElement> buySpans  = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("span.counterNumber-3l14ys0C-.brandColor-1WP1oBmS-")));

		// Summation of buy spans
		for (int i = 0; i < buySpans.size(); i++) {
			buyVal += Integer.parseInt(buySpans.get(i).getText());
		}

		// Deletion of sell spans from buy spans
		for (int i = 0; i < sellSpans.size(); i++) {
			buyVal -= Integer.parseInt(sellSpans.get(i).getText());
		}

//		System.out.println("Count of Buy Value: " + buySpans.size());
//		System.out.println("Updated Buy Value: " + buyVal);

		jse.executeScript("window.scrollBy(0,-250)", "");
		jse.executeScript("window.scrollBy(0,-250)", "");

		return buyVal;
	}

	private static String crawlNextPage(WebElement element, Integer cmpBuyVal) throws Exception {
		element.click();

//		String scenario = chrome.findElement(By.cssSelector("div.active-1jm_3h9d- div.active-1jm_3h9d-")).getText();
		Thread.sleep(sleepTime);
		Integer buyVal = getBuyValue();

		int retryCount = 0;
		while (buyVal.intValue() == cmpBuyVal.intValue() && retryCount < 3) {
			System.out.println("Waiting for change in Buy value from " + buyVal.intValue() + " !!!...");
			Thread.sleep(sleepTime);
			buyVal = getBuyValue();
			retryCount++;
		}

		if (retryCount >= 3) {
			retriedFlag = true;
		}

		cmpBuyVal = buyVal;

		String summary = chrome.findElement(By.cssSelector("div.summary-72Hk5lHE- span.speedometerSignal-pyzN--tL-"))
				.getText();
		System.out.println(summary);

		return summary;
	}

	private static String readLastRecordSymbol(String filename) throws IOException {

		String line = null;
		String lastLine = null;
		String lastSymbol = null;

		try {
			FileReader fileReader = new FileReader(filename);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<NSEScript> lines = new ArrayList<NSEScript>();

			while ((line = bufferedReader.readLine()) != null) {
				if (line != null && !line.trim().isEmpty()) {
					lastLine = line;
				}
			}

			bufferedReader.close();

			if (lastLine != null && !lastLine.trim().isEmpty()) {
				String[] rec = lastLine.split(",");
				lastSymbol = rec[0];
				startCrawling = false;
			}
		} catch (Exception ex) {
			System.err.println("Output file is not present: " + filename);
		}

		System.out.println("lastSymbol: " + lastSymbol);
		return lastSymbol;
	}

	private static List<NSEScript> readFile(String filename) throws IOException {

		List<NSEScript> lines = new ArrayList<NSEScript>();
		try {
			FileReader fileReader = new FileReader(filename);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				if (line != null && !line.trim().isEmpty() && !line.startsWith("Company")) {

//				System.out.println("Line: "+ line);				
					String[] rec = line.split(",");

//				for (int i = 0; i < rec.length; i++) {
//					System.out.println("rec["+i+"] : " + rec[i]);
//				}

					NSEScript scrip = new NSEScript(rec[0], rec[1], rec[2].replaceAll("&|-", "_"), rec[3], rec[4]);
					lines.add(scrip);
				}
			}

			bufferedReader.close();
		} catch (Exception ex) {
			System.err.println("Failure reading inputFile: " + filename);
			throw ex;
		}

		return lines;
	}

	private static void writeOutputFile(String filename, List<NSEScript> records) throws IOException {
		try {

			File f = new File(filename);
			FileWriter writer = new FileWriter(f, true);

			for (Iterator iterator = records.iterator(); iterator.hasNext();) {
				NSEScript nseScript = (NSEScript) iterator.next();
				writer.write(nseScript.getSymbol() + "," + nseScript.getDay() + "," + nseScript.getWeek() + ","
						+ nseScript.getMonth() + "\n");
			}

			System.out.println("Report Created is in Location : " + f.getAbsolutePath());
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		try {

			lastRecordSymbol = readLastRecordSymbol(outFilename);

//Read NSE scripts		
			records = readFile(inputFileName);
			System.out.println("Total NSEScripts to process: " + records.size());

//Crawl 		
			config();
			for (Iterator iterator = records.iterator(); iterator.hasNext();) {
				NSEScript nseScript = (NSEScript) iterator.next();

				if (startCrawling) {
					try
					{
						crawlMainPage(nseScript);
					}
					catch(Exception ex)
					{
						System.err.println(ex.getMessage());
						System.err.println("ERROR: Processing " + nseScript.getSymbol());
						retryRecords.add(nseScript);
					}
				} else if (lastRecordSymbol != null && !lastRecordSymbol.isEmpty()
						&& lastRecordSymbol.equalsIgnoreCase(nseScript.getSymbol())) {
					System.out.println(nseScript.getSymbol() + " - Ignored!!!");
					startCrawling = true;
				} else {
					System.out.println(nseScript.getSymbol() + " - Ignored!!!");
				}

			}

		} finally {

			writeOutputFile(outFilename, outRecords);

			writeOutputFile(retryfilename, retryRecords);

			if (chrome != null)
				chrome.quit();
		}

	}

}
