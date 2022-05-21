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

	 saveStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/saveStore.rmi";
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

	 createEntity: function(storeId, entity) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createEntity.rmi";
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

	 saveEntity: function(entity) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/saveEntity.rmi";
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

	 deleteEntity: function(entityId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/deleteEntity.rmi";
		var parameters = [entityId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			response.then(__callback__.call(__scope__));
		}
	},

	 createService: function(storeId, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createService.rmi";
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

	 saveService: function(service) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/saveService.rmi";
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

	 deleteService: function(serviceId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/deleteService.rmi";
		var parameters = [serviceId];

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

	 createOperation: function(serviceId, operation) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createOperation.rmi";
		var parameters = [serviceId, operation];

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

	 saveOperation: function(operation) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/saveOperation.rmi";
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

	 deleteOperation: function(operationId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/deleteOperation.rmi";
		var parameters = [operationId];

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
	}
};
