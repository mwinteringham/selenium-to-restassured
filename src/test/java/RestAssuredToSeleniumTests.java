import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.approvaltests.Approvals;
import org.junit.Test;
import uk.co.mwtestconsultancy.CookieAdapter;

import java.util.Calendar;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestAssuredToSeleniumTests {

    @Test
    public void convertBasicCookieTest(){
        Cookie restAssuredCookie = new Cookie.Builder("Cookie name", "Cookie value").build();

        CookieAdapter cookieAdapter = new CookieAdapter();
        org.openqa.selenium.Cookie adaptedCookie = cookieAdapter.convertToSelenium(restAssuredCookie);

        Approvals.verify(adaptedCookie.toString());
    }

    @Test
    public void convertComplexCookieTest(){
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(2018, 01, 01);
        currentDate.set(Calendar.HOUR, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);

        Cookie restAssuredCookie = new Cookie.Builder("Cookie name", "Cookie value")
                                        .setComment("Cookie comment")
                                        .setDomain("www.mwtestconsultancy.co.uk")
                                        .setExpiryDate(currentDate.getTime())
                                        .setHttpOnly(true)
                                        .setPath("/test")
                                        .setSecured(false)
                                        .setVersion(1)
                                        .build();

        CookieAdapter cookieAdapter = new CookieAdapter();
        org.openqa.selenium.Cookie adaptedCookie = cookieAdapter.convertToSelenium(restAssuredCookie);

        Approvals.verify(adaptedCookie);
    }

    @Test
    public void convertMaxAgeCookieTest(){
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        currentDate.add(Calendar.SECOND, 86400);


        Cookie restAssuredCookie = new Cookie.Builder("Cookie name", "Cookie value")
                                            .setMaxAge(86400)
                                            .build();

        CookieAdapter cookieAdapter = new CookieAdapter();
        org.openqa.selenium.Cookie adaptedCookie = cookieAdapter.convertToSelenium(restAssuredCookie);

        assertThat(adaptedCookie.getExpiry().toString(), is(currentDate.getTime().toString()));
    }

    @Test
    public void convertLiveCookieTest(){
        Response response = given()
                                .get("http://the-internet.herokuapp.com/");

        CookieAdapter cookieAdapter = new CookieAdapter();
        org.openqa.selenium.Cookie convertedCookie = cookieAdapter.convertToSelenium(response.getDetailedCookie("rack.session"));

        Approvals.verify(convertedCookie.getClass());
    }

}
