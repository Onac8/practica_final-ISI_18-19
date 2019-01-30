package urjc.isi.pruebasSparkJava;

import static org.junit.Assert.*;
import org.junit.*;

public class GetNPredictedTest {
	
	int user;
	int nItems;
	String retString;
	String expString;
	Injector connector;
	SlopeOneFilter so;

	@Before
	public void setUp()
	{
		user = 0;
		nItems = 0;
		retString = null;
		expString = null;
		connector = new Injector("JDBC_DATABASE_URL");
		so = new SlopeOneFilter(connector);
	}
	
	@After
	public void tearDown()
	{
		user = 0;
		nItems = 0;
		retString = null;
		expString = null;
		connector.close();
	}

	@Test
	public void InvalidUser()
	{
		user = 0;
		nItems = 0;
		expString = "SlopeOneFilter.getNPredicted: NullPointerException";
		
		try {
			retString = so.getNPredicted(user, nItems);
		} catch (NullPointerException e) {
			return;
		}
		assertEquals(expString, retString);
	}

	@Test
	public void testForZeroNItems()
	{
		user = 1;
		nItems = 0;
		expString = "";
		retString = so.getNPredicted(user, nItems);
		assertEquals(expString, retString);
	}
	
	@Test
	public void testForEmptyPredictions()
	{		
		user = 1;
		nItems = 5;
		expString = "";
		so.predictions.get(user).clear();
		retString = so.getNPredicted(user, nItems);
		assertEquals(expString, retString);
	}
	
	@Test
	public void testForTwoEntryPredictions()
	{		
		user = 1;
		nItems = 2;
		expString = "<tr><td>titleID: 1, Predicción: 5.00</td></tr><tr><td>titleID: 2, Predicción: 5.00</td></tr>";
		so.predictions.get(user).clear();
		so.predictions.get(user).add(so.new Node(1, 5.0));
		so.predictions.get(user).add(so.new Node(2, 5.0));
		retString = so.getNPredicted(user, nItems);
		assertEquals(expString, retString);
	}
	
	@Test
	public void testForNItemsGreaterThanPredictionsEntries()
	{		
		user = 1;
		nItems = 5;
		expString = "<tr><td>titleID: 1, Predicción: 5.00</td></tr><tr><td>titleID: 2, Predicción: 5.00</td></tr>";
		so.predictions.get(user).clear();
		so.predictions.get(user).add(so.new Node(1, 5.0));
		so.predictions.get(user).add(so.new Node(2, 5.0));
		retString = so.getNPredicted(user, nItems);
		assertEquals(expString, retString);
	}
}
