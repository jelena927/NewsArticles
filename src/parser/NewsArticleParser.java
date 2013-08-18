package parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.URIGenerator;

import domain.AboutThing;
import domain.ImageObject;
import domain.NewsArticle;
import domain.Organisation;
import domain.Person;
import domain.Thing;

public class NewsArticleParser {

	public static void main(String[] args) {
		try {
			NewsArticle article = parse("http://travel.nytimes.com/2013/08/11/travel/36-hours-in-lecce-italy.html");
			System.out.println(article);
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
	
	static public NewsArticle parse(String url) throws IOException, URISyntaxException, ParseException{

		NewsArticle article = new NewsArticle();
		
		Document doc = Jsoup.connect(url).get();
		
		Elements htmlElement = doc.select("html");				
		URI uri = new URI(htmlElement.get(0).attributes().get("itemid"));
		article.setUri(uri);
		
		Elements h1Elements = doc.select("nyt_headline");
		String headline = h1Elements.get(0).ownText();
		article.setHeadline(headline);
		
		Elements metaElements = doc.select("head meta");
		for (Iterator iterator = metaElements.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			if (element.hasAttr("itemprop")) {
				String itemprop = element.attributes().get("itemprop");
				String content = element.attributes().get("content");
				switch (itemprop) {
				case "inLanguage":
					article.setInLanguage(content);
					break;
				case "description":
					article.setDescription(content);
					break;
				case "dateModified":
					Date dateModified = new SimpleDateFormat("yyyy-MM-dd").parse(content);
					article.setDateModified(dateModified);
					break;
				case "datePublished":
					Date datePublished = new SimpleDateFormat("yyyy-MM-dd").parse(content);
					article.setDatePublished(datePublished);
					break;
				case "alternativeHeadline":
					article.setAlternativeHeadline(content);
					break;
				case "genre":
					article.setGenre(content);
					break;
				case "articleSection":
					article.setArticleSection(content);
					break;
				case "identifier":
					article.setIdentifier(content);
					break;
				case "thumbnailUrl":
					article.setThumbnailUrl(new URL(content));
					break;
				default:
					break;
				}
			}
		}
		
		Elements spanElements = doc.select("span");
		for (Iterator iterator = spanElements.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			if (element.hasAttr("itemprop")) {
				String itemprop = element.attributes().get("itemprop");
				String content = element.attributes().get("content");
				switch (itemprop) {
				case "associatedMedia":
					ImageObject media = new ImageObject();
					Elements children = element.getElementsByAttribute("itemprop");
					media.setUri(new URI(children.get(2).attributes().get("content")));
					media.setHeight(Double.parseDouble(children.get(3).attributes().get("content")));
					media.setWidth(Double.parseDouble(children.get(4).attributes().get("content")));
					media.setCopyrightHolder(children.get(5).text());
					media.setDescription(children.get(6).text());
					article.setAssociatedMedia(media);
					break;
				case "author creator":
					Person author = new Person();
					author.setName(element.text());
					author.setUri(URIGenerator.generate(author));
					article.setAuthor(author);
					break;
				case "copyrightHolder provider sourceOrganization":
					Organisation provider = new Organisation();
					Elements children2 = element.getElementsByAttribute("itemprop");
					provider.setName(children2.get(1).attr("content"));
					provider.setUri(new URI(children2.get(2).attr("content")));
					provider.setTickerSymbol(children2.get(3).attr("content"));
					article.setProvider(provider);
					break;
				case "about":
					AboutThing about = new AboutThing();
					about.setName(element.text());
					about.setUri(URIGenerator.generate(about));
					article.addAbout(about);
					break;
				default:
					break;
				}
			}
		}
		
		return article;
	}
}
