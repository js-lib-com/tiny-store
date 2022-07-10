/**
 * Proxy handler factory for model object. Returned handler intercepts property
 * assignment operations on model object, get bound element from bindings and
 * update element text content with related object property value.
 * 
 * @param {Array} stack object properties stack,
 * @param {Object} bindings object property paths mapped to related view elements.
 * @returns {Object} object proxy handler.
 */
function ObjectHandler(stack, bindings) {
    let propertyPath = stack.join('.');
    if (propertyPath) {
        propertyPath += '.';
    }

    return {
        propertyPath: propertyPath,
        bindings: bindings,

        set(object, property, value, proxy) {
            const propertyPath = this.propertyPath + property;
            console.log(`Set object property ${propertyPath}:${JSON.stringify(value)}.`);

            const elements = this.bindings[propertyPath];
            if (typeof elements != "undefined") {
                elements.forEach(element => ObjectView.setValue(element, value));
            }

            return Reflect.set(object, property, value, proxy);
        }
    };
}

/**
 * Proxy handler factory for model arrays. Returned handler intercepts operations on model
 * array and delegates bound {@link ListView}.
 * 
 * Delegated model array actions:
 * - push to array: {@link ListView#addItem(Object)},
 * - assign to array item: {@link ListView#setItem(Number, Object)},
 * - delete array item: {@link ListView#removeItem(Number)}.
 * 
 * @param {Array} stack object properties stack,
 * @param {Object} bindings object property paths mapped to related view elements.
 * @returns {Object} array proxy handler.
 */
function ArrayHandler(stack, bindings) {
    return {
        objectPath: stack.join('.'),
        bindings: bindings,

        set(array, property, value, proxy) {
            if (!isNaN(property)) {
                bindings[this.objectPath].forEach(listView => {
                    if (array.length <= Number(property)) {
                        console.log(`Add array ${this.objectPath}, item ${property}:${JSON.stringify(value)}.`);
                        listView.addItem(value);
                    }
                    else {
                        const index = Number(property);
                        console.log(`Update array ${this.objectPath}, item ${index}:${JSON.stringify(value)}.`);
                        listView.setItem(index, value);
                    }
                });
            }
            else {
                console.log(`Set array property ${this.objectPath}.${property}:${JSON.stringify(value)}.`);
            }
            return Reflect.set(array, property, value, proxy);
        },

        deleteProperty(array, property) {
            if (!isNaN(property)) {
                bindings[this.objectPath].forEach(listView => {
                    const index = Number(property);
                    console.log(`Delete array ${this.objectPath}, item ${index}.`);
                    listView.removeItem(index);
                });
            }
            else {
                console.log(`Delete array property ${this.objectPath}.${property}.`);
            }
            return Reflect.set(array, property);
        }
    };
}

/**
 * Object view is a container with descendat elements instrumented for one-way data binding. 
 * 
 * An object view layout is designed for a particular model object. Object view has 
 * descendant elements specifically bound to model object proerties. Bindings are 
 * declared on elements using <code>data-text</code> and <code>data-list</code> attributes.
 * 
 * Object view has a single method: {@link #setObject(Object)}. It traverses all instrumented
 * descendant elements and populate them from model object properties. Model object supports 
 * arbitrary complex hierarchy; it is regarded as a tree of primitive data value and compund
 * types (objects and arrays) with primitive values as tree leafs. The path through model object
 * tree is called object property path - OPP.
 * 
 * Also {@link #setObject(Object)} takes care to discover and intialize one-way data bindings
 * between model object properties and related object view descendat elements. It creates and
 * return a proxy for given model object. This proxy intercepts assignment operation on model 
 * and update bound element state accordingly.
 * 
 * So, first execution of {@link #setObject(Object)} populates object view elements from model
 * object properties. After that controller operates directly on model object via assignment 
 * language operation and the one-way data binding proxy takes care to update view elements.
 * 
 * Object view deals only with static layout. For list of views created dynamically this class
 * delegates {@link ListView} class.
 */
ObjectView = class extends HTMLElement {
    static setValue(element, value) {
        if (value) {
            if (element.dataset.format) {
                value = FormatFactory.get(element.dataset.format).format(value);
            }
        }
        else {
            value = '';
        }
        element.textContent = value;
    }

    constructor() {
        super();
    }

    set object(object) {
        this._object = object;
        this._inject(this, object, null);
    }

    get object() {
        return this._object;
    }

    /**
     * Inject model object into object view descendants and create one-way data binding proxy.
     * 
     * @param {Object} object model object, usualy loaded from server.
     * @returns {Proxy} one-way data binding proxy for given model object.
     */
    setObject(object) {
        const bindings = {};
        this._inject(this, object, bindings);
        return this._proxy(object, [], bindings, true);
    }

    _inject(element, object, bindings) {
        const propertyPath = this._bind(bindings, element, "text");
        if (propertyPath) {
            ObjectView.setValue(element, OPP.get(object, propertyPath));
            return;
        }

        let childElement = element.firstElementChild;
        while (childElement) {
            if (childElement.dataset.exclude) {
                console.debug(`Excluded element ${childElement.tagName}.`);
            }
            else if (childElement.dataset.if) {
                let expression = OPP.get(object, childElement.dataset.if);
                if (expression) {
                    childElement.classList.remove("hidden");
                    this._inject(childElement, object, bindings);
                }
                else {
                    childElement.classList.add("hidden");
                }
            }
            else if (childElement.dataset.list) {
                if (!(childElement instanceof ListView)) {
                    throw "List view element should implements ListView abstract class.";
                }
                const propertyPath = this._bind(bindings, childElement, "list");
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

    /**
     * Collect one-way data bindings from DOM element. Given element may or may not be
     * instrumented with one-way data bindings using a specific <code>data-</code> attribute.
     * If element has not requested dataset property this method does nothing and returns null.
     * 
     * When object view does not use bindings, <code>bindings</code> parameter is null in which case
     * this method just return the property path, still possible null.
     * 
     * @param {Object} bindings optional bindings collector, null if bindings is not performed,
     * @param {HTMLElement} element DOM element, possible instrumented with data binding,
     * @param {String} property dataset property used to declare data binding.
     * @returns {String} property path or null if element has no property path declared.
     */
    _bind(bindings, element, property) {
        const propertyPath = element.dataset[property];
        if (!propertyPath) {
            return null;
        }

        if (bindings != null) {
            let boundElements = bindings[propertyPath];
            if (typeof boundElements == "undefined") {
                boundElements = [];
                bindings[propertyPath] = boundElements;
            }
            console.log(`Bind ${propertyPath} property to view element ${element.tagName}#${element.dataset[property]}.`);
            boundElements.push(element);
        }
        return propertyPath;
    }

    /**
     * Recursivelly replace all objects from model object with one-way data binding proxy. 
     * At start, <code>object</code> parameter is set to model object but for next iterations
     * it is the descendant object from hierarchy.
     * 
     * Both object and array are substitued but array items are not processed. For that
     * <code>processing</code> parameter is set to false when an array is discovered.
     * 
     * @param {Object} object model object or descendant,
     * @param {Array} stack object properties stack, update at every iteration,
     * @param {Object} bindings object property paths mapped to related view elements,
     * @param {Boolean} processing flag true as long as processing should continue.
     * @returns {Proxy} proxy instance for given model object.
     */
    _proxy(object, stack, bindings, processing) {
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
                object[property] = this._proxy(object[property], stack, bindings, !array);
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

/**
 * Interface for list of views. This interface is known and used by {@link ObjectView}
 * to deal with dynamically managed layout.
 * 
 * There is a major distinction between object view and list view: object view has
 * an immutable structure. While object view has only logic to update value elements, 
 * the list view must also create and remove layout elemnents dynamically.
 * 
 * On its initialization, {@link ObjectView} discovers list view elements and invoked 
 * {@link #setItems(Array)} that takes care to create and populate its item elements. 
 * After that controller operates directly on model array, e.g. when controller perform 
 * a push operation on model array, one-way data binding proxy delegates this class 
 * {@link #addItem(Object)} method.
 */
ListView = class extends HTMLElement {
    constructor() {
        super();
    }

    /**
     * Create child views populated from given array. This method is invoked by {@link ObjectView}
     * when it discovers an list view element.
     * @param {Array} items model array items.
     * @returns {Proxy} one-way data binding proxy for given model array.
     */
    setItems(items) {
        return new Proxy(items, ArrayHandler(["self"], { self: [this] }));
    }

    /**
     * Create a new child view populated from given item object. This method is delegated when
     * controller performs push operation on model array.
     * @param {Object} item item added to model array. 
     */
    addItem(item) {
        throw "Concrete list view should implement 'addItem(item)' method.";
    }

    /**
     * Replace an the item identified by array index. This method is delegated when controller
     * performs an assignation to a model array item.
     * @param {Number} index array index for item to replace,
     * @param {Object} item model array item. 
     */
    setItem(index, item) {
        throw "Concrete list view should implement 'setItem(index, item)' method.";
    }

    /**
     * Remove an item identified by array index. This method is delegated when controller 
     * remove an item from model array.
     * @param {Number} index array index for item to remove.
     */
    removeItem(index) {
        throw "Concrete list view should implement 'removeItem(index)' method.";
    }
};
