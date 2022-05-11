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
        }

        setTitle(title) {
            this._titleView.textContent = title;
        }

        open(callback) {
            this.classList.remove("hidden");
            this._callback = callback;

            if (this._form) {
                this._form.reset();
            }
        }

        edit(object, callback) {
            this.classList.remove("hidden");
            this._callback = callback;

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
                this._callback(this._form.getObject());
            }
            else {
                this._callback();
            }
            this.classList.add("hidden");
        }

        _onNegativeButton() {
            this.classList.add("hidden");
        }

        toString() {
            return "SideDialog";
        }
    }

    customElements.define("side-dialog", SideDialog);

})();
