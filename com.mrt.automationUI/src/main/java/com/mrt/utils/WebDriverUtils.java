package com.mrt.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v99.network.Network;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.collect.ImmutableList;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
public class WebDriverUtils {

    public static void stopNetwork() {
        DevTools devTools = ((ChromiumDriver) Driver.getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setBlockedURLs(ImmutableList.of("")));
    }

    public static void stopNetwork(String url) {
        DevTools devTools = ((ChromiumDriver) Driver.getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setBlockedURLs(ImmutableList.of(url)));
    }

    public static void waitForVisibility(WebElement element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
        } catch (Exception e) {
            System.out.println("Element is not visible!");
        }
    }

    public static void waitForVisibilityList(List<WebElement> element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElements(element)));
        } catch (Exception e) {
            System.out.println("Element List is not visible!");
        }
    }

    public static void waitForInvisibility(WebElement element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            System.out.println("Element is not invisible!");
        }
    }

    public static void waitForInvisibilityList(List<WebElement> element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.invisibilityOfAllElements(element));
        } catch (Exception e) {
            System.out.println("Element List is not invisible!");
        }
    }

    public static void waitForPageToLoad(long timeOutInSeconds) {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            System.out.println("Waiting for page to load...");
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeOutInSeconds));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println(
                    "Timeout waiting for Page Load Request to complete after " + timeOutInSeconds + " seconds");
        }
    }

    public static WebElement waitForClickability(WebElement element, int timeout) {
        try {
            WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(timeout));
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            System.out.println("Element is not clickable!");
            return null;
        }
    }

    public static void highlightWebElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
                    element);
        } catch (Exception e) {
            System.out.println("Element is not visible to highlight");
        }
    }

    public static void highlightAndRemoveWebElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
                    element);
            smallStaticWait();
            js.executeScript("arguments[0].removeAttribute('style','')", element);
        } catch (Exception e) {
            System.out.println("Element is not visible to highlight and remove");
        }
    }

    public static void highlightAndClick(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
            js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');",
                    element);
            js.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            System.out.println("Element is not visible to highlight and click");
        }
    }

    public static void waitFor(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void switchToTabAndStay() {
        ArrayList<String> tabs2 = new ArrayList<String>(Driver.getDriver().getWindowHandles());
        for (int i = 0; i < 10; i++) {
            if (tabs2.size() < 2) {
                waitFor(3);
                tabs2 = new ArrayList<String>(Driver.getDriver().getWindowHandles());
            } else {
                break;
            }
        }
        Driver.getDriver().switchTo().window(tabs2.get(1));
        waitFor(3);
    }

    /* Get the latest file from a specific directory */
    public static String getLatestFilefromDir() {
        String dirPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "resources" + File.separator + "downloads"; /// getting it from eclipse download
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        String[] fileName = splitPath(lastModifiedFile.toString());
        String lastFile = fileName[fileName.length - 1];
        return lastFile;
    }

    private static String[] splitPath(String pathString) {
        Path path = Paths.get(pathString);
        return StreamSupport.stream(path.spliterator(), false).map(Path::toString).toArray(String[]::new);
    }

    public static void action(WebElement element) {
        try {
            Actions action = new Actions(Driver.getDriver());
            action.moveToElement(element).click().build().perform();
        } catch (Exception e) {
            System.out.println("Element is not available to action");
        }
    }

    public static void actionScrollDown() {
        Actions a = new Actions(Driver.getDriver());
        a.sendKeys(Keys.PAGE_DOWN).build().perform();
    }

    public static void scrollUp() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverUtils.waitFor(2);
        js.executeScript("window.scrollTo(0, -document.body.scrollHeight);");
        WebDriverUtils.waitFor(2);
    }

    public static void hover(WebElement webElement) {
        try {
            Actions actions = new Actions(Driver.getDriver());
            actions.moveToElement(webElement).perform();
        } catch (Exception e) {
            System.out.println("Element is not available to hover");
        }
    }

    // it will scroll down the webElement until it is centered in the page
    public static void moveToWebElementDown(WebElement element) {
        try {
            String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                    + "var elementTop = arguments[0].getBoundingClientRect().top;"
                    + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
            ((JavascriptExecutor) Driver.getDriver()).executeScript(scrollElementIntoMiddle, element);
        } catch (Exception e) {
            System.out.println("Element is not available to scrolldown");
        }
    }

    // it will scroll up the webElement until it is centered in the page
    public static void moveToWebElementUp(WebElement element) {
        try {
            String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);"
                    + "var elementTop = arguments[0].getBoundingClientRect().top;"
                    + "var elementCenter = elementTop - (viewPortHeight/2);"
                    + "window.scrollTo(0, elementCenter < 0 ? 0 : elementCenter);";

            ((JavascriptExecutor) Driver.getDriver()).executeScript(scrollElementIntoMiddle, element);
        } catch (Exception e) {
            System.out.println("Element is not available to scrollup");
        }
    }

    public static List<String> getSpecialChars() {
        List<String> specialChar = Arrays.asList("`", "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_",
                "+", "=", "[", "{", "]", "}", ":", ";", "'", "<", ",", ">", ".", "/", "?", "\"", "\\");
        return specialChar;
    }

    public static boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public static boolean isElementPresentList(List<WebElement> element) {
        try {
            element.get(0).isDisplayed();
            return true;
        } catch (java.lang.IndexOutOfBoundsException e) {
            return false;
        }
    }

    public static void clickOnCoordinate(int x, int y) {
        new Actions(Driver.getDriver()).moveByOffset(x, y).click().perform();
    }

    public static List<Integer> getElementLocation(WebElement element) {
        List<Integer> locations = new ArrayList<Integer>();
        Point point = element.getLocation();
        System.out.println("x:" + point.getX());
        System.out.println("y:" + point.getY());
        locations.add(point.getX());
        locations.add(point.getY());
        return locations;
    }

    public static String getFileFromFolder(String str) {
        File filePath = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                + File.separator + "resources" + File.separator + "TestData_Files" + File.separator + str);
        return filePath.getAbsoluteFile().toString();
    }

    public static void clearText(WebElement element) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    public static void sendKeysAndEnter(WebElement element, String data) {
        element.sendKeys(data, Keys.ENTER);
    }

    public static String getCurrentUrl() {
        String currentUrl = Driver.getDriver().getCurrentUrl();
        return currentUrl;
    }

    public static void waitForNotification(WebElement element) {
        waitForVisibility(element, 15);
        for (int i = 0; i < 10; i++) {
            if (((Integer.parseInt(element.getText())) <= 1)) {
                WebDriverUtils.waitFor(3);
                System.out.println("waiting for notification");
                continue;
            } else {
                break;
            }
        }
    }

    //	Get values out of the browsers local storage using a key
    public String getLocalStorageValue(String key) {
        if (isEmptyString(key)) {
            return "";
        }
        try {
            String value = "";
            for (int i = 0; i < 30; i++) {
                LocalStorage local = ((WebStorage) Driver.getDriver()).getLocalStorage();
                value = local.getItem(key);
                if (!isEmptyString(value)) {
                    break;
                } else {
                    System.out.println("value is empty");
                    mediumStaticWait();
                }
            }
            return value;

        } catch (Exception e) {
            System.out.println("==== error: " + e.getMessage());
            return "";
        }
    }

    public static boolean isEmptyString(String value) {
        if (value == null || value.equals("")) {
            return true;
        }
        return false;
    }

    //	gets current date
    public static String getCurrentDateWithFormat() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, MMM d, YYYY | HH:MM");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String getPreviousDate(int day) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY h:mm a");
        LocalDateTime now = LocalDateTime.now().minusDays(day);
        return dtf.format(now);
    }

    public static String getLaterDateWithFormat(int day, String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now().plusDays(day);
        return dtf.format(now);
    }

    public static String getLaterDate(int day) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY h:mm a");
        LocalDateTime now = LocalDateTime.now().plusDays(day);
        return dtf.format(now);
    }

    //	 Format date
    public static String formatDate(String dateFormat) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    // Scrolls to specific element
    public static void scrollToSpecificWebElement(WebElement target) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverUtils.waitFor(1);
        js.executeScript("arguments[0].scrollIntoView(true)", target);
        WebDriverUtils.waitFor(1);
    }

    // Scrolls to specific element
    public static void scrollToSpecificWebElement1(WebElement target) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverUtils.waitFor(2);
        js.executeScript("arguments[0].scrollIntoView(false)", target);
    }

    // Hard click with JSE
    public static void clickWithJSE(WebElement target) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        waitForClickability(target, 10);
        js.executeScript("arguments[0].click()", target);
    }

    public static void clickAndScroll(WebElement element) {
        int loop = 0;
        while (loop < 10) {
            try {
                element.click();
                break;
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
                js.executeScript("window.scrollBy(0,100)");
                WebDriverUtils.waitFor(2);
                System.out.println("scroll: " + loop);
                loop++;
            }
        }
    }

    //wait for element to be not stale and @throws InterruptedException
    public static void waitForStaleElement(WebElement element) {
        WebElement el = element;
        int y = 0;
        while (y <= 10) {
            if (y <= 10) {
                try {
                    el.isDisplayed();
                    break;
                } catch (StaleElementReferenceException st) {
                    y++;
                    try {
                        Thread.sleep(4000);
                        el = element;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (WebDriverException we) {
                    y++;
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static WebElement fluentWait(WebElement element) {
        Wait<WebDriver> wait = new FluentWait<>(Driver.getDriver()).withTimeout(Duration.ofSeconds(40))
                .pollingEvery(Duration.ofSeconds(2)).ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        return wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
    }

    public static void smallStaticWait() {
        waitFor(1);
    }

    public static void mediumStaticWait() {
        waitFor(3);
    }

    public static void largeStaticWait() {
        waitFor(5);
    }

    public static void jseScrollInToViewFalseList(List<WebElement> allElements, int i) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(false);",
                allElements.get(i));
    }

    public static void jseScrollInToViewFalse(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(false);", element);
    }

    public static void jseScrollInToViewTrue(WebElement element) {
        ((JavascriptExecutor) Driver.getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void scrollDownlittle() {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverUtils.waitFor(2);
        js.executeScript("window.scrollBy(0,300)");
        WebDriverUtils.waitFor(2);
    }

    public static void scrollDownByDimentions(int horizontal, int vertical) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getDriver();
        WebDriverUtils.waitFor(2);
        js.executeScript("window.scrollBy(" + horizontal + "," + vertical + ")");
        WebDriverUtils.waitFor(2);
    }

    public static void refreshURL() {
        Driver.getDriver().navigate().refresh();
    }

    public static void resetAccountLockout() {
        Driver.getDriver().get(ConfigReader.getProperty("url") + "/axesreset");
    }

    public static List<Integer> sorted(List<String> sort) {
        List<Integer> sortedFirstNum = new ArrayList<>();
        for (int i = 0; i < sort.size(); i++) {
            int firstNum = Integer.valueOf(sort.get(i).split("-")[0]);
            sortedFirstNum.add(firstNum);
        }
        Collections.sort(sortedFirstNum);
        return sortedFirstNum;
    }
}
