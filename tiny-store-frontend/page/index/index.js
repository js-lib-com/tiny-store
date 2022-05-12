IndexPage = class extends Page {
	constructor() {
		super();

		this._storesListView = document.getElementById("stores-list-view");
		this._storesListView.addEventListener("select", this._onStoreItemSelect.bind(this));

		this._storeForm = document.getElementById("store-form");

		this._menu("create-store", this._onCreateStore, this);
		this._menu("edit-store", this._onEditStore, this);
		this._menu("delete-store", this._onDeleteStore, this);
		this._menu("save-store", this._onSaveStore, this);
		this._menu("cancel-store", this._onCancelStore, this);

		WorkspaceService.getStores(stores => this._storesListView.setItems(stores));
	}

	_onCreateStore(ev) {
		console.log(`${this}#_onCreateStore(ev)`);
		this._show("store-form-section", true);
	}

	_onEditStore(ev) {
		console.log(`${this}#_onEditStore(ev)`);
		this._show("store-form-section", true);
	}

	_onDeleteStore(ev) {
		console.log(`${this}#_onDeleteStore(ev)`);
		const store = this._storesListView.getSelectedItem();
		if (store != null) {
			WorkspaceService.deleteStore(store, stores => {
				this._storesListView.setItems(stores);
			});
		}
	}

	_onSaveStore(ev) {
		console.log(`${this}#_onSaveStore(ev)`);
		if (this._storeForm.isValid()) {
			const store = this._storeForm.getObject();
			console.log(store);
			WorkspaceService.createStore(store, stores => {
				this._storeForm.reset();
				this._storesListView.setItems(stores);
				this._show("store-form-section", false);
			});
		}
	}

	_onCancelStore(ev) {
		console.log(`${this}#_onCancelStore(ev)`);
		this._storeForm.reset();
		this._show("store-form-section", false);
	}

	_onStoreItemSelect(event) {
		console.log(`${this}#_onStoreItemSelect(event)`);
		this._showActions(event.detail.selected);
	}

	_showActions(show) {
		this._show("edit-store", show);
		this._show("delete-store", show);

	}

	// ------------------------------------------------------------------------

	_onCreateDataSource(ev) {
		console.log(`${this}#_onCreateDataSource(ev)`);
		this._dataSourceFormSection.show();
	}

	_onEditDataSource(ev) {
		console.log(`${this}#_onEditDataSource(ev)`);
		this._dataSourceFormSection.show();
	}

	_onDeleteDataSource(ev) {
		console.log(`${this}#_onDeleteDataSource(ev)`);
	}

	_onTestDataSource(ev) {
		console.log(`${this}#_onTestDataSource(ev)`);
		if (this._dataSourceForm.isValid()) {
			const dataSource = this._dataSourceForm.getObject();
			console.log(dataSource);
			WorkspaceService.testDataSource(dataSource, success => {
				alert(success)
			});
		}
	}

	_onSaveDataSource(ev) {
		console.log(`${this}#_onSaveDataSource(ev)`);
		if (this._dataSourceForm.isValid()) {
			const dataSource = this._dataSourceForm.getObject();
			console.log(dataSource);
			WorkspaceService.createDataSource(dataSource, dataSources => {
				this._dataSourceForm.reset();
				this._dataSourcesListView.setObject(dataSources);
				this._dataSourceFormSection.hide();
			});
		}
	}

	_onCancelDataSource(ev) {
		console.log(`${this}#_onCancelDataSource(ev)`);
		this._dataSourceForm.reset();
		this._dataSourceFormSection.hide();
	}

	_onDataSourceItemSelect(event) {
		console.log(`${this}#_onDataSourceItemSelect(event)`);
		this._showActions(event.detail.selected);
	}

	_showDataSourceActions(show) {
		//this._editDataSourceAction.show(show);
		//this._deleteDataSourceAction.show(show);
	}

	/**
	 * Class string representation.
	 * 
	 * @return {string} this class string representation.
	 */
	toString() {
		return "IndexPage";
	}
};

WinMain.createPage(IndexPage);
