package js.tiny.store.tool;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;

import js.tiny.store.meta.Repository;
import js.tiny.store.meta.RepositoryService;
import js.tiny.store.meta.TypeDef;

public class VelocityUsage {
	private VelocityEngine engine;
	private VelocityContext context;

	@Before
	public void beforeTest() {
		engine = new VelocityEngine();
		engine.init();
		context = new VelocityContext();
	}

	@Test
	public void createServiceRemote() throws IOException {
		Repository repository = repository();
		context.put("repository", repository);
		for (RepositoryService service : repository.getServices()) {
			context.put("service", service);

			File file = new File("src/main/resources/service-remote.vtl");
			Writer writer = new StringWriter();
			Template template = engine.getTemplate(file.getPath());
			template.merge(context, writer);
			System.out.println(writer);
		}
	}

	private static Repository repository() throws IOException {
		Repository repository = new Repository(null, null);
		// repository.setName("OMS Expert");

		RepositoryService service = new RepositoryService();

		RepositoryService[] services = new RepositoryService[] { service };
		// repository.setServices(services);

		service.setType(new TypeDef("ro.gnotis.omsx.CallData"));
		service.setDescription("Data related to customer call history.");

		return repository;
	}
}
