Workspace = {
	 createStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/createStore.rmi";
		var parameters = [store];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/updateStore.rmi";
		var parameters = [store];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteStore: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/deleteStore.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStores: function() {
		var __callback__ = arguments[0];
		var __scope__ = arguments[1] || window;
		var url = "com/jslib/tiny/store/Workspace/getStores.rmi";
		var parameters = [];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 importStoreEntity: function(storeId, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Workspace/importStoreEntity.rmi";
		var parameters = [storeId, entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createDaoService: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/createDaoService.rmi";
		var parameters = [entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 testDataSource: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/testDataSource.rmi";
		var parameters = [store];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 buildProject: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/buildProject.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 commitChanges: function(storeId, message) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "com/jslib/tiny/store/Workspace/commitChanges.rmi";
		var parameters = [storeId, message];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 pushChanges: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/pushChanges.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getProject: function(projectName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/getProject.rmi";
		var parameters = [projectName];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getTypeOptionsByService: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/getTypeOptionsByService.rmi";
		var parameters = [serviceId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getTypeOptionsByStore: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "com/jslib/tiny/store/Workspace/getTypeOptionsByStore.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getMavenOptions: function() {
		var __callback__ = arguments[0];
		var __scope__ = arguments[1] || window;
		var url = "com/jslib/tiny/store/Workspace/getMavenOptions.rmi";
		var parameters = [];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	fetch: function(url, parameters, callback, scope) {
		if(this.contextBaseUrl) {
			url = this.contextBaseUrl + url;
		}
		
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
