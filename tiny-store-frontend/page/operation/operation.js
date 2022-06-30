OperationPage = class extends Page {
    constructor() {
        super();

        const operationId = location.search.substring(1);
        Database.getServiceOperation(operationId, this._onOperationLoaded, this);

        this._parametersListView = document.getElementById("parameters-list");
        this._parametersListView.addEventListener("select", this._onParameterSelect.bind(this));

        this._exceptionsListView = document.getElementById("exceptions-list");
        this._exceptionsListView.addEventListener("select", this._onExceptionSelect.bind(this));

        const sideMenu = this.getSideMenu();
        sideMenu.setLink("service-page", () => `service.htm?${this._operation.serviceId}`);

        this._actionBar = this.getActionBar("OperationPage");
        this._actionBar.setHandler("edit-operation", this._onEditOperation);
        this._actionBar.setHandler("delete-operation", this._onDeleteOperation);

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
        this._operation = this.setModel(operation);
        Workspace.getTypeOptionsByService(this._operation.serviceId, optionsMeta => this.loadTypeOptions(optionsMeta));
    }

    _onEditOperation() {
        const dialog = this.getCompo("operation-form");
        dialog.validator = (operation, callback) => {
            Validator.assertEditOperation(this._operation, operation, callback);
        };
        dialog.edit(this._operation, () => Database.updateServiceOperation(this._operation));
    }

    _onDeleteOperation() {
        const dialog = this.getCompo("operation-delete");
        dialog.open(() => {
            Database.deleteServiceOperation(this._operation, () => location.assign(`service.htm?${this._operation.serviceId}`));
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
        dialog.title = "Create Parameter";
        dialog.validator = (parameter, callback) => Validator.assertCreateParameter(this._operation, parameter, callback);

        const parameter = {
            //restEnabled: this._operation.restEnabled
        };

        dialog.edit(parameter, parameter => {
            this._operation.parameters.push(parameter);
            Database.updateServiceOperation(this._operation);
        });
    }

    _onEditParameter() {
        const dialog = this.getCompo("parameter-form");
        dialog.title = "Edit Parameter";
        dialog.validator = (parameter, callback) => {
            Validator.assertEditParameter(this._operation, this._parametersListView.getSelectedIndex(), parameter, callback);
        };

        const index = this._parametersListView.getSelectedIndex();
        dialog.edit(this._operation.parameters[index], parameter => {
            this._operation.parameters[index] = parameter;
            Database.updateServiceOperation(this._operation);
        });
    }

    _onDeleteParameter() {
        const dialog = this.getCompo("parameter-delete");
        const index = this._parametersListView.getSelectedIndex();
        dialog.open(() => {
            this._operation.parameters.splice(index, 1);
            Database.updateServiceOperation(this._operation);
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
        dialog.title = "Create Exception";
        dialog.open(exception => {
            this._operation.exceptions.push(exception);
            Database.updateServiceOperation(this._operation);
        });
    }

    _onEditException() {
        const dialog = this.getCompo("exception-form");
        dialog.title = "Edit Exception";
        const index = this._exceptionsListView.getSelectedIndex();
        dialog.edit(this._operation.exceptions[index], exception => {
            this._operation.exceptions[index] = exception;
            Database.updateServiceOperation(this._operation);
        });
    }

    _onDeleteException() {
        const dialog = this.getCompo("exception-delete");
        const index = this._exceptionsListView.getSelectedIndex();
        dialog.open(() => {
            this._operation.exceptions.splice(index, 1);
            Database.updateServiceOperation(this._operation);
        });
    }

    toString() {
        return "OperationPage";
    }
};

WinMain.createPage(OperationPage);
