(function () {

    class ActionBar extends HTMLElement {
        constructor() {
            super();

            this._actions = {};

            let child = this.firstElementChild;
            while (child) {
                if (!child.id) {
                    throw "Invalid action element. Missing id.";
                }
                this._actions[child.id] = child;
                child = child.nextElementSibling;
            }
        }

        setHandlersScope(handlersScope, scopeName) {
            this._handlersScope = handlersScope;
            this._scopeName = scopeName;
        }

        setHandler(actionId, handler) {
            const action = this._actions[actionId];
            if (!action) {
                throw `Missing action element with id ${actionId} from ${this._scopeName} action bar.`;
            }
            action.addEventListener("click", handler.bind(this._handlersScope));
        }

        show(actionId, show) {
            const action = this._actions[actionId];
            if (!action) {
                throw `Missing action element with id ${actionId} from ${this._scopeName} action bar.`;
            }
            action.classList[show ? "remove" : "add"]("hidden");
        }
    }

    customElements.define("action-bar", ActionBar);

})();