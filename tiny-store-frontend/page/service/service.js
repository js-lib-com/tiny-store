ServicePage = class extends Page {
    constructor() {
        super();

        this._operationsView = document.getElementById("operations-list");
        this._operationsView.addEventListener("select", this._onOperationSelect.bind(this));

        const serviceId = location.search.substring(1);
        WorkspaceService.getService(serviceId, this._onServiceLoaded.bind(this));
        WorkspaceService.getServiceOperations(serviceId, operations => this._operationsView.setItems(operations));

        this._sideMenu = this.getSideMenu();
        this._sideMenu.setLink("store-page", () => `store.htm?${this._service.storeId}`);
        this._sideMenu.setLink("operation-page", () => `operation.htm?${this._operationsView.getSelectedId()}`);

        this._actionBar = this.getActionBar();
        this._actionBar.setHandler("edit-service", this._onEditService);
        this._actionBar.setHandler("create-operation", this._onCreateOperation);
        this._actionBar.setHandler("edit-operation", this._onEditOperation);
        this._actionBar.setHandler("delete-operation", this._onDeleteOperation);

        this._onOperationSelect({ detail: { selected: false } });
    }

    _onServiceLoaded(service) {
        this._service = service;
        this._storeId = service.storeId;
        this._setObject(service);
    }

    _onOperationSelect(event) {
        const selected = event.detail.selected;
        this._sideMenu.enable("operation-page", selected);
        this._actionBar.show("create-operation", !selected);
        this._actionBar.show("edit-operation", selected);
        this._actionBar.show("delete-operation", selected);
    }

    _onEditService() {
        const dialog = this.getCompo("service-form");
        dialog.setTitle("Edit Service");
        dialog.edit(this._service, service => {
            WorkspaceService.updateDataService(service, () => this._setObject(service));
        });
    }

    _onCreateOperation() {
        const dialog = this.getCompo("operation-form");
        dialog.setTitle("Create Operation");

        const operation = {
            restEnabled: this._service.restEnabled
        };

        dialog.edit(operation, operation => {
            WorkspaceService.createServiceOperation(this._service, operation, operation => {
                this._operationsView.addItem(operation);
            });
        });
    }

    _onEditOperation() {
        const dialog = this.getCompo("operation-form");
        dialog.setTitle("Edit Operation");

        dialog.edit(this._operationsView.getSelectedItem(), operation => {
            WorkspaceService.updateServiceOperation(operation, () => this._operationsView.setSelectedItem(operation));
        });
    }

    _onDeleteOperation() {
        const dialog = this.getCompo("operation-delete");
        dialog.open(() => {
            const operation = this._operationsView.getSelectedItem();
            WorkspaceService.deleteServiceOperation(operation, () => this._operationsView.deleteSelectedRow());
        });
    }
};

WinMain.createPage(ServicePage);
