package dev.ao.example.beans;

import com.github.agogs.holidayapi.api.APIConsumer;
import com.github.agogs.holidayapi.api.impl.HolidayAPIConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Bean
    public WebClient geoApify() {
        //        TODO: move to constants
        WebClient client = WebClient.create("https://api.geoapify.com");
        return client;
    }

    @Bean
    public APIConsumer holidayApi() {
        //        TODO: move to constants
        APIConsumer consumer = new HolidayAPIConsumer("https://holidayapi.com/v1/holidays");
        return consumer;
    }
}