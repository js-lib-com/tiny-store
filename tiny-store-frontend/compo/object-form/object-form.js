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
                input.value = OPP.get(object, input.getAttribute("name"));
            });
        }

        getObject() {
            let object = {};
            this._inputs.forEach(input => OPP.set(object, input.getAttribute("name"), input.value.trim()));
            return object;
        }

        isValid() {
            let valid = true;
            this._inputs.forEach(input => {
                if (!input.classList.contains("optional")) {
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

        toString() {
            return "ObjectForm";
        }
    };

    customElements.define("object-form", ObjectForm);

})();