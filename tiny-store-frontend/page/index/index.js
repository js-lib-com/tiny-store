/**
 * Index page class.
 * 
 * @author Iulian Rotaru
 * @since 1.0
 * 
 * @constructor Construct an instance of home page class.
 */
Index = function () {
	this.$super();

	this._storesListView = document.getElementById("stores-list-view");
	this._storesListView.addEventListener("select", this._onStoreItemSelect.bind(this));

	this._storeFormSection = this.getById("store-form-section");
	this._storeForm = this.getById("store-form");

	this._defineStoreAction = this.getById("define-store");
	this._editStoreAction = this.getById("edit-store");
	this._deleteStoreAction = this.getById("delete-store");

	this._dataSourcesListView = document.getElementById("data-sources-list-view");
	this._dataSourcesListView.addEventListener("select", this._onDataSourceItemSelect, this);

	this._dataSourceFormSection = this.getById("data-source-form-section");
	this._dataSourceForm = this.getById("data-source-form");

	this.getById("store-tab").on("click", this._onStoreTab, this);
	this.getById("data-source-tab").on("click", this._onDataSourceTab, this);

	this.getById("create-store").on("click", this._onCreateStore, this);
	this._defineStoreAction.on("click", this._onDefineStore, this);
	this._editStoreAction.on("click", this._onEditStore, this);
	this._deleteStoreAction.on("click", this._onDeleteStore, this);
	this.getById("save-store").on("click", this._onSaveStore, this);
	this.getById("cancel-store").on("click", this._onCancelStore, this);

	this.getById("create-data-source").on("click", this._onCreateDataSource, this);
	this.getById("edit-data-source").on("click", this._onEditDataSource, this);
	this.getById("delete-data-source").on("click", this._onDeleteDataSource, this);
	this.getById("test-data-source").on("click", this._onTestDataSource, this);
	this.getById("save-data-source").on("click", this._onSaveDataSource, this);
	this.getById("cancel-data-source").on("click", this._onCancelDataSource, this);

	WorkspaceService.getDataSources(dataSources => this._dataSourcesListView.setItems(dataSources));
	WorkspaceService.getStores(stores => this._storesListView.setItems(stores));
};

Index.prototype = {
	_onStoreTab: function (ev) {
		console.log(`${this}#_onStoreTab(ev)`);
		this.findByName("store").removeCssClass("hidden");
		this.findByName("data-source").addCssClass("hidden");

		this.findByCss(".menu li").removeCssClass("selected");
		ev.target.addCssClass("selected");
	},

	_onDataSourceTab: function (ev) {
		console.log(`${this}#_onDataSourceTab(ev)`);
		this.findByName("store").addCssClass("hidden");
		this.findByName("data-source").removeCssClass("hidden");

		this.findByCss(".menu li").removeCssClass("selected");
		ev.target.addCssClass("selected");
	},

	// ------------------------------------------------------------------------

	_onCreateStore: function (ev) {
		console.log(`${this}#_onCreateStore(ev)`);
		this._storeFormSection.show();
	},

	_onDefineStore: function (ev) {
		console.log(`${this}#_onDefineStore(ev)`);
		location.assign("store.htm");
	},

	_onEditStore: function (ev) {
		console.log(`${this}#_onEditStore(ev)`);
		this._storeFormSection.show();
	},

	_onDeleteStore: function (ev) {
		console.log(`${this}#_onDeleteStore(ev)`);
		const store = this._storesListView.getSelectedItem();
		if (store != null) {
			WorkspaceService.deleteStore(store, stores => {
				this._storesListView.setItems(stores);
			});
		}
	},

	_onSaveStore: function (ev) {
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
	},

	_onCancelStore: function (ev) {
		console.log(`${this}#_onCancelStore(ev)`);
		this._storeForm.reset();
		this._storeFormSection.hide();
	},

	_onStoreItemSelect: function (event) {
		console.log(`${this}#_onStoreItemSelect(event)`);
		this._showActions(event.detail.selected);
	},

	_showActions: function (show) {
		this._defineStoreAction.show(show);
		this._editStoreAction.show(show);
		this._deleteStoreAction.show(show);
	},

	// ------------------------------------------------------------------------

	_onCreateDataSource: function (ev) {
		console.log(`${this}#_onCreateDataSource(ev)`);
		this._dataSourceFormSection.show();
	},

	_onEditDataSource: function (ev) {
		console.log(`${this}#_onEditDataSource(ev)`);
		this._dataSourceFormSection.show();
	},

	_onDeleteDataSource: function (ev) {
		console.log(`${this}#_onDeleteDataSource(ev)`);
	},

	_onTestDataSource: function (ev) {
		console.log(`${this}#_onTestDataSource(ev)`);
		if (this._dataSourceForm.isValid()) {
			const dataSource = this._dataSourceForm.getObject();
			console.log(dataSource);
			WorkspaceService.testDataSource(dataSource, success => {
				alert(success)
			});
		}
	},

	_onSaveDataSource: function (ev) {
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
	},

	_onCancelDataSource: function (ev) {
		console.log(`${this}#_onCancelDataSource(ev)`);
		this._dataSourceForm.reset();
		this._dataSourceFormSection.hide();
	},

	_onDataSourceItemSelect: function (event) {
		console.log(`${this}#_onDataSourceItemSelect(event)`);
		this._showActions(event.detail.selected);
	},

	_showDataSourceActions: function (show) {
		//this._editDataSourceAction.show(show);
		//this._deleteDataSourceAction.show(show);
	},

	/**
	 * Class string representation.
	 * 
	 * @return {string} this class string representation.
	 */
	toString: function () {
		return "Index";
	}
};
$extends(Index, Page);
