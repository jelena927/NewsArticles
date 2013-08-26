package main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import parser.WebCrawler;
import persistance.RDFModel;

import domain.NewsArticle;

public class Main {

	public static void main(String[] args) {
		try {
			List<NewsArticle> list = WebCrawler.craw();
			NewsArticle article = list.get(0);
			for (NewsArticle newsArticle : list) {
				RDFModel.getInstance().save(newsArticle);
			}
			RDFModel.getInstance().printOut();
			NewsArticle newsArticleCopy = (NewsArticle) RDFModel.getInstance().
					load(article.getUri().toString());
			System.out.println(newsArticleCopy);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
