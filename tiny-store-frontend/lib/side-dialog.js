class SideDialog extends HTMLElement {
    constructor() {
        super();

        this._validator = null;
        this._alert = null;
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

    show() {
        this.classList.remove("hidden");
    }

    close() {
        this._validator = null;
        this._autoClose = false;
        this.classList.add("hidden");
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

    /**
     * Set custom button handler for click event. Function should have a single argument,
     * namely the form data object.
     * 
     * Configuration object: 
     * - autoClose: boolean flag, when true close dialog after button handler execution.
     *  
     * @param {String} buttonName button name,
     * @param {Function} handler button click handler,
     * @param {Object} config configuration object.
     */
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
            this._getObject(object => {
                handler(object);
                if (this._autoClose) {
                    this.close();
                }
            });
        });
    }

    open(callback) {
        this._callback = callback;
        this._object = {};

        if (this._form) {
            this._form.reset();
        }
    }

    edit(object, callback) {
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
        this._getObject(object => {
            this._callback(object);
            this.close();
        });
    }

    _onNegativeButton() {
        this.close();
    }

    _getObject(callback) {
        if (this._form && !this._form.isValid()) {
            return;
        }

        function invoker(callback) {
            if (this._form) {
                callback(this._form.getObject(this._object));
            }
            else {
                callback();
            }
        }

        if (!this._validator) {
            invoker.call(this, callback);
            return;
        }

        const parameters = [];
        if (this._form) {
            parameters.push(this._form.getObject());
        }
        parameters.push(fail => {
            if (fail) {
                this._alert(fail);
            }
            else {
                invoker.call(this, callback);
            }
        });

        this._validator.apply(this, parameters);
    }
}

customElements.define("side-dialog", SideDialog);
