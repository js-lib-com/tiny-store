Validator = {
	 assertCreateStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Validator/assertCreateStore.rmi";
		var parameters = [store];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertCreateService: function(storeId, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertCreateService.rmi";
		var parameters = [storeId, service];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertEditService: function(model, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertEditService.rmi";
		var parameters = [model, service];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertCreateEntity: function(storeId, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertCreateEntity.rmi";
		var parameters = [storeId, entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertEditEntity: function(model, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertEditEntity.rmi";
		var parameters = [model, entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 allowDeleteEntity: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Validator/allowDeleteEntity.rmi";
		var parameters = [entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertCreateField: function(model, field) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertCreateField.rmi";
		var parameters = [model, field];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertEditField: function(model, fieldIndex, field) {
		var __callback__ = arguments[3];
		var __scope__ = arguments[4] || window;
		var url = "com/jslib/tiny/store/Validator/assertEditField.rmi";
		var parameters = [model, fieldIndex, field];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertCreateOperation: function(service, operation) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertCreateOperation.rmi";
		var parameters = [service, operation];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertEditOperation: function(model, operation) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertEditOperation.rmi";
		var parameters = [model, operation];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertCreateParameter: function(operation, parameter) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Validator/assertCreateParameter.rmi";
		var parameters = [operation, parameter];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 assertEditParameter: function(operation, parameterIndex, parameter) {
		var __callback__ = arguments[3];
		var __scope__ = arguments[4] || window;
		var url = "com/jslib/tiny/store/Validator/assertEditParameter.rmi";
		var parameters = [operation, parameterIndex, parameter];

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
