package com.simplecontainers.containers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;

public class WireMockContainer extends AbstractSimpleContainer {

    private static final int WIREMOCK_PORT = 8080;
    private static final String DEFAULT_IMAGE = "rodolpheche/wiremock:2.23.2";
    private final String wireMockImage;
    private final String mappingPath;
    private final String filesPath;

    public WireMockContainer() {
        this(DEFAULT_IMAGE, "/mappings", "/files");
    }

    public WireMockContainer(String mappingPath, String filesPath) {
        this(DEFAULT_IMAGE, mappingPath, filesPath);
    }

    public WireMockContainer(String wireMockImage, String mappingPath, String filesPath) {
        this.wireMockImage = wireMockImage;
        this.mappingPath = mappingPath;
        this.filesPath = filesPath;
    }

    @Override
    public GenericContainer getUnderlyingContainer() {
        return new GenericContainer(wireMockImage)
                .withExposedPorts(WIREMOCK_PORT)
                .withClasspathResourceMapping(mappingPath, "/home/wiremock/mappings" , BindMode.READ_ONLY)
                .withClasspathResourceMapping(filesPath, "/home/wiremock/__files" , BindMode.READ_ONLY);
    }

    @Override
    public Integer getExposedPort() {
        return WIREMOCK_PORT;
    }
}
