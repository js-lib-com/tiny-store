StorePage = class extends Page {
	constructor() {
		super();
		this._repositoriesListView = document.getElementById("repositories-list");
		this._repositoriesListView.addEventListener("select", this._onRepositorySelect.bind(this));

		this._entitiesListView = document.getElementById("entities-list");
		this._entitiesListView.addEventListener("select", this._onEntitySelect.bind(this));

		const storePackage = location.search.substring(1);
		WorkspaceService.getStore(storePackage, this._onStoreLoaded.bind(this));
		WorkspaceService.getRepositories(storePackage, repositories => this._repositoriesListView.setItems(repositories));
		WorkspaceService.getStoreEntities(storePackage, entities => this._entitiesListView.setItems(entities));

		this._menu("create-repository", this._onCreateRepository, this);
		this._menu("edit-repository", this._onEditRepository, this);
		this._menu("delete-repository", this._onDeleteRepository, this);

		this._menu("create-entity", this._onCreateEntity, this);
		this._menu("edit-entity", this._onEditEntity, this);
		this._menu("delete-entity", this._onDeleteEntity, this);
	}

	_onStoreLoaded(store) {
		this._store = store;
		const pageCaption = document.getElementById("page-caption");
		this._inject(pageCaption, store);
	}

	_onRepositorySelect(event) {
		this._show("create-repository", !event.detail.selected);
		this._show("edit-repository", event.detail.selected);
		this._show("delete-repository", event.detail.selected);
	}

	_onCreateRepository(event) {
		this._show("repository-form", true);
	}

	_onEditRepository(event) {
		this._show("repository-form", true);
	}

	_onDeleteRepository(event) {
		this._show("repository-delete", true);
	}

	_onEntitySelect(event) {
		this._show("create-entity", !event.detail.selected);
		this._show("edit-entity", event.detail.selected);
		this._show("delete-entity", event.detail.selected);
	}

	_onCreateEntity(event) {
		this._show("entity-form", true);
	}

	_onEditEntity(event) {
		this._show("entity-form", true);
	}

	_onDeleteEntity(event) {
		this._show("entity-delete", true);
	}

	toString() {
		return "StorePage";
	}
};

WinMain.createPage(StorePage);
