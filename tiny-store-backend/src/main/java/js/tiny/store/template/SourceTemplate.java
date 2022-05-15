package js.tiny.store.template;

import java.io.Writer;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.StoreEntity;

public class SourceTemplate {
	private final String templateName;
	private final VelocityEngine engine;
	private final VelocityContext context;

	public SourceTemplate(String templateName) {
		this.templateName = templateName;
		this.engine = new VelocityEngine();
		this.engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		this.engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		this.engine.init();
		this.context = new VelocityContext();

		// to load template from file do no set above engine properties and use next lines
		// in this case template name is the actual file path
		// File file = new File("src/main/resources/service-remote.java.vtl");
		// Template template = engine.getTemplate(file.getPath());
	}
	
	public void generate(StoreEntity entity, Writer writer) {
		StoreEntityTemplate entityTemplate = new StoreEntityTemplate(entity);
		context.put("entity", entityTemplate);
		
		Template template = engine.getTemplate(templateName);
		template.merge(context, writer);
	}
	
	public void generate(String repositoryName, DataService service, List<ServiceOperation> operations, Writer writer) {
		DataServiceTemplate serviceTemplate = new DataServiceTemplate(repositoryName, service, operations);
		context.put("service", serviceTemplate);
		
		Template template = engine.getTemplate(templateName);
		template.merge(context, writer);
	}
	
	public void generate(String contextName, Object contextValue, Writer writer) {
		context.put(contextName, contextValue);

		Template template = engine.getTemplate(templateName);
		template.merge(context, writer);
	}
}
