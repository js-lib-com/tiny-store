ServicePage = class extends Page {
    constructor() {
        super();

        this._operationsView = document.getElementById("operations-list");
        this._operationsView.addEventListener("select", this._onOperationSelect.bind(this));

        const serviceId = location.search.substring(1);
        WorkspaceService.getService(serviceId, this._onServiceLoaded.bind(this));
        WorkspaceService.getServiceOperations(serviceId, operations => this._operationsView.setItems(operations));

        this._menu("edit-service", this._onEditService, this);

        this._menu("create-operation", this._onCreateOperation, this);
        this._menu("edit-operation", this._onEditOperation, this);
        this._menu("delete-operation", this._onDeleteOperation, this);
    }

    _onServiceLoaded(service) {
        this._service = service;
        this._setObject(service);
    }

    _onOperationSelect(event) {
        this._show("create-operation", !event.detail.selected);
        this._show("edit-operation", event.detail.selected);
        this._show("delete-operation", event.detail.selected);
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
            WorkspaceService.createOperation(this._service.id, operation, () => this._operationsView.addItem(operation));
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
