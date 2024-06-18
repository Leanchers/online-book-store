package mate.academy.onlinebookstore;

import java.math.BigDecimal;
import mate.academy.onlinebookstore.model.Book;
import mate.academy.onlinebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {
	@Autowired
	private BookService bookService;

	public static void main(String[] args) {
		SpringApplication.run(OnlineBookStoreApplication.class, args);
	}
	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			Book book = new Book();
			book.setTitle("War and Peace");
			book.setAuthor("Tolstoy");
			book.setPrice(BigDecimal.valueOf(199));
			book.setIsbn("12344");

			bookService.save(book);
			System.out.println("before");
			System.out.println(bookService.findAll());
			System.out.println("AFTER");
		};
	}


}
