IndexPage = class extends Page {
	constructor() {
		super();

		this._storesView = document.getElementById("stores-list");
		Workspace.getStores(stores => this._onModelLoaded(stores));
		this._storesView.addEventListener("select", this._onStoreSelect.bind(this));

		this._sideMenu = this.getSideMenu();
		this._sideMenu.setLink("store-page", () => `store.htm?${this._stores[this._storesView.selectedIndex].id}`);
		this._sideMenu.setLink("server-page", () => `server.htm`);

		this._actionBar = this.getActionBar("IndexPage");
		this._actionBar.setHandler("create-store", this._onCreateStore);
		this._actionBar.setHandler("edit-store", this._onEditStore);
		this._actionBar.setHandler("build-project", this._onBuildProject);
		this._actionBar.setHandler("commit-changes", this._onCommitChanges);
		this._actionBar.setHandler("push-changes", this._onPushChanges);
		this._onStoreSelect({ detail: { selected: false } });
	}

	_onModelLoaded(stores) {
		this._stores = this._storesView.setItems(stores);
	}

	_onStoreSelect(event) {
		const selected = event.detail.selected;
		this._sideMenu.enable("store-page", selected);
		this._actionBar.show("create-store", !selected);
		this._actionBar.show("edit-store", selected);
		this._actionBar.show("build-project", selected);
		this._actionBar.show("commit-changes", selected);
		this._actionBar.show("push-changes", selected);
	}

	_onCreateStore() {
		const dialog = this.getCompo("store-form");
		dialog.title = "Create Store";
		dialog.validator = (store, callback) => Validator.assertCreateStore(store, callback);
		dialog.setHandler("test", this._onTestStore.bind(this));

		dialog.open(store => {
			Workspace.createStore(store, store => this._stores.push(store));
		});
	}

	_onEditStore() {
		const dialog = this.getCompo("store-form");
		dialog.title = "Edit Store";
		dialog.setHandler("test", this._onTestStore.bind(this));

		const index = this._storesView.selectedIndex;
		dialog.edit(this._stores[index], store => {
			Workspace.updateStore(store, () => this._stores[index] = store);
		});
	}

	_onTestStore(store) {
		Workspace.testDataSource(store, result => this.alert(result));
	}

	_onBuildProject() {
		const storeId = this._stores[this._storesView.selectedIndex].id;
		Workspace.buildProject(storeId, result => this.alert(result));
	}

	_onCommitChanges() {
		const storeId = this._stores[this._storesView.selectedIndex].id;
		super._onCommitChanges(storeId);
	}

	_onPushChanges() {
		const storeId = this._stores[this._storesView.selectedIndex].id;
		super._onPushChanges(storeId);
	}
};

WinMain.createPage(IndexPage);
