(function () {

    ObjectView = class extends HTMLElement {
        constructor() {
            super();
        }

        setObject(object) {
            this._inject(this, object);
        }

        _inject(element, object) {
            const propertyPath = element.getAttribute("data-text");
            if (propertyPath) {
                let value = OPP.get(object, propertyPath);
                if (value) {
                    if (element.hasAttribute("data-format")) {
                        value = FormatFactory.get(element.getAttribute("data-format")).format(value);
                    }
                }
                else {
                    value = '';
                }
                element.textContent = value;
                return;
            }

            let childElement = element.firstElementChild;
            while (childElement) {
                if (childElement.hasAttribute("data-exclude")) {
                    console.debug(`Excluded element ${childElement.tagName}.`);
                }
                else if (childElement.hasAttribute("data-list")) {
                    console.debug(`List element ${childElement.tagName}.`);
                    const list = OPP.get(object, childElement.getAttribute("data-list"));
                    if (list && list.length) {
                        console.debug(`List property ${list}.`);
                        childElement.classList.remove("hidden");
                        childElement.setItems(list);
                    }
                    else {
                        childElement.classList.add("hidden");
                    }
                }
                else {
                    this._inject(childElement, object);
                }
                childElement = childElement.nextElementSibling;
            }
        }

        toString() {
            return "ObjectView";
        }
    }

    customElements.define("object-view", ObjectView);

})();