class TableView extends ListView {
    static CLASS_SELECTED = "selected";
    static EVENT_SELECTED = "select";

    constructor() {
        super();
        console.log(`${this}#constructor()`);
        this._selectedRow = null;

        let elements = this.getElementsByTagName("thead");
        if (elements.length == 0) {
            throw `Invalid table view #${this.getAttribute("id")}. Missing header.`;
        }
        const thead = elements.item(0);
        if (!thead.firstElementChild) {
            throw `Invalid table view #${this.getAttribute("id")}. Missing header row.`;
        }
        this._headers = Array.from(thead.firstElementChild.children);

        elements = this.getElementsByTagName("tbody");
        if (elements.length > 0) {
            this._tbody = elements.item(0);
            this._tbody.addEventListener("click", this._onClick.bind(this));
            this._rowTemplate = this._tbody.removeChild(this._tbody.firstElementChild);
        }
    }

    setItems(items) {
        this.clear();
        items.forEach(item => {
            this.addItem(item);
        });
    }

    addItem(item) {
        const row = this._rowTemplate.cloneNode(true);
        this._setRowItem(row, item);
        this._tbody.appendChild(row);
    }

    setItem(index, item) {
        const row = this._tbody.children.item(index);
        if (!row) {
            throw `Out of bound table index ${index}.`;
        }
        this._setRowItem(row, item);
    }

    removeItem(index) {
        const row = this._tbody.children.item(index);
        if (!row) {
            throw `Out of bound table index ${index}.`;
        }
        row.parentElement.removeChild(row);
    }

    clear() {
        this._tbody.innerHTML = '';
    }

    getSelectedIndex() {
        if (this._selectedRow == null) {
            return -1;
        }
        return Array.prototype.indexOf.call(this._tbody.children, this._selectedRow);
    }

    getSelectedItem() {
        return this._selectedRow != null ? this._selectedRow.__item__ : null;
    }

    getSelectedId() {
        return this._selectedRow != null ? this._selectedRow.__item__.id : null;
    }

    _setRowItem(row, item) {
        let cell = row.firstElementChild;
        let index = 0;
        while (cell) {
            if (cell.hasAttribute("data-if")) {
                const expression = OPP.get(item, cell.getAttribute("data-if"));
                cell.classList.toggle("hidden", !expression);
                this._headers[index].classList.toggle("hidden", !expression);
            }
            let value = OPP.get(item, cell.getAttribute("data-text"));
            if (cell.hasAttribute("data-format")) {
                value = FormatFactory.get(cell.getAttribute("data-format")).format(value);
            }
            cell.textContent = value;
            cell = cell.nextElementSibling;
            ++index;
        }
        row.__item__ = item;
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
