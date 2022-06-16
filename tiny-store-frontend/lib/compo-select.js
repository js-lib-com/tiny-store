(function () {

    /**
     * Select a child component at a time. This select has a list of child components and ensure that at
     * most one is visible at a given time. 
     */
    class CompoSelect extends HTMLElement {
        constructor() {
            super();

            this._compos = {};
            this._selectedCompo = null;

            Array.from(this.children).forEach(child => {
                this._compos[child.id] = child;
                child.classList.add("hidden");
            });
        }

        get(compoName) {
            const compo = this._compos[compoName];
            if (!compo) {
                throw `Missing component ${compoName}.`;
            }

            if (compo.classList.contains("overlapped")) {
                compo.classList.remove("hidden");
                return compo;
            }

            if (this._selectedCompo) {
                this._selectedCompo.classList.add("hidden");
            }

            this._selectedCompo = compo;

            this._selectedCompo.classList.remove("hidden");
            return this._selectedCompo;
        }
    }

    customElements.define("compo-select", CompoSelect);

})();