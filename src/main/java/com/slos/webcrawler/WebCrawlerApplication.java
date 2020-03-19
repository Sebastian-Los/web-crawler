package com.slos.webcrawler;

import com.slos.webcrawler.site.processor.SiteProcessor;
import com.slos.webcrawler.site.processor.SiteProcessorApi;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

import static java.lang.System.exit;

@AllArgsConstructor
public class WebCrawlerApplication {

    private static final String URL = "http://wiprodigital.com";
    private static final String INITIAL_INDENT = "";

    public static void main(String[] args) {

        List<String> processedURLs = new ArrayList<>();
        SiteProcessorApi siteProcessor = new SiteProcessor();
        siteProcessor.processSite(URL, processedURLs, INITIAL_INDENT);

        exit(0);
    }

}
