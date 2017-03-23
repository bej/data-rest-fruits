package de.derjonk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;

@SpringBootApplication
@Configuration
public class DataRestFruitsApplication {

	@Autowired
	private BasketRepository basketRepository;

	public static void main(String[] args) {
		SpringApplication.run(DataRestFruitsApplication.class, args);
	}

	@PostConstruct
	public void setup() {
		final Basket basket = new Basket("grandmas basket");
		basket.setFruits(Arrays.asList(new Apple("green apple"), new Orange("orange orange"), new Apple("red apple")));

		basketRepository.save(basket);

	}
}
