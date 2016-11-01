# Readme

## Requirements

*   JDK 8uxx - [download latest JDK](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
*   Application server for Java web applications - [download Apache Tomcat 8.5.6](http://tomcat.apache.org/download-80.cgi#8.5.6)

## How to run the application

From within

<pre>url-shortener-web</pre>

module run the following maven command `spring-boot:run` which will run the application using the embedded tomcat server, or build a war archive and deploy it onto the application server. The latter is explained in the next section.

## How to build a deployable war archive

To build a deployable war archive run the following maven command `clean install` from within

<pre>url-shortener-web</pre>

module. The built war file is located inside the target directory of the module.

## How to deploy the war file onto an Apache Tomcat application server

Place the war file inside webapps directory of installed Apache Tomcat and then start the application server using either startup.sh for *nix or startup.bat for windows operating systems

## How to use the service

The service exposes the following actions

| URI | HTTP Method | Requires authentication* | Description | Request type | Sample request | Response type | Sample response |
| --- | --- | --- | --- | --- | --- | --- | --- |
| /account | POST | NO | Creates a new service account for given accountId | application/json | `{ "accountId":"mlovrekov" }` | application/json | `{ "success":true, "description":"Your account is opened", "password":"0fGaW3kC" }` |
| /register | POST | YES | Generates and registers a short url for a given long url | application/json | `{ "url":"http://stackoverflow.com/", "redirectType":301 }` | application/json | `{ "shortUrl":"http://host.com/Wsy3gH5a" }` |
| /statistic/{accountId} | GET | YES | Returns hit statistics for each url registered by provided account | application/json | application/json | `{ "http://stackoverflow.com/":68, "http://reddit.com/r/java":12 }` |
| /{token} | GET | NO | Redirects user to long url registered to given token |

*Authentication is done using HTTP basic. Request must provide `Authorization Basic [base64]username:password[/base64]` header
