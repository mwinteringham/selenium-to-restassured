package uk.co.mwtestconsultancy;

import com.jayway.restassured.response.Cookie;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.toIntExact;

public class CookieAdapter {

    private ExpiryType expiryType;

    public CookieAdapter() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    public CookieAdapter(ExpiryType expiryType){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        this.expiryType = expiryType;
    }

    public org.openqa.selenium.Cookie convertToSelenium(Cookie cookie) {
        org.openqa.selenium.Cookie.Builder newCookie = new org.openqa.selenium.Cookie.Builder(cookie.getName(), cookie.getValue());

        if(cookie.getComment() != null){
            System.out.println("NOTE: The cookie taken from RestAssured has a comment attribute of '" + cookie.getComment() + "'. Selenium doesn't support the addition of comments so this has been discarded");
        }

        if(cookie.getDomain() != null){
            newCookie.domain(cookie.getDomain());
        }

        if(cookie.getExpiryDate() != null){
            newCookie.expiresOn(cookie.getExpiryDate());
        }

        if(cookie.isHttpOnly()){
            newCookie.isHttpOnly(cookie.isHttpOnly());
        }

        if(cookie.getMaxAge() != -1){
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTimeZone(TimeZone.getTimeZone("GMT"));
            currentDate.add(Calendar.SECOND, cookie.getMaxAge());

            newCookie.expiresOn(currentDate.getTime());
        }

        if(cookie.getPath() != null){
            newCookie.path(cookie.getPath());
        }

        if(cookie.isSecured()){
            newCookie.isSecure(cookie.isSecured());
        }

        if(cookie.getVersion() != -1){
            System.out.println("NOTE: The cookie taken from RestAssured has a version attribute of '" + cookie.getVersion() + "'. Selenium doesn't support the addition of version so this has been discarded");
        }

        return newCookie.build();
    }

    public Cookie convertToRestAssured(org.openqa.selenium.Cookie cookie) {
        Cookie.Builder newCookie = new Cookie.Builder(cookie.getName(), cookie.getValue());

        if(cookie.getDomain() != null){
            newCookie.setDomain(cookie.getDomain());
        }

        if(cookie.getExpiry() != null){
            if(expiryType.equals(ExpiryType.EXPIRY)){
                newCookie.setExpiryDate(cookie.getExpiry());
            } else if(expiryType.equals(ExpiryType.MAXAGE)){
                Calendar futureDate = Calendar.getInstance();
                futureDate.setTime(cookie.getExpiry());
                Calendar currentDate = Calendar.getInstance();

                long end = TimeUnit.MILLISECONDS.toSeconds(futureDate.getTimeInMillis());
                long start = TimeUnit.MILLISECONDS.toSeconds(currentDate.getTimeInMillis());
                int result = toIntExact(end - start);

                newCookie.setMaxAge(result);
            }
        }

        if(cookie.isHttpOnly()){
            newCookie.setHttpOnly(cookie.isHttpOnly());
        }

        if(cookie.isSecure()){
            newCookie.setSecured(cookie.isSecure());
        }

        if(cookie.getPath() != null){
            newCookie.setPath(cookie.getPath());
        }

        return newCookie.build();
    }
}
