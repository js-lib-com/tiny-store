package com.jslib.tiny.store.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.jslib.tiny.store.meta.DataService;
import com.jslib.tiny.store.meta.ServiceOperation;
import com.jslib.tiny.store.meta.Store;
import com.jslib.tiny.store.util.Strings;

public class DataServiceTemplate {
	private final String repositoryName;
	private final DataService service;

	private final String className;
	private final String implementationPackage;
	private final String implementationName;
	private final String interfacePackage;
	private final String interfaceName;
	private final SortedSet<String> imports;
	private final List<ServiceOperationTemplate> operations;
	private final String restPath;

	public DataServiceTemplate(Store store, DataService service) {
		this(store, service, Collections.emptyList());
	}

	public DataServiceTemplate(Store store, DataService service, List<ServiceOperation> operations) {
		this.repositoryName = store.getName();
		this.service = service;

		this.className = Strings.simpleName(service.getClassName());
		this.implementationPackage = Strings.packageName(service.getClassName());
		this.implementationName = Strings.simpleName(service.getClassName());
		this.interfacePackage = this.implementationPackage;
		this.interfaceName = 'I' + this.implementationName;

		this.operations = new ArrayList<>();
		operations.forEach(operation -> this.operations.add(new ServiceOperationTemplate(store, operation)));

		this.imports = new TreeSet<>();
		this.operations.forEach(operation -> imports.addAll(operation.getImports()));

		String restPath = service.getRestPath();
		this.restPath = restPath != null && !restPath.isEmpty() && restPath.charAt(0) == '/' ? restPath.substring(1) : restPath;
	}

	public String getClassName() {
		return className;
	}

	public String getQualifiedClassName() {
		return Strings.concat(implementationPackage, '.', implementationName);
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
