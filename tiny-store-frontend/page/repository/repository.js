RepositoryPage = class extends Page {
    constructor() {
        super();

        this._servicesView = document.getElementById("services-list");
        this._servicesView.addEventListener("select", this._onRepositorySelect.bind(this));

        const repositoryId = location.search.substring(1);
        WorkspaceService.getRepository(repositoryId, this._onRepositoryLoaded.bind(this));
        WorkspaceService.getRepositoryServices(repositoryId, services => this._servicesView.setItems(services));

        this._menu("edit-repository", this._onEditRepository, this);

        this._menu("create-service", this._onCreateService, this);
        this._menu("edit-service", this._onEditService, this);
        this._menu("delete-service", this._onDeleteService, this);
    }

    _onRepositoryLoaded(repository) {
        this._repository = repository;
        this._setObject(repository);
    }

    _onRepositorySelect(event) {
        this._show("create-service", !event.detail.selected);
        this._show("edit-service", event.detail.selected);
        this._show("delete-service", event.detail.selected);
    }

    _onEditRepository() {
        const dialog = document.getElementById("repository-form");
        dialog.setTitle("Edit Repository");
        dialog.setHandler("test", this._onTestRepository.bind(this));

        dialog.edit(this._repository, repository => {
            WorkspaceService.saveRepository(repository, () => this._setObject(repository));
        });
    }

    _onTestRepository(repository) {
        WorkspaceService.testDataSource(repository, success => alert(success));
    }

    _onCreateService() {
        const dialog = document.getElementById("service-form");
        dialog.setTitle("Create Service");

        dialog.open(service => {
            WorkspaceService.createService(this._repository.id, service, () => this._servicesView.addItem(service));
        });
    }

    _onEditService() {
        const dialog = document.getElementById("service-form");
        dialog.setTitle("Edit Service");

        dialog.edit(this._servicesView.getSelectedItem(), service => {
            WorkspaceService.saveService(service, () => this._servicesView.setSelectedItem(service));
        });
    }

    _onDeleteService() {
        const dialog = document.getElementById("service-delete");
        dialog.open(() => {
            const service = this._servicesView.getSelectedItem();
            WorkspaceService.deleteService(service.id, () => this._servicesView.deleteSelectedRow());
        });
    }
};

WinMain.createPage(RepositoryPage);
