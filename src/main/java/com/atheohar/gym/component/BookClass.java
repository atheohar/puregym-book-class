package com.atheohar.gym.component;

import static org.junit.Assert.fail;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.atheohar.gym.model.SessionInfo;

@Service
public class BookClass {

  private static final Logger log = LoggerFactory.getLogger(BookClass.class);

  private WebDriver driver;
  private String baseUrl;
  private StringBuffer verificationErrors = new StringBuffer();

  @Value("${gym.user}")
  private String user;
  @Value("${gym.code}")
  private String code;
  @Value("${gym.monday}")
  private String mondaySession;
  @Value("${gym.tuesday}")
  private String tuesdaySession;
  @Value("${gym.wednesday}")
  private String wednesdaySession;
  @Value("${gym.thursday}")
  private String thursdaySession;
  @Value("${gym.friday}")
  private String fridaySession;
  @Value("${gym.saturday}")
  private String saturdaySession;
  @Value("${gym.sunday}")
  private String sundaySession;

  @Scheduled(cron = "${gym.frequency}")
  public void bookSession() throws InterruptedException {

    log.info(user);

    FirefoxOptions fireFoxOptions = new FirefoxOptions();
    fireFoxOptions.addArguments("-headless");
    DesiredCapabilities firefoxcapabilities = DesiredCapabilities.firefox();
    firefoxcapabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, fireFoxOptions);

    driver = new FirefoxDriver(firefoxcapabilities);
    baseUrl = "https://www.puregym.com";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"));
    localCalendar.add(Calendar.DATE, 7);

    SessionInfo sessionInfo = getSessionInfo(localCalendar);

    if (sessionInfo == null) {
      log.info("No session set for this day");
      return;
    }

    log.info(sessionInfo.toString());

    driver.get(baseUrl);
    driver.findElement(By.id("loginStatus")).click();
    driver.findElement(By.id("email")).clear();
    driver.findElement(By.id("email")).sendKeys("antonis.theocharides@argos.co.uk");
    driver.findElement(By.id("pin")).clear();
    driver.findElement(By.id("pin")).sendKeys("12014751");
    driver.findElement(By.id("login-submit")).click();
    while (!driver.findElement(By.cssSelector("#menu-bookaclass > a.tabs__link")).isDisplayed()) {

    }
    driver.findElement(By.cssSelector("#menu-bookaclass > a.tabs__link")).click();

    while (!driver.findElement(By.id("selector-list")).isDisplayed()) {

    }

    driver.findElement(By.id("selector-list")).click();

    log.info("//div[@class=\"calendar-column__title\"][span=\"" + sessionInfo.getDate()
        + "\"]/../a[@class=\"calendar-card\"]/div[contains(string(),\"" + sessionInfo.getTitle()
        + "\")]/..");

    try {
      driver.findElement(By.xpath("//div[@class=\"calendar-column__title\"][span=\""
          + sessionInfo.getDate() + "\"]/../a[@class=\"calendar-card\"]/div[contains(string(),\""
          + sessionInfo.getTitle() + "\")]/..")).click();

      driver.findElement(By.id("book")).click();

    } catch (NoSuchElementException e) {
      log.info("Already booked");
    } finally {
      driver.quit();
      String verificationErrorString = verificationErrors.toString();
      if (!"".equals(verificationErrorString)) {
        fail(verificationErrorString);
      }
    }
  }

  private SessionInfo getSessionInfo(Calendar localCalendar) {

    int currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);

    String date = "";
    String title = "";

    switch (currentDayOfWeek) {
      case Calendar.MONDAY:
        title = mondaySession;
        date += "Mon";
        break;
      case Calendar.TUESDAY:
        title = tuesdaySession;
        date += "Tue";
        break;
      case Calendar.WEDNESDAY:
        title = wednesdaySession;
        date += "Wed";
        break;
      case Calendar.THURSDAY:
        title = thursdaySession;
        date += "Thu";
        break;
      case Calendar.FRIDAY:
        title = fridaySession;
        date += "Fri";
        break;
      case Calendar.SATURDAY:
        title = saturdaySession;
        date += "Sat";
        break;
      case Calendar.SUNDAY:
        title = sundaySession;
        date += "Sun";
        break;

      default:
        break;
    }

    Integer currentDay = localCalendar.get(Calendar.DATE);
    date += " " + currentDay.toString();
    Integer currentMonth = localCalendar.get(Calendar.MONTH) + 1;
    date += "/" + currentMonth.toString();

    if (title.equals("")) {
      return null;
    }

    return new SessionInfo(date, title);
  }
}
