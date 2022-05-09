package js.tiny.store.tool;

import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.EntityField;
import js.tiny.store.meta.Identity;
import js.tiny.store.meta.OperationException;
import js.tiny.store.meta.OperationParameter;
import js.tiny.store.meta.OperationValue;
import js.tiny.store.meta.Repository;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.StoreEntity;
import js.tiny.store.meta.TypeDef;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SourceFileTest {
	@Mock
	private Repository repository;
	@Mock
	private DataService service;

	@Mock
	private ServiceOperation operation;
	@Mock
	private OperationParameter parameter1;
	@Mock
	private OperationParameter parameter2;
	@Mock
	private OperationValue value;
	@Mock
	private OperationException exception1;
	@Mock
	private OperationException exception2;

	@Mock
	private StoreEntity entity;
	@Mock
	private Identity identity;
	@Mock
	private EntityField field1;
	@Mock
	private EntityField field2;

	private Writer writer;

	@Before
	public void beforeTest() {
		writer = new StringWriter();

		when(entity.getClassName()).thenReturn("ro.gnotis.omsx.model.CallCoordinates");
		when(entity.getAlias()).thenReturn("customer_data");
		when(entity.getDescription()).thenReturn("Data related to customer call history.");
		when(entity.getIdentity()).thenReturn(identity);
		when(entity.getFields()).thenReturn(Arrays.asList(field1, field2));

		when(identity.getName()).thenReturn("id");
		when(identity.getTitle()).thenReturn("Id");
		when(identity.getType()).thenReturn(new TypeDef(int.class.getCanonicalName()));
		when(identity.getAlias()).thenReturn("nlc");

		when(field1.getName()).thenReturn("userName");
		when(field1.getTitle()).thenReturn("UserName");
		when(field1.getType()).thenReturn(new TypeDef(Set.class.getCanonicalName(), String.class.getCanonicalName()));
		when(field1.getAlias()).thenReturn("user_name");

		when(field2.getName()).thenReturn("phoneNumber");
		when(field2.getTitle()).thenReturn("PhoneNumber");
		// when(field2.getType()).thenReturn(new Type(String.class.getCanonicalName()));
		when(field2.getType()).thenReturn(new TypeDef("ro.gnotis.omsx.model.CallCoordinates"));
		when(field2.getAlias()).thenReturn("phone_number");

		//when(service.getClassName()).thenReturn("ro.gnotis.omsx.CallDataService");
		when(service.getInterfaceName()).thenReturn("ro.gnotis.omsx.ICallDataService");
		when(service.getRestPath()).thenReturn("call");
		when(service.getDescription()).thenReturn("Data related to customer call history.");

		when(operation.getName()).thenReturn("readCallCoordinates");
		when(operation.getRestMethod()).thenReturn("GET");
		when(operation.getRestPath()).thenReturn("coordinates");
		when(operation.getDescription()).thenReturn("Reads coordinates for call performed from given phone number or null if phone number not found.");
		when(operation.getParameters()).thenReturn(Arrays.asList(parameter1, parameter2));
		// when(operation.getParameter()).thenReturn(parameter1);
		when(operation.getValue()).thenReturn(value);
		when(operation.getExceptions()).thenReturn(Arrays.asList(exception1, exception2));

		//when(operation.getDataOpcode()).thenReturn(DataOpcode.READ);
		when(operation.getQuery()).thenReturn("select e from CallEvent e where e.latitude=?0 and e.longitude=?1");

		when(value.getType()).thenReturn(new TypeDef(Set.class.getCanonicalName(), "ro.gnotis.omsx.model.CallCoordinates"));
		when(value.getType()).thenReturn(new TypeDef("ro.gnotis.omsx.model.CallCoordinates"));
		// when(value.getType()).thenReturn(new TypeName(void.class));
		when(value.getDescription()).thenReturn("geographical coordinates for source call location.");

		when(exception1.getType()).thenReturn(IOException.class.getCanonicalName());
		when(exception1.getCause()).thenReturn("coordinates reading fails");

		when(exception2.getType()).thenReturn(FileNotFoundException.class.getCanonicalName());
		when(exception2.getCause()).thenReturn("file is missing");

		// when(parameter1.getType()).thenReturn(new TypeName(String.class.getCanonicalName()));
		when(parameter1.getType()).thenReturn(new TypeDef("ro.gnotis.omsx.model.CallCoordinates"));
		when(parameter1.getName()).thenReturn("phoneNumber");
		when(parameter1.getDescription()).thenReturn("customer phone number");
		when(parameter1.isEntityParam()).thenReturn(true);

		when(parameter2.getType()).thenReturn(new TypeDef(String.class.getCanonicalName()));
		when(parameter2.getName()).thenReturn("userName");
		when(parameter2.getDescription()).thenReturn("user name");
		when(parameter2.isEntityParam()).thenReturn(true);
	}

	@Test
	public void GivenServiceRemote_WhenGenerateSource_Then() {
		// given
		SourceFile sourceFile = new SourceFile("/service-remote.java.vtl");

		// when
		sourceFile.generate("service", service, writer);

		// then
		System.out.println(writer);
	}

	@Test
	public void GivenServiceInterface_WhenGenerateSource_Then() {
		// given
		SourceFile sourceFile = new SourceFile("/service-interface.java.vtl");

		// when
		sourceFile.generate("service", service, writer);

		// then
		System.out.println(writer);
	}

	@Test
	public void GivenServiceImplementation_WhenGenerateSource_Then() {
		// given
		SourceFile sourceFile = new SourceFile("/service-implementation.java.vtl");

		// when
		sourceFile.generate("service", service, writer);

		// then
		System.out.println(writer);
	}

	@Test
	public void GivenEntity_WhenGenerateSource_Then() {
		// given
		SourceFile sourceFile = new SourceFile("/entity.java.vtl");

		// when
		sourceFile.generate("entity", entity, writer);

		// then
		System.out.println(writer);
	}
}
