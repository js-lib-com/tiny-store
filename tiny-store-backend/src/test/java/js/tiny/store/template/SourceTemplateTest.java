package js.tiny.store.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.jslib.tiny.store.meta.DataOpcode;
import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.EntityField;
import com.jslib.tiny.store.meta.FieldFlag;
import com.jslib.tiny.store.meta.OperationException;
import com.jslib.tiny.store.meta.OperationParameter;
import com.jslib.tiny.store.meta.OperationValue;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.StoreEntity;
import com.jslib.tiny.store.meta.TypeDef;
import com.jslib.tiny.store.template.SourceTemplate;

public class SourceTemplateTest {
	private SourceTemplate sourceTemplate;
	
	@Test
	public void GivenRepositoryEntity_WhenGenerateEntity_Then() {
		// given
		StringWriter writer = new StringWriter();
		sourceTemplate = new SourceTemplate("/entity.java.vtl");
		
		Store store = new Store();
		store.setName("call");
		store.setPackageName("ro.gnotis");
		
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
		sourceTemplate.generate(writer, entity);
		
		// then
		System.out.println(writer);
	}
	
	@Test
	public void GivenRepositoryService_WhenGenerateServiceImplementation_Then() {
		// given
		StringWriter writer = new StringWriter();
		sourceTemplate = new SourceTemplate("/service-implementation.java.vtl");
		
		Store store = new Store();
		store.setName("call");
		store.setPackageName("ro.gnotis");
		
		DataService service = new DataService();
		service.setClassName("ro.gnotis.CallDAO");
		
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
		sourceTemplate.generate(writer, store, service, Arrays.asList(operation));
		
		// then
		System.out.println(writer);
	}	
}
