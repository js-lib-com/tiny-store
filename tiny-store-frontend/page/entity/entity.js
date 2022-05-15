EntityPage = class extends Page {
    constructor() {
        super();

        this._fieldsView = document.getElementById("fields-list");
        this._fieldsView.addEventListener("select", this._onFieldSelect.bind(this));

        const className = location.search.substring(1);
        WorkspaceService.getEntity(className, this._onEntityLoaded.bind(this));

        this._menu("edit-entity", this._onEditEntity, this);

        this._menu("create-field", this._onCreateField, this);
        this._menu("edit-field", this._onEditField, this);
        this._menu("delete-field", this._onDeleteField, this);
    }

    _onEntityLoaded(entity) {
        this._entity = entity;
        this._setObject(entity);
        //this._fieldsView.addItem(entity.identity);
        this._fieldsView.addItems(this._entity.fields);
    }

    _onFieldSelect(event) {
        this._show("create-field", !event.detail.selected);
        this._show("edit-field", event.detail.selected);
        this._show("delete-field", event.detail.selected);
    }

    _onEditEntity() {
        const dialog = document.getElementById("entity-form");
        dialog.setTitle("Edit Entity");
        dialog.edit(this._entity, entity => {
            WorkspaceService.saveEntity(entity, () => this._setObject(entity));
        });
    }

    _onCreateField() {
        const dialog = document.getElementById("field-form");
        dialog.setTitle("Create Field");

        dialog.open(field => {
            this._entity.fields.push(field);
            WorkspaceService.saveEntity(this._entity, () => this._fieldsView.addItem(field));
        });
    }

    _onEditField() {
        const dialog = document.getElementById("field-form");
        dialog.setTitle("Edit Field");

        dialog.edit(this._fieldsView.getSelectedItem(), field => {
            this._entity.fields[this._fieldsView.getSelectedIndex()] = field;
            WorkspaceService.saveEntity(this._entity, () => this._fieldsView.setSelectedItem(field));
        });
    }

    _onDeleteField() {
        const dialog = document.getElementById("field-delete");
        dialog.open(() => {
            this._entity.fields.splice(this._fieldsView.getSelectedIndex(), 1);
            WorkspaceService.saveEntity(this._entity, () => this._fieldsView.deleteSelectedRow());
        });
    }
};

WinMain.createPage(EntityPage);