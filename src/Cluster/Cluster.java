package Cluster;

import java.util.HashMap;
import java.util.Map.Entry;

public class Cluster {

	String name;
	HashMap<Cluster, Integer> valueByCluster;
	int similarityValue;

	Cluster(String name) {
		this.name = name;
		this.valueByCluster = new HashMap<>();
		similarityValue = -1;
	}

	Cluster(String name, HashMap<Cluster, Integer> valueByCluster) {
		this.name = name;
		this.valueByCluster = valueByCluster;
	}

	public Integer getValue(Cluster cluster) {
		if (!valueByCluster.containsKey(cluster))
			return -1;
		else
			return valueByCluster.get(cluster);
	}

	public void setValues(HashMap<Cluster, Integer> values) {
		valueByCluster = values;
	}

	public HashMap<Cluster, Integer> getValues() {
		return valueByCluster;
	}

	public String getName() {
		return name;
	}
	
	
	public void remove(Cluster cluster) {
		if (valueByCluster.containsKey(cluster)) {
			valueByCluster.remove(cluster);
		}
	}
	
	public void add(Cluster cluster, Integer value) {
		valueByCluster.put(cluster, value);
	}
	
	public ClusterValuePair getMaxCluster() {
		
		Cluster maxCluster = null;
		int maxValue = -1;
		for (Entry<Cluster, Integer> entry : valueByCluster.entrySet()) {
			if (maxValue < entry.getValue()) {
				maxCluster = entry.getKey();
				maxValue = entry.getValue();
			}
		}
		return new ClusterValuePair(maxCluster,maxValue);
	}

	public Integer getMaxValue() {
		
		int maxValue = -1;
		for (Entry<Cluster, Integer> entry : valueByCluster.entrySet()) {
			if (maxValue < entry.getValue()) {
				maxValue = entry.getValue();
			}
		}

		return maxValue;
	}
	
	public int getSimi() {
		return similarityValue;
	}
	
	public void setSimi(int simi) {
		if(similarityValue == -1 ) {
			System.out.println("Set simi");
			similarityValue = simi;
		}
	}
	
	public void accept(ClusterVisitor visitor) {
		visitor.visit(this);
	}

	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(name);
		/*builder.append(" -> ");
		builder.append("[ ");

		for (Entry<Cluster, Integer> entry : valueByCluster.entrySet()) {
			builder.append(entry.getKey().getName());
			builder.append(",");
		}

		builder.append("]");*/

		return builder.toString();
	}

}
