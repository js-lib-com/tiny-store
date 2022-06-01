(function () {

    class CommitDialog extends SideDialog {
        constructor() {
            super();

            this._textarea = this.getElementsByTagName("textarea")[0];

            this._tableView = this.getElementsByTagName("table-view")[0];
            this._tableView.addEventListener("select", this._onChangeLogSelect.bind(this));
        }

        _onChangeLogSelect(event) {
            if (event.detail.selected) {
                this._textarea.value = this._tableView.getSelectedItem().change;
            }
            else {
                this._textarea.value = '';
            }
        }
    }

    customElements.define("commit-dialog", CommitDialog);

})();