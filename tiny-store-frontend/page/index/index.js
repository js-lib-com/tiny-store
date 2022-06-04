IndexPage = class extends Page {
	constructor() {
		super();

		this._storesView = document.getElementById("stores-list");
		this._storesView.addEventListener("select", this._onStoreSelect.bind(this));

		this._sideMenu = this.getSideMenu();
		this._sideMenu.setLink("store-page", () => `store.htm?${this._storesView.getSelectedId()}`);

		this._actionBar = this.getActionBar("IndexPage");
		this._actionBar.setHandler("create-store", this._onCreateStore);
		this._actionBar.setHandler("edit-store", this._onEditStore);
		this._actionBar.setHandler("build-project", this._onBuildProject);
		this._actionBar.setHandler("commit-changes", this._onCommitChanges);
		this._actionBar.setHandler("push-changes", this._onPushChanges);
		this._onStoreSelect({ detail: { selected: false } });

		Workspace.getStores(stores => this._storesView.setItems(stores));
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
		dialog.setTitle("Create Store");
		dialog.setHandler("test", this._onTestStore.bind(this));

		dialog.open(store => {
			Workspace.createStore(store, store => this._storesView.addItem(store));
		});
	}

	_onEditStore() {
		const dialog = this.getCompo("store-form");
		dialog.setTitle("Edit Store");
		dialog.setHandler("test", this._onTestStore.bind(this));

		dialog.edit(this._storesView.getSelectedItem(), store => {
			Workspace.updateStore(store, () => this._storesView.setSelectedItem(store));
		});
	}

	_onTestStore(store) {
		Workspace.testDataSource(store, this.alert);
	}

	_onBuildProject() {
		Workspace.buildProject(this._storesView.getSelectedId(), this.alert);
	}

	_onCommitChanges() {
		super._onCommitChanges(this._storesView.getSelectedId());
	}

	_onPushChanges() {
		super._onPushChanges(this._storesView.getSelectedId());
	}
};

WinMain.createPage(IndexPage);
