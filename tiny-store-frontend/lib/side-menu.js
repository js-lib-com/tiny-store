(function () {
    class SideMenu extends HTMLElement {
        constructor() {
            super();

            const children = Array.from(this.children);
            this._linkIds = children.map(child => child.id);
        }

        setLink(linkId, urlProducer) {
            this._link(linkId).addEventListener("click", event => location.assign(urlProducer()));
        }

        enable(linkId, enabled) {
            this._link(linkId).classList.toggle("disabled", !enabled);
        }

        _link(linkId) {
            const link = this.children.item(this._linkIds.indexOf(linkId));
            if (!link) {
                throw `Missing link element with id ${linkId}.`;
            }
            return link;
        }
    };

    customElements.define("side-menu", SideMenu);
})();