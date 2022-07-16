package js.tiny.store.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import js.tiny.store.meta.Store;
import js.tiny.store.tool.Project;

public class ProjectPersistenceUnitInfo implements PersistenceUnitInfo, AutoCloseable {
	private final String unitName;
	private final URL rootUrl;
	private final ComboPooledDataSource datasource;
	private final EntityClassLoader entitiesLoader;
	private final List<String> entities;
	private final Properties properties;

	public ProjectPersistenceUnitInfo(Project project, List<String> entities) throws IOException {
		Store store = project.getStore();
		this.unitName = store.getName();
		this.rootUrl = project.getServerClassesDir().toURI().toURL();

		this.datasource = new ComboPooledDataSource();
		this.datasource.setAcquireRetryAttempts(1);
		this.datasource.setJdbcUrl(store.getDatabaseURL());
		this.datasource.setUser(store.getDatabaseUser());
		this.datasource.setPassword(store.getDatabasePassword());

		this.entitiesLoader = new EntityClassLoader(this.rootUrl);
		this.entities = entities;

		this.properties = new Properties();
		this.properties.put("eclipselink.logging.level.sql", "FINE");
		this.properties.put("eclipselink.logging.parameters", "true");
	}

	@Override
	public String getPersistenceUnitName() {
		return unitName;
	}

	@Override
	public String getPersistenceProviderClassName() {
		return org.eclipse.persistence.jpa.PersistenceProvider.class.getCanonicalName();
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
		return rootUrl;
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
		return entitiesLoader;
	}

	@Override
	public void addTransformer(ClassTransformer transformer) {
	}

	@Override
	public ClassLoader getNewTempClassLoader() {
		return null;
	}

	@Override
	public void close() {
		datasource.close();
	}

	/**
	 * Custom class loader to temporary load entity classes from store workspace, target classes directory. At the moment this
	 * class loader is enacted entity source file should be generated and compiled. This class loader implementation assumes the
	 * class file is updated and recompiled to reflect latest entity metadata changes.
	 * 
	 * @author Iulian Rotaru
	 */
	private static class EntityClassLoader extends ClassLoader {
		/**
		 * Target classes directory from store workspace, loaded from {@link PersistenceUnitInfo#getPersistenceUnitRootUrl()}.
		 */
		private final File classesDir;

		/**
		 * Create custom entity class loader and initialize classes directory where entities are searched.
		 * 
		 * @param rootUrl root URL returned by {@link PersistenceUnitInfo#getPersistenceUnitRootUrl()}.
		 */
		public EntityClassLoader(URL rootUrl) {
			this.classesDir = new File(rootUrl.getFile());
		}

		/**
		 * Load entity class located on classes directory from store workspace.
		 * 
		 * @param className qualified class name.
		 * @return entity class, never null.
		 * @throws ClassNotFoundException if entity class file not found.
		 */
		@Override
		protected Class<?> findClass(String className) throws ClassNotFoundException {
			File classFile = Files.classFile(classesDir, className);
			if (!classFile.exists()) {
				return super.findClass(className);
			}

			try (InputStream stream = new BufferedInputStream(new FileInputStream(classFile))) {
				ByteArrayOutputStream bytecode = new ByteArrayOutputStream();

				int nextValue = 0;
				while ((nextValue = stream.read()) != -1) {
					bytecode.write(nextValue);
				}

				return defineClass(className, bytecode.toByteArray(), 0, bytecode.size());
			} catch (IOException e) {
				throw new ClassNotFoundException(className);
			}
		}

		/**
		 * When persistence unit is created it should scan for configured entity classes and obtain entity name from
		 * {@literal}Entity annotation. In order to avoid interfering with class loading process, implementation reads class
		 * files as a bytes streams using {@link ClassLoader#getResourceAsStream(String)} that, on its turn calls this method.
		 * 
		 * Class path parameter is used as resource identifier and observes resources naming rules: path components are
		 * separated by slash and file name extension (.class) is present, e.g. <code>com/jslib/test/Person.class</code>. Class
		 * path parameter is relative to target classes directory from store workspace, see {@link #classesDir}.
		 * 
		 * @param classPath entity class path used as resource identifier.
		 * @return entity class file URL or null is not found.
		 */
		@Override
		protected URL findResource(String classPath) {
			File classFile = new File(classesDir, classPath);
			if (!classFile.exists()) {
				return super.findResource(classPath);
			}
			try {
				return classFile.toURI().toURL();
			} catch (MalformedURLException e) {
				return null;
			}
		}

		@Override
		public String toString() {
			return "EntityClassLoader";
		}
	}
}
