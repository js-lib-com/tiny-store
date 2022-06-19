package js.tiny.store.db;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import js.tiny.store.meta.Store;
import js.tiny.store.meta.StoreEntity;

public class PersistenceUnitInfoImpl/* implements PersistenceUnitInfo*/ {
	private final String unitName;
	private final ComboPooledDataSource datasource;
	private final List<String> entities;
	private final Properties properties;

	public PersistenceUnitInfoImpl(Store store, List<StoreEntity> entities) {
		this.unitName = store.getName();

		this.datasource = new ComboPooledDataSource();
		this.datasource.setAcquireRetryAttempts(1);
		this.datasource.setJdbcUrl(store.getDatabaseURL());
		this.datasource.setUser(store.getDatabaseUser());
		this.datasource.setPassword(store.getDatabasePassword());

		this.entities = entities.stream().map(entity -> entity.getClassName()).collect(Collectors.toList());

		this.properties = new Properties();
		this.properties.put("eclipselink.logging.level.sql", "FINE");
		this.properties.put("eclipselink.logging.parameters", "true");
	}
/*
	
	@Override
	public String getPersistenceUnitName() {
		return unitName;
	}

	@Override
	public String getPersistenceProviderClassName() {
		return "org.eclipse.persistence.jpa.PersistenceProvider";
	}

	@Override
	public PersistenceUnitTransactionType getTransactionType() {
		return PersistenceUnitTransactionType.RESOURCE_LOCAL;
	}

	@Override
	public DataSource getJtaDataSource() {
		return null;
	}

	@Override
	public DataSource getNonJtaDataSource() {
		return datasource;
	}

	@Override
	public List<String> getMappingFileNames() {
		return Collections.emptyList();
	}

	@Override
	public List<URL> getJarFileUrls() {
		return Collections.emptyList();
	}

	@Override
	public URL getPersistenceUnitRootUrl() {
		try {
			return new File("D:\\runtime\\tiny-store\\webapps\\test#1.0\\WEB-INF\\classes").toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public List<String> getManagedClassNames() {
		return entities;
	}

	@Override
	public boolean excludeUnlistedClasses() {
		return true;
	}

	@Override
	public SharedCacheMode getSharedCacheMode() {
		return SharedCacheMode.NONE;
	}

	@Override
	public ValidationMode getValidationMode() {
		return ValidationMode.NONE;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public String getPersistenceXMLSchemaVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClassLoader getClassLoader() {
		return getClass().getClassLoader();
	}

	@Override
	public void addTransformer(ClassTransformer transformer) {
	}

	@Override
	public ClassLoader getNewTempClassLoader() {
		return getClass().getClassLoader();
	}
*/	
}
