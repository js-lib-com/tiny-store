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
	private final DataService dataService;

	private final String implementationPackage;
	private final String implementationName;
	private final String interfacePackage;
	private final String interfaceName;
	private final SortedSet<String> imports;
	private final List<ServiceOperationTemplate> operations;

	public DataServiceTemplate(Store store, DataService dataService, List<ServiceOperation> operations) {
		this.repositoryName = store.getName();
		this.dataService = dataService;

		this.implementationPackage = store.getPackageName();
		this.implementationName = Strings.getSimpleName(dataService.getClassName());
		this.interfacePackage = this.implementationPackage;
		this.interfaceName = 'I' + this.implementationName;

		this.operations = new ArrayList<>();
		operations.forEach(operation -> this.operations.add(new ServiceOperationTemplate(store, operation)));

		this.imports = new TreeSet<>();
		this.operations.forEach(operation -> imports.addAll(operation.getImports()));
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getDescription() {
		return dataService.getDescription();
	}

	public boolean isRestEnabled() {
		return dataService.isRestEnabled();
	}

	public String getRestPath() {
		return dataService.getRestPath();
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
