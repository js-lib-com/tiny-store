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
		this._setObject(store);
	}

	_onRepositorySelect(event) {
		this._show("create-repository", !event.detail.selected);
		this._show("edit-repository", event.detail.selected);
		this._show("delete-repository", event.detail.selected);
	}

	_onCreateRepository(event) {
		const dialog = document.getElementById("repository-form");
		dialog.setTitle("Create Repository");

		dialog.open(repository => {
			WorkspaceService.createRepository(this._store, repository, () => {
				this._repositoriesListView.addItem(repository);
			});
		});
	}

	_onEditRepository(event) {
		const dialog = document.getElementById("repository-form");
		dialog.setTitle("Edit Repository");

		dialog.update(this._repositoriesListView.getSelectedItem(), repository => {
			WorkspaceService.saveRepository(repository, () => {
				this._repositoriesListView.setSelectedItem(repository);
			});
		});
	}

	_onDeleteRepository(event) {
		const dialog = document.getElementById("repository-delete");
		dialog.open(() => {
			WorkspaceService.deleteRepository(this._repositoriesListView.getSelectedItem(), () => {
				this._repositoriesListView.deleteSelectedRow();
			});
		});
	}

	_onEntitySelect(event) {
		this._show("create-entity", !event.detail.selected);
		this._show("edit-entity", event.detail.selected);
		this._show("delete-entity", event.detail.selected);
	}

	_onCreateEntity(event) {
		const dialog = document.getElementById("entity-form");
		dialog.setTitle("Create Entity");

		dialog.open(entity => {
			WorkspaceService.createEntity(this._store, entity, () => {
				this._entitiesListView.addItem(entity);
			});
		});
	}

	_onEditEntity(event) {
		const dialog = document.getElementById("entity-form");
		dialog.setTitle("Edit Entity");

		dialog.update(this._entitiesListView.getSelectedItem(), entity => {
			WorkspaceService.saveEntity(entity, () => {
				this._entitiesListView.setSelectedItem(entity);
			});
		});
	}

	_onDeleteEntity(event) {
		this._show("entity-delete", true);
		const dialog = document.getElementById("entity-delete");
		dialog.open(() => {
			WorkspaceService.deleteEntity(this._entitiesListView.getSelectedItem(), () => {
				this._entitiesListView.deleteSelectedRow();
			});
		});
	}
};

WinMain.createPage(StorePage);
