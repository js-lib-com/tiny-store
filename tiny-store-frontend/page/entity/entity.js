EntityPage = class extends Page {
    constructor() {
        super();

        this._fieldsListView = document.getElementById("fields-list");

        const className = location.search.substring(1);
        WorkspaceService.getEntity(className, this._onEntityLoaded.bind(this));
    }

    _onEntityLoaded(entity) {
        this._entity = entity;
        this._setObject(entity);
        this._fieldsListView.addItem(entity.identity);
        this._fieldsListView.addItems(this._entity.fields);
    }

    toString() {
        return "EntityPage";
    }
};

WinMain.createPage(EntityPage);