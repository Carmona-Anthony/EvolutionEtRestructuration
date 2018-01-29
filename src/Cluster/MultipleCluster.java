package Cluster;

import java.util.HashMap;
import java.util.Map.Entry;

public class MultipleCluster extends Cluster {

	Cluster clusterA;
	Cluster clusterB;

	MultipleCluster(String name, Cluster A, Cluster B, int similarity) {
		super(name);
		clusterA = A;
		clusterB = B;
		setValues(calculateValue());
		similarityValue = similarity;
	}

	private HashMap<Cluster, Integer> calculateValue() {

		HashMap<Cluster, Integer> valueA = clusterA.getValues();
		HashMap<Cluster, Integer> valueB = clusterB.getValues();

		HashMap<Cluster, Integer> result = new HashMap<>(valueA);
		
		for(Entry<Cluster, Integer> entry : valueB.entrySet()){ 
			if(result.containsKey(entry.getKey())){ 
				result.put(entry.getKey(), entry.getValue() + result.get(entry.getKey())); 
			}
			else result.put(entry.getKey(),entry.getValue()); 
		}
		
		return result;
	}
	
	public void accept(ClusterVisitor v) {
		v.visit(this);
	}
	public String toString() {
		return name;
	}
}
