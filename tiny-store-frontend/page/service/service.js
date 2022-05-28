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
        const dialog = document.getElementById("service-form");
        dialog.setTitle("Edit Service");
        dialog.edit(this._service, service => {
            WorkspaceService.saveService(service, () => this._setObject(service));
        });
    }

    _onCreateOperation() {
        const dialog = document.getElementById("operation-form");
        dialog.setTitle("Create Operation");

        dialog.open(operation => {
            WorkspaceService.createOperation(this._service.id, operation, operation => {
                this._operationsView.addItem(operation);
            });
        });
    }

    _onEditOperation() {
        const dialog = document.getElementById("operation-form");
        dialog.setTitle("Edit Operation");

        dialog.edit(this._operationsView.getSelectedItem(), operation => {
            WorkspaceService.saveOperation(operation, () => this._operationsView.setSelectedItem(operation));
        });
    }

    _onDeleteOperation() {
        const dialog = document.getElementById("operation-delete");
        dialog.open(() => {
            const operation = this._operationsView.getSelectedItem();
            WorkspaceService.deleteOperation(operation.id, () => this._operationsView.deleteSelectedRow());
        });
    }
};

WinMain.createPage(ServicePage);
