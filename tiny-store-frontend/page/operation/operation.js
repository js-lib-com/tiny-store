(function () {

    OperationPage = class extends Page {
        constructor() {
            super();

            this._parametersListView = document.getElementById("parameters-list");

            const methodQualifiedName = location.search.substring(1).split('-');
            const serviceInterface = methodQualifiedName[0];
            const methodName = methodQualifiedName[1];
            WorkspaceService.getOperation(serviceInterface, methodName, this._onOperationLoaded, this);
        }

        _onOperationLoaded(operation) {
            this._operation = operation;
            const pageCaption = document.getElementById("page-caption");
            this._inject(pageCaption, operation);
            this._parametersListView.setItems(operation.parameters);
        }

        toString() {
            return "OperationPage";
        }
    };

    WinMain.createPage(OperationPage);

})();