IndexPage = class extends Page {
	constructor() {
		super();

		this._storesListView = document.getElementById("stores-list");
		this._storesListView.addEventListener("select", this._onStoreItemSelect.bind(this));

		this._storeForm = document.getElementById("store-form");

		this._menu("create-store", this._onCreateStore, this);
		this._menu("edit-store", this._onEditStore, this);
		this._menu("delete-store", this._onDeleteStore, this);

		WorkspaceService.getStores(stores => this._storesListView.setItems(stores));
	}

	_onStoreItemSelect(event) {
		this._show("create-store", !event.detail.selected);
		this._show("edit-store", event.detail.selected);
		this._show("delete-store", event.detail.selected);
	}

	_onCreateStore(event) {
		const dialog = document.getElementById("store-form");
		dialog.setTitle("Create Store");

		const store = this._storesListView.getSelectedItem();
		dialog.open(store => {
			WorkspaceService.createStore(store, stores => this._storesListView.setItems(stores));
		});
	}

	_onEditStore(event) {
		const dialog = document.getElementById("store-form");
		dialog.setTitle("Edit Store");

		dialog.edit(this._storesListView.getSelectedItem(), store => {
			WorkspaceService.saveStore(store, () => this._storesListView.setSelectedItem(store));
		});
	}

	_onDeleteStore(event) {
		const dialog = document.getElementById("store-delete");
		const store = this._storesListView.getSelectedItem();
		dialog.open(() => WorkspaceService.deleteStore(store, () => this._storesListView.deleteSelectedRow()));
	}
};

WinMain.createPage(IndexPage);
