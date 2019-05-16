package com.xti.eventsourcingbackend;

import com.xti.eventsourcingbackend.repository.inheritance.InheritanceAwareMongoRepositoryFactoryBean;
import com.xti.eventsourcingbackend.repository.inheritance.InheritanceAwareSimpleMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableMongoRepositories(
        repositoryBaseClass = InheritanceAwareSimpleMongoRepository.class,
        repositoryFactoryBeanClass = InheritanceAwareMongoRepositoryFactoryBean.class)
public class EventsourcingbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventsourcingbackendApplication.class, args);
    }

}
