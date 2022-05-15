OperationPage = class extends Page {
    constructor() {
        super();

        this._parametersListView = document.getElementById("parameters-list");
        this._parametersListView.addEventListener("select", this._onParameterSelect.bind(this));

        this._exceptionsListView = document.getElementById("exceptions-list");
        this._exceptionsListView.addEventListener("select", this._onExceptionSelect.bind(this));

        const operationId = location.search.substring(1);
        WorkspaceService.getOperation(operationId, this._onOperationLoaded, this);

        this._menu("edit-operation", this._onEditOperation, this);

        this._menu("add-parameter", this._onAddParameter, this);
        this._menu("edit-parameter", this._onEditParameter, this);
        this._menu("delete-parameter", this._onDeleteParameter, this);

        this._menu("add-exception", this._onAddException, this);
        this._menu("edit-exception", this._onEditException, this);
        this._menu("delete-exception", this._onDeleteException, this);
    }

    _onOperationLoaded(operation) {
        this._operation = operation;
        this._setObject(operation);
    }

    _onEditOperation() {
        const dialog = document.getElementById("operation-form");
        dialog.edit(this._operation, () => {
            this._setObject(this._operation);
            this._saveOperation();
        });
    }

    // --------------------------------------------------------------------

    _onParameterSelect(event) {
        this._show("add-parameter", !event.detail.selected);
        this._show("edit-parameter", event.detail.selected);
        this._show("delete-parameter", event.detail.selected);
    }

    _onAddParameter() {
        const dialog = document.getElementById("parameter-form");
        dialog.setTitle("Create Parameter");
        dialog.open(parameter => {
            this._operation.parameters.push(parameter);
            this._parametersListView.addItem(parameter);
            this._saveOperation();
        });
    }

    _onEditParameter() {
        const dialog = document.getElementById("parameter-form");
        dialog.setTitle("Edit Parameter");
        dialog.edit(this._parametersListView.getSelectedItem(), parameter => {
            this._operation.parameters[this._parametersListView.getSelectedIndex()] = parameter;
            this._parametersListView.setSelectedItem(parameter);
            this._saveOperation();
        });
    }

    _onDeleteParameter() {
        const dialog = document.getElementById("parameter-delete");
        dialog.open(() => {
            this._operation.parameters.splice(this._parametersListView.getSelectedIndex(), 1);
            this._parametersListView.deleteSelectedRow();
            this._saveOperation();
        });
    }

    // --------------------------------------------------------------------

    _onExceptionSelect(event) {
        this._show("add-exception", !event.detail.selected);
        this._show("edit-exception", event.detail.selected);
        this._show("delete-exception", event.detail.selected);
    }

    _onAddException() {
        const dialog = document.getElementById("exception-form");
        dialog.setTitle("Create Exception");
        dialog.open(exception => {
            this._operation.exceptions.push(exception);
            this._exceptionsListView.addItem(exception);
            this._saveOperation();
        });
    }

    _onEditException() {
        const dialog = document.getElementById("exception-form");
        dialog.setTitle("Edit Exception");
        dialog.edit(this._exceptionsListView.getSelectedItem(), exception => {
            this._operation.exceptions[this._exceptionsListView.getSelectedIndex()] = exception;
            this._exceptionsListView.setSelectedItem(exception);
            this._saveOperation();
        });
    }

    _onDeleteException() {
        const dialog = document.getElementById("exception-delete");
        dialog.open(() => {
            this._operation.exceptions.splice(this._exceptionsListView.getSelectedIndex(), 1);
            this._exceptionsListView.deleteSelectedRow();
            this._saveOperation();
        });
    }

    _saveOperation() {
        WorkspaceService.saveOperation(this._operation);
    }

    toString() {
        return "OperationPage";
    }
};

WinMain.createPage(OperationPage);
