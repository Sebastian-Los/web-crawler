package com.slos.webcrawler.jsoup.connector;

import java.io.IOException;
import org.jsoup.nodes.Document;

public interface JsoupConnectorApi {
    Document connect(String url) throws IOException;
}
