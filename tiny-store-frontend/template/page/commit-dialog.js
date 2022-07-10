(function () {

    class CommitDialog extends SideDialog {
        constructor() {
            super();

            this._textarea = this.getElementsByTagName("textarea")[0];

            this._tableView = this.getElementsByTagName("table-view")[0];
            this._tableView.addEventListener("select", this._onChangeLogSelect.bind(this));
        }

        edit(changeLog, callback) {
            this._changeLog = changeLog;
            super.edit({ changeLog: changeLog }, callback);
        }

        _onChangeLogSelect(event) {
            if (event.detail.selected) {
                this._textarea.value = this._changeLog[this._tableView.selectedIndex].change;
            }
            else {
                this._textarea.value = '';
            }
        }
    }

    customElements.define("commit-dialog", CommitDialog);

})();