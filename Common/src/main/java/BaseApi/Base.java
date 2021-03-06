package BaseApi;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by HP on 5/11/2015.
 */
public class Base {

    public WebDriver driver=null;
    @Parameters({"UseSauceLab","UserName","Key","BrowserName","BrowserVersion","os","url"})
    @BeforeMethod
    public void setUp(Boolean UseSauceLab,String UserName,String Key,String BrowserName,
                      String BrowserVersion,String os,String url)throws IOException{
        if(UseSauceLab==true){
            setUpCloud(UserName,Key,BrowserName,BrowserVersion,os,url);
        }
        else{
            localEnvironment(BrowserName,BrowserVersion,url);
        }

    }


    public void setUpCloud(String UserName,String Key,String BrowserName,
                           String BrowserVersion,String os,String url)throws IOException{
        DesiredCapabilities cap=new DesiredCapabilities();
        cap.setBrowserName(BrowserName);
        cap.setCapability("version", BrowserVersion);
        cap.setCapability("platform",os);
        this.driver=new RemoteWebDriver(new URL("http://"+UserName+":"+Key+"@ondemand.saucelabs.com:80/wd/hub"),cap);
        driver.navigate().to(url);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();

    }

    public void localEnvironment(String BrowserName,String BrowserVersion,String url){
        if(BrowserName.equalsIgnoreCase("firefox")){
            driver=new FirefoxDriver();
        }

        else if(BrowserName.equalsIgnoreCase("chrome")){
            System.setProperty("webdriver.chrome.driver", "../Common/Selenium-Driver/chromedriver.exe");
            driver=new ChromeDriver();
        }
        else if(BrowserName.equalsIgnoreCase("IE")){
            System.setProperty("webdriver.ie.driver","../Common/Selenium-Driver/IEDriverServer.exe");
            driver=new InternetExplorerDriver();
        }

        driver.navigate().to(url);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        //driver.manage().window().maximize();
    }







    @AfterMethod
    public void cleanUp(){
        driver.quit();
    }

    //Utility Method

    public void clickByCss(String locator){
        driver.findElement(By.cssSelector(locator)).click();
    }

    public String clickByXpath(String locator){
        driver.findElement(By.xpath(locator)).click();
        return locator;
    }

    public void typeByCss(String locator,String value){
        driver.findElement(By.cssSelector(locator)).sendKeys(value);
    }

    public void typeByXpath(String locator,String value){
        driver.findElement(By.xpath(locator)).sendKeys(value);
    }

    public void typeAndEnterByCss(String locator,String value){
        driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
    }

    public void typeAndEnterByXpath(String locator,String value){
        driver.findElement(By.xpath(locator)).sendKeys(value, Keys.ENTER);
    }

    public void sleepFor(int sec) throws InterruptedException {
        Thread.sleep(sec*1000);
    }

    public List<String> getListOfTextByCss(String locator){
        List<WebElement> elements=driver.findElements(By.cssSelector(locator));
        List<String> text=new ArrayList<String>();
        for(WebElement wb:elements){
            text.add(wb.getText());
        }
        return text;
    }


    public List<String> getListOfTextByXpath(String locator){
        List<WebElement> elements=driver.findElements(By.xpath(locator));
        List<String> text=new ArrayList<String>();
        for(WebElement wb:elements){
            text.add(wb.getText());
        }
        return text;
    }

    public void displayText(List<String> text){
        for(String st:text){
            System.out.println(st);
        }
    }
    public List<WebElement> getWebElements(String locator){
        List<WebElement> elements=driver.findElements(By.cssSelector(locator));
        return elements;
    }

    public List<WebElement> getWebElementsByXpath(String locator){
        List<WebElement> elements=driver.findElements(By.xpath(locator));
        return elements;
    }

    public void clickByText(String locator){
        driver.findElement(By.linkText(locator)).click();
    }

    public void navigateBack(){
        driver.navigate().back();
    }

    public String getTextByXpath(String locator){
        String text=driver.findElement(By.xpath(locator)).getText();
        return text;
    }
    //MouseHover on menu or image
    public void mouseOver(List<WebElement> elements) throws InterruptedException {
        for(WebElement st:elements){
            Actions action = new Actions(driver);
            action.moveToElement(st).build().perform();
            sleepFor(2);
        }
    }
    //Take a ScreenShot from the browser
    public void takeScreenShoot(String locator) throws IOException {
        File file=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, new File(locator),true);
    }



}
