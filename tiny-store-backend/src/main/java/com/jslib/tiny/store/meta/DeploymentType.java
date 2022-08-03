package com.jslib.tiny.store.meta;

/**
 * Deployment scenarios that affects the way application consumes data store services at runtime.
 * 
 * @author Iulian Rotaru
 */
public enum DeploymentType {
	/**
	 * This is the canonical deployment type: data store services are deployed on own runtime environment - virtual server or
	 * container, and consumed by Java application using remote EJB. There is also a store client library (JAR) that Java
	 * application uses to facilitate data services integration.
	 * 
	 * Tiny Store generates Java source code files and descriptors for data store Maven project and push it on a Git repository.
	 * External CD tools should be used to pull data store project from Git repository, build and deploy data store services on
	 * external runtime.
	 * 
	 * Store client library is created by Tiny Store and usually deployed on a Maven repository. If private Maven repository is
	 * not configured store client library is not created. In this case store data services are consumed with a HTTP REST
	 * client. This is indeed the case for non Java applications.
	 */
	SERVICE,
	/**
	 * Data service code base is generated as for {@link #SERVICE} deployment type but is packed as web application and deployed
	 * on the same web server that runs Tiny Store. Note that for this deployment scenario there is no Git repository configured
	 * but store client library (JAR) is still created and deployed on private Maven repository, if configured. Non Java
	 * applications can still consume store data services using a HTTP REST client.
	 */
	HOSTED,
	/**
	 * Data store is packed as JPA persistence layer archive (JAR) that include entity, local interface and implementation
	 * classes and persistence descriptor. Archive is usually deployed on a private Maven repository but there is also the
	 * option to download it. Application includes persistence layer archive dependencies that with be part of application
	 * distribution artifacts.
	 * 
	 * Note that embedded persistence layer does not require external Git or Maven repositories.
	 */
	EMBEDDED
}
