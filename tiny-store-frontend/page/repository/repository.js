RepositoryPage = class extends Page {
    constructor() {
        super();

        this._servicesView = document.getElementById("services-list");

        const repositoryId = location.search.substring(1);
        WorkspaceService.getRepository(repositoryId, this._onRepositoryLoaded.bind(this));
        WorkspaceService.getRepositoryServices(repositoryId, services => this._servicesView.setItems(services));
    }

    _onRepositoryLoaded(repository) {
        this._repository = repository;
        this._setObject(repository);
    }
};

WinMain.createPage(RepositoryPage);