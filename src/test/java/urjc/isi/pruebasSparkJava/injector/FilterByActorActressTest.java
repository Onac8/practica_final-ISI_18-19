package urjc.isi.pruebasSparkJava.injector;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import urjc.isi.pruebasSparkJava.Injector;

public class FilterByActorActressTest {
	
	// Test fixture
	Injector connector = new Injector("JDBC_DATABASE_URL");
	private String name;
	
	
	// Test 1: A1 = T => hay películas con ese/a actor/actriz en la BD.
	// Lo comprobamos con "Jennifer Lawrence"
	// Recorre el camino de prueba i = [1, 2, 4, 5, 6, 7, 8, 6, 7, 8, 6, 9]
	@Test
	public void test1() {
		List<String> expected = new ArrayList<String>();
		expected.add("X-Men: Apocalypse");							// movie 1
		expected.add("The Hunger Games: Mockingjay - Part 2");		// movie 2
		expected.add("The Hunger Games: Mockingjay - Part 1");		// movie 3
		expected.add("The Hunger Games: Catching Fire");			// movie 4
		expected.add("American Hustle");							// movie 5
		expected.add("The Hunger Games");							// movie 6
		expected.add("Passengers");									// movie 7
		expected.add("X-Men: First Class");							// movie 8
		
		name = "Jennifer Lawrence";
    	List<String> movies = connector.filterByActorActress(name);
    	
		assertEquals(expected, movies);
	}
	
	
	// Test 2: A2 = F => no hay películas con ese/a actor/actriz en la BD.
	// Lo comprobamos con "Lady Gaga"
	// Recorre el camino de prueba ii = [1, 2, 4, 5, 6, 9] 
	@Test
	public void test2() {
		name = "Lady Gaga";
		List<String> movies = connector.filterByActorActress(name);
		
		assertTrue(movies.isEmpty());
	}
}
