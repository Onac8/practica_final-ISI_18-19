package urjc.isi.pruebasSparkJava;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author nshandra, jaimefdez96, AlbertoCoding
 * <p>
 * data: userID | titleID | score<br>
 * diffMap: titleID-A | titleID-B | difference<br>
 * weightMap: titleID-A | titleID-B | weight<br>
 * predictionsN: userID | titleID | prediction
 */
public class SlopeOneFilterNode {

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
			return "titleID: " + getKey() + ", Pred: " + getValue();
		}		
	}
	
	class NodeComp implements Comparator<Node> {
		@Override
		public int compare(Node d1, Node d2) {
			if(d2.prediction<d1.prediction) {
				return -1;
			} else if(d1.prediction<d2.prediction) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	Map<Integer, Map<Integer, Double>> data;
	Map<Integer, Map<Integer, Double>> diffMap;
	Map<Integer, Map<Integer, Integer>> weightMap;
	Map<Integer, LinkedList<Node>> predictionsN;

	public SlopeOneFilterNode() {
//		data  = new HashMap<>();
//		JDBC call
//		buildMaps();
//		for (int user : data.keySet()){
//			predict(user);
//		}
	}

	public void buildMaps(){
		// Crear diffMap y weightMap a partir de data.
	}


/*

	public float[] pelisPuntuadasPorUsuario(int user) {
		
		- Mirar en data a ver qué pelis ha puntuado el usuario
		return pelisPuntuadas;
	}

	public int predictPuntPeli(int user, int peli) {
		pelisPunt = pelisPuntuadasPorUsuario(user); // un array de los IDs de las pelis puntuadas por el usuario
		for(i=0;i<size(pelisPunt);i++){
			puntPelisPunt = data[pelisPunt[i]]; // Puntuaciones de las pelis puntuadas por el usuario
		}

		diffs [] // array con las diferencias medias de puntuaciones respecto a una peli concreta
		frecs [] // array con el número de diferencias de puntuaciones que se han usado para comparar 

		if(size(frecs)==size(diffs)==size(puntPelisPunt)){
			total=0;
			n=sum(frecs[]);

			for(i=0;i<size(frecs);i++) {
				total = total + (frecs[i] * (diffs[i]+puntPelisPunt[i]) )
			}
			total = total/n;

		}else{
			print("Error en los tamaños de los arrays");
		}

		return total;
	}
*/

	public void predict(int user) {
		// Crear predictionsN para ese usuario determinado.
/*
		predictionsN = [];

		for(i=0;i<npelis;i++) {
			peli= pelis[i];
			if(noPuntuada(user, peli)){
				predictionsN.add(predictPuntPeli(user, peli));
			}else{
				predictionsN.add(valorPuntuadoPorElUsuario);
			}
		}
*/
	}
	
	public int getIndex(int user, double value) {
		int pos = 0;
		ListIterator<Node> itrator = predictionsN.get(user).listIterator();
		while (itrator.hasNext()) {
			if (itrator.next().getValue() <= value) {
				break;
			} else {
				pos++;
			}
		}
		return pos;
	}
	
	public void recommendN(int user, int nItems) {
		// Mostrar nItems predicciones con mayor puntuación.
		LinkedList<Node> predictionList = predictionsN.get(user);
		predictionList.sort(new NodeComp());
		
		ListIterator<Node> itrator = predictionList.listIterator();
		 
		for (int i=0; i < nItems; i++) {
			itrator.hasNext();
			System.out.println(itrator.next());
		}
	}
	
	public static void main(String args[]){
		SlopeOneFilterNode so = new SlopeOneFilterNode();
		
		so.predictionsN = new HashMap<>();
		so.predictionsN.put(1, new LinkedList<Node>());

		so.predictionsN.get(1).add( so.new Node(1 , 8.0));
		so.predictionsN.get(1).add( so.new Node(2 , 1.0));
		so.predictionsN.get(1).add( so.new Node(3 , 10.0));
		
		System.out.println(so.predictionsN);
		so.recommendN(1, 2);
		System.out.println(so.predictionsN);
		so.predictionsN.get(1).add(so.getIndex(1, 5.0), so.new Node(4 , 5.0));
		so.predictionsN.get(1).add(so.getIndex(1, 4.0), so.new Node(5 , 4.0));
		so.predictionsN.get(1).add(so.getIndex(1, 6.0), so.new Node(6 , 6.0));
		System.out.println(so.predictionsN);
	}
}
