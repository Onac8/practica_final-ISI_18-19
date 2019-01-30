package urjc.isi.pruebasSparkJava.injector;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import urjc.isi.pruebasSparkJava.Injector;
import org.junit.*;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class insertTest {
	
	Injector connection = new Injector("JDBC_DATABASE_URL");

	//Test1: insertFilm
//	@Test 
//	public void testInsertFullFieldsMovie()
//	{
//		boolean name = false;	
//		boolean year = false;	
//		boolean resultado = false;

//		connection.insertFilm("Kill Bill: Volumen 3", "2020", "Accion");
//		name = connection.searchFilm("Kill Bill: Volumen 3");
//		year = connection.searchYear("2020");
//		resultado = name & year;
//		assertTrue("No se ha añadido a la base de datos", resultado);
//	}

	//Test3: insertFilm
	@Test (expected = NullPointerException.class)
	public void testInsertNullMovie()
	{
		connection.insertFilm(null, "2019", null);
	}

	//Test4: insertFilm
	@Test (expected = NullPointerException.class)
	public void testInsertNullMovieandYear()
	{
		connection.insertFilm(null, null, null);
	}

	//Test1: insertActor
	@Test (expected = NullPointerException.class)
	public void testInsertNullActor() throws SQLException
	{
		connection.insertActor(null);
	}


	//Test1: insertWorks_In
	//@Test (expected = NullPointerException.class)
  	//public void testInsertNullWorks() throws SQLException
	//{
    //		Main.insertWorks_In(connection, null, null);
	//}	

	//Test1: searchTitleId
	@Test
	public void testTitleIdFound() 
	{
		//titleid de entrada presente en BD
		assertTrue(connection.searchTitleId(27977));
	}
	
	//Test1: searchNameId
	@Test
	public void testNameIdFound() 
	{
		//nameid de entrada presente en BD
		assertTrue(connection.searchNameId(1410815));
	}
}
