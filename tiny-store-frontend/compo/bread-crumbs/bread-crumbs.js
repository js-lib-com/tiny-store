(function () {

    class BreadCrumbs extends HTMLElement {
        constructor() {
            super();
        }

        setPath(path) {
            if(!path) {
                this.classList.add("hidden");
                return;
            }

            this.classList.remove("hidden");
            this.textContent = path.join(" / ");
        }
    };

    customElements.define("bread-crumbs", BreadCrumbs);

})();
