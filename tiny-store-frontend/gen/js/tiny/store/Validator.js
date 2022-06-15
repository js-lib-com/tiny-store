Validator = {
	 assertEditEntity: function(model, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/Validator/assertEditEntity.rmi";
		var parameters = [model, entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertCreateField: function(model, field) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/Validator/assertCreateField.rmi";
		var parameters = [model, field];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertEditField: function(model, fieldIndex, field) {
		var __callback__ = arguments[3];
		var __scope__ = arguments[4] || window;
		var url = "js/tiny/store/Validator/assertEditField.rmi";
		var parameters = [model, fieldIndex, field];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	fetch: function(url, parameters, callback, scope) {
		var responsePromise = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		var responseOK = true;
		var jsonPromise = responsePromise.then(response => { 
			if(!response.ok) {
				responseOK = false;
			}
			if(response.headers.get("Content-Type")) { 
				return response.json(); 
			}
		});

		jsonPromise.then(json => {
			if(!responseOK) {
				if(this.errorHandler) {
					this.errorHandler(json);
				}
				console.error(json);
				return;
			}
			if (callback) {
				callback.call(scope, json);
			}
		});
	}
};
