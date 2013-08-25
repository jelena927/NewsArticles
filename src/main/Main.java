package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

import parser.NewsArticleParser;
import parser.WebCrawler;
import persistance.RDFModel;

import domain.NewsArticle;

public class Main {

	public static void main(String[] args) {
		try {
			//NewsArticle article = NewsArticleParser.parse("http://travel.nytimes.com/2013/08/11/travel/36-hours-in-lecce-italy.html");
			//System.err.println(article);
			//RDFModel.getInstance().save(article);
			//RDFModel.getInstance().printOut();
			System.out.println(WebCrawler.craw());
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
