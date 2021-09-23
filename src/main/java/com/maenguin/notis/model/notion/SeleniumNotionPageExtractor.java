package com.maenguin.notis.model.notion;

import com.maenguin.notis.model.notion.NotionPage;
import com.maenguin.notis.model.notion.NotionPageExtractor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SeleniumNotionPageExtractor implements NotionPageExtractor {

    private final String END_POINT = "posted by NoTis.";

    private WebDriver newWebDriver() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--blink-settings=imagesEnabled=false");
        log.info("webDriver created");
        return new ChromeDriver(chromeOptions);
    }

    @Override
    public NotionPage extractPage(String uri) {
        var webDriver = newWebDriver();
        log.info("extract notion page from -> {}", uri);
        String title = "제목 없음";
        String content = "내용 없음";
        var javascriptExecutor = (JavascriptExecutor) webDriver;
        webDriver.get(uri);
        try {
            waitUntilContentInitialized(webDriver);
            var titleElement = webDriver.findElement(By.className("notion-page-block"));
            var contentElement = webDriver.findElement(By.className("notion-page-content"));
            adjustContent(javascriptExecutor, contentElement);
            content = contentElement.getAttribute("outerHTML");
            title = titleElement.getText();
            log.info("notion page title -> {}", title);
            log.info("notion page content -> {}", content);
        } catch (TimeoutException e) {
            log.error("error : timeout", e);
        } catch (NoSuchElementException noSuchElementException) {
            log.error("error : noSuchElementException", noSuchElementException);
        } finally {
            log.info("webDriver quit -> {}", webDriver);
            webDriver.quit();
        }

        return new NotionPage(title, content);
    }

    private void waitUntilContentInitialized(WebDriver webDriver) {
        var webDriverWait = new WebDriverWait(webDriver, 10);
        webDriverWait.until(driver -> driver.findElement(By.xpath(String.format("//*[contains(text(),'%s')]", END_POINT))));
    }

    private void adjustContent(JavascriptExecutor javascriptExecutor, WebElement contentElement) {
        relocateBulletPoint(javascriptExecutor, contentElement);
        relocateImage(javascriptExecutor, contentElement);
        relocateEmoji(javascriptExecutor, contentElement);
    }

    private void relocateBulletPoint(JavascriptExecutor javascriptExecutor, WebElement contentElement) {
        var elements = contentElement.findElements(By.className("pseudoBefore"));
        var pattern = Pattern.compile("(?<=pseudoBefore--content:\")(.+)(?=\")");
        for (WebElement element : elements) {
            var style = element.getAttribute("style");
            var matcher = pattern.matcher(style);
            if (matcher.find()) {
                var bulletPoint = matcher.group();
                javascriptExecutor.executeScript(String.format("arguments[0].innerHTML = '%s';", bulletPoint), element);
//                log.debug("relocate bulletPoint -> {}", bulletPoint);
            }
        }
    }

    private void relocateImage(JavascriptExecutor javascriptExecutor, WebElement contentElement) {
        var imageBlockElements = contentElement.findElements(By.className("notion-image-block"));
        var pattern = Pattern.compile("(?<=image\\/).*");
        for (WebElement blockElement : imageBlockElements) {
            var imageTagElements = blockElement.findElements(By.tagName("img"));
            for (WebElement imageTagElement : imageTagElements) {
                var originImageSrc = imageTagElement.getAttribute("src");
                var decodedImageSrc = URLDecoder.decode(originImageSrc, StandardCharsets.UTF_8);
                var matcher = pattern.matcher(decodedImageSrc);
                if (matcher.find()) {
                    var newImageSrc = matcher.group();
                    javascriptExecutor.executeScript(String.format("arguments[0].setAttribute('src', '%s')", newImageSrc), imageTagElement);
                    log.debug("relocate image src -> {}", newImageSrc);
                }
            }
        }
    }

    private void relocateEmoji(JavascriptExecutor javascriptExecutor, WebElement contentElement) {
        var elements = contentElement.findElements(By.className("notion-emoji"));
        for (WebElement element : elements) {
            var emoji = element.getAttribute("alt");
            javascriptExecutor.executeScript("arguments[0].setAttribute('style', '');", element);
            javascriptExecutor.executeScript("arguments[0].setAttribute('src', '');", element);
            javascriptExecutor.executeScript(String.format("arguments[0].outerHTML = '%s';", emoji), element);
            log.debug("relocate emoji -> {}", emoji);
        }
    }

//    @PostConstruct
//    public void init() {
//        extractPage("https://delirious-sock-4dc.notion.site/Gradle-Wrapper-b876548362b842fd927bd77ea2373f6c");
//    }

}
