package com.critter.cam;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import io.helidon.config.Config;
import io.helidon.health.HealthSupport;
import io.helidon.health.checks.HealthChecks;
import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.metrics.MetricsSupport;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

public final class Main {
	
	private Main() {		
	}
	
	//initial project startup code prior to helidon
	static String introduction(String intro) {
		if (intro.length() == 2) {
			return "hello to you";
		} else
			return "goodbye";
	}
		
	public static void main(final String[] args) throws IOException {
		System.out.println(introduction("hi"));
		startServer();
	}
	
	/**
     * Start the server.
     * @return the created {@link WebServer} instance
     * @throws IOException if there are problems reading logging properties
     */
	static WebServer startServer() throws IOException {
		//load logging configuration
		setupLogging();
		
		//by default this will pickup application.yaml from classpath
		Config config = Config.create();
		
		//build server with JSONP support
		WebServer server = WebServer.builder(createRouting(config))
				.config(config.get("server"))
				.addMediaSupport(JsonpSupport.create())
				.build();
		
		// Try to start the server. If successful, print some info and arrange to
        // print a message at shutdown. If unsuccessful, print the exception.
		server.start()
			.thenAccept(ws -> {
				System.out.println(
						"Web Server is up! http://localhost:" + ws.port() + "/greet");
				ws.whenShutdown().thenRun(() -> System.out.println("Web Server is DOWN. Good Bye!"));
			})
			.exceptionally(t -> {
				System.err.println("Startup failed: " + t.getMessage());
				t.printStackTrace(System.err);
				return null;
			});
		// Server threads are not daemon. No need to block. Just react.
		return server;		
	}
	
	/**
     * Creates new {@link Routing}.
     *
     * @return routing configured with JSON support, a health check, and a service
     * @param config configuration of this server
     */
	private static Routing createRouting(Config config) {
		MetricsSupport metrics = MetricsSupport.create();
		GreetService greetService = new GreetService(config);
		HealthSupport health = HealthSupport.builder()
				.addLiveness(HealthChecks.healthChecks())
				.build();
		
		return Routing.builder()
				.register(health)	// Health at "/health"
				.register(metrics)	// Metrics at "/metrics"
				.register("/greet", greetService)
				.build();
	}

	private static void setupLogging() throws IOException{
		try (InputStream is = Main.class.getResourceAsStream("/logging.properties")) {
			LogManager.getLogManager().readConfiguration(is);
		}
		
	}

}
