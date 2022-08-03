package js.tiny.store.db;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.jslib.tiny.store.meta.DataOpcode;
import com.jslib.tiny.store.meta.OperationParameter;
import com.jslib.tiny.store.meta.OperationValue;
import com.jslib.tiny.store.meta.ParameterFlag;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.meta.TypeDef;
import com.jslib.tiny.store.tool.Project;
import com.jslib.tiny.store.util.ProjectPersistenceUnitInfo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceUnitInfo;
import js.util.Classes;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class JpqlExecuteTest {
	@Mock
	private Store store;
	@Mock
	private Project project;

	private List<String> entities = new ArrayList<>();

	@Before
	public void beforeTest() throws IOException {
		when(store.getName()).thenReturn("test");
		when(store.getDatabaseURL()).thenReturn("jdbc:mysql://localhost:3306/test");
		when(store.getDatabaseUser()).thenReturn("test");
		when(store.getDatabasePassword()).thenReturn("test");
		
		when(project.getStore()).thenReturn(store);
		when(project.getServerClassesDir()).thenReturn(new File("D:\\runtime\\tiny-store\\workspace\\test\\server\\target\\classes"));
	}

	@Test
	public void execute() throws Exception {
		ServiceOperation operation = new ServiceOperation();
		operation.setName("read");
		operation.setDataOpcode(DataOpcode.READ);
		operation.setQuery("select p from Person p where p.id=?1");
		operation.setValue(new OperationValue("com.jslib.test.Person"));
		operation.setParameters(Arrays.asList(new OperationParameter(Integer.class, "id"), new OperationParameter(Integer.class, "max", ParameterFlag.MAX_RESULTS)));

		entities = Arrays.asList("com.jslib.test.Person");

		PersistenceUnitInfo info = new ProjectPersistenceUnitInfo(project, entities);
		PersistenceProvider provider = Classes.loadService(PersistenceProvider.class);

		Map<String, Object> configuration = new HashMap<>();
		EntityManagerFactory factory = provider.createContainerEntityManagerFactory(info, configuration);

		try  {
			EntityManager em = factory.createEntityManager();
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			try {
				Query query = em.createQuery(operation.getQuery());

				int position = 0;
				if (operation.getParameters() != null) {
					for (OperationParameter parameter : operation.getParameters()) {
						if (parameter.getFlag() == ParameterFlag.FIRST_RESULT) {
							query.setFirstResult(0);
							continue;
						}
						if (parameter.getFlag() == ParameterFlag.MAX_RESULTS) {
							query.setMaxResults(1);
							continue;
						}
						query.setParameter(++position, parameter(parameter));
					}
				}

				switch (operation.getDataOpcode()) {
				case CREATE:
					query.executeUpdate();
					break;

				case READ:
					TypeDef valueType = operation.getValue().getType();
					if (valueType.getCollection() != null) {
						query.getResultList();
					} else {
						query.getSingleResult();
					}
					break;

				case UPDATE:
					query.executeUpdate();
					break;

				case DELETE:
					query.executeUpdate();
					break;

				}

			} finally {
				transaction.rollback();
				em.close();
			}
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	private static Object parameter(OperationParameter parameter) {
		switch (parameter.getType().getName()) {
		case "java.lang.String":
			return "string";

		case "java.lang.Boolean":
			return true;

		case "java.lang.Byte":
		case "java.lang.Short":
		case "java.lang.Integer":
		case "java.lang.Long":
			return 4;

		case "java.lang.Float":
		case "java.lang.Double":
			return 1.0;

		case "java.util.Date":
			return new Date();

		case "java.sql.Time":
			return new Time(new Date().getTime());

		case "java.sql.Timestamp":
			return new Timestamp(new Date().getTime());
		}

		return new Object();
	}
}
