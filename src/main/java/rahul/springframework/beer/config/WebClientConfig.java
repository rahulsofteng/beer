package rahul.springframework.beer.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import static rahul.springframework.beer.config.WebClientProperties.BASE_URL;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
                        .wiretap("reactor.netty.client.HttpClient", LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)))
                .build();
    }
}
