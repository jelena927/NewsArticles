package parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import domain.NewsArticle;

public class WebCrawler {

	public static List<NewsArticle> craw() throws IOException,
			URISyntaxException, ParseException {
		List<NewsArticle> list = new ArrayList<>();
		String url = "http://query.nytimes.com/svc/cse/v2/sitesearch.json?query=&page=4";
		//String url = "http://api.nytimes.com/svc/search/v1/article?format=json&query=j&api-key=64d22b959a89a9bc7dde19a1cb00612a:13:67388534";
		HttpClient httpClient = new DefaultHttpClient();
		String stringResponse = null;
		HttpResponse response = null;
		int m = 0;
		int n = 0;
		for (int i = 0; i < 10; i++) {

			HttpGet httpGet = new HttpGet(url + i);
			response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					response.getEntity().writeTo(out);
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				stringResponse = out.toString();
				JsonParser parser = new JsonParser();
				JsonArray searchResults = (((JsonObject) parser
						.parse(stringResponse)).get("results")
						.getAsJsonObject()).get("results").getAsJsonArray();

				for (JsonElement jsonElement : searchResults) {
					System.out.println("url -> "
							+ jsonElement.getAsJsonObject().get("url")
									.getAsString());
					String articleUrl = jsonElement.getAsJsonObject()
							.get("url").getAsString();
					try {
						NewsArticle article = NewsArticleParser
								.parse(articleUrl);
						list.add(article);
						n++;
						System.out.println(n + ". OK");
					} catch (Exception e) {
						m++;
						System.err.println(m + ". greska, " + e.getMessage());
					}
				}

			}
		}
		return list;
	}
}
