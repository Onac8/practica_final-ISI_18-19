package urjc.isi.pruebasSparkJava.injector;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import urjc.isi.pruebasSparkJava.Injector;

public class FilterByNameTest {

	// Test fixture
	Injector connector = new Injector("JDBC_DATABASE_URL");
	private String name;
	
	
	// Test 1: A1 = T => el nombre de la película está en la BD.
	// Lo comprobamos con "Avengers: Infinity War"
	// Recorre el camino de prueba i = [1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
	@Test
	public void test1() {
		List<String> expected = new ArrayList<String>();
		expected.add("Avengers: Infinity War");			// title
		expected.add("2018");							// year
		expected.add("149");							// runtime_minutes
		expected.add("8.5");							// average_rating
		expected.add("538571");							// num_votes
		expected.add("Action,Adventure,Fantasy");		// genres
		expected.add("4154756");						// titleid
		
		name = "Avengers: Infinity War";
    	List<String> movieFields = connector.filterByName(name);
    	
		assertEquals(expected, movieFields);
	}
	
	
	// Test 2: A2 = F => el nombre de la película no está en la BD.
	// Lo comprobamos con "A Star Is Born"
	// Recorre el camino de prueba ii = [1, 2, 4, 5, 6, 15] 
	@Test
	public void test2() {
		name = "A Star Is Born";
		List<String> movieFields = connector.filterByName(name);
		
		assertTrue(movieFields.isEmpty());
	}
}
