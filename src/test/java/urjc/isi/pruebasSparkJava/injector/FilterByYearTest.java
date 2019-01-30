package urjc.isi.pruebasSparkJava.injector;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import urjc.isi.pruebasSparkJava.Injector;

public class FilterByYearTest {

	// Test fixture
	Injector connector = new Injector("JDBC_DATABASE_URL");
	private String year;
	
	
	// Test 1: A1 = T => hay películas de ese año en la BD.
	// Lo comprobamos con "2018"
	// Recorre el camino de prueba i = [1, 2, 4, 5, 6, 7, 8, 6, 7, 8, 6, 9]
	@Test
	public void test1() {
		List<String> expected = new ArrayList<String>();
		expected.add("Venom");								// movie 1
		expected.add("Ready Player One");					// movie 2
		expected.add("Black Panther");						// movie 3
		expected.add("Annihilation");						// movie 4
		expected.add("Solo: A Star Wars Story");			// movie 5
		expected.add("Avengers: Infinity War");				// movie 6
		expected.add("Jurassic World: Fallen Kingdom");		// movie 7
		expected.add("Mission: Impossible - Fallout");		// movie 8
		expected.add("Ant-Man and the Wasp");				// movie 9
		expected.add("Deadpool 2");							// movie 10
		expected.add("A Quiet Place");						// movie 11
		
		year = "2018";
    	List<String> movies = connector.filterByYear(year);
    	
		assertEquals(expected, movies);
	}
	
	
	// Test 2: A2 = F => no hay películas de ese año en la BD.
	// Lo comprobamos con "1937"
	// Recorre el camino de prueba ii = [1, 2, 4, 5, 6, 9] 
	@Test
	public void test2() {
		year = "1937";
		List<String> movies = connector.filterByYear(year);
		
		assertTrue(movies.isEmpty());
	}
}
