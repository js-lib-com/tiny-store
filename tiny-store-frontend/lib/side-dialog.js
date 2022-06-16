class SideDialog extends HTMLElement {
    constructor() {
        super();

        this._validator = null;
        this._alert = alert;

        this._autoClose = false;

        this._titleView = this.getElementsByTagName("h2")[0];
        this._form = this.getElementsByTagName("object-form")[0];
        this._objectView = this.getElementsByTagName("object-view")[0];

        let positiveButton = this.querySelector("[name=positive]");
        if (!positiveButton) {
            throw "Invalid side dialog. Missing positive button.";
        }
        positiveButton.addEventListener("click", this._onPositiveButton.bind(this));

        let negativeButton = this.querySelector("[name=negative]");
        if (!negativeButton) {
            throw "Invalid side dialog. Missing negative button.";
        }
        negativeButton.addEventListener("click", this._onNegativeButton.bind(this));

        this._registeredHandlers = [];
    }

    /**
     * @param {Function} alert system alert function.
     */
    set alert(alert) {
        this._alert = alert;
    }

    /**
     * @param {String} title
     */
    set title(title) {
        this._titleView.textContent = title;
    }

    /**
     * @param {Function} validator
     */
    set validator(validator) {
        this._validator = validator;
    }

    setTitle(title) {
        this._titleView.textContent = title;
    }

    setHandler(buttonName, handler, config) {
        this._autoClose = typeof config != "undefined" && config.autoClose;
        // ensure handler is registered only once
        if (this._registeredHandlers.indexOf(buttonName) !== -1) {
            return;
        }

        let button = this.querySelector(`[name=${buttonName}]`);
        if (!button) {
            throw `Invalid side dialog. Missing ${buttonName} button.`;
        }
        this._registeredHandlers.push(buttonName);

        button.addEventListener("click", event => {
            if (this._form.isValid()) {
                handler(this._form.getObject(this._object));
                if (this._autoClose) {
                    this._onNegativeButton();
                }
            }
        });
    }

    open(callback) {
        this.classList.remove("hidden");
        this._callback = callback;
        this._object = {};

        if (this._form) {
            this._form.reset();
        }
    }

    edit(object, callback) {
        this.classList.remove("hidden");
        this._callback = callback;
        this._object = object;

        if (!this._form) {
            throw "Invalid side dialog. Missing form.";
        }
        this._form.reset();
        this._form.setObject(object);

        if (this._objectView) {
            this._objectView.setObject(object);
        }
    }

    _onPositiveButton() {
        if (this._form) {
            if (!this._form.isValid()) {
                return;
            }
            if (this._validator) {
                this._validator(this._form.getObject(), fail => {
                    if (fail) {
                        this._alert(fail);
                        return;
                    }
                    this._callback(this._form.getObject(this._object));
                    this._onNegativeButton();
                });
                return;
            }
            else {
                this._callback(this._form.getObject(this._object));
            }
        }
        else {
            if (this._validator) {
                this._validator(fail => {
                    if (fail) {
                        alert(fail);
                        return;
                    }
                    this._callback();
                    this._onNegativeButton();
                });
                return;
            }
            this._callback();
        }
        this._onNegativeButton();
    }

    _onNegativeButton() {
        this._autoClose = false;
        this.classList.add("hidden");
    }
}

customElements.define("side-dialog", SideDialog);
