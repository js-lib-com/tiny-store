ServicePage = class extends Page {
    constructor() {
        super();

        this._operationsListView = document.getElementById("operations-list");

        const interfaceName = location.search.substring(1);
        WorkspaceService.getService(interfaceName, this._onServiceLoaded.bind(this));
        WorkspaceService.getOperations(interfaceName, operations => this._operationsListView.setItems(operations));
    }

    _onServiceLoaded(service) {
        this._service = service;
        const pageCaption = document.getElementById("page-caption");
        this._inject(pageCaption, service);
    }

    toString() {
        return "ServicePage";
    }
};

WinMain.createPage(ServicePage);