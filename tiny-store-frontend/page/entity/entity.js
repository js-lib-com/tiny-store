EntityPage = class extends Page {
    constructor() {
        super();

        const className = location.search.substring(1);
        Database.getStoreEntity(className, this._onEntityLoaded.bind(this));

        this._fieldsView = document.getElementById("fields-list");
        this._fieldsView.addEventListener("select", this._onFieldSelect.bind(this));

        const sideMenu = this.getSideMenu();
        sideMenu.setLink("store-page", () => `store.htm?${this._entity.storeId}`);

        this._actionBar = this.getActionBar("EntityPage");
        this._actionBar.setHandler("edit-entity", this._onEditEntity);
        this._actionBar.setHandler("delete-entity", this._onDeleteEntity);

        this._actionBar.setHandler("create-field", this._onCreateField);
        this._actionBar.setHandler("edit-field", this._onEditField);
        this._actionBar.setHandler("delete-field", this._onDeleteField);
        this._onFieldSelect({ detail: { selected: false } });
    }

    _onEntityLoaded(entity) {
        this._storeId = entity.storeId;
        this._entity = this.setModel(entity);
    }

    _onFieldSelect(event) {
        const show = event.detail.selected;
        this._actionBar.show("create-field", !show);
        this._actionBar.show("edit-field", show);
        this._actionBar.show("delete-field", show);
    }

    _onEditEntity() {
        const dialog = this.getCompo("entity-form");
        dialog.title = "Edit Entity";
        dialog.validator = (entity, callback) => Validator.assertEditEntity(this._entity, entity, callback);

        dialog.setHandler("import", this._onImportEntity.bind(this), { autoClose: true });
        dialog.edit(this._entity, entity => Database.updateStoreEntity(entity));
    }

    _onImportEntity(entity) {
        Workspace.importStoreEntity(this._storeId, entity, entity => this._onEntityLoaded(entity));
    }

    _onDeleteEntity() {
        Validator.allowDeleteEntity(this._entity, fail => {
            if (fail) {
                this.alert("Delete Entity", `Cannot delete entity ${this._entity.className}.\r\n${fail}`);
                return;
            }

            const dialog = this.getCompo("entity-delete");
            dialog.open(() => {
                Database.deleteStoreEntity(this._entity, () => location.assign(`store.htm?${this._storeId}`));
            });
        });
    }

    _onCreateField() {
        const dialog = this.getCompo("field-form");
        dialog.title = "Create Field";
        dialog.validator = (field, callback) => Validator.assertCreateField(this._entity, field, callback);

        dialog.open(field => {
            this._entity.fields.push(field);
            Database.updateStoreEntity(this._entity);
        });
    }

    _onEditField() {
        const dialog = this.getCompo("field-form");
        dialog.title = "Edit Field";

        const index = this._fieldsView.selectedIndex;
        dialog.validator = (field, callback) => Validator.assertEditField(this._entity, index, field, callback);

        dialog.edit(this._entity.fields[index], field => {
            this._entity.fields[index] = field;
            Database.updateStoreEntity(this._entity);
        });
    }

    _onDeleteField() {
        const dialog = this.getCompo("field-delete");
        const index = this._fieldsView.selectedIndex;
        dialog.open(() => {
            this._entity.fields.splice(index, 1);
            Database.updateStoreEntity(this._entity);
        });
    }
};

WinMain.createPage(EntityPage);