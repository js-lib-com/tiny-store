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
                element.textContent = OPP.get(object, propertyPath);
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
                    if (list) {
                        console.debug(`List property ${list}.`);
                        childElement.setItems(list);
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