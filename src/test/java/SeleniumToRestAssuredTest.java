import org.approvaltests.Approvals;
import org.junit.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import uk.co.mwtestconsultancy.CookieAdapter;
import uk.co.mwtestconsultancy.ExpiryType;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.toIntExact;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SeleniumToRestAssuredTest {

    @Test
    public void convertBasicCookieTest(){
        Cookie seleniumCookie = new Cookie.Builder("Cookie name", "Cookie value").build();

        CookieAdapter cookieAdapter = new CookieAdapter();
        com.jayway.restassured.response.Cookie adaptedCookie = cookieAdapter.convertToRestAssured(seleniumCookie);

        Approvals.verify(adaptedCookie.toString());
    }

    @Test
    public void convertComplexCookieTest(){
        Cookie seleniumCookie = new Cookie.Builder("Cookie name", "Cookie value")
                                            .domain("www.mwtestconsultancy.co.uk")
                                            .isHttpOnly(true)
                                            .isSecure(true)
                                            .path("/test")
                                            .build();

        CookieAdapter cookieAdapter = new CookieAdapter();
        com.jayway.restassured.response.Cookie adaptedCookie = cookieAdapter.convertToRestAssured(seleniumCookie);

        Approvals.verify(adaptedCookie.toString());
    }

    @Test
    public void convertCookieToExpiryDateTest(){
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2018, 01, 01);
        currentDate.set(Calendar.HOUR, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);

        Cookie seleniumCookie = new Cookie.Builder("Cookie name", "Cookie value")
                                            .expiresOn(currentDate.getTime())
                                            .build();

        CookieAdapter cookieAdapter = new CookieAdapter(ExpiryType.EXPIRY);
        com.jayway.restassured.response.Cookie adaptedCookie = cookieAdapter.convertToRestAssured(seleniumCookie);

        Approvals.verify(adaptedCookie);
    }

    @Test
    public void convertCookieToMaxAgeTest(){
        Calendar futureDate = Calendar.getInstance();
        futureDate.set(2030, 01, 01);
        futureDate.set(Calendar.HOUR, 0);
        futureDate.set(Calendar.MINUTE, 0);
        futureDate.set(Calendar.SECOND, 0);
        futureDate.set(Calendar.HOUR_OF_DAY, 0);

        Calendar currentDate = Calendar.getInstance();
        long end = TimeUnit.MILLISECONDS.toSeconds(futureDate.getTimeInMillis());
        long start = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTimeInMillis());
        int result = toIntExact(end - start);

        Cookie seleniumCookie = new Cookie.Builder("Cookie name", "Cookie value")
                .expiresOn(futureDate.getTime())
                .build();

        CookieAdapter cookieAdapter = new CookieAdapter(ExpiryType.MAXAGE);
        com.jayway.restassured.response.Cookie adaptedCookie = cookieAdapter.convertToRestAssured(seleniumCookie);

        assertThat(adaptedCookie.getMaxAge(), is(result));
    }

    @Test
    public void convertLiveCookieTest(){
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("http://the-internet.herokuapp.com/");
        Cookie cookie = driver.manage().getCookieNamed("rack.session");

        CookieAdapter cookieAdapter = new CookieAdapter();
        com.jayway.restassured.response.Cookie adaptedCookie = cookieAdapter.convertToRestAssured(cookie);

        driver.quit();
        Approvals.verify(adaptedCookie.getClass());
    }

}
