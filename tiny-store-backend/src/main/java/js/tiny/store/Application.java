package js.tiny.store;

import org.bson.types.ObjectId;

import jakarta.annotation.Priority;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import js.converter.ConverterRegistry;
import js.tiny.store.dao.ObjectIdConverter;

@ApplicationScoped
@Startup
@Priority(0)
public class Application {
	public Application() {
		ConverterRegistry.getInstance().registerConverter(ObjectId.class, ObjectIdConverter.class);
	}
}
