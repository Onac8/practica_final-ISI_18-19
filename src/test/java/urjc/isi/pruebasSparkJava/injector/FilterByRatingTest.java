package urjc.isi.pruebasSparkJava.injector;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import urjc.isi.pruebasSparkJava.Injector;

public class FilterByRatingTest {

	// Test fixture
	Injector connector = new Injector("JDBC_DATABASE_URL");
	private String rating;

	
	// Test 1: Para probar que funciona el método filterByRating, lo que voy a hacer es pasarle como argumento un 0.1
	// y me tendrá que devolver un ArrayList de mas de 1 posición
	@Test
	public void test1() {
		rating = "0.1";
		List<String> movies = connector.filterByRating(rating);
		
		assertTrue(movies.size()>0);
	}
	
	
	// Test 2: A2 = F => no hay películas con una valoración mayor en la BD.
	// Lo comprobamos con "10.1"
	// Recorre el camino de prueba i = [1, 2, 4, 5, 6, 9] 
	@Test
	public void test2() {
		rating = "10.1";
		List<String> movies = connector.filterByRating(rating);
		
		assertTrue(movies.isEmpty());
	}
	//No podemos hacer ningún otro test de prueba ya que las calificaciones van cambiando dinamicamente y no siempre
	//son las mismas salidas
}
