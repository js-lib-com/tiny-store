package com.jslib.tiny.store.template;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;

import com.jslib.lang.GType;
import com.jslib.util.Params;

public class SourceTemplate {
	private final String templateName;
	private final VelocityEngine engine;
	private final VelocityContext context;
	private final ContextConfig config;

	public SourceTemplate(String templateName) {
		this.templateName = templateName;
		this.engine = new VelocityEngine();
		this.engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		this.engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		this.engine.init();
		this.context = new VelocityContext();
		this.config = CONFIGS.get(templateName);

		// to load template from file do no set above engine properties and use next lines
		// in this case template name is the actual file path
		// File file = new File("src/main/resources/service-remote.java.vtl");
		// Template template = engine.getTemplate(file.getPath());
	}

	public void generate(Writer writer, Object... values) {
		config.apply(context, values);
		Template template = engine.getTemplate(templateName);
		template.merge(context, writer);
	}

	@FunctionalInterface
	private interface ContextConfig {
		void apply(VelocityContext context, Object... arguments);
	}

	private static void empty(VelocityContext context, Object... arguments) {
	}

	private static void store(VelocityContext context, Object... arguments) {
		Params.EQ(arguments.length, 1, "Arguments length");
		Params.isKindOf(arguments[0].getClass(), Store.class, "Argument");

		final Store store = (Store) arguments[0];
		context.put("store", new StoreTemplate(store));
	}

	private static void entity(VelocityContext context, Object... arguments) {
		Params.EQ(arguments.length, 1, "Arguments length");
		Params.isKindOf(arguments[0].getClass(), StoreEntity.class, "Argument");

		final StoreEntity entity = (StoreEntity) arguments[0];
		context.put("entity", new StoreEntityTemplate(entity));
	}

	private static void service(VelocityContext context, Object... arguments) {
		Params.EQ(arguments.length, 3, "Arguments length");
		Params.isKindOf(arguments[0].getClass(), Store.class, "First argument");
		Params.isKindOf(arguments[1].getClass(), DataService.class, "Second argument");
		Params.isKindOf(arguments[2].getClass(), new GType(List.class, ServiceOperation.class), "Third argument");

		final Store store = (Store) arguments[0];
		final DataService service = (DataService) arguments[1];
		@SuppressWarnings("unchecked")
		final List<ServiceOperation> operations = (List<ServiceOperation>) arguments[2];
		context.put("service", new DataServiceTemplate(store, service, operations));
	}

	private static void app(VelocityContext context, Object... arguments) {
		Params.EQ(arguments.length, 2, "Arguments length");
		Params.isKindOf(arguments[0].getClass(), Store.class, "First argument");
		Params.isKindOf(arguments[1].getClass(), new GType(List.class, DataService.class), "Second argument");

		final Store store = (Store) arguments[0];
		@SuppressWarnings("unchecked")
		final List<DataService> services = (List<DataService>) arguments[1];

		context.put("store", store);
		context.put("services", services.stream().map(service -> new DataServiceTemplate(store, service)).collect(Collectors.toList()));
	}

	private static void persistence(VelocityContext context, Object... arguments) {
		Params.EQ(arguments.length, 2, "Arguments length");
		Params.isKindOf(arguments[0].getClass(), Store.class, "First argument");
		Params.isKindOf(arguments[1].getClass(), new GType(List.class, StoreEntity.class), "Second argument");

		final Store store = (Store) arguments[0];
		@SuppressWarnings("unchecked")
		final List<StoreEntity> entities = (List<StoreEntity>) arguments[1];
		context.put("store", store);
		context.put("entities", entities);
	}

	@SuppressWarnings("unchecked")
	private static void manual(VelocityContext context, Object... arguments) {
		Params.EQ(arguments.length, 4, "Arguments length");
		Params.isKindOf(arguments[0].getClass(), Store.class, "First argument");
		Params.isKindOf(arguments[1].getClass(), new GType(List.class, ServiceOperation.class), "Second argument");
		Params.isKindOf(arguments[2].getClass(), new GType(Map.class, String.class, new GType(List.class, ServiceOperation.class)), "Third argument");
		Params.isKindOf(arguments[3].getClass(), new GType(List.class, StoreEntity.class), "Fourth argument");

		final Store store = (Store) arguments[0];
		final List<DataService> services = (List<DataService>) arguments[1];
		final Map<String, List<ServiceOperation>> operations = (Map<String, List<ServiceOperation>>) arguments[2];
		final List<StoreEntity> entities = ((List<StoreEntity>) arguments[3]);

		context.put("store", store);
		context.put("services", services.stream().map(service -> new DataServiceTemplate(store, service, operations.get(service.id()))).collect(Collectors.toList()));
		context.put("entities", entities.stream().map(entity -> new StoreEntityTemplate(entity)).collect(Collectors.toList()));
	}

	private static final Map<String, ContextConfig> CONFIGS = new HashMap<>();
	static {
		CONFIGS.put("/project-pom.xml.vtl", SourceTemplate::store);
		CONFIGS.put("/server-pom.xml.vtl", SourceTemplate::store);
		CONFIGS.put("/client-pom.xml.vtl", SourceTemplate::store);
		CONFIGS.put("/gitignore.vtl", SourceTemplate::empty);
		CONFIGS.put("/README.md.vtl", SourceTemplate::store);
		CONFIGS.put("/manual.md.vtl", SourceTemplate::manual);

		CONFIGS.put("/entity.java.vtl", SourceTemplate::entity);
		CONFIGS.put("/model.java.vtl", SourceTemplate::entity);

		CONFIGS.put("/service-server-interface.java.vtl", SourceTemplate::service);
		CONFIGS.put("/service-implementation.java.vtl", SourceTemplate::service);
		CONFIGS.put("/service-client-interface.java.vtl", SourceTemplate::service);

		CONFIGS.put("/web.xml.vtl", SourceTemplate::store);
		CONFIGS.put("/app.xml.vtl", SourceTemplate::app);
		CONFIGS.put("/context.xml.vtl", SourceTemplate::store);
		CONFIGS.put("/persistence.xml.vtl", SourceTemplate::persistence);
	}
}
