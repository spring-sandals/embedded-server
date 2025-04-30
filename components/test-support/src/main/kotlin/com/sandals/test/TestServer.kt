package com.sandals.test

import com.sandals.web.EmbeddedServer

open class TestServer(port: Int, configLocations: String) {
    private val server = EmbeddedServer(port, "com.sandals.test,$configLocations")

    fun start() = server.start()

    fun stop() = server.stop()
}