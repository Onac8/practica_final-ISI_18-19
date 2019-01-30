package urjc.isi.pruebasSparkJava;

import static org.junit.Assert.*;
import org.junit.*;
import urjc.isi.pruebasSparkJava.Injector;
import urjc.isi.pruebasSparkJava.SlopeOneFilter;
import java.util.Map;
import java.util.HashMap;

public class BuildMapsTest {
	

	Injector connector;
	SlopeOneFilter sof;
	
    Map<Integer,Double> movie_score_1;
    Map<Integer,Double> movie_score_2;
    Map<Integer,Double> movie_score_3;

    @Before
    public void setUp(){
    	connector = new Injector("JDBC_DATABASE_URL");
    	sof = new SlopeOneFilter(connector);
    	movie_score_1 = new HashMap<Integer,Double>();
    	movie_score_2 = new HashMap<Integer,Double>();
    	movie_score_3 = new HashMap<Integer,Double>();
    }
    
    @After
    public void tearDown() {
    	connector.close();
    }
    
    @Test
    public void testHappyPath(){
    	sof.data = new HashMap<>();
    	movie_score_1.put(1,3.0);
  		movie_score_1.put(2,7.0);
  		movie_score_2.put(1,5.0);
  		movie_score_2.put(2,9.0);
  		sof.data.put(1,movie_score_1);
  		sof.data.put(2,movie_score_2);
  		sof.buildMaps();
  		Double diff_1 = ((3.0-7.0) + (5.0-9.0)) / 2;
  		Double diff_2 = -diff_1;
  		Map<Integer,Double> movie_1_val = sof.diffMap.get(1);
  		Map<Integer,Double> movie_2_val = sof.diffMap.get(2);
  		Double real_diff_1 = movie_1_val.get(2);
  		Double real_diff_2 = movie_2_val.get(1);
  		assertEquals("Error",real_diff_1,diff_1);
  		assertEquals("Error",real_diff_2,diff_2);
    }


    @Test (expected = IllegalArgumentException.class)
    public void testInvalidRatings(){
      sof.data = new HashMap<>();
      movie_score_1.put(1,5.0);
      movie_score_1.put(2,-3.0);
      movie_score_2.put(1,5.0);
  		movie_score_2.put(2,9.0);
      sof.data.put(1,movie_score_1);
      sof.data.put(2,movie_score_2);
      sof.buildMaps();
    }



    @Test (expected = NullPointerException.class)
    public void testNullMovie(){
      sof.data = new HashMap<>();
      movie_score_1.put(1,3.0);
      movie_score_1.put(2,7.0);
      movie_score_2.put(1,5.0);
      movie_score_2.put(null,9.0);
      sof.data.put(1,movie_score_1);
      sof.data.put(2,movie_score_2);
      sof.buildMaps();
    }

    @Test (expected = NullPointerException.class)
    public void testNullData(){
      sof.data = null;
      sof.buildMaps();
    }

}
