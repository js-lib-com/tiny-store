(function () {

    class StoreDialog extends SideDialog {
        constructor() {
            super();

            const form = this.getElementsByTagName("object-form")[0];

            const nameInput = form.getElementByName("name");
            nameInput.addEventListener("change", this._onNameChanged.bind(this));

            const deploymentSelect = form.getElementByName("deploymentType");
            deploymentSelect.addEventListener("change", this._onDeploymentChange.bind(this));

            this._displayInput = form.getElementByName("display");
            this._packageNameInput = form.getElementByName("packageName");
            this._restPathInput = form.getElementByName("restPath");
            this._gitUrlInput = form.getElementByName("gitURL");
        }

        _onNameChanged(event) {
            function toTitleCase(name) {
                return name.split('-').map(word => word[0].toUpperCase() + word.substring(1)).join(' ');
            }

            this._displayInput.value = toTitleCase(event.target.value);
            this._packageNameInput.value = "com.jslib." + event.target.value.replace('-', '');
        }

        _onDeploymentChange(event) {
            switch (event.target.value) {
                case "EMBEDDED":
                    this._restPathInput.classList.toggle("hidden", true);
                    this._gitUrlInput.classList.toggle("hidden", true);
                    break;

                case "HOSTED":
                    this._restPathInput.classList.toggle("hidden", false);
                    this._gitUrlInput.classList.toggle("hidden", true);
                    break;

                default:
                    this._restPathInput.classList.toggle("hidden", false);
                    this._gitUrlInput.classList.toggle("hidden", false);
            }
        }
    }

    customElements.define("store-dialog", StoreDialog);

})();