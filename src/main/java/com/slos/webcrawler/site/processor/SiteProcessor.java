package com.slos.webcrawler.site.processor;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@Slf4j
public class SiteProcessor implements SiteProcessorApi {

    private static final String REGULAR_INDENT = "    ";
    private static final String MEDIA_TAG = "[src]";
    private static final String IMPORTS_TAG = "link[href]";
    private static final String LINKS_TAG = "a[href]";
    private static final String ABS_LINKS_TAG = "abs:href";
    private static final String ABS_SRC_TAG = "abs:src";
    private static final String REL_ATTRIBUTE = "rel";
    private static final String WIDTH_ATTRIBUTE = "width";
    private static final String HEIGHT_ATTRIBUTE = "height";
    private static final String ALT_ATTRIBUTE = "alt";
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";
    private static final String IMG_ATTRIBUTE = "img";

    public String processSite(String url, List<String> processedURLs, String currentIndent) {
        try {
            url = assureHttpsPrefix(url);
            Document document = Jsoup.connect(url).get();
            Elements media = document.select(MEDIA_TAG);
            Elements imports = document.select(IMPORTS_TAG);
            Elements links = document.select(LINKS_TAG);

            currentIndent += REGULAR_INDENT;
            handleMedia(currentIndent, media);
            handleImports(currentIndent, imports);
            currentIndent = handleLinks(url, processedURLs, currentIndent, links);
            currentIndent = currentIndent.substring(currentIndent.length() - 4);

        } catch (IOException e) {
            System.err.println("For '" + url + "': " + e.getMessage());
        }
        return currentIndent;
    }

    String assureHttpsPrefix(String url) {
        return url.contains(HTTP_PREFIX) ? url.replace(HTTP_PREFIX, HTTPS_PREFIX) : url;
    }

    void handleMedia(String currentIndent, Elements media) {
        for (Element src : media) {
            if (src.normalName().equals(IMG_ATTRIBUTE)) {
                print(currentIndent + " * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr(ABS_SRC_TAG), src.attr(WIDTH_ATTRIBUTE), src.attr(HEIGHT_ATTRIBUTE),
                        trim(src.attr(ALT_ATTRIBUTE), 20));
            } else {
                print(currentIndent + " * %s: <%s>", src.tagName(), src.attr(ABS_SRC_TAG));
            }
        }
    }

    void handleImports(String currentIndent, Elements imports) {
        for (Element link : imports) {
            print(currentIndent + " * %s <%s> (%s)", link.tagName(),link.attr(ABS_LINKS_TAG), link.attr(REL_ATTRIBUTE));
        }
    }

    String handleLinks(String url, List<String> urls, String currentIndent, Elements links) {
        for (Element link : links) {
            String currentUrl = link.attr(ABS_LINKS_TAG);

            print(currentIndent + " * a: <%s>  (%s)", link.attr(ABS_LINKS_TAG), trim(link.text(), 35));
            if (currentUrl.contains(url) && ! urls.contains(currentUrl)) {
                urls.add(currentUrl);
                currentIndent = processSite(link.attr(ABS_LINKS_TAG), urls, currentIndent);
            }
        }
        return currentIndent;
    }

    void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    String trim(String s, int width) {
        return s.length() > width ? s.substring(0, width - 1) + "." : s;
    }
}
