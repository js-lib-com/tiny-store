(function () {

    function PropertyPath(stack) {
        let propertyPath = stack.join('.');
        if (propertyPath) {
            propertyPath += '.';
        }
        return propertyPath;
    }

    function ObjectHandler(stack, bindings) {
        return {
            propertyPath: PropertyPath(stack),
            bindings: bindings,

            set(object, property, value, proxy) {
                const propertyPath = this.propertyPath + property;
                console.log(`Set object property ${propertyPath}:${JSON.stringify(value)}.`);

                const elements = this.bindings[propertyPath];
                if (typeof elements != "undefined") {
                    elements.forEach(element => element.textContent = value);
                }

                return Reflect.set(object, property, value, proxy);
            }
        };
    }

    function ArrayHandler(stack, bindings) {
        return {
            objectPath: stack.join('.'),
            bindings: bindings,

            set(target, property, value, receiver) {
                if (!isNaN(property)) {
                    const listView = bindings[this.objectPath][0];
                    if (typeof listView != "undefined") {
                        console.log(`Delegate list view ${listView}.`);
                        if (target.length <= Number(property)) {
                            console.log(`Add array ${this.objectPath}, item ${property}:${JSON.stringify(value)}.`);
                            listView.addItem(value);
                        }
                        else {
                            const index = Number(property);
                            console.log(`Update array ${this.objectPath}, item ${index}:${JSON.stringify(value)}.`);
                            listView.setItem(index, value);
                        }
                    }
                }
                else {
                    console.log(`Set array property ${this.objectPath}.${property}:${JSON.stringify(value)}.`);
                }

                return Reflect.set(target, property, value, receiver);
            },

            deleteProperty(target, property) {
                if (!isNaN(property)) {
                    const listView = bindings[this.objectPath][0];
                    if (typeof listView != "undefined") {
                        console.log(`Delegate list view ${listView}.`);
                        const index = Number(property);
                        console.log(`Delete array ${this.objectPath}, item ${index}.`);
                        listView.removeItem(index);
                    }
                }
                else {
                    console.log(`Delete array property ${this.objectPath}.${property}.`);
                }
                return Reflect.set(target, property);
            }
        };
    }

    ObjectView = class extends HTMLElement {
        constructor() {
            super();
        }

        setObject(object) {
            const bindings = {};
            this._inject(this, object, bindings);
            return this._proxy([], object, bindings, true);
        }

        _inject(element, object, bindings) {
            const propertyPath = this._bind(bindings, element, "data-text");
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
                else if (childElement.hasAttribute("data-if")) {
                    let expression = OPP.get(object, childElement.getAttribute("data-if"));
                    if (expression) {
                        childElement.classList.remove("hidden");
                        this._inject(childElement, object, bindings);
                    }
                    else {
                        childElement.classList.add("hidden");
                    }
                }
                else if (childElement.hasAttribute("data-list")) {
                    if(!(childElement instanceof ListView)) {
                        throw "List view element should implements ListView abstract class.";
                    }
                    const propertyPath = this._bind(bindings, childElement, "data-list");
                    console.debug(`List element ${childElement.tagName}.`);
                    const list = OPP.get(object, propertyPath);
                    console.debug(`List property ${list}.`);
                    childElement.setItems(list);
                }
                else {
                    this._inject(childElement, object, bindings);
                }
                childElement = childElement.nextElementSibling;
            }
        }

        _bind(bindings, element, attribute) {
            const propertyPath = element.getAttribute(attribute);
            if (!propertyPath) {
                return;
            }

            let boundElements = bindings[propertyPath];
            if (typeof boundElements == "undefined") {
                boundElements = [];
                bindings[propertyPath] = boundElements;
            }
            console.log(`Bind ${propertyPath} property to view element ${element.tagName}#${element.getAttribute(attribute)}.`);
            boundElements.push(element);
            return propertyPath;
        }

        _proxy(stack, object, bindings, processing) {
            if (processing) {
                for (const property in object) {
                    if (!object.hasOwnProperty(property) || object[property] == null) {
                        continue;
                    }
                    if (typeof object[property] != "object") {
                        continue;
                    }
                    stack.push(property);
                    const array = Array.isArray(object[property]);
                    console.log(`Create ${array ? 'array' : 'object'} proxy for ${stack.join('.')}.`);
                    // continue processing only if not array
                    object[property] = this._proxy(stack, object[property], bindings, !array);
                    stack.pop(property);
                }
            }

            if (Array.isArray(object)) {
                return new Proxy(object, ArrayHandler(stack, bindings));
            }
            return new Proxy(object, ObjectHandler(stack, bindings));
        }

        toString() {
            return "ObjectView";
        }
    }

    customElements.define("object-view", ObjectView);

})();


ListView = class extends HTMLElement {
    constructor() {
        super();
    }

    setItems(items) {
        throw "Concrete list view should implement 'setItems(items)' method.";
    }

    addItem(item) {
        throw "Concrete list view should implement 'addItem(item)' method.";
    }

    setItem(index, item) {
        throw "Concrete list view should implement 'setItem(index, item)' method.";
    }

    removeItem(index) {
        throw "Concrete list view should implement 'removeItem(index)' method.";
    }

    clear() {
        throw "Concrete list view should implement 'clear()' method.";
    }
};
