package com.simplecontainers.containers;


import com.simplecontainers.rules.SimpleContainersSpinnerRule;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import static io.restassured.RestAssured.when;

public class WireMockContainerTest {

    private final static WireMockContainer wiremockContainer = new WireMockContainer("mappings", "files");

    @ClassRule
    public final static SimpleContainersSpinnerRule rule = new SimpleContainersSpinnerRule(wiremockContainer);

    @Test
    public void shouldAbleToReturnPropertiesOfContainer() {
        Assert.assertNotNull(wiremockContainer.getExternalHost());
        Assert.assertNotNull(wiremockContainer.getExternalPort());
        Assert.assertNotNull(wiremockContainer.getInternalHost());
        Assert.assertNotNull(wiremockContainer.getInternalPort());
        Assert.assertNotNull(wiremockContainer.url());
    }

    @Test
    public void shouldReturnHelloWorld() {
        when()
                .get(String.format("http://%s:%d/hello-world", wiremockContainer.getExternalHost(), wiremockContainer.getExternalPort()))
                .then()
                .statusCode(200)
                .assertThat()
                .body("message", CoreMatchers.equalTo("Hello World"));
    }

}