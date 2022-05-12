class Page {
	constructor() {
		const sideMenu = document.getElementsByTagName("side-menu")[0];
		sideMenu.bind(this);
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

	_inject(element, object) {
		const propertyPath = element.getAttribute("data-text");
		if (propertyPath) {
			element.textContent = OPP.get(object, propertyPath);
			return;
		}
		let childElement = element.firstElementChild;
		while (childElement) {
			this._inject(childElement, object);
			childElement = childElement.nextElementSibling;
		}
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