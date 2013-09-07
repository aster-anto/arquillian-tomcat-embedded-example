/**
 * 
 */
package com.example;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author aster
 * 
 */
@RunWith(Arquillian.class)
public class BaseTest {

	private static final String BASE_TESTING_URL = "http://localhost:8080/arquillian-tomcat-embedded-example";
	private static final String WEBAPP_SRC = "src/main/webapp";
	private static final String RESOURCES = "src/main/resources";

	@Test
	@RunAsClient
	public void testHelloServlet() throws Exception {
		Assert.assertEquals(200, getStatusCode(BASE_TESTING_URL + "/Hello"));
	}

	private int getStatusCode(String url) throws HttpException, IOException {
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		int code = client.executeMethod(method);
		System.out.println(new String(method.getResponseBody()));
		return code;
	}

	@Deployment(testable = true)
	@OverProtocol("Servlet 2.5")
	public static WebArchive createDeployment() {
		PomEquippedResolveStage mavenResolver = Maven.resolver().loadPomFromFile("pom.xml");
		File[] libs = mavenResolver.importRuntimeAndTestDependencies().resolve().withTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "arquillian-tomcat-embedded-example.war");
		war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC).as(GenericArchive.class), "/", Filters.includeAll());

		for (File file : libs) {
			war.addAsLibrary(file);
		}
		for (File file : new File(RESOURCES).listFiles()) {
			war.addAsResource(file, file.getName());
		}
		war.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"));
		System.out.println(war.toString(true));
		return war;
	}

}
