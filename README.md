# selenium-to-restassured

Easily share Selenium WebDriver cookies with Rest Assured back and forth with this Java api.

[![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://travis-ci.org/mwinteringham/selenium-to-restassured.svg?branch=master)](https://travis-ci.org/mwinteringham/selenium-to-restassured)
[![GitHub release](https://img.shields.io/github/release/mwinteringham/selenium-to-restassured/all.svg?colorB=brightgreen)](https://github.com/mwinteringham/selenium-to-restassured)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.co.mwtestconsultancy/selenium-to-restassured/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.mwtestconsultancy/selenium-to-restassured)
[![Javadocs](http://www.javadoc.io/badge/uk.co.mwtestconsultancy/selenium-to-restassured.svg)](http://www.javadoc.io/doc/uk.co.mwtestconsultancy/selenium-to-restassured)

## Installation

[Download the latest jar](https://github.com/mwinteringham/selenium-to-restassured/releases) and add to your project. You can then add it to your maven project by adding the following dependency:

```java
<dependency>
    <groupId>uk.co.mwtestconsultancy</groupId>
    <artifactId>selenium-to-restassured</artifactId>
    <version>0.1</version>
    <scope>system</scope>
    <systemPath>${project.basedir}/path/to/jar</systemPath>
</dependency>
```

## Usage

You have two options you can either convert from RestAssured to Selenium:

```java
io.restassured.http.Cookie cookieToConvert = response.getDetailedCookie("COOKIE NAME")

CookieAdapter cookieAdapter = new CookieAdapter();
org.openqa.selenium.Cookie convertedCookie = cookieAdapter.convertToSelenium(cookieToConvert);
```

Or you can convert from Selenium to RestAssured

```java
org.openqa.selenium.Cookie cookieToConvert = driver.manage().getCookieNamed("COOKIE NAME");

CookieAdapter cookieAdapter = new CookieAdapter();
io.restassured.http.Cookie adaptedCookie = cookieAdapter.convertToRestAssured(seleniumCookie);
```

Additionally ```CookieAdapter``` will take an enum to convert Selenium expiry dates into either:

**Expiry Dates**
```java
CookieAdapter cookieAdapter = new CookieAdapter(ExpiryType.EXPIRY);
```

**Max Age**
```java
CookieAdapter cookieAdapter = new CookieAdapter(ExpiryType.MAXAGE);
```