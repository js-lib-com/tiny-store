ServerPage = class extends Page {
    constructor() {
        super();

        this._serversView = document.getElementById("servers-list");
        this._serversView.addEventListener("select", this._onServerSelect.bind(this));

        Database.getServers(servers => this._serversView.setItems(servers));

        this._sideMenu = this.getSideMenu();
        this._sideMenu.setLink("index-page", () => `index.htm`);

        this._actionBar = this.getActionBar("ServerPage");
        this._actionBar.setHandler("create-server", this._onCreateServer);
        this._actionBar.setHandler("edit-server", this._onEditServer);
        this._actionBar.setHandler("delete-server", this._onDeleteServer);

        this._onServerSelect({ detail: { selected: false } });
    }

    _onModelLoaded(servers) {
        this._servers = this.getModelView("servers-list").setModel(servers);
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
            Database.createServer(server, server => this._servers.push(server));
        });
    }

    _onEditServer() {
        const dialog = this.getCompo("server-form");
        dialog.title = "Edit External Server";
        const index = this._serversView.getSelectedIndex();
        dialog.edit(this._servers[index], server => {
            this._servers[index] = server;
            Database.updateServer(server);
        });
    }

    _onDeleteServer() {
        const dialog = this.getCompo("server-delete");
        const index = this._serversView.getSelectedIndex();
        dialog.open(() => {
            const server = this._servers.splice(index, 1)[0];
            Database.deleteServer(server);
        });

        const server = this._serversView.getSelectedItem();
        dialog.open(() => {
            Database.deleteServer(server, () => location.assign(`store.htm?${this._storeId}`));
        });
    }
};

WinMain.createPage(ServerPage);
