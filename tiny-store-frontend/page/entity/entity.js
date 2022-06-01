EntityPage = class extends Page {
    constructor() {
        super();

        this._fieldsView = document.getElementById("fields-list");
        this._fieldsView.addEventListener("select", this._onFieldSelect.bind(this));

        const className = location.search.substring(1);
        WorkspaceService.getEntity(className, this._onEntityLoaded.bind(this));

        const sideMenu = this.getSideMenu();
        sideMenu.setLink("store-page", () => `store.htm?${this._entity.storeId}`);

        this._actionBar = this.getActionBar();
        this._actionBar.setHandler("edit-entity", this._onEditEntity);

        this._actionBar.setHandler("create-field", this._onCreateField);
        this._actionBar.setHandler("edit-field", this._onEditField);
        this._actionBar.setHandler("delete-field", this._onDeleteField);
        this._onFieldSelect({ detail: { selected: false } });
    }

    _onEntityLoaded(entity) {
        this._entity = entity;
        this._storeId = entity.storeId;
        this._setObject(entity);
        this._fieldsView.addItems(this._entity.fields);
    }

    _onFieldSelect(event) {
        const show = event.detail.selected;
        this._actionBar.show("create-field", !show);
        this._actionBar.show("edit-field", show);
        this._actionBar.show("delete-field", show);
    }

    _onEditEntity() {
        const dialog = document.getElementById("entity-form");
        dialog.setTitle("Edit Entity");
        dialog.edit(this._entity, entity => {
            WorkspaceService.updateStoreEntity(entity, () => this._setObject(entity));
        });
    }

    _onCreateField() {
        const dialog = document.getElementById("field-form");
        dialog.setTitle("Create Field");

        dialog.open(field => {
            this._entity.fields.push(field);
            WorkspaceService.updateStoreEntity(this._entity, () => this._fieldsView.addItem(field));
        });
    }

    _onEditField() {
        const dialog = document.getElementById("field-form");
        dialog.setTitle("Edit Field");

        dialog.edit(this._fieldsView.getSelectedItem(), field => {
            this._entity.fields[this._fieldsView.getSelectedIndex()] = field;
            WorkspaceService.updateStoreEntity(this._entity, () => this._fieldsView.setSelectedItem(field));
        });
    }

    _onDeleteField() {
        const dialog = document.getElementById("field-delete");
        dialog.open(() => {
            this._entity.fields.splice(this._fieldsView.getSelectedIndex(), 1);
            WorkspaceService.updateStoreEntity(this._entity, () => this._fieldsView.deleteSelectedRow());
        });
    }
};

WinMain.createPage(EntityPage);