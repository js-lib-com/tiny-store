/**
 * Index page class.
 * 
 * @author Iulian Rotaru
 * @since 1.0
 * 
 * @constructor Construct an instance of home page class.
 */
Index = function () {
	this.$super();

	// this._phoneMappingForm = this.getById("phone-mapping-form");
	// this._existingCashingCodeInput = this._phoneMappingForm.getByName("existingCashingCode");

	// this._searchListView = this.getById("search-list-view");
	// this._searchListView.on("click", this._onCasingCodeSelect, this);

	// this.getById("search-phone").on("click", this._onSearchPhone, this);
	// this.getById("update-cashing-code").on("click", this._onUpdateCashingCode, this);
	// this.getById("delete-cashing-code").on("click", this._onDeleteCashingCode, this);
	// this.getById("create-cashing-code").on("click", this._onCreateCashingCode, this);
	// this.getById("clear-input").on("click", this._onClearInput, this);
	// this._onClearInput();
};

Index.prototype = {
	_onSearchPhone: function (ev) {
		this._clearErrors();

		const phoneNumber = this._phoneMappingForm.getByName("phoneNumber").getValue();
		if (!phoneNumber) {
			this._phoneMappingForm.getByName("phoneNumber").addCssClass("invalid");
			return;
		}

		ContactCenterService.getCallInteractions(phoneNumber, resultItems => {
			this._searchListView.setObject(resultItems);
		});
	},

	_onUpdateCashingCode: function (ev) {
		if (this._phoneMappingForm.isValid()) {
			ContactCenterService.updatePhoneMapping(this._phoneMappingForm, result => {
				if (result == "SUCCESS") {
					this._onClearInput();
				}
				alert(result);
			});
		}
	},

	_onDeleteCashingCode: function (ev) {
		console.log(`${this}#_onDeleteCashingCode(ev)`);
		this._clearErrors();

		const phoneNumber = this._phoneMappingForm.getByName("phoneNumber").getValue();
		if (!phoneNumber) {
			this._phoneMappingForm.getByName("phoneNumber").addCssClass("invalid");
		}

		const existingCashingCode = this._phoneMappingForm.getByName("existingCashingCode").getValue();
		if (!existingCashingCode) {
			this._phoneMappingForm.getByName("existingCashingCode").addCssClass("invalid");
		}

		if (!phoneNumber || !existingCashingCode) {
			return;
		}

		ContactCenterService.deleteCashingCodeMapping(phoneNumber, existingCashingCode, result => {
			if (result == "SUCCESS") {
				this._onClearInput();
			}
			alert(result);
		});
	},

	_onCreateCashingCode: function (ev) {
		console.log(`${this}#_onCreateCashingCode(ev)`);
		this._clearErrors();

		const phoneNumber = this._phoneMappingForm.getByName("phoneNumber").getValue();
		if (!phoneNumber) {
			this._phoneMappingForm.getByName("phoneNumber").addCssClass("invalid");
		}

		const cashingCode = this._phoneMappingForm.getByName("cashingCode").getValue();
		if (!cashingCode) {
			this._phoneMappingForm.getByName("cashingCode").addCssClass("invalid");
		}

		if (!phoneNumber || !cashingCode) {
			return;
		}

		ContactCenterService.createCashingCodeMapping(phoneNumber, cashingCode, result => {
			if (result == "SUCCESS") {
				this._onClearInput();
			}
			alert(result);
		});
	},

	_onCasingCodeSelect: function (ev) {
		const tr = ev.target.getParentByTag("tr");
		if (tr.hasCssClass("selected")) {
			tr.removeCssClass("selected");
			this._existingCashingCodeInput.reset();
			return;
		}

		this._searchListView.getChildren().forEach(tr => tr.removeCssClass("selected"));
		tr.addCssClass("selected");
		this._existingCashingCodeInput.setValue(tr.getByCssClass("cashingCode").getValue());
	},

	_onClearInput: function (ev) {
		this._phoneMappingForm.reset();
		this._searchListView.resetObject();
	},

	_clearErrors: function () {
		this._phoneMappingForm.getByName("phoneNumber").removeCssClass("invalid");
		this._phoneMappingForm.getByName("cashingCode").removeCssClass("invalid");
		this._phoneMappingForm.getByName("existingCashingCode").removeCssClass("invalid");
	},

	/**
	 * Class string representation.
	 * 
	 * @return {string} this class string representation.
	 */
	toString: function () {
		return "Index";
	}
};
$extends(Index, Page);
