class Page {
	static PAGE_BREAD_CRUMBS = {
		"/index.htm": ["home"],
		"/store.htm": ["home", "store"],
		"/repository.htm": ["home", "store", "repository"],
		"/service.htm": ["home", "store", "repository", "service"],
		"/operation.htm": ["home", "store", "repository", "service", "operation"],
		"/entity.htm": ["home", "store", "entity"]
	};

	constructor() {
		Database.errorHandler = function (error) { this.alert(error.message); }.bind(this);

		this._sideMenu = document.getElementsByTagName("side-menu")[0];
		this._sideMenu.setAction("commit-changes", this._onCommitChanges, this);
		this._sideMenu.setAction("push-changes", this._onPushChanges, this);

		this._actionBar = document.getElementsByTagName("action-bar")[0];
		this._compoSelect = document.getElementsByTagName("compo-select")[0];

		const breadCrumbs = document.getElementsByTagName("bread-crumbs")[0];
		breadCrumbs.setPath(Page.PAGE_BREAD_CRUMBS[location.pathname]);
	}

	loadTypeOptions(options) {
		const typeSelects = document.querySelectorAll("select-edit.types");
		for (let i = 0; i < typeSelects.length; ++i) {
			typeSelects.item(i).load(options);
		}
	}

	getSideMenu() {
		return this._sideMenu;
	}

	getActionBar(scopeName) {
		this._actionBar.setHandlersScope(this, scopeName);
		return this._actionBar;
	}

	getCompo(compoName) {
		this._compoSelect.get("side-alert").close();
		const compo = this._compoSelect.get(compoName);
		compo.alert = this.alert.bind(this);
		return compo;
	}

	setModel(object) {
		const pageView = document.getElementById("object-view");
		return pageView.setObject(object);
	}

	_show(id, show) {
		const element = document.getElementById(id);
		if (!element) {
			throw `Missing element with id ${id}.`;
		}
		element.classList[show ? "remove" : "add"]("hidden");
	}

	_onCommitChanges(event) {
		const storeId = typeof event == "string" ? event : this._storeId;
		Database.getChangeLog(storeId, changeLog => {
			const dialog = this.getCompo("commit-form");
			dialog.edit(changeLog, commit => {
				Workspace.commitChanges(storeId, commit.message, result => this.alert(result));
			});
		});
	}

	_onPushChanges(event) {
		const storeId = typeof event == "string" ? event : this._storeId;
		const dialog = this.getCompo("push-confirm");
		dialog.open(() => {
			Workspace.pushChanges(storeId, result => this.alert(result));
		});
	}

	alert(message) {
		let title;
		if (arguments.length == 2) {
			title = arguments[0];
			message = arguments[1];
		}
		else {
			title = "System Alert";
		}

		if (typeof message != "string") {
			message = JSON.stringify(message);
		}
		this.getCompo("side-alert").title(title).message(message);
	}
};

WinMain = {
	createPage(pageClass) {
		document.addEventListener("DOMContentLoaded", event => this._page = new pageClass());
	}
}