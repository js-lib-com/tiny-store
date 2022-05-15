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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
		}
	},

	 deleteStore: function(id) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/deleteStore.rmi";
		var parameters = [id];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
		}
	},

	 createRepository: function(storeId, repository) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createRepository.rmi";
		var parameters = [storeId, repository];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
		}
	},

	 saveRepository: function(repository) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/saveRepository.rmi";
		var parameters = [repository];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
		}
	},

	 deleteRepository: function(id) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/deleteRepository.rmi";
		var parameters = [id];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
		}
	},

	 createService: function(repositoryId, service) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/createService.rmi";
		var parameters = [repositoryId, service];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
		}
	},

	 getStoreRepositories: function(storeId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getStoreRepositories.rmi";
		var parameters = [storeId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
		}
	},

	 getRepository: function(name) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getRepository.rmi";
		var parameters = [name];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
		}
	},

	 getRepositoryServices: function(repositoryId) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getRepositoryServices.rmi";
		var parameters = [repositoryId];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
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
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
		}
	},

	 testDataSource: function(meta) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/testDataSource.rmi";
		var parameters = [meta];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
		}
	},

	 buildProject: function(projectName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/buildProject.rmi";
		var parameters = [projectName];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			__callback__.call(__scope__);
		}
	}
};
