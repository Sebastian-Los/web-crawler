package com.slos.webcrawler.site.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.slos.webcrawler.jsoup.connector.JsoupConnectorApi;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;


public class SiteProcessorTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Mock
	private JsoupConnectorApi jsoupConnectorApi;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@Test
	@SneakyThrows
	public void processSiteImageTest() {
		String html = "<img class=\"\" src=\"https://s17776.pcdn.co/wp-content/themes/wiprodigital/images/logo-dk-2X.png\" alt=\"wipro digital\" title=\"Wipro Digital\" style=\"width: 153px; height: 29px;\">";
		String expected = "     * img: <https://s17776.pcdn.co/wp-content/themes/wiprodigital/images/logo-dk-2X.png> x (wipro digital)\n";
		processSiteTestTemplate(html, expected);
	}

	@Test
	@SneakyThrows
	public void processSiteLinkTest() {
		String html = "<a class=\"cta-link\" href=\"https://wiprodigital.com/who-we-are/\">Who we are</a>";
		String expected = "     * a: <https://wiprodigital.com/who-we-are/>  (Who we are)\n";
		processSiteTestTemplate(html, expected);
	}

	@Test
	@SneakyThrows
	public void processSiteImportTest() {
		String html = "<script type=\"text/javascript\" src=\"https://s17776.pcdn.co/wp-content/themes/wiprodigital/scripts/bootstrap.min.js\"></script>";
		String expected = "     * script: <https://s17776.pcdn.co/wp-content/themes/wiprodigital/scripts/bootstrap.min.js>\n";
		processSiteTestTemplate(html, expected);
	}

	@Test
	@SneakyThrows
	public void processSiteExceptionTest() {
		String url = "https://example.com";
		when(jsoupConnectorApi.connect(url)).thenThrow(new IOException("Wrong website"));

		SiteProcessorApi siteProcessorApi = new SiteProcessor(jsoupConnectorApi);
		siteProcessorApi.processSite(url, new ArrayList<>(), "");
	}

	@Test
	@SneakyThrows
	public void processSiteHttpTest() {
		when(jsoupConnectorApi.connect("https://example.com")).thenThrow(new IOException("Wrong website"));

		SiteProcessorApi siteProcessorApi = new SiteProcessor(jsoupConnectorApi);
		siteProcessorApi.processSite("http://example.com", new ArrayList<>(), "");
		assertThat(errContent.toString()).isEqualTo("For 'https://example.com': Wrong website\n");
	}

	@Test
	public void assureHttpsPrefix() {
		String result = SiteProcessor.assureHttpsPrefix("http://test.com");
		assertThat(result).isEqualTo("https://test.com");
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	private void processSiteTestTemplate(String html, String expected) throws IOException {
		Document doc = Jsoup.parse(html);

		String url = "https://example.com";

		when(jsoupConnectorApi.connect(url)).thenReturn(doc);

		SiteProcessorApi siteProcessorApi = new SiteProcessor(jsoupConnectorApi);
		siteProcessorApi.processSite(url, new ArrayList<>(), "");

		assertThat(outContent.toString()).isEqualTo(expected);
	}

}
