(function () {

    class SideDialog extends HTMLElement {
        constructor() {
            super();

            let elements = this.getElementsByName("positive");
            if (elements.length === 0) {
                throw "Invalid side dialog. Missing positive button.";
            }
            elements[0].addEventListener("click", this._onPositiveButton.bind(this));

            elements = this.getElementsByName("negative");
            if (elements.length === 0) {
                throw "Invalid side dialog. Missing negative button.";
            }
            elements[0].addEventListener("click", this._onNegativeButton.bind(this));
        }

        open(callback) {

        }

        _onPositiveButton() {
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
