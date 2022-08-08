package com.mrt.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Driver {

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();

    // so no one can create object of Driver class
    private Driver() {

    }

    /**
     * synchronized makes method thread safe. It ensures that only 1 thread can use
     * it at the time.
     * <p>
     * Thread safety reduces performance but it makes everything safe.
     *
     */
    public synchronized static WebDriver getDriver() {

        if (driverPool.get() == null) {
            // specify browser type in configuration.properties file
            String browser = ConfigReader.getProperty("browser").toLowerCase();

            if (System.getProperty("browser") != null) {
                browser = System.getProperty("browser");
            }

            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    WebDriverManager.chromedriver().setup();
                    HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                    // sets the download folder
                    chromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "src"
                            + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                    chromeOptions.setExperimentalOption("prefs", chromePrefs);
                    // This will make Selenium WebDriver to wait until the initial HTML document has
                    // been completely loaded
                    chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                    driverPool.set(new ChromeDriver(chromeOptions));
                    driverPool.get().manage().deleteAllCookies();
                    break;
                case "chrome-headless":
                    ChromeOptions chromeHeadlessOptions = new ChromeOptions();
                    LoggingPreferences chromeLogs = new LoggingPreferences();
                    chromeLogs.enable(LogType.BROWSER, Level.ALL);
                    chromeHeadlessOptions.setCapability(CapabilityType.LOGGING_PREFS, chromeLogs);
                    chromeHeadlessOptions.setAcceptInsecureCerts(true);
                    WebDriverManager.chromedriver().setup();
                    HashMap<String, Object> headlessChromePrefs = new HashMap<String, Object>();
                    // sets the download folder
                    headlessChromePrefs.put("download.default_directory",
                            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                                    + File.separator + "resources" + File.separator + "downloads");
                    chromeHeadlessOptions.setExperimentalOption("prefs", headlessChromePrefs);
                    chromeHeadlessOptions.addArguments("--headless"); // disabling extensions
                    chromeHeadlessOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
                    chromeHeadlessOptions.addArguments("--no-sandbox"); // Bypass OS security model
                    chromeHeadlessOptions.addArguments("--disable-dev-shm-usage");
                    chromeHeadlessOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                    chromeHeadlessOptions.addArguments("--ignore-certificate-errors");
                    chromeHeadlessOptions.addArguments("--acceptInsecureCerts");
                    driverPool.set(new ChromeDriver(chromeHeadlessOptions));
                    driverPool.get().manage().deleteAllCookies();
                    Dimension dimension = new Dimension(1936, 1056);
                    driverPool.get().manage().window().setSize(dimension);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    // Creating firefox profile
                    FirefoxProfile profile = new FirefoxProfile();
                    // Instructing firefox to use custom download location
                    profile.setPreference("browser.download.folderList", 2);
                    // Setting custom download directory
                    profile.setPreference("browser.download.dir", System.getProperty("user.dir") + File.separator + "src"
                            + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                    // Skipping Save As dialog box for types of files with their MIME
                    profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                            "text/csv,application/java-archive, application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.microsoft.portable-executable");
                    // Creating FirefoxOptions to set profile
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setProfile(profile);
                    driverPool.set(new FirefoxDriver(firefoxOptions));
                    break;
                case "firefox-headless":
                    WebDriverManager.firefoxdriver().setup();
                    // Creating firefox profile
                    FirefoxProfile firefoxHeadlessProfile = new FirefoxProfile();
                    // Instructing firefox to use custom download location
                    firefoxHeadlessProfile.setPreference("browser.download.folderList", 2);
                    // Setting custom download directory
                    firefoxHeadlessProfile.setPreference("browser.download.dir",
                            System.getProperty("user.dir") + File.separator + "src" + File.separator + "test"
                                    + File.separator + "resources" + File.separator + "downloads");
                    // Skipping Save As dialog box for types of files with their MIME
                    firefoxHeadlessProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                            "text/csv,application/java-archive, application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.microsoft.portable-executable");
                    FirefoxOptions firefoxHeadlessOptions = new FirefoxOptions();
                    firefoxHeadlessOptions.setHeadless(true);
                    firefoxHeadlessOptions.setProfile(firefoxHeadlessProfile);
                    driverPool.set(new FirefoxDriver(firefoxHeadlessOptions));
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    EdgeDriverService edgeDriverService = EdgeDriverService.createDefaultService();
                    final String downloadPath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                            + "test" + File.separator + "resources" + File.separator + "downloads";
                    // ************* Enable downloading files / set path *******************
                    Map<String, Object> commandParamsEdge = new HashMap<>();
                    commandParamsEdge.put("cmd", "Page.setDownloadBehavior");
                    Map<String, String> paramsEdge = new HashMap<>();
                    paramsEdge.put("behavior", "allow");
                    paramsEdge.put("downloadPath", downloadPath);
                    commandParamsEdge.put("params", paramsEdge);
                    driverPool.set(new EdgeDriver(edgeDriverService, edgeOptions));
                    break;
                case "edge-headless":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeHeadlessOptions = new EdgeOptions();
                    edgeHeadlessOptions.addArguments("-inprivate");
                    edgeHeadlessOptions.addArguments("--headless");
                    edgeHeadlessOptions.addArguments("disable-gpu");
                    EdgeDriverService edgeDriverServiceHeadless = EdgeDriverService.createDefaultService();
                    final String downloadPathHeadless = System.getProperty("user.dir") + File.separator + "src"
                            + File.separator + "test" + File.separator + "resources" + File.separator + "downloads";
                    // ************* Enable downloading files / set path *******************
                    Map<String, Object> commandParamsEdgeHeadless = new HashMap<>();
                    commandParamsEdgeHeadless.put("cmd", "Page.setDownloadBehavior");
                    Map<String, String> paramsEdgeHeadless = new HashMap<>();
                    paramsEdgeHeadless.put("behavior", "allow");
                    paramsEdgeHeadless.put("downloadPath", downloadPathHeadless);
                    commandParamsEdgeHeadless.put("params", paramsEdgeHeadless);
                    driverPool.set(new EdgeDriver(edgeDriverServiceHeadless, edgeHeadlessOptions));
                    break;

                default:
                    throw new RuntimeException("Wrong browser name!");
            }
        }
        return driverPool.get();
    }

    public synchronized static WebDriver getOneDriver(String browser) {

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
                // sets the download folder
                chromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator + "src"
                        + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                chromeOptions.setExperimentalOption("prefs", chromePrefs);
                // This will make Selenium WebDriver to wait until the initial HTML document has
                // been completely loaded
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                driverPool.set(new ChromeDriver(chromeOptions));
                driverPool.get().manage().deleteAllCookies();
                break;
            case "chrome-headless":
                ChromeOptions chromeHeadlessOptions = new ChromeOptions();
                LoggingPreferences chromeLogs = new LoggingPreferences();
                chromeLogs.enable(LogType.BROWSER, Level.ALL);
                chromeHeadlessOptions.setCapability(CapabilityType.LOGGING_PREFS, chromeLogs);
                chromeHeadlessOptions.setAcceptInsecureCerts(true);
                WebDriverManager.chromedriver().setup();
                HashMap<String, Object> headlessChromePrefs = new HashMap<String, Object>();
                // sets the download folder
                headlessChromePrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator
                        + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                chromeHeadlessOptions.setExperimentalOption("prefs", headlessChromePrefs);
                chromeHeadlessOptions.addArguments("--headless"); // disabling extensions
                chromeHeadlessOptions.addArguments("--window-size=1280,800");
                chromeHeadlessOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
                chromeHeadlessOptions.addArguments("--no-sandbox"); // Bypass OS security model
                chromeHeadlessOptions.addArguments("--disable-dev-shm-usage");
                chromeHeadlessOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
                chromeHeadlessOptions.addArguments("--ignore-certificate-errors");
                chromeHeadlessOptions.addArguments("--acceptInsecureCerts");
                driverPool.set(new ChromeDriver(chromeHeadlessOptions));
                driverPool.get().manage().deleteAllCookies();
                break;
            case "chrome-incognito":
                ChromeOptions chromeIncognitoOptions = new ChromeOptions();
                WebDriverManager.chromedriver().setup();
                HashMap<String, Object> chromeIncognitoPrefs = new HashMap<String, Object>();
                // sets the download folder
                chromeIncognitoPrefs.put("download.default_directory", System.getProperty("user.dir") + File.separator
                        + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                chromeIncognitoOptions.setExperimentalOption("prefs", chromeIncognitoPrefs);
                // This will make Selenium WebDriver to wait until the initial HTML document has
                // been completely loaded
                chromeIncognitoOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
                chromeIncognitoOptions.addArguments("--incognito");
                driverPool.set(new ChromeDriver(chromeIncognitoOptions));
                driverPool.get().manage().deleteAllCookies();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                // Creating firefox profile
                FirefoxProfile profile = new FirefoxProfile();
                // Instructing firefox to use custom download location
                profile.setPreference("browser.download.folderList", 2);
                // Setting custom download directory
                profile.setPreference("browser.download.dir", System.getProperty("user.dir") + File.separator + "src"
                        + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                // Skipping Save As dialog box for types of files with their MIME
                profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                        "text/csv,application/java-archive, application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.microsoft.portable-executable");
                // Creating FirefoxOptions to set profile
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setProfile(profile);
                driverPool.set(new FirefoxDriver(firefoxOptions));
                break;
            case "firefox-headless":
                WebDriverManager.firefoxdriver().setup();
                // Creating firefox profile
                FirefoxProfile firefoxHeadlessProfile = new FirefoxProfile();
                // Instructing firefox to use custom download location
                firefoxHeadlessProfile.setPreference("browser.download.folderList", 2);
                // Setting custom download directory
                firefoxHeadlessProfile.setPreference("browser.download.dir", System.getProperty("user.dir") + File.separator
                        + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "downloads");
                // Skipping Save As dialog box for types of files with their MIME
                firefoxHeadlessProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
                        "text/csv,application/java-archive, application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.microsoft.portable-executable");
                FirefoxOptions firefoxHeadlessOptions = new FirefoxOptions();
                firefoxHeadlessOptions.setHeadless(true);
                firefoxHeadlessOptions.setProfile(firefoxHeadlessProfile);
                driverPool.set(new FirefoxDriver(firefoxHeadlessOptions));
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                EdgeDriverService edgeDriverService = EdgeDriverService.createDefaultService();
                final String downloadPath = System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "test" + File.separator + "resources" + File.separator + "downloads";
                // ************* Enable downloading files / set path *******************
                Map<String, Object> commandParamsEdge = new HashMap<>();
                commandParamsEdge.put("cmd", "Page.setDownloadBehavior");
                Map<String, String> paramsEdge = new HashMap<>();
                paramsEdge.put("behavior", "allow");
                paramsEdge.put("downloadPath", downloadPath);
                commandParamsEdge.put("params", paramsEdge);
                driverPool.set(new EdgeDriver(edgeDriverService, edgeOptions));
                break;
            case "edge-headless":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeHeadlessOptions = new EdgeOptions();
                edgeHeadlessOptions.addArguments("-inprivate");
                edgeHeadlessOptions.addArguments("--headless");
                edgeHeadlessOptions.addArguments("disable-gpu");
                EdgeDriverService edgeDriverServiceHeadless = EdgeDriverService.createDefaultService();
                final String downloadPathHeadless = System.getProperty("user.dir") + File.separator + "src" + File.separator
                        + "test" + File.separator + "resources" + File.separator + "downloads";
                // ************* Enable downloading files / set path *******************
                Map<String, Object> commandParamsEdgeHeadless = new HashMap<>();
                commandParamsEdgeHeadless.put("cmd", "Page.setDownloadBehavior");
                Map<String, String> paramsEdgeHeadless = new HashMap<>();
                paramsEdgeHeadless.put("behavior", "allow");
                paramsEdgeHeadless.put("downloadPath", downloadPathHeadless);
                commandParamsEdgeHeadless.put("params", paramsEdgeHeadless);
                driverPool.set(new EdgeDriver(edgeDriverServiceHeadless, edgeHeadlessOptions));
                break;

            default:
                throw new RuntimeException("Wrong browser name!");
        }
        return driverPool.get();
    }

    public static void analyzeLog(Scenario scenario) {
        String scenarioId = scenario.getUri().toString();
        String feature = "";
        try {
            String[] featuresPath = scenarioId.split("/");
            feature = (featuresPath[featuresPath.length - 1].split("\\.")[0]);
        } catch (Exception e) {
            System.out.println("error getting feature: " + e);
        }
        String scenarioName = scenario.getName();
        LogEntries logEntries = Driver.getDriver().manage().logs().get(LogType.BROWSER);
        ReadData data = new ReadData();
        ArrayList<String> issues = data.getAllIssues("not fixed");
        ArrayList<String> comments = data.getAllIssueComments("not fixed");
        boolean isWarningCaptured = false;
        entry: for (LogEntry entry : logEntries) {
            String level = entry.getLevel().toString().trim();
            String message = entry.getMessage().toString().trim();
            if (level.equals("WARNING") || level.equals("SEVERE")) {
                for (int i = 0; i < issues.size(); i++) {
                    Pattern pattern = Pattern.compile(issues.get(i));
                    Matcher matcher = pattern.matcher(message);
                    boolean matchFound = matcher.find();
                    if (matchFound) {
                        System.out.println("Match found");
                        createLogs("!KNOWN_ISSUE! --> " + level + ": " + message + "-->comment: " + comments.get(i));
                        continue entry;
                    }
                }
                createLogs(level + ": " + message + " -- > feature: " + feature + " -- > scenario: " + scenarioName);
                System.out.println(" <--- Browser warning/error has been captured! ---> ");
                isWarningCaptured = true;
            }
        }
        Assertions.assertFalse(isWarningCaptured);
    }

    public static void closeDriver() {
        if (driverPool != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }

    static String path = System.getProperty("user.dir") + File.separator + "target" + File.separator
            + "browserErrors.txt";

    public static void createLogs(String log) {
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferFileWriter = new BufferedWriter(fileWriter);
            Scanner scan = new Scanner(file);
            if (file.length() == 0) {
                bufferFileWriter.append(log);
                bufferFileWriter.newLine();
                bufferFileWriter.close();
            } else {
                boolean dublicate = false;
                while (scan.hasNextLine()) {
                    String nectLineMessage = scan.nextLine().toString();
                    if ((nectLineMessage.equals(log)) || (log.startsWith(nectLineMessage))) {
                        dublicate = true;
                    }
                }
                if (dublicate == false) {
                    bufferFileWriter.append(log);
                    bufferFileWriter.newLine();
                    bufferFileWriter.close();
                }
            }
            scan.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
