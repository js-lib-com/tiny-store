IndexPage = class extends Page {
	constructor() {
		super();

		this._storesView = document.getElementById("stores-list");
		this._storesView.addEventListener("select", this._onStoreSelect.bind(this));

		this._sideMenu = this.getSideMenu();
		this._sideMenu.setLink("store-page", () => `store.htm?${this._storesView.getSelectedId()}`);

		this._actionBar = this.getActionBar();
		this._actionBar.setHandler("create-store", this._onCreateStore);
		this._actionBar.setHandler("edit-store", this._onEditStore);
		this._actionBar.setHandler("delete-store", this._onDeleteStore);
		this._actionBar.setHandler("build-project", this._onBuildProject);
		this._actionBar.setHandler("commit-changes", this._onCommitChanges);
		this._actionBar.setHandler("push-changes", this._onPushChanges);
		this._onStoreSelect({ detail: { selected: false } });

		WorkspaceService.getStores(stores => this._storesView.setItems(stores));
	}

	_onStoreSelect(event) {
		const selected = event.detail.selected;
		this._sideMenu.enable("store-page", selected);
		this._actionBar.show("create-store", !selected);
		this._actionBar.show("edit-store", selected);
		this._actionBar.show("delete-store", selected);
		this._actionBar.show("build-project", selected);
		this._actionBar.show("commit-changes", selected);
		this._actionBar.show("push-changes", selected);
	}

	_onCreateStore() {
		const dialog = document.getElementById("store-form");
		dialog.setTitle("Create Store");
		dialog.setHandler("test", this._onTestStore.bind(this));

		dialog.open(store => {
			WorkspaceService.createStore(store, store => this._storesView.addItem(store));
		});
	}

	_onEditStore() {
		const dialog = document.getElementById("store-form");
		dialog.setTitle("Edit Store");
		dialog.setHandler("test", this._onTestStore.bind(this));

		dialog.edit(this._storesView.getSelectedItem(), store => {
			WorkspaceService.updateStore(store, () => this._storesView.setSelectedItem(store));
		});
	}

	_onTestStore(store) {
		WorkspaceService.testDataSource(store, this._alert);
	}

	_onDeleteStore() {
		const dialog = document.getElementById("store-delete");
		dialog.open(() => {
			const store = this._storesView.getSelectedItem();
			WorkspaceService.deleteStore(store.id, () => this._storesView.deleteSelectedRow());
		});
	}

	_onBuildProject() {
		WorkspaceService.buildProject(this._storesView.getSelectedId(), this._alert);
	}

	_onCommitChanges() {
		super._onCommitChanges(this._storesView.getSelectedId());
	}

	_onPushChanges() {
		super._onPushChanges(this._storesView.getSelectedId());
	}
};

WinMain.createPage(IndexPage);
