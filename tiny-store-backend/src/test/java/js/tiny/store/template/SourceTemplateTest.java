package js.tiny.store.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import js.tiny.store.meta.DataOpcode;
import js.tiny.store.meta.DataService;
import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.FieldFlag;
import js.tiny.store.meta.OperationException;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.OperationValue;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;

public class SourceTemplateTest {
	private SourceTemplate sourceTemplate;
	
	@Test
	public void GivenRepositoryEntity_WhenGenerateEntity_Then() {
		// given
		StringWriter writer = new StringWriter();
		sourceTemplate = new SourceTemplate("/entity.java.vtl");
		
		EntityField identity = new EntityField();
		identity.setName("id");
		identity.setType(new TypeDef(String.class.getCanonicalName()));
		identity.setAlias("nlc");
		identity.setDescription("Call identity.");
		identity.setFlag(FieldFlag.IDENTITY_KEY);
		
		EntityField field = new EntityField();
		field.setName("person");
		field.setType(new TypeDef(List.class.getCanonicalName(),"ro.gnotis.Person"));
		field.setAlias("employee");
		field.setDescription("Humble employee.");
		
		StoreEntity entity = new StoreEntity();
		entity.setClassName("ro.gnotis.Call");
		entity.setAlias("call");
		entity.setDescription("Recorded calls.");
		entity.setFields(Arrays.asList(identity, field));
		
		// when
		sourceTemplate.generate(entity, writer);
		
		// then
		System.out.println(writer);
	}
	
	@Test
	public void GivenRepositoryService_WhenGenerateServiceImplementation_Then() {
		// given
		StringWriter writer = new StringWriter();
		sourceTemplate = new SourceTemplate("/service-implementation.java.vtl");
		
		DataService service = new DataService();
		service.setClassName("ro.gnotis.CallDAO");
		service.setInterfaceName("ro.gnotis.intf.ICallDAO");
		
		OperationParameter parameter = new OperationParameter();
		parameter.setType(new TypeDef(String.class.getCanonicalName()));
		parameter.setName("customerID");
		parameter.setDescription("customer database ID");
		
		OperationValue value = new OperationValue();
		value.setType(new TypeDef("ro.gnotis.model.CallerID"));
		value.setDescription("customer caller ID");
		
		OperationException exception = new OperationException();
		exception.setType(IOException.class.getCanonicalName());
		exception.setCause("user file read fails");
		
		ServiceOperation operation = new ServiceOperation();
		operation.setName("getCallerID");
		operation.setDescription("Return customer caller ID, if provided. Otherwise return null.");
		operation.setParameters(Arrays.asList(parameter));
		operation.setValue(value);
		operation.setExceptions(Arrays.asList(exception));
		operation.setDataOpcode(DataOpcode.READ);

		
		// when
		sourceTemplate.generate(service, Arrays.asList(operation), writer);
		
		// then
		System.out.println(writer);
	}	
}
