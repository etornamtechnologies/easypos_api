package com.etxtechstack.api.easypos_application.configs;


import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my")
public class ConfigParams {
    @NotNull
    @Value("${my.appName}")
    private String appName;

    @NotNull
    @Value("${my.appVersion}")
    private  String appVersion;

    @NotNull
    @Value("${my.jwtSecretKey}")
    private String jwtSecretKey;

    @NotNull
    @Value("${my.jwtIssuer}")
    private String jwtIssuer;

    @NotNull
    @Value("${my.jwtTokenValidity}")
    private Integer jwtTokenValidity;

    @NotNull
    @Value("${my.failedLoginCount}")
    private Integer failedLoginCount;

    @NotNull
    @Value("${my.payeeFailedLoginCount}")
    private Integer payeeFailedLoginCount;

    @NotNull
    @Value("${my.appConfigId}")
    private Integer appConfigId;

    @NotNull
    @Value("${my.firebaseCloudMessagingUrl}")
    private String firebaseCloudMessagingUrl;

    @NotNull
    @Value("${my.firebaseCloudMessagingKey}")
    private String firebaseCloudMessagingKey;

    @NotNull
    @Value("${my.platformName}")
    private String platformName;

    @NotNull
    @Value("${my.tokenExpirationInMinutes}")
    private Integer tokenExpirationInMinutes;

    public ConfigParams() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getJwtSecretKey() {
        return jwtSecretKey;
    }

    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    public Integer getJwtTokenValidity() {
        return jwtTokenValidity;
    }

    public void setJwtTokenValidity(Integer jwtTokenValidity) {
        this.jwtTokenValidity = jwtTokenValidity;
    }

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public Integer getPayeeFailedLoginCount() {
        return payeeFailedLoginCount;
    }

    public void setPayeeFailedLoginCount(Integer payeeFailedLoginCount) {
        this.payeeFailedLoginCount = payeeFailedLoginCount;
    }

    public String getFirebaseCloudMessagingUrl() {
        return firebaseCloudMessagingUrl;
    }

    public void setFirebaseCloudMessagingUrl(String firebaseCloudMessagingUrl) {
        this.firebaseCloudMessagingUrl = firebaseCloudMessagingUrl;
    }

    public String getFirebaseCloudMessagingKey() {
        return firebaseCloudMessagingKey;
    }

    public void setFirebaseCloudMessagingKey(String firebaseCloudMessagingKey) {
        this.firebaseCloudMessagingKey = firebaseCloudMessagingKey;
    }

    public Integer getAppConfigId() {
        return appConfigId;
    }

    public void setAppConfigId(Integer appConfigId) {
        this.appConfigId = appConfigId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public Integer getTokenExpirationInMinutes() {
        return tokenExpirationInMinutes;
    }

    public void setTokenExpirationInMinutes(Integer tokenExpirationInMinutes) {
        this.tokenExpirationInMinutes = tokenExpirationInMinutes;
    }
}

