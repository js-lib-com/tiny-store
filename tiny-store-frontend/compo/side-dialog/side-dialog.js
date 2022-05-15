(function () {

    class SideDialog extends HTMLElement {
        constructor() {
            super();

            this._titleView = this.getElementsByTagName("h2")[0];
            this._form = this.getElementsByTagName("object-form")[0];

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

        setTitle(title) {
            this._titleView.textContent = title;
        }

        setHandler(buttonName, handler) {
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
            this._form.setObject(object);
        }

        _onPositiveButton() {
            if (this._form) {
                if (!this._form.isValid()) {
                    return;
                }
                this._callback(this._form.getObject(this._object));
            }
            else {
                this._callback();
            }
            this._onNegativeButton();
        }

        _onNegativeButton() {
            this.classList.add("hidden");
        }
    }

    customElements.define("side-dialog", SideDialog);

})();
