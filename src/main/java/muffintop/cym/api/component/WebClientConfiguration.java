package muffintop.cym.api.component;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${host.nickname}")
    private String nicknameHost;

    @Bean(name = "nicknameClient")
    public WebClient nicknameWebClient() {
        return WebClient.builder().baseUrl(nicknameHost).build();
    }

}
