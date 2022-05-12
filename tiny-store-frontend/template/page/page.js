class Page {
	constructor() {
		const sideMenu = document.getElementsByTagName("side-menu")[0];
		sideMenu.bind(this);
	}

	_setObject(object) {
		const pageView = document.getElementById("page-view");
		pageView.setObject(object);
	}

	_menu(id, listener, scope) {
		const element = document.getElementById(id);
		if (!element) {
			throw `Missing element with id ${id}.`;
		}
		element.addEventListener("click", listener.bind(scope));
	}

	_show(id, show) {
		const element = document.getElementById(id);
		if (!element) {
			throw `Missing element with id ${id}.`;
		}
		element.classList[show ? "remove" : "add"]("hidden");
	}

	/**
	 * Class string representation.
	 * 
	 * @return {string} this class string representation.
	 */
	toString() {
		return "Page";
	}
};

WinMain = {
	createPage(pageClass) {
		document.addEventListener("DOMContentLoaded", event => this._page = new pageClass());
	}
}