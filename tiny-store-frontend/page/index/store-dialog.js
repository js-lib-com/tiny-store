(function () {

    class StoreDialog extends SideDialog {
        constructor() {
            super();

            const form = this.getElementsByTagName("object-form")[0];

            this._displayInput = form.getElementByName("display");
            this._packageNameInput = form.getElementByName("packageName");
            this._restPathInput = form.getElementByName("restPath");
            this._gitUrlInput = form.getElementByName("gitURL");
            this._mavenServerInput = form.getElementByName("mavenServer");

            const nameInput = form.getElementByName("name");
            nameInput.addEventListener("change", this._onNameChanged.bind(this));

            const deploymentSelect = form.getElementByName("deploymentType");
            deploymentSelect.addEventListener("change", this._onDeploymentChange.bind(this));
            this._onDeploymentChange({ target: deploymentSelect });

        }

        open(callback) {
            super.open(callback);
            this._mavenServerInput.value = "https://maven.js-lib.com/";
        }

        _onNameChanged(event) {
            function toTitleCase(name) {
                return name.split('-').map(word => word[0].toUpperCase() + word.substring(1)).join(' ');
            }

            this._restPathInput.value = `/${event.target.value}/1.0/`;
            this._displayInput.value = toTitleCase(event.target.value);
            this._packageNameInput.value = `com.jslib.${event.target.value.replace('-', '')}`;
        }

        _onDeploymentChange(event) {
            switch (event.target.value) {
                case "EMBEDDED":
                    this._restPathInput.classList.add("hidden");
                    this._gitUrlInput.classList.add("hidden");
                    break;

                case "HOSTED":
                    this._restPathInput.classList.remove("hidden");
                    this._gitUrlInput.classList.add("hidden");
                    break;

                default:
                    this._restPathInput.classList.remove("hidden");
                    this._gitUrlInput.classList.remove("hidden");
            }
        }
    }

    customElements.define("store-dialog", StoreDialog);

})();