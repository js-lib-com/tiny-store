(function () {

    class ActionBar extends HTMLElement {
        constructor() {
            super();
            this._actions = {};

            let child = this.firstElementChild;
            while (child) {
                if (!child.hasAttribute("id")) {
                    throw "Invalid action element. Missing id.";
                }
                this._actions[child.getAttribute("id")] = child;
                child = child.nextElementSibling;
            }
        }

        setHandlersScope(handlersScope) {
            this._handlersScope = handlersScope;
        }

        setHandler(actionId, handler) {
            const action = this._actions[actionId];
            if (!action) {
                throw `Missing action element with id ${actionId}.`;
            }
            action.addEventListener("click", handler.bind(this._handlersScope));
        }

        show(actionId, show) {
            const action = this._actions[actionId];
            if (!action) {
                throw `Missing action element with id ${actionId}.`;
            }
            action.classList[show ? "remove" : "add"]("hidden");
        }
    }

    customElements.define("action-bar", ActionBar);

})();