Index = function () {
	this.$super();

	this._entitiesListView = document.getElementById("entities-list-view");
	this._entitiesListView.addEventListener("select", this._onEntitySelect.bind(this));

	const storePackage = "ro.gnotis.omsx";
	WorkspaceService.getStoreEntities(storePackage, entities => this._entitiesListView.setItems(entities));
};

Index.prototype = {
	_onEntitySelect: function (event) {

	},

	toString: function () {
		return "Store";
	}
};
$extends(Index, Page);
