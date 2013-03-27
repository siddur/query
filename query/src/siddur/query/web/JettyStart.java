package siddur.query.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStart {
	public static void main(String[] args) throws Exception {
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "true");
		
		WebAppContext app = new WebAppContext();
		String webroot = "web";
		app.setResourceBase(webroot);
		app.setDescriptor(webroot + "/WEB-INF/web.xml");
		Server server = new Server(80);
		server.setHandler(app);
		server.start();
		server.join();
	}
}
