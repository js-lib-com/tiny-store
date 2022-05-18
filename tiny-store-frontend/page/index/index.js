IndexPage = class extends Page {
	constructor() {
		super();

		this._storesView = document.getElementById("stores-list");
		this._storesView.addEventListener("select", this._onStoreItemSelect.bind(this));

		this._storeForm = document.getElementById("store-form");

		this._menu("create-store", this._onCreateStore, this);
		this._menu("edit-store", this._onEditStore, this);
		this._menu("delete-store", this._onDeleteStore, this);

		WorkspaceService.getStores(stores => this._storesView.setItems(stores));
	}

	_onStoreItemSelect(event) {
		this._show("create-store", !event.detail.selected);
		this._show("edit-store", event.detail.selected);
		this._show("delete-store", event.detail.selected);
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
