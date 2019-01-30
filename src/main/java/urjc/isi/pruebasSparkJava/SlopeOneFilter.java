package urjc.isi.pruebasSparkJava;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import spark.Request;

/**
 * @author nshandra, jaimefdez96, AlbertoCoding
 * <p>
 * data: userID | titleID | score<br>
 * diffMap: titleID-A | titleID-B | difference<br>
 * weightMap: titleID-A | titleID-B | weight<br>
 * predictions: userID | titleID | prediction
 */
public class SlopeOneFilter {

	Map<Integer, Map<Integer, Double>> data;
	Map<Integer, Map<Integer, Double>> diffMap;
	Map<Integer, Map<Integer, Integer>> weightMap;
	Map<Integer, LinkedList<Node>> predictions;

	class Node {
		int titleID;
		double prediction;

		public Node(int t, double p) {
			titleID = t;
			prediction = p;
		}

		public int getKey() {
			return titleID;
		}

		public double getValue() {
			return prediction;
		}

		public String toString() {
			DecimalFormat numberFormat = new DecimalFormat("#.00");
			return "titleID: " + getKey() + ", Predicción: " + numberFormat.format(getValue());
		}
	}

	class NodeComp implements Comparator<Node> {
		@Override
		public int compare(Node d1, Node d2) {
			if(d2.prediction<d1.prediction) {
				return 1;
			} else if(d1.prediction<d2.prediction) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public SlopeOneFilter(Injector connector) {
		data  = new HashMap<>();
		predictions = new HashMap<Integer, LinkedList<Node>>();

		connector.makeDataHashMap(data);

		buildMaps();
		for (int user : data.keySet()){
			predict(user);
		}
	}

	//Introduzco las diferencias totales de cada pareja de películas: movie_A movie_B total_diff
	public void setDiffMap(int movie_A, int movie_B, Double diff, Map<Integer,Double> movie_diff) {
		if(!diffMap.containsKey(movie_A)) {
			movie_diff = new HashMap<Integer,Double>();
			movie_diff.put(movie_B,diff);
			diffMap.put(movie_A,movie_diff);
		}else{
			movie_diff = diffMap.get(movie_A);
			if(!movie_diff.containsKey(movie_B)) {
				movie_diff.put(movie_B,diff);
			}else {
				movie_diff.put(movie_B,diff + movie_diff.get(movie_B));
			}
		}
	}

	//Introduzco la frecuencia de cada pareja de peliculas: movie_A movie_B weight
	public void setWeightMap(int movie_A, int movie_B,Map<Integer,Integer> movie_weight) {
		if(!weightMap.containsKey(movie_A)) {
			movie_weight = new HashMap<Integer,Integer>();
			movie_weight.put(movie_B,1);
			weightMap.put(movie_A,movie_weight);
		}else {
			movie_weight = weightMap.get(movie_A);
			if(!movie_weight.containsKey(movie_B)) {
				movie_weight.put(movie_B,1);
			}else {
				movie_weight.put(movie_B,movie_weight.get(movie_B) + 1);
			}
		}
	}

	//Calculo y establezco la diferencia promedio de cada pareja apoyandome de la frecuencia
	public void setAvgDiff() {
		for(Entry<Integer, Map<Integer, Double>> entry: diffMap.entrySet()) {
			int movie_A = entry.getKey();
			Map<Integer,Double> movie = entry.getValue();
			for(Entry<Integer, Double> my_movie: movie.entrySet()) {
				Map<Integer,Integer> movie_weight = weightMap.get(movie_A);
				int movie_B = my_movie.getKey();
				int weight = movie_weight.get(movie_B);
				my_movie.setValue(my_movie.getValue()/weight);
			}
		}
	}

	public void buildMaps() throws RuntimeException{
		// Crear diffMap y weightMap a partir de data.
		diffMap = new HashMap<Integer,Map<Integer,Double>>();
		weightMap = new HashMap<Integer,Map<Integer,Integer>>();
		Map<Integer,Double> movie_B_diff = new HashMap<Integer,Double>();
		Map<Integer,Integer> movie_B_weight = new HashMap<Integer,Integer>();


		for(Map<Integer, Double> user_movies: data.values()) {
			for(Entry<Integer, Double> movie: user_movies.entrySet()) {
				int movie_A = movie.getKey();
				for(Entry<Integer, Double> other_movie: user_movies.entrySet()) {
					int movie_B = other_movie.getKey();

					if(movie_A == movie_B) {
						continue;
					}

					Double score_A = movie.getValue();
					Double score_B = other_movie.getValue();
					Double diff = score_A - score_B;

					if(((score_A < 0) || (score_A > 10)) || ((score_B < 0) || (score_B > 10))){
						throw new IllegalArgumentException();
					}

					setDiffMap(movie_A,movie_B,diff,movie_B_diff);
					setWeightMap(movie_A,movie_B,movie_B_weight);
				}
			}
		}
		setAvgDiff();
	}
	
//  Map<Integer, Map<Integer, Integer>> weights? No hace falta pasar un mapa original por referencia,
//	ni que el metodo sea static
	public static int sumaWeights(Map<Integer, Map<Integer, Integer>> weights, int movieKey) {

		int suma = 0;

//		Map<Integer, Integer> weights_movie = weights.get(movieKey);
		Map<Integer, Integer> weights_movie = new HashMap<Integer, Integer>();
		weights_movie = weights.get(movieKey);

		for(Integer weight: weights_movie.values()) {
			suma = suma + weight;
		}
		return suma;
	}

	public double predictOneMovie(int movieKey, Map<Integer, Double> user_movies) {

		double total = 0;
		int n = sumaWeights(this.weightMap, movieKey);


//		Map<Integer, Double> movie_diffs = diffMap.get(movieKey);
		Map<Integer, Double> movie_diffs = new HashMap<Integer, Double>();
		movie_diffs = diffMap.get(movieKey);

		for(Integer current_movie: movie_diffs.keySet()) {
			if(user_movies.containsKey(current_movie)){
				double diff = movie_diffs.get(current_movie);

				int weight = weightMap.get(movieKey).get(current_movie);

				Double punt_user = user_movies.get(current_movie);

				total = total + (weight * (diff+punt_user));

			}
		}
		//	total = total + (frec * (diff + );


		total = total/n;

                return total;
        }


	public void predict(Integer user) {

		double prediction;
		
		if(data.containsKey(user)) {

			Map<Integer, Double> user_movies = data.get(user);
			
			predictions.put(user, new LinkedList<Node>());

			LinkedList<Node> predList = predictions.get(user);

			for(Integer movieKey: diffMap.keySet()) {
				if(!user_movies.containsKey(movieKey)) {
					prediction = predictOneMovie(movieKey, user_movies);
					predList.add(getIndex(user, prediction), new Node(movieKey , prediction));
				}
			}
		} else {
			System.out.println("predict: No such user");
		}
	}

	
	public int getIndex(int user, double value) {
		int pos = 0;
		ListIterator<Node> itrator = predictions.get(user).listIterator();
		while (itrator.hasNext()) {
			if (itrator.next().getValue() <= value) {
				break;
			} else {
				pos++;
			}
		}
		return pos;
	}

	public String menu() {
		String menu = 	"<h1>Recomendar películas a un usuario</h1><hr>" +
				"<h4>Elige el usuario y el numero de recomendaciones.</h4>" +
				"<hr><form action='/recommend' method='post'>" +
				"<label for='user'>Usuario: </label>" + 
				"<input type='number' name='user' id='user' min='0' required> " +
				"<label for='n_items'>Numero de recomendaciones: </label>" + 
				"<input type='number' name='n_items' id='n_items' min='0' required> " +
				"<input type='submit' value='Recomendar'>"  +
				"</form><hr>";
		return menu;
	}
	
	public String backHome() {
		String home = "<form action='/' method='get'>" +
				"<div class='button'>" +
				"<button type='submit'>Volver a home</button>" +
				"</div>" +
				"</form>";
		return home;
	}

	public String showSOMenu() {
		String response = menu();
		response += backHome();
		return response;
	}
	
	public String getNPredicted(int user, int nItems) {

		String predString = "";

		ListIterator<Node> itrator = predictions.get(user).listIterator();

		for (int i=0; (i < nItems && itrator.hasNext()); i++) {
			predString += ("<tr><td>" + itrator.next().toString() + "</td></tr>");
		}
		
		return predString;
	}

	public String recommend(Request request) {

		String response = menu();
		
		try {
			int user = Integer.parseInt(request.queryParams("user"));
			int nItems = Integer.parseInt(request.queryParams("n_items"));
			
			if(predictions.containsKey(user)) {
				response += "<h4>Recomendaciones:</h4><hr><table>";
				response += getNPredicted(user, nItems);
				response += "</table><hr>";
			} else {
				response += "<h4>El usuario no existe.</h4><hr>";
			}
			return (response + backHome());	
		} catch (NumberFormatException e) {
			return "NumberFormatException";
		} catch (NullPointerException e) {
			return "NullPointerException";
		}
	}

	public void updateData(Integer score, Integer user, Integer film_id) throws RuntimeException{

		try {
			Double double_score = score.doubleValue();
			Map<Integer,Double> film_score = new HashMap<>();
			if(double_score < 0 || double_score > 10) throw new IllegalArgumentException();
			if(!data.containsKey(user)) {
				film_score.put(film_id, double_score);
				data.put(user, film_score);
			}else {
				film_score = data.get(user);
				film_score.computeIfPresent(film_id, (k, v) -> double_score);
				film_score.put(film_id,double_score);
			}
			//Una vez que esta actualizado data, se calculan de nuevo las predicciones
			for (int data_user : data.keySet()){
				predict(data_user);
			}
		}catch(RuntimeException runTime) {
			System.err.println(runTime);
		}
	}
}
