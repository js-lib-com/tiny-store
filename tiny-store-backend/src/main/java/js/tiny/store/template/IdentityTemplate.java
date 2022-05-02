package js.tiny.store.template;

import js.tiny.store.meta.Identity;

public class IdentityTemplate extends EntityFieldTemplate {
	private final Identity identity;

	public IdentityTemplate(Identity identity) {
		super(identity);
		this.identity = identity;
	}

	public boolean isGenerated() {
		return identity.isGenerated();
	}
}
