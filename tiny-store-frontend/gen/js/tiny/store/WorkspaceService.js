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
	 * Create data source.
	 *
	 * @param js.tiny.store.meta.DataSourceMeta meta,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.DataSourceMeta>
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createDataSource: function(meta) {
		$assert(typeof meta !== "undefined", "js.tiny.store.WorkspaceService#createDataSource", "Meta argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#createDataSource", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#createDataSource", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "createDataSource");
		rmi.setParameters(meta);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Test data source.
	 *
	 * @param js.tiny.store.meta.DataSourceMeta meta,
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
	 * Get data sources.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<js.tiny.store.meta.DataSourceMeta>
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getDataSources: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getDataSources", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getDataSources", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getDataSources");
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof WorkspaceService === 'undefined') {
	WorkspaceService = js.tiny.store.WorkspaceService;
}
