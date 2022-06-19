package js.tiny.store.db;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public class JpqlExecuteTest {
	private Store store = new Store();
	private List<StoreEntity> entities = new ArrayList<>();

	@Test
	public void execute() {
		/*
		store.setName("test");
		store.setDatabaseURL("jdbc:mysql://localhost:3306/test");
		store.setDatabaseUser("test");
		store.setDatabasePassword("test");
		
		entities.add(entity(Person.class));

		PersistenceUnitInfo info = new PersistenceUnitInfoImpl(store, entities);

		PersistenceProvider provider = new org.eclipse.persistence.jpa.PersistenceProvider();

		Map<String, Object> configuration = new HashMap<>();
		EntityManagerFactory factory = provider.createContainerEntityManagerFactory(info, configuration);

		try (EntityManager em = factory.createEntityManager()) {
			TypedQuery<Person> query = em.createQuery("select p from Person p", Person.class);
			query.setMaxResults(1);
			Person person = query.getSingleResult();
			System.out.println(person);
		}
		*/
	}
	
	private static StoreEntity entity(Class<?> entityClass) {
		StoreEntity entity = new StoreEntity();
		entity.setClassName(entityClass.getCanonicalName());
		return entity;
	}
}
