package Cluster;

import java.util.HashMap;
import java.util.Map.Entry;

public class MultipleCluster extends Cluster{
	
	Cluster clusterA;
	Cluster clusterB;

	MultipleCluster(String name, Cluster A, Cluster B) {
		super(name);
		clusterA = A;
		clusterB = B;
		
		setValues(calculateValue());
	}
	
	private HashMap<Cluster, Integer> calculateValue() {
		
		HashMap<Cluster, Integer> valueA = clusterA.getValues();
		HashMap<Cluster, Integer> valueB = clusterB.getValues();
		
		for(Entry<Cluster,Integer> pairA : valueA.entrySet()) {
			if(valueB.containsKey(pairA.getKey())) {
				valueA.put(pairA.getKey(), pairA.getValue() + valueB.get(pairA.getKey()));
			}
		}
		
		HashMap<Cluster, Integer> result = new HashMap<>(valueA);
		result.keySet().retainAll(valueB.keySet());
		
		return result;
	}
	
	public String toString() {
		return name;
	}
}
