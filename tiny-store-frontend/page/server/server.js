ServerPage = class extends Page {
    constructor() {
        super();

        this._serversView = document.getElementById("servers-list");
        Database.getServers(servers => this._onModelLoaded(servers));
        this._serversView.addEventListener("select", this._onServerSelect.bind(this));

        this._sideMenu = this.getSideMenu();
        this._sideMenu.setLink("index-page", () => `index.htm`);

        this._actionBar = this.getActionBar("ServerPage");
        this._actionBar.setHandler("create-server", this._onCreateServer);
        this._actionBar.setHandler("edit-server", this._onEditServer);
        this._actionBar.setHandler("delete-server", this._onDeleteServer);

        this._onServerSelect({ detail: { selected: false } });
    }

    _onModelLoaded(servers) {
        this._servers = this._serversView.setItems(servers);
    }

    _onServerSelect(event) {
        const selected = event.detail.selected;
        this._actionBar.show("create-server", !selected);
        this._actionBar.show("edit-server", selected);
        this._actionBar.show("delete-server", selected);
    }

    _onCreateServer() {
        const dialog = this.getCompo("server-form");
        dialog.title = "Create External Server";

        dialog.open(server => {
            this._servers.push(server);
            Database.createServer(server);
        });
    }

    _onEditServer() {
        const dialog = this.getCompo("server-form");
        dialog.title = "Edit External Server";

        const index = this._serversView.selectedIndex;
        dialog.edit(this._servers[index], server => {
            this._servers[index] = server;
            Database.updateServer(server);
        });
    }

    _onDeleteServer() {
        const dialog = this.getCompo("server-delete");
        dialog.open(() => {
            const server = this._servers.splice(this._serversView.selectedIndex, 1)[0];
            Database.deleteServer(server);
        });
    }
};

WinMain.createPage(ServerPage);
