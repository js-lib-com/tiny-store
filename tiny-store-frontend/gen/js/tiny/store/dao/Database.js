Database = {
	 createStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createStore.rmi";
		var parameters = [store];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateStore.rmi";
		var parameters = [store];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteStore: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteStore.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStore: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStore.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStoreByName: function(name) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreByName.rmi";
		var parameters = [name];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 findStoresByOwner: function(ownerName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/findStoresByOwner.rmi";
		var parameters = [ownerName];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createStoreEntity: function(storeId, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/createStoreEntity.rmi";
		var parameters = [storeId, entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateStoreEntity: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateStoreEntity.rmi";
		var parameters = [entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteStoreEntity: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteStoreEntity.rmi";
		var parameters = [entity];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStoreEntity: function(entityId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreEntity.rmi";
		var parameters = [entityId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStoreEntities: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreEntities.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStoreEntityByClassName: function(storeId, className) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/getStoreEntityByClassName.rmi";
		var parameters = [storeId, className];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 findStoreEntityByClassName: function(storeId, className) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/findStoreEntityByClassName.rmi";
		var parameters = [storeId, className];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createDataService: function(storeId, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/createDataService.rmi";
		var parameters = [storeId, service];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateDataService: function(service) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateDataService.rmi";
		var parameters = [service];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteDataService: function(service) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteDataService.rmi";
		var parameters = [service];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getDataService: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getDataService.rmi";
		var parameters = [serviceId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getStoreServices: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreServices.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getDataServiceByClassName: function(storeId, className) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/getDataServiceByClassName.rmi";
		var parameters = [storeId, className];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 findDataServiceByClassName: function(storeId, className) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/findDataServiceByClassName.rmi";
		var parameters = [storeId, className];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createOperation.rmi";
		var parameters = [operation];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createServiceOperation: function(service, operation) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/createServiceOperation.rmi";
		var parameters = [service, operation];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateServiceOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateServiceOperation.rmi";
		var parameters = [operation];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateOperationsServiceClass: function(serviceId, serviceClass) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/updateOperationsServiceClass.rmi";
		var parameters = [serviceId, serviceClass];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteServiceOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteServiceOperation.rmi";
		var parameters = [operation];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getServiceOperations: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getServiceOperations.rmi";
		var parameters = [serviceId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getServiceOperation: function(operationId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getServiceOperation.rmi";
		var parameters = [operationId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createServer: function(server) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createServer.rmi";
		var parameters = [server];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateServer: function(server) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateServer.rmi";
		var parameters = [server];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteServer: function(server) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteServer.rmi";
		var parameters = [server];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getServers: function() {
		var __callback__ = arguments[0];
		var __scope__ = arguments[1] || window;
		var url = "js/tiny/store/dao/Database/getServers.rmi";
		var parameters = [];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getServerByHostURL: function(hostURL) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getServerByHostURL.rmi";
		var parameters = [hostURL];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 findServersByType: function(type) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/findServersByType.rmi";
		var parameters = [type];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 createChangeLog: function(changeLog) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createChangeLog.rmi";
		var parameters = [changeLog];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 updateChangeLog: function(changeLog) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateChangeLog.rmi";
		var parameters = [changeLog];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 deleteChangeLog: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteChangeLog.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getChangeLog: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getChangeLog.rmi";
		var parameters = [storeId];

		this.fetch(url, parameters, __callback__, __scope__);
	},

	 getChangeLogByText: function(storeId, text) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/getChangeLogByText.rmi";
		var parameters = [storeId, text];

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
