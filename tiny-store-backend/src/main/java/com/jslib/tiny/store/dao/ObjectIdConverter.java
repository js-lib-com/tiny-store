package com.jslib.tiny.store.dao;

import org.bson.types.ObjectId;

import com.jslib.converter.Converter;
import com.jslib.converter.ConverterException;

public class ObjectIdConverter implements Converter {
	@SuppressWarnings("unchecked")
	@Override
	public <T> T asObject(String string, Class<T> valueType) throws IllegalArgumentException, ConverterException {
		return (T) new ObjectId(string);
	}

	@Override
	public String asString(Object object) throws ConverterException {
		return ((ObjectId)object).toHexString();
	}
}
