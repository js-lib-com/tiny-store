(function () {
    class SideMenu extends HTMLElement {
        constructor() {
            super();
        }

        bind(scope) {
            const links = this.getElementsByTagName("li");
            for (let i = 0; i < links.length; ++i) {
                const link = links.item(i);
                const expression = `location.assign(\`${link.getAttribute("data-link")}\`);`;
                console.debug(`expression: ${expression}`);
                link.addEventListener("click", event => Function(expression).bind(scope)());
            }
        }
    };

    customElements.define("side-menu", SideMenu);
})();