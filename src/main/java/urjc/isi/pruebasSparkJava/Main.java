package urjc.isi.pruebasSparkJava;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;
import javax.servlet.MultipartConfigElement;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main {
	
    // Connection to the SQLite database. Used by insert and select methods.
    // Initialized in main
    private static Connection connection;
	
    static int getHerokuAssignedPort() {
    	ProcessBuilder processBuilder = new ProcessBuilder();
    	if (processBuilder.environment().get("PORT") != null) {
    		return Integer.parseInt(processBuilder.environment().get("PORT"));
    	}
    	return 4600; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    
    // Used to illustrate how to route requests to methods instead of
    // using lambda expressions
    public static String doSelect(Request request, Response response) {
    	return select (connection, request.params(":table"), request.params(":film"));
    }
    
    public static String select(Connection conn, String table, String film) {
    	String sql = "SELECT * FROM " + table + " WHERE film=?";
    	String result = new String();
	
    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, film);
    		ResultSet rs = pstmt.executeQuery();
    		// Commit after query is executed
    		connection.commit();

    		while (rs.next()) {
    			// read the result set
    			result += "film = " + rs.getString("film") + "\n";
    			System.out.println("film = "+rs.getString("film") + "\n");

    			result += "actor = " + rs.getString("actor") + "\n";
    			System.out.println("actor = "+rs.getString("actor")+"\n");
    		}
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return result;
    }
    
    
    public static void insert(Connection conn, String film, String actor) {
    	String sql = "INSERT INTO films(film, actor) VALUES(?,?)";

    	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setString(1, film);
    		pstmt.setString(2, actor);
    		pstmt.executeUpdate();
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    public static String infoPost(Request request, Response response) throws 
    		ClassNotFoundException, URISyntaxException {
    	String result = new String("TODA LA INFORMACIÓN QUE QUIERAS SOBRE PELÍCULAS"
        							+ " A TRAVÉS DE UN POST");
    	return result;
    }

    public static String infoGet(Request request, Response response) throws
    		ClassNotFoundException, URISyntaxException {
    	String result = new String("TODA LA INFORMACIÓN QUE QUIERAS SOBRE PELÍCULAS"
        							+ " A TRAVÉS DE UN GET");
    	return result;
    }

    public static String doWork(Request request, Response response) throws
    		ClassNotFoundException, URISyntaxException {
    	String result = new String("Hello World");
    	return result;
    }
    
    
    /**
     * Calcula la distancia entre actores o pelicuas.
     * @param graph sobre el que calcular la distancia
     * @param name1 para buscar y comparar
     * @param name2 para buscar y comparar
     * @return String con la distancia y la ruta, u otro string en caso de error o 'no ruta'.
     */
    public static String doDistance(Graph graph, String name1, String name2) {
    	if (graph.V() == 0) throw new NullPointerException("Main.doDistance");
    	
    	String result = new String("");    	
    	try {
	    	if (name1.equals("") || name2.equals("")){ //caso de no introducir nada
	    		throw new IllegalArgumentException("Main.doDistance");
	    	}else if (!graph.hasVertex(name1) || !graph.hasVertex(name2)){ //no coincidencia
				result = nameChecker(name1, name2); //Control de nombres
			}else{
				PathFinder pf = new PathFinder(graph, name1);
				if (pf.hasPathTo(name2)) { //si tenemos ruta, procedemos	
					System.out.println(pf.hasPathTo(name2));
					String edge = " --> ";
					for (String v : pf.pathTo(name2)) {
						result += v + edge;
					}       
					result = result.substring(0, result.length() - edge.length());
					result += "<br><br>Distancia = " + pf.distanceTo(name2);
				} else {
					result = "<p>Ninguna ruta disponible entre " + name1 + " y " + name2 + ".</p>";
				}
			}
    	}catch(IllegalArgumentException e) {
    		result ="<p>ERROR. Ver 'uso'. Por favor, inténtalo de nuevo.</p>";
    	}
		return result;
	}
    
    /**
     * Comprueba si se han introducido nombres incorrectos/incompletos 
     * (p.e. 'Crush, Tom' en vez de 'Cruise, Tom', o no coincidente como 'abcd').
     * @param name1 para buscar y comparar
     * @param name2 para buscar y comparar
     * @return Nombres coincidentes con parte de los strings dados (p.e. devuelve todos los
     * 'Tom' de la tabla, en el caso de haber introducido 'Tom Crush' en vez de 'Tom Cruise'.
     * Devuelve string de 'no coincidencia' en caso de que no exista nada similar en la BD.
     */
    public static String nameChecker(String name1, String name2) {
    	//por el momento no implementado. Necesitamos BD para Select.
    	//si no resultados --> result = "no existen nombres name1 y name 2. intentalo de nuevo".
    	String result = new String("<p>Ninguna ruta disponible entre '" + name1 + "' y '" + 
    								name2 + "'. Error al introducir nombres, o no existen en " +
    								"nuestra BD</p>");
    	return result;
    }
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	// Establecemos el puerto del server con el método getHerokuAssignedPort()
    	port(getHerokuAssignedPort());

    	// Connect to SQLite sample.db database
    	// connection will be reused by every query in this simplistic example
    	connection = DriverManager.getConnection("jdbc:sqlite:Database/IMDb.db");

    	// SQLite default is to auto-commit (1 transaction / statement execution)
    	// Set it to false to improve performance
    	connection.setAutoCommit(false);

    	String home = "<html><body>" +
    		"<h1>Bienvenidos a la web de películas</h1>" +
    			"<form action='/info' method='post'>" +
    				"<div class='button'>" +
    					"Ir a info: <br/>" +
    					"<button type='submit'>Información</button>" +
    				"</div>" +
    			"</form></br>"+
    			"<form action='/hello' method='get'>" +
    				"<div class='button'>" +
    					"Ir a helloWorld: <br/>" +
    					"<button type='submit'>Hello</button>" +
    				"</div>" +
    			"</form>" +
    			"<form action='/upload_films' method='get'>" +
    				"<div class='button'>" +
    					"Subir fichero con películas: <br/>" +
    					"<button type='submit'>Upload Films</button>" +
    				"</div>" +
    			"</form>" +
    			"<form action='/addfilms' method='get'>" +
    				"<div class='button'>" +
    					"Añade película: <br/>" +
    					"<button type='submit'>Add Films</button>" +
    				"</div>" +
    			"</form>" +
    			"<a href='/filter'>Filtrado</a>" +
    			"<br><br>" + 
				"<a href= '/distance'>Distancia entre actores, películas, directores...<a/>" +
    		"</body></html>";

        // spark server
        get("/", (req, res) -> home);
        get("/info", Main::infoGet);
        post("/info", Main::infoPost);
        get("/hello", Main::doWork);

        // In this case we use a Java 8 method reference to specify
        // the method to be called when a GET /:table/:film HTTP request
        // Main::doWork will return the result of the SQL select
        // query. It could've been programmed using a lambda
        // expression instead, as illustrated in the next sentence.
        get("/:table/:film", Main::doSelect);

        // In this case we use a Java 8 Lambda function to process the
        // GET /upload_films HTTP request, and we return a form
        get("/upload_films", (req, res) -> 
        	"<form action='/upload' method='post' enctype='multipart/form-data'>" 
        	+ "    <input type='file' name='uploaded_films_file' accept='.txt'>"
        	+ "    <button>Upload file</button>" + "</form>");
        // You must use the name "uploaded_films_file" in the call to
        // getPart to retrieve the uploaded file. See next call:

        // Retrieves the file uploaded through the /upload_films HTML form
        // Creates table and stores uploaded file in a two-columns table
        post("/upload", (req, res) -> {
        	req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
        	String result = "File uploaded!";
        	
        	try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()) { 
        		// getPart needs to use the same name "uploaded_films_file" used in the form

        		// Prepare SQL to create table
        		Statement statement = connection.createStatement();
        		statement.setQueryTimeout(30); // set timeout to 30 sec.
        		statement.executeUpdate("drop table if exists films");
        		statement.executeUpdate("create table films (film string, actor string)");

        		// Read contents of input stream that holds the uploaded file
        		InputStreamReader isr = new InputStreamReader(input);
        		BufferedReader br = new BufferedReader(isr);
        		String s;
        		
        		while ((s = br.readLine()) != null) {
        			System.out.println(s);

        			// Tokenize the film name and then the actors, separated by "/"
        			StringTokenizer tokenizer = new StringTokenizer(s, "/");

        			// First token is the film name(year)
        			String film = tokenizer.nextToken();

        			// Now get actors and insert them
        			while (tokenizer.hasMoreTokens()) {
        				insert(connection, film, tokenizer.nextToken());
        			}
        			// Commit only once, after all the inserts are done
        			// If done after each statement performance degrades
        			connection.commit();
        		}
        		input.close();
        	}
        	return result;
        });

        get("/addfilms", (req, res) ->
    		"<div style='color:#1A318C'><b>PÁGINA PARA AÑADIR PELÍCULA A LA BASE DE DATOS:</b>"
    		+"<form action='/add_films' method='post'>" +
    		"<label for='film'>Película que desea añadir: </label>" + //required: campo obligatorio
    		"<input type='text' required name='film' id='film'" +
    		"pattern=[A-Za-z0-9 ].{1,}>" + //Excluimos todos lo que no sean letras y numeros, y no se puede dejar el campo vacio.
    		"<p></p>"+
    		"<form action='/add_films' method='post'>" +
    		"<label for='year'>Año: </label>" + 
    		"<input type='text' required name='year' id='year'" + //required: Campo obligatorio
    		"pattern=[0-9]{4}>" + //Solo se pueden introducir 4 cifras
    		"<p></p>"+
    		"<form action='/add_films' method='post'>" +
    		"<label for='genres'>Género: </label>" + 
    		"<input type='text' name='genres' id='genres'" +
    		"pattern=[A-Za-z]{0,}>" +
    		"<p></p>"+
    		"<form action='/add_films' method='post'>" +
    		"<label for='actor'>Actor: </label>" + 
    		"<input type='text' name='actor' id='actor'" +
    		"pattern=[A-Za-z]{0,}>" +
    		"<p><input type='submit' value='Enviar'></p>" +
		"</form>");
        //Incluido formulario para añadir películas
        
        post("/add_films", (req, res) -> {
        	String result = "Has añadido ->"
        		+ "</p>pelicula: " + req.queryParams("film")
        		+ "</p>year: " + req.queryParams("year") 
        		+ "</p>Género: " + req.queryParams("genres")
        		+ "</p>Actor: " + req.queryParams("actor");
        	return result;	
        });
        
        // Recurso /filter encargado de la funcionalidad del filtrado
        get("/filter", (req, res) ->
        	"<form action='/filter_film' method='post'>" +
        		"<label for='film'>Película que desea buscar: </label>" + 
        		"<input type='text' name='film' id='film'> " +
        		"<input type='submit' value='Enviar'>" +
    		"</form>"
        );
        
        post("/filter_film", (req, res) -> {
        	// Con el atributo queryParams accedemos al valor del parametro "film" del form
        	return "Has buscado: " + req.queryParams("film");
        });
        
        
        get("/distance", (req, res) -> {
        	String form = 
        		"<h3>Calculador de distancias mediante grafos</h3> " +
        		"<form action='/distance_show' method='post'>" +
    				"<div>" + 
    					"<label for='name'>Nombre del actor o película (1): </label>" +
    					"<input type='text' name='name1'/><br>" +
    					"<label for='name'>Nombre del actor o película (2): </label>" +
    					"<input type='text' name='name2'/><br>" +
    					"<button type='submit'>Enviar</button>" +
    				"</div>" +
    			"</form>" +
        		"<br><p><u>--Uso--</u></p>" + 
        		"<ul>" + 
    			  "<li>Pelicula --> (1):'NombrePeli1 (Año)' | (2): 'NombrePeli2 (Año)'" + 
    			  "<br>Ejemplo: '2001: A Space Odyssey (1968)'</li>" +
    			  "<br>" +
    			  "<li>Actor --> (1):'Apellido1, Nombre1' | (2): 'Apellido2, Nombre2" +
    			  "<br>Ejemplo: 'Travolta, John'</li>" +
    			"</ul>" +
        		"*Nota* Si no se sabe el nombre exacto, poner una palabra (p.e. 'Travolta' " +
    			"u 'Odyssey' en este caso). Se ofreceran las coincidencias de esa palabra.";
    		return form;
        });
        
        post("/distance_show", (req, res) -> {
    		Graph graph = new Graph("data/other-data/moviesG.txt", "/");
    		String name1= req.queryParams("name1");
    		String name2 = req.queryParams("name2");
    		String result = doDistance(graph, name1, name2);    		
        	return "<p>Has buscado la distancia entre: '" + 
        			name1 + "' y '" + name2 + "'.</p>" +
        			"<p>RESULTADO:</p>" + 
        			result + 
        			"<br><a href='/'>Volver</a>";
        	
        	//EJEMPLO:
        	//Travolta, John (That's Dancing! (1985)) --> NAME 1
        	//Garland, Judy (mago de oz, dancing dancing) --> actriz que relaciona
        	//Burke, Billie (mago de oz) --> NAME 2
        	//Distancia 4
        });
    }
}

