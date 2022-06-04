Database = {
	 createStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createStore.rmi";
		var parameters = [store];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 updateStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateStore.rmi";
		var parameters = [store];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 deleteStore: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteStore.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 getStore: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStore.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 getStoreByName: function(name) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreByName.rmi";
		var parameters = [name];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 findStoresByOwner: function(ownerName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/findStoresByOwner.rmi";
		var parameters = [ownerName];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 createStoreEntity: function(storeId, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/createStoreEntity.rmi";
		var parameters = [storeId, entity];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 updateStoreEntity: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateStoreEntity.rmi";
		var parameters = [entity];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 deleteStoreEntity: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteStoreEntity.rmi";
		var parameters = [entity];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 getStoreEntity: function(entityId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreEntity.rmi";
		var parameters = [entityId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 getStoreEntities: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreEntities.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 createDataService: function(storeId, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/createDataService.rmi";
		var parameters = [storeId, service];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 updateDataService: function(service) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateDataService.rmi";
		var parameters = [service];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 deleteDataService: function(service) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteDataService.rmi";
		var parameters = [service];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 getDataService: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getDataService.rmi";
		var parameters = [serviceId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 getStoreServices: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getStoreServices.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 createOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createOperation.rmi";
		var parameters = [operation];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 createServiceOperation: function(service, operation) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/dao/Database/createServiceOperation.rmi";
		var parameters = [service, operation];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 updateServiceOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/updateServiceOperation.rmi";
		var parameters = [operation];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 deleteServiceOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteServiceOperation.rmi";
		var parameters = [operation];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 getServiceOperations: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getServiceOperations.rmi";
		var parameters = [serviceId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 getServiceOperation: function(operationId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getServiceOperation.rmi";
		var parameters = [operationId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	},

	 createChangeLog: function(changeLog) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/createChangeLog.rmi";
		var parameters = [changeLog];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 deleteChangeLog: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/deleteChangeLog.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 getChangeLog: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/dao/Database/getChangeLog.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	}
};
