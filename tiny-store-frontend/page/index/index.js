IndexPage = class extends Page {
	constructor() {
		super();

		this._storesView = document.getElementById("stores-list");
		this._storesView.addEventListener("select", this._onStoreSelect.bind(this));

		this._storeForm = document.getElementById("store-form");

        this._sideMenu = this.getSideMenu();
        this._sideMenu.setLink("store-page", () => `store.htm?${this._storesView.getSelectedId()}`);

		this._actionBar = this.getActionBar();
		this._actionBar.setHandler("create-store", this._onCreateStore);
		this._actionBar.setHandler("edit-store", this._onEditStore);
		this._actionBar.setHandler("delete-store", this._onDeleteStore);
		this._onStoreSelect({ detail: { selected: false } });

		WorkspaceService.getStores(stores => this._storesView.setItems(stores));
	}

	_onStoreSelect(event) {
		const selected = event.detail.selected;
        this._sideMenu.enable("store-page", selected);
		this._actionBar.show("create-store", !selected);
		this._actionBar.show("edit-store", selected);
		this._actionBar.show("delete-store", selected);
	}

	_onCreateStore() {
		const dialog = document.getElementById("store-form");
		dialog.setTitle("Create Store");
		dialog.setHandler("test", this._onTestStore.bind(this));

		const store = this._storesView.getSelectedItem();
		dialog.open(store => {
			WorkspaceService.createStore(store, stores => this._storesView.setItems(stores));
		});
	}

	_onEditStore() {
		const dialog = document.getElementById("store-form");
		dialog.setTitle("Edit Store");
		dialog.setHandler("test", this._onTestStore.bind(this));

		dialog.edit(this._storesView.getSelectedItem(), store => {
			WorkspaceService.saveStore(store, () => this._storesView.setSelectedItem(store));
		});
	}

	_onTestStore(store) {
		WorkspaceService.testDataSource(store, success => alert(success));
	}

	_onDeleteStore() {
		const dialog = document.getElementById("store-delete");
		dialog.open(() => {
			const store = this._storesView.getSelectedItem();
			WorkspaceService.deleteStore(store.id, () => this._storesView.deleteSelectedRow());
		});
	}
};

WinMain.createPage(IndexPage);
