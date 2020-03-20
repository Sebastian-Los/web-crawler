package com.slos.webcrawler.jsoup.connector;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupConnector implements JsoupConnectorApi {

    @Override
    public Document connect(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
