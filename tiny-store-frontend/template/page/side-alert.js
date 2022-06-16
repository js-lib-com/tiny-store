(function () {

    class SideAlert extends HTMLElement {
        constructor() {
            super();

            this._titleView = this.getElementsByTagName("h2")[0];
            this._messageView = this.getElementsByTagName("section")[0];

            let positiveButton = this.querySelector("[name=positive]");
            if (!positiveButton) {
                throw "Invalid side alert dialog. Missing positive button.";
            }
            positiveButton.addEventListener("click", this._onPositiveButton.bind(this));

        }

        /**
         * @param {String} title side alert title.
         * @returns {SideAlert} this object.
         */
        title(title) {
            this._titleView.textContent = title;
            return this;
        }

        /**
         * @param {String} message multiline message; uses '\r\n' as line separator.
         */
        show(message) {
            message.split("\r\n").forEach(line => { this._messageView.innerHTML += `<p>${line}</p>\r\n` });
        }

        _onPositiveButton() {
            this.classList.add("hidden");
            this._titleView.innerHTML = '';
            this._messageView.innerHTML = '';
        }
    }

    customElements.define("side-alert", SideAlert);

})();