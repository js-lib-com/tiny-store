StorePage = class extends Page {
	constructor() {
		super();
		this._servicesView = document.getElementById("services-list");
		this._servicesView.addEventListener("select", this._onServiceSelect.bind(this));

		this._entitiesView = document.getElementById("entities-list");
		this._entitiesView.addEventListener("select", this._onEntitySelect.bind(this));

		const storeId = location.search.substring(1);
		Database.getStore(storeId, this._onStoreLoaded.bind(this));
		Database.getStoreServices(storeId, services => this._servicesView.setItems(services));
		Database.getStoreEntities(storeId, entities => this._entitiesView.setItems(entities));

		this._sideMenu = this.getSideMenu();
		this._sideMenu.setLink("index-page", () => `index.htm`);
		this._sideMenu.setLink("service-page", () => `service.htm?${this._servicesView.getSelectedId()}`);
		this._sideMenu.setLink("entity-page", () => `entity.htm?${this._entitiesView.getSelectedId()}`);

		this._actionBar = this.getActionBar("StorePage");
		this._actionBar.setHandler("edit-store", this._onEditStore);
		this._actionBar.setHandler("delete-store", this._onDeleteStore);
		this._actionBar.setHandler("build-project", this._onBuildProject);

		this._actionBar.setHandler("create-service", this._onCreateService);
		this._actionBar.setHandler("edit-service", this._onEditService);
		this._actionBar.setHandler("delete-service", this._onDeleteService);
		this._onServiceSelect({ detail: { selected: false } });

		this._actionBar.setHandler("create-dao", this._onCreateDAO);
		this._actionBar.setHandler("create-entity", this._onCreateEntity);
		this._actionBar.setHandler("edit-entity", this._onEditEntity);
		this._actionBar.setHandler("delete-entity", this._onDeleteEntity);
		this._onEntitySelect({ detail: { selected: false } });
	}

	_onStoreLoaded(store) {
		this._store = store;
		this._storeId = store.id;
		this._setObject(store);
	}

	_onServiceSelect(event) {
		const selected = event.detail.selected;
		this._sideMenu.enable("service-page", selected);
		this._actionBar.show("create-service", !selected);
		this._actionBar.show("edit-service", selected);
		this._actionBar.show("delete-service", selected);
	}

	_onEditStore() {
		const dialog = this.getCompo("store-form");
		dialog.setHandler("test", this._onTestStore.bind(this));
		dialog.edit(this._store, store => {
			Workspace.updateStore(store, () => this._setObject(store));
		});
	}

	_onDeleteStore() {
		const dialog = this.getCompo("store-delete");
		dialog.open(() => {
			Workspace.deleteStore(this._store.id, () => location.assign("index.htm"));
		});
	}

	_onTestStore(store) {
		Workspace.testDataSource(store, this.alert);
	}

	_onCreateService() {
		const dialog = this.getCompo("service-form");
		dialog.setTitle("Create Service");

		const service = {
			restEnabled: this._store.restPath != null
		};

		dialog.edit(service, service => {
			Database.createDataService(this._store.id, service, service => {
				this._servicesView.addItem(service);
			});
		});
	}

	_onCreateDAO() {
		Workspace.createDaoService(this._entitiesView.getSelectedItem(), service => {
			this._servicesView.addItem(service);
		});
	}

	_onEditService() {
		const dialog = this.getCompo("service-form");
		dialog.setTitle("Edit Service");

		dialog.edit(this._servicesView.getSelectedItem(), service => {
			Database.updateDataService(service, () => {
				this._servicesView.setSelectedItem(service);
			});
		});
	}

	_onDeleteService() {
		const dialog = this.getCompo("service-delete");
		dialog.open(() => {
			const service = this._servicesView.getSelectedItem();
			Database.deleteDataService(service, () => {
				this._servicesView.deleteSelectedRow();
			});
		});
	}

	_onEntitySelect(event) {
		const selected = event.detail.selected;
		this._sideMenu.enable("entity-page", selected);
		this._actionBar.show("create-dao", selected);
		this._actionBar.show("create-entity", !selected);
		this._actionBar.show("edit-entity", selected);
		this._actionBar.show("delete-entity", selected);
		this._actionBar.show("build-project", !selected);
	}

	_onCreateEntity() {
		const dialog = this.getCompo("entity-form");
		dialog.setTitle("Create Entity");
		
		dialog.setHandler("import", entity => {
			Workspace.importStoreEntity(this._store.id, entity, entity => {
				this._entitiesView.addItem(entity);
			});
		}, { autoClose: true });

		dialog.open(entity => {
			Database.createStoreEntity(this._store.id, entity, entity => {
				this._entitiesView.addItem(entity);
			});
		});
	}

	_onEditEntity() {
		const dialog = this.getCompo("entity-form");
		dialog.setTitle("Edit Entity");

		dialog.setHandler("import", entity => {
			Workspace.importStoreEntity(this._store.id, entity, entity => {
				this._entitiesView.setSelectedItem(entity);
			});
		}, { autoClose: true });

		dialog.edit(this._entitiesView.getSelectedItem(), entity => {
			Database.updateStoreEntity(entity, () => {
				this._entitiesView.setSelectedItem(entity);
			});
		});
	}

	_onDeleteEntity() {
		this._show("entity-delete", true);
		const dialog = this.getCompo("entity-delete");
		dialog.open(() => {
			const entity = this._entitiesView.getSelectedItem();
			Database.deleteStoreEntity(entity, () => {
				this._entitiesView.deleteSelectedRow();
			});
		});
	}

	_onBuildProject() {
		Workspace.buildProject(this._store.id, result => alert(JSON.stringify(result)));
	}
};

WinMain.createPage(StorePage);
