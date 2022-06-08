(function () {

    class ObjectForm extends HTMLElement {
        constructor() {
            super();

            this._inputs = [];
            this._inputsByNameIndex = {};

            let input = this.firstElementChild;
            while (input) {
                if (input.hasAttribute("name")) {
                    this._inputs.push(input);
                    this._inputsByNameIndex[input.getAttribute("name")] = input;
                    input.addEventListener("focus", event => event.target.classList.remove("invalid"));
                }
                input = input.nextElementSibling;
            }
        }

        getElementByName(name) {
            const input = this._inputsByNameIndex[name];
            if (!input) {
                throw `Missing input element with name ${name}.`;
            }
            return input;
        }

        setObject(object) {
            this._inputs.forEach(input => {
                if (input.hasAttribute("data-if")) {
                    const expression = OPP.get(object, input.getAttribute("data-if"));
                    input.classList.toggle("hidden", !expression);
                }
                input.classList.remove("invalid");
                let value = OPP.get(object, input.getAttribute("name"));
                if (value) {
                    if (input.hasAttribute("data-format")) {
                        value = FormatFactory.get(input.getAttribute("data-format")).format(value);
                    }
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
                let inputValid = true;
                const value = input.value.trim();
                if (value && input.hasAttribute("pattern")) {
                    const expression = new RegExp(input.getAttribute("pattern"));
                    inputValid = expression.test(value);
                }
                if (inputValid && input.hasAttribute("required")) {
                    inputValid = !!value;
                }
                input.classList.toggle("invalid", !inputValid);
                valid = inputValid && valid;
            });
            return valid;
        }

        reset() {
            this._inputs.forEach(input => {
                input.classList.remove("invalid");
                input.value = '';

                const options = input.options;
                if (options) {
                    for (let i = 0; i < options.length; ++i) {
                        if (options[i].defaultSelected) {
                            input.selectedIndex = i;
                            break;
                        }
                    }
                }
            });
        }
    };

    customElements.define("object-form", ObjectForm);

})();