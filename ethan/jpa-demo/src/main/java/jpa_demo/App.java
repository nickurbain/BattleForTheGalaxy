package jpa_demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App 
{
	private static final Logger log = LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class);
    }
    
    @Bean
    public CommandLineRunner demo(PersonRepo repository) {
    	return (args) -> {
			repository.save(new Person("Jack", "Bauer"));
			repository.save(new Person("Chloe", "O'Brian"));
			repository.save(new Person("Kim", "Bauer"));
			repository.save(new Person("David", "Palmer"));
			repository.save(new Person("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Person person: repository.findAll()) {
				log.info(person.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Person person = repository.findOne(1L);
			log.info("Customer found with findOne(1L):");
			log.info("--------------------------------");
			log.info(person.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (Person bauer : repository.findByLastName("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");
		};
    }
}
