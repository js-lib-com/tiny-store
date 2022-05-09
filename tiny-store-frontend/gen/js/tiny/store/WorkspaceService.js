$package("js.tiny.store");

/**
 * Workspace service.
 */
js.tiny.store.WorkspaceService = {
	/**
	 * Create store.
	 *
	 * @param js.tiny.store.meta.Store store,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.Store>
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createStore: function(store) {
		$assert(typeof store !== "undefined", "js.tiny.store.WorkspaceService#createStore", "Store argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#createStore", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#createStore", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "createStore");
		rmi.setParameters(store);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Delete store.
	 *
	 * @param js.tiny.store.meta.Store store,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.Store>
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 deleteStore: function(store) {
		$assert(typeof store !== "undefined", "js.tiny.store.WorkspaceService#deleteStore", "Store argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#deleteStore", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#deleteStore", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "deleteStore");
		rmi.setParameters(store);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Update store.
	 *
	 * @param java.lang.String projectName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 updateStore: function(projectName) {
		$assert(typeof projectName !== "undefined", "js.tiny.store.WorkspaceService#updateStore", "Project name argument is undefined.");
		$assert(projectName === null || js.lang.Types.isString(projectName), "js.tiny.store.WorkspaceService#updateStore", "Project name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#updateStore", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#updateStore", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "updateStore");
		rmi.setParameters(projectName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get stores.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.Store>
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getStores: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getStores", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getStores", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getStores");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get store.
	 *
	 * @param java.lang.String packageName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return js.tiny.store.meta.Store
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getStore: function(packageName) {
		$assert(typeof packageName !== "undefined", "js.tiny.store.WorkspaceService#getStore", "Package name argument is undefined.");
		$assert(packageName === null || js.lang.Types.isString(packageName), "js.tiny.store.WorkspaceService#getStore", "Package name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getStore", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getStore", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getStore");
		rmi.setParameters(packageName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get repositories.
	 *
	 * @param java.lang.String storePackage,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.Repository>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getRepositories: function(storePackage) {
		$assert(typeof storePackage !== "undefined", "js.tiny.store.WorkspaceService#getRepositories", "Store package argument is undefined.");
		$assert(storePackage === null || js.lang.Types.isString(storePackage), "js.tiny.store.WorkspaceService#getRepositories", "Store package argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getRepositories", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getRepositories", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getRepositories");
		rmi.setParameters(storePackage);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get repository.
	 *
	 * @param java.lang.String name,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return js.tiny.store.meta.Repository
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getRepository: function(name) {
		$assert(typeof name !== "undefined", "js.tiny.store.WorkspaceService#getRepository", "Name argument is undefined.");
		$assert(name === null || js.lang.Types.isString(name), "js.tiny.store.WorkspaceService#getRepository", "Name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getRepository", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getRepository", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getRepository");
		rmi.setParameters(name);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get services.
	 *
	 * @param java.lang.String repositoryName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.DataService>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getServices: function(repositoryName) {
		$assert(typeof repositoryName !== "undefined", "js.tiny.store.WorkspaceService#getServices", "Repository name argument is undefined.");
		$assert(repositoryName === null || js.lang.Types.isString(repositoryName), "js.tiny.store.WorkspaceService#getServices", "Repository name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getServices", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getServices", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getServices");
		rmi.setParameters(repositoryName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get entity.
	 *
	 * @param java.lang.String className,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return js.tiny.store.meta.StoreEntity
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getEntity: function(className) {
		$assert(typeof className !== "undefined", "js.tiny.store.WorkspaceService#getEntity", "Class name argument is undefined.");
		$assert(className === null || js.lang.Types.isString(className), "js.tiny.store.WorkspaceService#getEntity", "Class name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getEntity", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getEntity", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getEntity");
		rmi.setParameters(className);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get service.
	 *
	 * @param java.lang.String interfaceName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return js.tiny.store.meta.DataService
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getService: function(interfaceName) {
		$assert(typeof interfaceName !== "undefined", "js.tiny.store.WorkspaceService#getService", "Interface name argument is undefined.");
		$assert(interfaceName === null || js.lang.Types.isString(interfaceName), "js.tiny.store.WorkspaceService#getService", "Interface name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getService", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getService", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getService");
		rmi.setParameters(interfaceName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get operations.
	 *
	 * @param java.lang.String serviceInterface,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.ServiceOperation>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getOperations: function(serviceInterface) {
		$assert(typeof serviceInterface !== "undefined", "js.tiny.store.WorkspaceService#getOperations", "Service interface argument is undefined.");
		$assert(serviceInterface === null || js.lang.Types.isString(serviceInterface), "js.tiny.store.WorkspaceService#getOperations", "Service interface argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getOperations", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getOperations", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getOperations");
		rmi.setParameters(serviceInterface);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Test data source.
	 *
	 * @param js.tiny.store.meta.Repository meta,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return boolean
	 * @throws java.beans.PropertyVetoException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 testDataSource: function(meta) {
		$assert(typeof meta !== "undefined", "js.tiny.store.WorkspaceService#testDataSource", "Meta argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#testDataSource", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#testDataSource", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "testDataSource");
		rmi.setParameters(meta);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get store entities.
	 *
	 * @param java.lang.String storePackage,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.StoreEntity>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getStoreEntities: function(storePackage) {
		$assert(typeof storePackage !== "undefined", "js.tiny.store.WorkspaceService#getStoreEntities", "Store package argument is undefined.");
		$assert(storePackage === null || js.lang.Types.isString(storePackage), "js.tiny.store.WorkspaceService#getStoreEntities", "Store package argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getStoreEntities", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getStoreEntities", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getStoreEntities");
		rmi.setParameters(storePackage);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof WorkspaceService === 'undefined') {
	WorkspaceService = js.tiny.store.WorkspaceService;
}
