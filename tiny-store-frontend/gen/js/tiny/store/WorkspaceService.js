$package("js.tiny.store");

/**
 * Workspace service.
 */
js.tiny.store.WorkspaceService = {
	/**
	 * Get projects.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.Set<js.tiny.store.ProjectItem>
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getProjects: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#getProjects", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#getProjects", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "getProjects");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Build client jar.
	 *
	 * @param java.lang.String repositoryName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 buildClientJar: function(repositoryName) {
		$assert(typeof repositoryName !== "undefined", "js.tiny.store.WorkspaceService#buildClientJar", "Repository name argument is undefined.");
		$assert(repositoryName === null || js.lang.Types.isString(repositoryName), "js.tiny.store.WorkspaceService#buildClientJar", "Repository name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "js.tiny.store.WorkspaceService#buildClientJar", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "js.tiny.store.WorkspaceService#buildClientJar", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("js.tiny.store.WorkspaceService", "buildClientJar");
		rmi.setParameters(repositoryName);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof WorkspaceService === 'undefined') {
	WorkspaceService = js.tiny.store.WorkspaceService;
}
