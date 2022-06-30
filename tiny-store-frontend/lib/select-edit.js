(function () {

    class SelectEdit extends HTMLElement {
        constructor() {
            super();

            this._select = document.createElement("select");
            this.appendChild(this._select);

            // I do not understand how next lines of code are working
            // options HTMLCollection is live and moving an option does affect the collection
            // but collection length remains the same, so need to increment index, even if not use it for item retrieve
            const options = this.getElementsByTagName("option");
            for (let i = 0; i < options.length; ++i) {
                this._select.appendChild(options.item(0));
            }

            this._input = document.createElement("input");
            this.appendChild(this._input);

            ["placeholder"].forEach(name => {
                this._input.setAttribute(name, this.getAttribute(name));
                this.removeAttribute(name);
            });

            if (this.hasAttribute("required")) {
                this.classList.add("required")
            }

            this._select.addEventListener("change", this._onSelectChange.bind(this));
            this._input.addEventListener("focus", this._onInputFocus.bind(this));
        }

        set value(value) {
            this._select.value = value;
            this._input.value = value;
        }

        get value() {
            return this._input.value;
        }

        get options() {
            return this._select.options;
        }

        set selectedIndex(index) {
            this._select.selectedIndex = index;
        }

        get selectedIndex() {
            return this._select.selectedIndex;
        }

        load(optionValues) {
            while (this._select.firstChild) {
                this._select.removeChild(this._select.lastChild);
            }
            const addOption = function (optionValue) {
                const option = document.createElement("option");
                option.textContent = optionValue;
                if(!optionValue) {
                    option.selected = true;
                }
                this._select.appendChild(option);
            }.bind(this);

            addOption('');
            optionValues.forEach(optionValue => addOption(optionValue));
        }

        _onSelectChange(event) {
            this._input.value = event.target.value;
            this.classList.remove("invalid");
        }

        _onInputFocus(event) {
            this.dispatchEvent(new Event(event.type));
        }

        toString() {
            return "SelectEdit";
        }
    }

    customElements.define("select-edit", SelectEdit);

})();
