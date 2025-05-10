package com.sandals.web

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder
import org.slf4j.LoggerFactory
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet

class EmbeddedServer(port: Int, private val configLocations: String) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val server = Server(port)

    fun start() {
        val springHandler = springServletContextHandler(configLocations)
        server.handler = HandlerList().apply { addHandler(springHandler) }
        server.stopAtShutdown = true
        Runtime.getRuntime().addShutdownHook(Thread {
            try {
                if (server.isRunning) {
                    server.stop()
                }
                logger.info("The embedded server has stopped.")
            } catch (e: Exception) {
                logger.info("Error shutting down.", e)
            }
        })
        logger.info("Starting the embedded server.")
        server.start()
    }

    private fun springServletContextHandler(configLocations: String): ServletContextHandler {
        val context = AnnotationConfigWebApplicationContext().apply {
            setConfigLocation(configLocations)
        }
        val contextHandler = ServletContextHandler().apply {
            contextPath = "/"
            addServlet(ServletHolder(DispatcherServlet(context)), "/*")
            addEventListener(ContextLoaderListener(context))
        }
        return contextHandler
    }

    fun stop() {
        logger.info("Stopping the embedded server.")
        server.stop()
    }
}