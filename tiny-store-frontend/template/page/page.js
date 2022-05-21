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
		this._sideMenu = document.getElementsByTagName("side-menu")[0];
		this._actionBar = document.getElementsByTagName("action-bar")[0];

		const breadCrumbs = document.getElementsByTagName("bread-crumbs")[0];
		breadCrumbs.setPath(Page.PAGE_BREAD_CRUMBS[location.pathname]);
	}

	getSideMenu() {
		return this._sideMenu;
	}

	getActionBar() {
		this._actionBar.setHandlersScope(this);
		return this._actionBar;
	}

	_setObject(object) {
		const pageView = document.getElementById("page-view");
		pageView.setObject(object);
	}

	_show(id, show) {
		const element = document.getElementById(id);
		if (!element) {
			throw `Missing element with id ${id}.`;
		}
		element.classList[show ? "remove" : "add"]("hidden");
	}
};

WinMain = {
	createPage(pageClass) {
		document.addEventListener("DOMContentLoaded", event => this._page = new pageClass());
	}
}