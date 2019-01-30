package urjc.isi.pruebasSparkJava;
import org.junit.*;
import urjc.isi.pruebasSparkJava.Injector;
import urjc.isi.pruebasSparkJava.SlopeOneFilter;


public class UpdateDataTest {
	Injector connector; 
	SlopeOneFilter sof; 
	
	@Before
	public void setUp() {
		connector = new Injector("JDBC_DATABASE_URL");
		sof = new SlopeOneFilter(connector);
	}
	
    @After
    public void tearDown() {
    	connector.close();
    }
    
	//Camino 1,3,11: Integer.doubleValue throws exception
	@Test
	public void testPath1() {
		sof.updateData(null,1,120363);
	}
	
	//Camino 1 2 6 3 11: score < 0 throws exception
	@Test
	public void testPath2() {
		sof.updateData(-4, 1, 120363);
	}
	
	//Camino 1 2 5 3 11: score > 10 throws exception
	@Test
	public void testPath3() {
		sof.updateData(11, 1, 120363);
	}
	
	//Camino 1 2 4 7 9 10 11 10 12: usuario no contenido
	@Test
	public void testPath4() {
		sof.updateData(7, 300, 120363);
	}
	
	//Camino 1 2 8 9 10 11 10 12: usuario contenido
	@Test
	public void testPath5() {
		sof.updateData(6, 1, 120363);
	}
	
	
	//Camino 1 2 8 13 9 10 11 10 12
	@Test
	public void testPath6() {
		sof.updateData(8, 1, 84787);
	}
	
	
}
