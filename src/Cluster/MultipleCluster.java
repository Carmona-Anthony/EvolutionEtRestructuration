package Cluster;

import java.util.HashMap;
import java.util.Map.Entry;

import DendrogramCreator.DendrogramGraphVisitor;

public class MultipleCluster extends Cluster {

	private Cluster clusterA;
	private Cluster clusterB;

	MultipleCluster(String name, Cluster A, Cluster B, int id, int similarity) {
		super(name,id);
		setClusterA(A);
		setClusterB(B);
		setValues(calculateValue());
		similarityValue = similarity;
	}

	private HashMap<Cluster, Integer> calculateValue() {

		HashMap<Cluster, Integer> valueA = getClusterA().getValues();
		HashMap<Cluster, Integer> valueB = getClusterB().getValues();

		HashMap<Cluster, Integer> result = new HashMap<>(valueA);
		
		for(Entry<Cluster, Integer> entry : valueB.entrySet()){ 
			if(result.containsKey(entry.getKey())){ 
				result.put(entry.getKey(), entry.getValue() + result.get(entry.getKey())); 
			}
			else result.put(entry.getKey(),entry.getValue()); 
		}
		
		return result;
	}
	
	public void accept(DendrogramGraphVisitor visitor) {
		visitor.visit(this);
	}
	
	public void accept(ClusterVisitor v) {
		v.visit(this);
	}
	public String toString() {
		return name;
	}

	public Cluster getClusterA() {
		return clusterA;
	}

	public void setClusterA(Cluster clusterA) {
		this.clusterA = clusterA;
	}

	public Cluster getClusterB() {
		return clusterB;
	}

	public void setClusterB(Cluster clusterB) {
		this.clusterB = clusterB;
	}
}
