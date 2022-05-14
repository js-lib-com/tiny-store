ServicePage = class extends Page {
    constructor() {
        super();

        this._operationsView = document.getElementById("operations-list");

        const serviceId = location.search.substring(1);
        WorkspaceService.getService(serviceId, this._onServiceLoaded.bind(this));
        WorkspaceService.getServiceOperations(serviceId, operations => this._operationsView.setItems(operations));
    }

    _onServiceLoaded(service) {
        this._service = service;
        this._setObject(service);
    }
};

WinMain.createPage(ServicePage);