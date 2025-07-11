package com.example.demo;

import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlantsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantsApplication.class, args);
	}

	//1/4punktów. CRUD + Spring Security z podziałem na role : admin -dodaje i usuwa  ksiazki z ksiegarni, user możne przeglądać.
	//admin
	//dodanie i usuniecie rosliny

	//user
	//wysylanie listy wszystich roslin

	//1/4pkt punktów  CRUD + koszyk - dodawanie usuwanie książek przez Usera.
	//dodanie i usuniecie podedynczej rosliny
	//wysylanie calego koszyka koszyka

	//1/4pkt punktów Zamówienia -  Dodanie funkcji które może wykonywać User - składanie zamówień na książki, zmiana statusu zamówienia przez admina

	//1/4pkt punktów  Płatności - podpięcie np. Stripe

	@Bean
	public CommandLineRunner plants() {
		return args -> {

		};
    }

}
