package js.tiny.store.template;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import js.tiny.store.meta.DataService;
import js.tiny.store.meta.ServiceOperation;
import js.tiny.store.meta.Store;
import js.tiny.store.tool.Strings;

public class DataServiceTemplate {
	private final String repositoryName;
	private final DataService service;

	private final String implementationPackage;
	private final String implementationName;
	private final String interfacePackage;
	private final String interfaceName;
	private final SortedSet<String> imports;
	private final List<ServiceOperationTemplate> operations;
	private final String restPath;

	public DataServiceTemplate(Store store, DataService service, List<ServiceOperation> operations) {
		this.repositoryName = store.getName();
		this.service = service;

		this.implementationPackage = store.getPackageName();
		this.implementationName = Strings.getSimpleName(service.getClassName());
		this.interfacePackage = this.implementationPackage;
		this.interfaceName = 'I' + this.implementationName;

		this.operations = new ArrayList<>();
		operations.forEach(operation -> this.operations.add(new ServiceOperationTemplate(store, operation)));

		this.imports = new TreeSet<>();
		this.operations.forEach(operation -> imports.addAll(operation.getImports()));

		String restPath = service.getRestPath();
		this.restPath = restPath != null && !restPath.isEmpty() && restPath.charAt(0) == '/' ? restPath.substring(1) : restPath;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getDescription() {
		return service.getDescription();
	}

	public boolean isRestEnabled() {
		return service.isRestEnabled();
	}

	public String getRestPath() {
		return restPath;
	}

	public String getImplementationPackage() {
		return implementationPackage;
	}

	public String getImplementationName() {
		return implementationName;
	}

	public String getInterfacePackage() {
		return interfacePackage;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public SortedSet<String> getImports() {
		return imports;
	}

	public List<ServiceOperationTemplate> getOperations() {
		return operations;
	}
}
