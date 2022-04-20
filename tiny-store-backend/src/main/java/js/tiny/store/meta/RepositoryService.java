package js.tiny.store.meta;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class RepositoryService {
	private TypeDef type;
	private String restPath;
	private String description;
	private List<ServiceOperation> operations;

	private transient SortedSet<TypeDef> imports;
	private transient String repositoryName;
	private transient String author;

	public TypeDef getType() {
		return type;
	}

	public void setType(TypeDef type) {
		this.type = type;
	}

	public String getRestPath() {
		return restPath;
	}

	public void setRestPath(String restPath) {
		this.restPath = restPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ServiceOperation> getOperations() {
		return operations;
	}

	public void setOperations(List<ServiceOperation> operations) {
		this.operations = operations;
	}

	public void setImports(SortedSet<TypeDef> imports) {
		this.imports = imports;
	}

	public SortedSet<TypeDef> getImports() {
		if (imports == null) {
			imports = new TreeSet<>();
			for (ServiceOperation operation : operations) {
				imports.addAll(operation.getImports());
			}
		}
		return imports;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
