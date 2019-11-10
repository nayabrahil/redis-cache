package com.example.readiscache;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EnableCaching
@SpringBootApplication
public class ReadisCacheApplication {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /*@Bean
    ApplicationRunner demoData(PersonRepository personRepository) {
        return args -> {
            Person shubham = new Person("Shubham", 2000);
            Person pankaj = new Person("Pankaj", 29000);
            Person lewis = new Person("Lewis", 550);

            personRepository.save(shubham);
            personRepository.save(pankaj);
            personRepository.save(lewis);
            LOG.info("Done saving users. Data: {}.", personRepository.findAll());
        };
    }*/

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    @Bean
    ApplicationRunner demoApp(MeterRegistry mr){
        return args -> {
            this.executorService.scheduleWithFixedDelay(() ->
            {
                long ms = (long)(Math.random()*1000);
                mr.timer("transform-photo-task").record(Duration.ofMillis(ms));
            }, 500, 500, TimeUnit.MILLISECONDS);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ReadisCacheApplication.class, args);
    }

}
