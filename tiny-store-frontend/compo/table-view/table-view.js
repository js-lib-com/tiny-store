(function () {

    class TableView extends HTMLElement {
        static CLASS_SELECTED = "selected";
        static EVENT_SELECTED = "select";

        constructor() {
            super();
            console.log(`${this}#constructor()`);
            this._selectedRow = null;

            const elements = this.getElementsByTagName("tbody");
            if (elements.length > 0) {
                this._tbody = elements.item(0);
                this._tbody.addEventListener("click", this._onClick.bind(this));
                this._rowTemplate = this._tbody.removeChild(this._tbody.firstElementChild);
            }
        }

        setItems(items) {
            this.clear();
            this.addItems(items);
        }

        addItems(items) {
            items.forEach(item => {
                this.addItem(item);
            });
        }

        addItem(item) {
            const row = this._rowTemplate.cloneNode(true);
            let cell = row.firstElementChild;
            while (cell) {
                let value = item.__value__(cell.getAttribute("data-text"));
                if (cell.hasAttribute("data-format")) {
                    value = FormatFactory.get(cell.getAttribute("data-format")).format(value);
                }
                cell.textContent = value;
                cell = cell.nextElementSibling;
            }
            row.__item__ = item;
            this._tbody.appendChild(row);
        }

        clear() {
            this._tbody.innerHTML = '';
        }

        /**
         * 
         * @returns selected row or null.
         */
        getSelectedRow() {
            return this._selectedRow;
        }

        getSelectedItem() {
            return this._selectedRow != null ? this._selectedRow.__item__ : null;
        }

        _onClick(event) {
            console.log(`${this}#onClick(event)`);
            var row = event.target;
            while (row.tagName.toLowerCase() !== "tr") {
                row = row.parentElement;
                if (row === null) return;
            }

            if (row.classList.contains(TableView.CLASS_SELECTED)) {
                this._select(row, false);
                return;
            }

            if (this._selectedRow != null) {
                this._select(this._selectedRow, false);
            }
            this._selectedRow = row;
            this._select(this._selectedRow, true);
        }

        _select(row, selected) {
            const event = new CustomEvent(TableView.EVENT_SELECTED, { detail: { selected: selected } });
            this.dispatchEvent(event);
            row.classList[selected ? "add" : "remove"](TableView.CLASS_SELECTED);
        }

        toString() {
            return "TableView";
        }
    }

    customElements.define("table-view", TableView);
})();