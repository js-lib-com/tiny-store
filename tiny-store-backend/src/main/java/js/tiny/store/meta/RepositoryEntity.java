package js.tiny.store.meta;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class RepositoryEntity {
	private TypeDef type;
	private String description;
	private String alias;
	private Identity identity;
	private List<EntityField> fields;
	
	private transient SortedSet<TypeDef> imports;
	private transient String author;

	public TypeDef getType() {
		return type;
	}

	public void setType(TypeDef type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public List<EntityField> getFields() {
		return fields;
	}

	public void setFields(List<EntityField> fields) {
		this.fields = fields;
	}

	public void setImports(SortedSet<TypeDef> imports) {
		this.imports = imports;
	}

	public SortedSet<TypeDef> getImports() {
		if(imports == null) {
			imports = new TreeSet<>();
			for (EntityField field : fields) {
				final TypeDef type = field.getType();
				if(type.isCollection()) {
					imports.add(type.getCollection());
				}
				if(type.isDefaultPackage()) {
					continue;
				}
				if(type.isPrimitive()) {
					continue;
				}
				imports.add(type);
			}
		}
		return imports;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
