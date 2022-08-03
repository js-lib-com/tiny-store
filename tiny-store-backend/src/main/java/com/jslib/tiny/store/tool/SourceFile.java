package com.jslib.tiny.store.tool;

import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class SourceFile {
	private final String templateName;
	private final VelocityEngine engine;
	private final VelocityContext context;

	public SourceFile(String templateName) {
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
	
	public void generate(String contextName, Object contextValue, Writer writer) {
		context.put(contextName, contextValue);
		Template template = engine.getTemplate(templateName);
		template.merge(context, writer);
	}
}
