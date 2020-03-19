package com.slos.webcrawler.site.processor;

import java.util.List;

public interface SiteProcessorApi {
    String processSite(String url, List<String> processedURLs, String currentIndent);
}
