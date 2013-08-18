package domain;

import java.net.URI;

import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.RdfType;
import util.Constants;

@Namespace(Constants.SCHEMA)
@RdfType("Thing")
public class Thing {
	
	@RdfProperty(Constants.SCHEMA + "uri")
	protected URI uri;

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}
	
	@Override
	public String toString() {
		return "URI: " + uri;
	}
}
