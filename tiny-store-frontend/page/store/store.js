StorePage = class extends Page {
	constructor() {
		super();
		this._servicesView = document.getElementById("services-list");
		this._servicesView.addEventListener("select", this._onServiceSelect.bind(this));

		this._entitiesView = document.getElementById("entities-list");
		this._entitiesView.addEventListener("select", this._onEntitySelect.bind(this));

		this._loadStore();

		this._sideMenu = this.getSideMenu();
		this._sideMenu.setLink("index-page", () => `index.htm`);
		this._sideMenu.setLink("service-page", () => `service.htm?${this._servicesView.getSelectedId()}`);
		this._sideMenu.setLink("entity-page", () => `entity.htm?${this._entitiesView.getSelectedId()}`);

		this._actionBar = this.getActionBar("StorePage");
		this._actionBar.setHandler("edit-store", this._onEditStore);
		this._actionBar.setHandler("delete-store", this._onDeleteStore);
		this._actionBar.setHandler("build-project", this._onBuildProject);

		this._actionBar.setHandler("create-service", this._onCreateService);
		this._onServiceSelect({ detail: { selected: false } });

		this._actionBar.setHandler("create-dao", this._onCreateDAO);
		this._actionBar.setHandler("create-entity", this._onCreateEntity);
		this._onEntitySelect({ detail: { selected: false } });
	}

	_loadStore(delay) {
		const storeId = location.search.substring(1);
		Database.getStore(storeId, this._onStoreLoaded.bind(this));

		if (typeof delay != "undefined") {
			setTimeout(() => {
				Database.getStoreServices(storeId, services => this._servicesView.setItems(services));
				Database.getStoreEntities(storeId, entities => this._entitiesView.setItems(entities));
			}, delay);
			return;
		}

		Database.getStoreServices(storeId, services => this._servicesView.setItems(services));
		Database.getStoreEntities(storeId, entities => this._entitiesView.setItems(entities));
	}

	_onStoreLoaded(store) {
		this._store = store;
		this._storeId = store.id;
		this._setObject(store);
	}

	_onServiceSelect(event) {
		const selected = event.detail.selected;
		this._sideMenu.enable("service-page", selected);
	}

	_onEditStore() {
		const dialog = this.getCompo("store-form");
		dialog.setHandler("test", this._onTestStore.bind(this));
		dialog.edit(this._store, store => {
			Workspace.updateStore(store, () => this._loadStore(2000));
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
		dialog.title = "Create Service";

		const service = {
			className: `${this._store.packageName}.`,
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

	_onEntitySelect(event) {
		const selected = event.detail.selected;
		this._sideMenu.enable("entity-page", selected);
		this._actionBar.show("create-dao", selected);
	}

	_onCreateEntity() {
		const dialog = this.getCompo("entity-form");
		dialog.title = "Create Entity";
		dialog.validator = (entity, callback) => Validator.assertCreateEntity(this._store.id, entity, callback);

		dialog.setHandler("import", entity => {
			Workspace.importStoreEntity(this._store.id, entity, entity => {
				this._entitiesView.addItem(entity);
			});
		}, { autoClose: true });

		const entity = {
			className: `${this._store.packageName}.`
		};
		dialog.edit(entity, entity => {
			Database.createStoreEntity(this._store.id, entity, entity => {
				this._entitiesView.addItem(entity);
			});
		});
	}

	_onBuildProject() {
		Workspace.buildProject(this._store.id, result => alert(JSON.stringify(result)));
	}
};

WinMain.createPage(StorePage);
