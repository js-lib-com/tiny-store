(function () {

    OperationPage = class extends Page {
        constructor() {
            super();

            this._parametersListView = document.getElementById("parameters-list");
            this._parametersListView.addEventListener("select", this._onParameterSelect.bind(this));

            this._exceptionsListView = document.getElementById("exceptions-list");
            this._exceptionsListView.addEventListener("select", this._onExceptionSelect.bind(this));

            const methodQualifiedName = location.search.substring(1).split('-');
            const serviceInterface = methodQualifiedName[0];
            const methodName = methodQualifiedName[1];
            WorkspaceService.getOperation(serviceInterface, methodName, this._onOperationLoaded, this);

            this._menu("add-parameter", this._onAddParameter, this);
            this._menu("edit-parameter", this._onEditParameter, this);
            this._menu("delete-parameter", this._onDeleteParameter, this);

            this._menu("add-exception", this._onAddException, this);
            this._menu("edit-exception", this._onEditException, this);
            this._menu("delete-exception", this._onDeleteException, this);
        }

        _onOperationLoaded(operation) {
            this._operation = operation;
            const pageCaption = document.getElementById("page-caption");
            this._inject(pageCaption, operation);
            this._parametersListView.setItems(operation.parameters);
            this._exceptionsListView.setItems(operation.exceptions);
        }

        // --------------------------------------------------------------------

        _onParameterSelect(event) {
            this._show("add-parameter", !event.detail.selected);
            this._show("edit-parameter", event.detail.selected);
            this._show("delete-parameter", event.detail.selected);
        }

        _onAddParameter(event) {
            const dialog = document.getElementById("parameter-form");
            dialog.setTitle("Create Parameter");
            dialog.open(parameter => {
                this._operation.parameters.push(parameter);
                this._parametersListView.addItem(parameter);
                this._saveOperation();
            });
        }

        _onEditParameter(event) {
            const dialog = document.getElementById("parameter-form");
            dialog.setTitle("Edit Parameter");
            dialog.edit(this._parametersListView.getSelectedItem(), parameter => {
                this._operation.parameters[this._parametersListView.getSelectedIndex()] = parameter;
                this._parametersListView.setSelectedItem(parameter);
                this._saveOperation();
            });
        }

        _onDeleteParameter(event) {
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

        _onAddException(event) {
            const dialog = document.getElementById("exception-form");
            dialog.setTitle("Create Exception");
            dialog.open(exception => {
                this._operation.exceptions.push(exception);
                this._exceptionsListView.addItem(exception);
                this._saveOperation();
            });
        }

        _onEditException(event) {
            const dialog = document.getElementById("exception-form");
            dialog.setTitle("Edit Exception");
            dialog.edit(this._exceptionsListView.getSelectedItem(), exception => {
                this._operation.exceptions[this._exceptionsListView.getSelectedIndex()] = exception;
                this._exceptionsListView.setSelectedItem(exception);
                this._saveOperation();
            });
        }

        _onDeleteException(event) {
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

})();