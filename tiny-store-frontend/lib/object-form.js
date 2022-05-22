(function () {

    class ObjectForm extends HTMLElement {
        constructor() {
            super();
            this._inputs = [];
            let input = this.firstElementChild;
            while (input) {
                if (input.hasAttribute("name")) {
                    this._inputs.push(input);
                    input.addEventListener("focus", event => event.target.classList.remove("invalid"));
                }
                input = input.nextElementSibling;
            }
        }

        setObject(object) {
            this._inputs.forEach(input => {
                input.classList.remove("invalid");
                const value = OPP.get(object, input.getAttribute("name"));
                if (value) {
                    input.value = value;
                }
            });
        }

        getObject(object) {
            if (typeof object === "undefined") {
                object = {};
            }
            this._inputs.forEach(input => {
                const value = input.value.trim();
                OPP.set(object, input.getAttribute("name"), value || null);
            });
            return object;
        }

        isValid() {
            let valid = true;
            this._inputs.forEach(input => {
                if (input.hasAttribute("required")) {
                    const inputValid = !!input.value.trim();
                    input.classList[inputValid ? "remove" : "add"]("invalid");
                    valid = inputValid && valid;
                }
            });
            return valid;
        }

        reset() {
            this._inputs.forEach(input => {
                input.classList.remove("invalid");
                input.value = '';
            });
        }
    };

    customElements.define("object-form", ObjectForm);

})();