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

	 deleteStore: function(store) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/deleteStore.rmi";
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

	 getStore: function(packageName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getStore.rmi";
		var parameters = [packageName];

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

	 getRepositories: function(storePackage) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getRepositories.rmi";
		var parameters = [storePackage];

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

	 getServices: function(repositoryName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getServices.rmi";
		var parameters = [repositoryName];

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

	 getService: function(interfaceName) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getService.rmi";
		var parameters = [interfaceName];

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

	 getOperations: function(serviceInterface) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getOperations.rmi";
		var parameters = [serviceInterface];

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

	 getOperation: function(serviceInterface, name) {
		var __callback__ = arguments[2];
		var __scope__ = arguments[3] || window;
		var url = "js/tiny/store/WorkspaceService/getOperation.rmi";
		var parameters = [serviceInterface, name];

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

	 getStoreEntities: function(storePackage) {
		var __callback__ = arguments[1];
		var __scope__ = arguments[2] || window;
		var url = "js/tiny/store/WorkspaceService/getStoreEntities.rmi";
		var parameters = [storePackage];

		var response = fetch(url, {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify(parameters)
		});

		if (__callback__) {
			var body = response.then(response => response.json());
			body.then(data => __callback__.call(__scope__, data));
		}
	}
};
