RepositoryPage = class extends Page {
    constructor() {
        super();

        this._servicesListView = document.getElementById("services-list");

        const repositoryName = location.search.substring(1);
        WorkspaceService.getRepository(repositoryName, this._onRepositoryLoaded.bind(this));
        WorkspaceService.getServices(repositoryName, services => this._servicesListView.setItems(services));
    }

    _onRepositoryLoaded(repository) {
        this._repository = repository;
        this._setObject(repository);
    }

    toString() {
        return "RepositoryPage";
    }
};

WinMain.createPage(RepositoryPage);