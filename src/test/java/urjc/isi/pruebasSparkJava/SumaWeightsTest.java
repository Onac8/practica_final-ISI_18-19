package urjc.isi.pruebasSparkJava;

import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SumaWeightsTest {

	private String s;
	

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



// BLACK BOX TESTS

	@Test
	public void happyPath(){
		int expectedSum = 3;
		Map<Integer, Integer> w1 = new HashMap<Integer, Integer>();
                Map<Integer, Integer> w2 = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, Integer>> weights = new HashMap<Integer, Map<Integer, Integer>>();

		w1.put(3, 3);
		w2.put(2, 3);
		weights.put(1, w1);
		weights.put(1, w2);

			
		int Sum = so.sumaWeights(weights, 1);

		assertEquals(expectedSum, Sum);
	}



// WHITE BOX TESTS




}
