package urjc.isi.pruebasSparkJava.injector;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import urjc.isi.pruebasSparkJava.Injector;

public class FilterByDurationTest {

	// Test fixture
	Injector connector = new Injector("JDBC_DATABASE_URL");
	private String duration;
	
	
	// Test 1: A1 = T => hay películas con una duración menor en la BD.
	// Lo comprobamos con "150"
	// Recorre el camino de prueba i = [1, 2, 4, 5, 6, 7, 8, 6, 7, 8, 6, 9]
	@Test
	public void test1() {
		List<String> expected = new ArrayList<String>();
		expected.add("The Nightmare Before Christmas");								// movie 1
		expected.add("Corpse Bride");
		expected.add("Before Sunset");
		
		duration = "80";
    	List<String> movies = connector.filterByDuration(duration);
    	
		assertEquals(expected, movies);
	}
	
	
	// Test 2: A2 = F => no hay películas con una duración menor en la BD.
	// Lo comprobamos con "10"
	// Recorre el camino de prueba ii = [1, 2, 4, 5, 6, 9] 
	@Test
	public void test2() {
		duration = "10";
		List<String> movies = connector.filterByDuration(duration);
		
		assertTrue(movies.isEmpty());
	}
}
