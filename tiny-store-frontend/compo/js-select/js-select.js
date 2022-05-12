(function () {

    JsSelect = class extends HTMLElement {
        constructor() {
            super();
        }

        toString() {
            return "JsSelect";
        }
    };

    customElements.define("js-select", JsSelect);

})();