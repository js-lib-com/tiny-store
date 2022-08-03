package com.jslib.tiny.store;

import org.bson.types.ObjectId;

import com.jslib.tiny.store.dao.ObjectIdConverter;

import jakarta.annotation.Priority;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import js.converter.ConverterRegistry;

@ApplicationScoped
@Startup
@Priority(0)
public class Application {
	public Application() {
		ConverterRegistry.getInstance().registerConverter(ObjectId.class, ObjectIdConverter.class);
	}
}
