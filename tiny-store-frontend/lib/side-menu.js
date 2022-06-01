(function () {
    class SideMenu extends HTMLElement {
        constructor() {
            super();

            const children = Array.from(this.children);
            this._itemIds = children.map(child => child.id);
        }

        setLink(linkId, urlProducer) {
            const link = this.children.item(this._itemIds.indexOf(linkId));
            if (!link) {
                throw `Missing menu link element with id ${linkId}.`;
            }
            link.addEventListener("click", event => location.assign(urlProducer()));
        }

        setAction(actionId, listener, scope) {
            const action = this.children.item(this._itemIds.indexOf(actionId));
            if (action) {
                action.addEventListener("click", listener.bind(scope || window));
            }
        }

        enable(itemId, enabled) {
            const item = this.children.item(this._itemIds.indexOf(itemId));
            if (!item) {
                throw `Missing menu item element with id ${itemId}.`;
            }
            item.classList.toggle("disabled", !enabled);
        }
    };

    customElements.define("side-menu", SideMenu);
})();