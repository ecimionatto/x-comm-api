package com.ecimio.xcomm;

import com.ecimio.xcomm.endpoint.CommunicationEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableScheduling
@EnableWebFlux
public class BmxComRouter implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("PUT", "GET", "POST" ,"DELETE")
                .maxAge(3600);
    }

    @Bean
    public RouterFunction<ServerResponse> route(@Autowired CommunicationEndpoint communicationHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/communication/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), communicationHandler::get)
                .andRoute(
                        RequestPredicates.PUT("/communication/{id}")
                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), communicationHandler::put);
    }

}
