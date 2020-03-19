package com.slos.webcrawler.site.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SiteProcessorTest {

    @Test
    void assureHttpsPrefix() {
        SiteProcessor siteProcessor = new SiteProcessor();
        String result = siteProcessor.assureHttpsPrefix("http://test.com");
        assertEquals("https://test.com", result);
    }

}