package persistance;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import thewebsemantic.Bean2RDF;
import thewebsemantic.RDF2Bean;
import util.Constants;

public class RDFModel {

	Model graph;
	Bean2RDF writer;
	RDF2Bean reader;
	
	private static RDFModel INSTANCE;
	
	private RDFModel (){
		graph = ModelFactory.createDefaultModel();
		//graph.setNsPrefix("NewsArticle", Constants.SCHEMA);
		graph.setNsPrefix("schema", Constants.SCHEMA);
		graph.setNsPrefix("xsd", Constants.XSD);
		
		
		writer = new Bean2RDF(graph);
		reader = new RDF2Bean(graph);
	}
	
	public static RDFModel getInstance(){
		if (INSTANCE == null) {
			INSTANCE = new RDFModel();
		}
		
		return INSTANCE;
	}
	
	public void save(Object o) {
		writer.save(o);
	}
	
	
	public Object load(String uri) {
		return reader.load(uri);
	}
	
	public void printOut(){
		graph.write(System.out, "RDF/XML");
	}
}
