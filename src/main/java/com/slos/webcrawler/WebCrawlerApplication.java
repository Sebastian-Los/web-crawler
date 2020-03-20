package com.slos.webcrawler;

import static java.lang.System.exit;

import com.slos.webcrawler.jsoup.connector.JsoupConnector;
import com.slos.webcrawler.jsoup.connector.JsoupConnectorApi;
import com.slos.webcrawler.site.processor.SiteProcessor;
import com.slos.webcrawler.site.processor.SiteProcessorApi;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class WebCrawlerApplication {

    private static final String URL = "http://wiprodigital.com";
    private static final String INITIAL_INDENT = "";

    public static void main(String[] args) {

        List<String> processedURLs = new ArrayList<>();
        JsoupConnectorApi jsoupConnectorApi = new JsoupConnector();
        SiteProcessorApi siteProcessor = new SiteProcessor(jsoupConnectorApi);
        siteProcessor.processSite(URL, processedURLs, INITIAL_INDENT);

        exit(0);
    }

}
