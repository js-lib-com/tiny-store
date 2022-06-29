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
            positiveButton.addEventListener("click", this.close.bind(this));

        }

        /**
         * Returns true if this side dialog should be displayed over currently opened dialog, if any.
         * @returns {Boolean} always true.
         */
        get overlapped() {
            return true;
        }

        /**
         * Side dialog life cycle invoked by component selector when need to render the component visible.
         */
        show() {
            this.classList.remove("hidden");
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
        message(message) {
            message.split("\r\n").forEach(line => { this._messageView.innerHTML += `<p>${line}</p>\r\n` });
        }

        close() {
            this.classList.add("hidden");
            this._titleView.innerHTML = '';
            this._messageView.innerHTML = '';
        }
    }

    customElements.define("side-alert", SideAlert);

})();