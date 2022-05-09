Page = class extends js.ua.Page {
	constructor() {
		super();

		const sideMenu = document.getElementsByTagName("side-menu")[0];
		sideMenu.bind(this);
	}

	onServerFail(er) {
		$error("Page#onServerFail", "%s: %s", er.cause, er.message);
		js.ua.System.error("Server error. Please contact administrator.");
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
			element.textContent = object.__value__(propertyPath);
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

Object.prototype.__value__ = function (propertyPath) {
	const parts = propertyPath.split('.');
	let value = this;
	for (let i = 0; i < parts.length; ++i) {
		const property = value[parts[i]];
		value = typeof property == "function" ? property() : property;
	}
	return value;
};
