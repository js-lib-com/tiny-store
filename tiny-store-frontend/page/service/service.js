ServicePage = class extends Page {
    constructor() {
        super();

        const operationsView = document.getElementById("operations-list");
        operationsView.addEventListener("select", this._onOperationSelect.bind(this));

        const serviceId = location.search.substring(1);
        Database.getDataService(serviceId, this._onServiceLoaded.bind(this));
        Database.getServiceOperations(serviceId, operations => operationsView.setItems(operations));

        this._sideMenu = this.getSideMenu();
        this._sideMenu.setLink("store-page", () => `store.htm?${this._service.storeId}`);
        this._sideMenu.setLink("operation-page", () => `operation.htm?${operationsView.getSelectedId()}`);

        this._actionBar = this.getActionBar("ServicePage");
        this._actionBar.setHandler("edit-service", this._onEditService);
        this._actionBar.setHandler("delete-service", this._onDeleteService);
        this._actionBar.setHandler("create-operation", this._onCreateOperation);

        this._onOperationSelect({ detail: { selected: false } });
    }

    _onServiceLoaded(service) {
        this._storeId = service.storeId;
        this._service = this.setModel(service);
    }

    _onOperationSelect(event) {
        const selected = event.detail.selected;
        this._sideMenu.enable("operation-page", selected);
    }

    _onEditService() {
        const dialog = this.getCompo("service-form");
        dialog.title = "Edit Service";
        dialog.edit(this._service, service => {
            Database.updateDataService(service);
        });
    }

    _onDeleteService() {
        const dialog = this.getCompo("service-delete");
        dialog.open(() => {
            Database.deleteDataService(this._service, () => location.assign(`store.htm?${this._storeId}`));
        });
    }

    _onCreateOperation() {
        const dialog = this.getCompo("operation-form");
        dialog.title = "Create Operation";
        dialog.validator = (operation, callback) => Validator.assertCreateOperation(this._service, operation, callback);

        const operation = {
            restEnabled: this._service.restEnabled
        };

        dialog.edit(operation, operation => {
            Database.createServiceOperation(this._service, operation, operation => {
                location.assign(`operation.htm?${operation.id}`);
            });
        });
    }
};

WinMain.createPage(ServicePage);
