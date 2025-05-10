package com.sandals.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
open class TestServerWebConfigurer : WebMvcConfigurer {
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(MappingJackson2HttpMessageConverter(ObjectMapper()))
    }
}