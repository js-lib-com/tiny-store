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
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
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
			var json = response.then(response => response.json());
			json.then(data => __callback__.call(__scope__, data));
		}
	}
};
