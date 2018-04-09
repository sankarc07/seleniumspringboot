package com.selenium.controller;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/selenium")
public class SeleniumController {

	@RequestMapping(value = "/hai", method = RequestMethod.GET)
	public String sayHai() {
		return "userdetails";
	}
	
//	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String beginOrderCreation(Map<String,String> model) {
		model.put("sample", "test");
		return "begin-order-creation";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String begin() {
		runScript("yearly", "schellamuthu.consultant@consumer.org", false, "creditcard", 1, "ci");
//		try {
//			getDBConnection();
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
		return "begin-order-creation";
	}

	public static WebDriver getDriver() {
		String exePath = "E:\\Study\\Selenium\\SupportingJars\\chromedriver_win32\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}

	public static String getURL(String env) {
		if (env.equalsIgnoreCase("ci")) {
			return "https://ecqci.crinfra.net/ec/checkout/offer";
		} else if (env.equalsIgnoreCase("qa4")) {
			return "https://ecqci.crinfra.net/ec/checkout/offer";
		}
		return "https://10.242.208.136:8443/ec/checkout/offer";
		// return "https://10.242.208.136:8443/ec/checkout/offer";
	}

	public static void runScript(String offerType, String email, boolean donation, String paymentType,
			int numberOfOrders, String env) {
		WebDriver driver = getDriver();
		driver.get(getURL(env));
		for(int i=1; i<=numberOfOrders; i++) {
			WebElement offer;
			// String sPageName = (String)((JavascriptExecutor)driver).executeScript("return s.pageName;");
			if ("bundle".equalsIgnoreCase(offerType)) {
				offer = driver.findElement(By.cssSelector("#activate_access > div:nth-child(2) > button"));
			} else if ("yearly".equalsIgnoreCase(offerType)) {
				offer = driver.findElement(By.cssSelector("#activate_access > div:nth-child(4) > button"));
			} else if ("monthly".equalsIgnoreCase(offerType)) {
				offer = driver.findElement(By.cssSelector("#activate_access > div:nth-child(6) > button"));
			} else {
				return;
			}
			offer.click();
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement username = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
			String modifiedEmail = generateEmails(email);
			username.sendKeys(modifiedEmail);
			driver.findElement(By.id("password")).sendKeys("Password1");
			driver.findElement(By.id("confirm-password")).sendKeys("Password1");
			driver.findElement(By.id("desktop-signup-btn")).click();
			WebElement donationPopup = driver.findElement(By.id("propertiesModal"));
			if (donationPopup.isDisplayed()) {
				if (donation)
					driver.findElement(By.id("yes-btn")).click();
				else
					driver.findElement(By.className("no-thanks-btn")).click();
	//			driver.findElement(By.cssSelector("#propertiesModal > div > div > div.modal-header > button")).click();
			}
			if (null == paymentType || "creditcard".equalsIgnoreCase(paymentType)) {
				driver.findElement(
						By.cssSelector("#select_payment > div.form-buttons.hidden-xs > button.order-flow-btn.credit-card")).click();
				driver.findElement(By.name("firstname")).sendKeys("Sankar");
				driver.findElement(By.name("lastname")).sendKeys("Guru");
				driver.findElement(By.name("billingstreetaddress")).sendKeys("Chennai");
				WebElement zipCode = driver.findElement(By.name("zipbilling"));
				zipCode.sendKeys("12345");
				zipCode.sendKeys(Keys.TAB);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("hpf-iframe")));
				driver.switchTo().frame("hpf-iframe");
				driver.findElement(By.id("ccNumber")).sendKeys("4444444444444448");
				driver.findElement(By.id("expYear")).sendKeys("2020");
				driver.findElement(By.className("completeButton")).click();
			}
			completedOrderDetails(modifiedEmail);
//			driver.close();
		}
	}
	
	public static void completedOrderDetails(String email) {
		
	}

	public static String generateEmails(String sampleEmail) {
		Random random = new Random();
		String second = sampleEmail.substring(sampleEmail.indexOf("@"));
		String first = sampleEmail.substring(0, sampleEmail.indexOf("@"));
		sampleEmail = first + "+" + Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
				+ random.nextInt(99999) + second;
		return sampleEmail;
	}
	
	/*public static void getDBConnection() throws SQLException, ClassNotFoundException {
	   // JDBC driver name and database URL
	   final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";  
	   final String DB_URL = "jdbc:mysql://localhost/EMP";

	   //step1 load the driver class  
	   Class.forName(JDBC_DRIVER);  
	     
	   //step2 create  the connection object  
	   Connection con=DriverManager.getConnection(  
	   "jdbc:oracle:thin:@crecqdb1dev1.crinfra.net:1521:ecqdev1","SANDBOX1","sandbox1");  
	     
	   //step3 create the statement object  
	   Statement stmt=con.createStatement();  
	     
	   //step4 execute query  
	   ResultSet rs=stmt.executeQuery("select * from tcustomer");  
	   while(rs.next())  
	   System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
	     
	   //step5 close the connection object  
	   con.close();  

	}*/
	
}
