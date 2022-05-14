StorePage = class extends Page {
	constructor() {
		super();
		this._repositoriesView = document.getElementById("repositories-list");
		this._repositoriesView.addEventListener("select", this._onRepositorySelect.bind(this));

		this._entitiesView = document.getElementById("entities-list");
		this._entitiesView.addEventListener("select", this._onEntitySelect.bind(this));

		const storeId = location.search.substring(1);
		WorkspaceService.getStore(storeId, this._onStoreLoaded.bind(this));
		WorkspaceService.getRepositories(storeId, repositories => this._repositoriesView.setItems(repositories));
		WorkspaceService.getStoreEntities(storeId, entities => this._entitiesView.setItems(entities));

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
				this._repositoriesView.addItem(repository);
			});
		});
	}

	_onEditRepository(event) {
		const dialog = document.getElementById("repository-form");
		dialog.setTitle("Edit Repository");

		dialog.edit(this._repositoriesView.getSelectedItem(), repository => {
			WorkspaceService.saveRepository(repository, () => {
				this._repositoriesView.setSelectedItem(repository);
			});
		});
	}

	_onDeleteRepository(event) {
		const dialog = document.getElementById("repository-delete");
		dialog.open(() => {
			const repository = this._repositoriesView.getSelectedItem();
			WorkspaceService.deleteRepository(repository.id, () => {
				this._repositoriesView.deleteSelectedRow();
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
				this._entitiesView.addItem(entity);
			});
		});
	}

	_onEditEntity(event) {
		const dialog = document.getElementById("entity-form");
		dialog.setTitle("Edit Entity");

		dialog.edit(this._entitiesView.getSelectedItem(), entity => {
			WorkspaceService.saveEntity(entity, () => {
				this._entitiesView.setSelectedItem(entity);
			});
		});
	}

	_onDeleteEntity(event) {
		this._show("entity-delete", true);
		const dialog = document.getElementById("entity-delete");
		dialog.open(() => {
			const entity = this._entitiesView.getSelectedItem();
			WorkspaceService.deleteEntity(entity.id, () => {
				this._entitiesView.deleteSelectedRow();
			});
		});
	}
};

WinMain.createPage(StorePage);
