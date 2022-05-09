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
        const pageCaption = document.getElementById("page-caption");
        this._inject(pageCaption, repository);
    }

    toString() {
        return "RepositoryPage";
    }
};

WinMain.createPage(RepositoryPage);