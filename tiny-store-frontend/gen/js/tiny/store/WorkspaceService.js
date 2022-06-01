WorkspaceService = {
	 createStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/createStore.rmi";
		var parameters = [store];

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

	 updateStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/updateStore.rmi";
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
		var url = "js/tiny/store/WorkspaceService/deleteStore.rmi";
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

	 createStoreEntity: function(storeId, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createStoreEntity.rmi";
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
		var url = "js/tiny/store/WorkspaceService/updateStoreEntity.rmi";
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
		var url = "js/tiny/store/WorkspaceService/deleteStoreEntity.rmi";
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

	 createDataService: function(storeId, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createDataService.rmi";
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

	 createDaoService: function(storeId, entity, service) {
		var __callback__ = arguments[3];
		var __scope__ = arguments[4] || window;
		var url = "js/tiny/store/WorkspaceService/createDaoService.rmi";
		var parameters = [storeId, entity, service];

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
		var url = "js/tiny/store/WorkspaceService/updateDataService.rmi";
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
		var url = "js/tiny/store/WorkspaceService/deleteDataService.rmi";
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

	 getStores: function() {
		var __callback__ = arguments[0];
		var __scope__ = arguments[1] || window;
		var url = "js/tiny/store/WorkspaceService/getStores.rmi";
		var parameters = [];

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

	 getStore: function(id) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getStore.rmi";
		var parameters = [id];

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
		var url = "js/tiny/store/WorkspaceService/getStoreServices.rmi";
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

	 getEntity: function(className) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getEntity.rmi";
		var parameters = [className];

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
		var url = "js/tiny/store/WorkspaceService/getStoreEntities.rmi";
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

	 getService: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getService.rmi";
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

	 createServiceOperation: function(service, operation) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createServiceOperation.rmi";
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
		var url = "js/tiny/store/WorkspaceService/updateServiceOperation.rmi";
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
		var url = "js/tiny/store/WorkspaceService/deleteServiceOperation.rmi";
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
		var url = "js/tiny/store/WorkspaceService/getServiceOperations.rmi";
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

	 getOperation: function(operationId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getOperation.rmi";
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

	 testDataSource: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/testDataSource.rmi";
		var parameters = [store];

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

	 buildProject: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/buildProject.rmi";
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

	 commitChanges: function(storeId, message) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/commitChanges.rmi";
		var parameters = [storeId, message];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 pushChanges: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/pushChanges.rmi";
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
		var url = "js/tiny/store/WorkspaceService/getChangeLog.rmi";
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
