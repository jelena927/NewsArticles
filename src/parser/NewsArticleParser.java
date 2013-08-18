package parser;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		
		Element el = doc.getElementsByAttributeValue("itemtype", "http://schema.org/NewsArticle").first();
		URI uri = new URI(el.attributes().get("itemid"));
		article.setUri(uri);
		
		Elements elements = doc.getElementsByAttribute("itemprop");	
		for (Element element : elements) {
			String itemprop = element.attributes().get("itemprop");
			String content = element.attributes().get("content");
			switch (itemprop) {
			case "inLanguage":
				article.setInLanguage(content);
				break;
			case "description":
				if (article.getDescription() != null) break;
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
				if (article.getIdentifier() != null) break;
				article.setIdentifier(content);
				break;
			case "thumbnailUrl":
				article.setThumbnailUrl(new URL(content));
				break;
			case "associatedMedia":
				ImageObject media = new ImageObject();
				Elements children = element.getElementsByAttribute("itemprop");
				for (Element element2 : children) {
					itemprop = element2.attributes().get("itemprop");
					content = element2.attributes().get("content");
					switch (itemprop) {
					case "url":
						media.setUri(new URI(content));
						break;
					case "description":
						media.setDescription(content);
						break;
					case "width":
						media.setWidth(Double.parseDouble(content));
						break;
					case "height":
						media.setHeight(Double.parseDouble(content));
						break;
					case "copyrightHolder":
						media.setCopyrightHolder(element.text());
						break;
					default:
						break;
					}
				}
				article.setAssociatedMedia(media);
				break;
			case "author creator":
				Person author = new Person();
				children = element.getElementsByAttribute("itemprop");
				for (Element element2 : children) {
					itemprop = element2.attributes().get("itemprop");
					content = element2.attributes().get("content");
					switch (itemprop) {
					case "name":
						author.setName(element2.text());
						break;
					default:
						break;
					}
				}
				author.setUri(URIGenerator.generate(author));
				article.setAuthor(author);
				break;
			case "copyrightHolder provider sourceOrganization":
				Organisation provider = new Organisation();
				children = element.getElementsByAttribute("itemprop");
				for (Element element2 : children) {
					itemprop = element2.attributes().get("itemprop");
					content = element2.attributes().get("content");
					switch (itemprop) {
					case "name":
						provider.setName(content);
						break;
					case "url":
						provider.setUri(new URI(content));
						break;
					case "tickerSymbol":
						provider.setTickerSymbol(content);
						break;
					default:
						break;
					}
				}
				article.setProvider(provider);
				break;
			case "about":
				AboutThing about = new AboutThing();
				about.setName(element.text());
				about.setUri(URIGenerator.generate(about));
				article.addAbout(about);
				break;
			case "headline":
				article.setHeadline(element.text());
				break;
			default:
				break;
			}
		}
		return article;
	}
}
