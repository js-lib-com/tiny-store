OperationPage = class extends Page {
    constructor() {
        super();

        this._parametersListView = document.getElementById("parameters-list");
        this._parametersListView.addEventListener("select", this._onParameterSelect.bind(this));

        this._exceptionsListView = document.getElementById("exceptions-list");
        this._exceptionsListView.addEventListener("select", this._onExceptionSelect.bind(this));

        const operationId = location.search.substring(1);
        WorkspaceService.getOperation(operationId, this._onOperationLoaded, this);

        const sideMenu = this.getSideMenu();
        sideMenu.setLink("service-page", () => `service.htm?${this._operation.serviceId}`);

        this._actionBar = this.getActionBar("OperationPage");
        this._actionBar.setHandler("edit-operation", this._onEditOperation);

        this._actionBar.setHandler("add-parameter", this._onAddParameter);
        this._actionBar.setHandler("edit-parameter", this._onEditParameter);
        this._actionBar.setHandler("delete-parameter", this._onDeleteParameter);
        this._onParameterSelect({ detail: { selected: false } });

        this._actionBar.setHandler("add-exception", this._onAddException);
        this._actionBar.setHandler("edit-exception", this._onEditException);
        this._actionBar.setHandler("delete-exception", this._onDeleteException);
        this._onExceptionSelect({ detail: { selected: false } });
    }

    _onOperationLoaded(operation) {
        this._operation = operation;
        this._setObject(operation);
    }

    _onEditOperation() {
        const dialog = this.getCompo("operation-form");
        dialog.edit(this._operation, () => {
            this._setObject(this._operation);
            this._saveOperation();
        });
    }

    // --------------------------------------------------------------------

    _onParameterSelect(event) {
        const show = event.detail.selected;
        this._actionBar.show("add-parameter", !show);
        this._actionBar.show("edit-parameter", show);
        this._actionBar.show("delete-parameter", show);
    }

    _onAddParameter() {
        const dialog = this.getCompo("parameter-form");
        dialog.setTitle("Create Parameter");

        const parameter = {
            restEnabled: this._operation.restEnabled
        };

        dialog.edit(parameter, parameter => {
            this._operation.parameters.push(parameter);
            this._parametersListView.addItem(parameter);
            this._saveOperation();
        });
    }

    _onEditParameter() {
        const dialog = this.getCompo("parameter-form");
        dialog.setTitle("Edit Parameter");
        dialog.edit(this._parametersListView.getSelectedItem(), parameter => {
            this._operation.parameters[this._parametersListView.getSelectedIndex()] = parameter;
            this._parametersListView.setSelectedItem(parameter);
            this._saveOperation();
        });
    }

    _onDeleteParameter() {
        const dialog = this.getCompo("parameter-delete");
        dialog.open(() => {
            this._operation.parameters.splice(this._parametersListView.getSelectedIndex(), 1);
            this._parametersListView.deleteSelectedRow();
            this._saveOperation();
        });
    }

    // --------------------------------------------------------------------

    _onExceptionSelect(event) {
        const show = event.detail.selected;
        this._actionBar.show("add-exception", !show);
        this._actionBar.show("edit-exception", show);
        this._actionBar.show("delete-exception", show);
    }

    _onAddException() {
        const dialog = this.getCompo("exception-form");
        dialog.setTitle("Create Exception");
        dialog.open(exception => {
            this._operation.exceptions.push(exception);
            this._exceptionsListView.addItem(exception);
            this._saveOperation();
        });
    }

    _onEditException() {
        const dialog = this.getCompo("exception-form");
        dialog.setTitle("Edit Exception");
        dialog.edit(this._exceptionsListView.getSelectedItem(), exception => {
            this._operation.exceptions[this._exceptionsListView.getSelectedIndex()] = exception;
            this._exceptionsListView.setSelectedItem(exception);
            this._saveOperation();
        });
    }

    _onDeleteException() {
        const dialog = this.getCompo("exception-delete");
        dialog.open(() => {
            this._operation.exceptions.splice(this._exceptionsListView.getSelectedIndex(), 1);
            this._exceptionsListView.deleteSelectedRow();
            this._saveOperation();
        });
    }

    _saveOperation() {
        WorkspaceService.updateServiceOperation(this._operation);
    }

    toString() {
        return "OperationPage";
    }
};

WinMain.createPage(OperationPage);
