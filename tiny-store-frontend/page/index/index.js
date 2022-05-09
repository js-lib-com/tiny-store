IndexPage = class extends Page {
	constructor() {
		super();

		this._storesListView = document.getElementById("stores-list-view");
		this._storesListView.addEventListener("select", this._onStoreItemSelect.bind(this));

		this._storeFormSection = this.getById("store-form-section");
		this._storeForm = this.getById("store-form");

		this._editStoreAction = this.getById("edit-store");
		this._deleteStoreAction = this.getById("delete-store");

		this.getById("create-store").on("click", this._onCreateStore, this);
		this._editStoreAction.on("click", this._onEditStore, this);
		this._deleteStoreAction.on("click", this._onDeleteStore, this);
		this.getById("save-store").on("click", this._onSaveStore, this);
		this.getById("cancel-store").on("click", this._onCancelStore, this);

		WorkspaceService.getStores(stores => this._storesListView.setItems(stores));
	}

	_onCreateStore(ev) {
		console.log(`${this}#_onCreateStore(ev)`);
		this._storeFormSection.show();
	}

	_onEditStore(ev) {
		console.log(`${this}#_onEditStore(ev)`);
		this._storeFormSection.show();
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
				this._storeFormSection.hide();
			});
		}
	}

	_onCancelStore(ev) {
		console.log(`${this}#_onCancelStore(ev)`);
		this._storeForm.reset();
		this._storeFormSection.hide();
	}

	_onStoreItemSelect(event) {
		console.log(`${this}#_onStoreItemSelect(event)`);
		this._showActions(event.detail.selected);
	}

	_showActions(show) {
		this._editStoreAction.show(show);
		this._deleteStoreAction.show(show);
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
