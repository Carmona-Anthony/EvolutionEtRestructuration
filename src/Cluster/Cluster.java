package Cluster;

import java.util.HashMap;
import java.util.Map.Entry;

public class Cluster {

	String name;
	HashMap<Cluster, Integer> valueByCluster;

	Cluster(String name) {
		this.name = name;
		this.valueByCluster = new HashMap<>();
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

	public Cluster getMaxCluster() {

		Cluster maxCluster = null;
		int maxValue = -1;
		for (Entry<Cluster, Integer> entry : valueByCluster.entrySet()) {
			if (maxValue < entry.getValue()) {
				maxCluster = entry.getKey();
				maxValue = entry.getValue();
			}
		}

		return maxCluster;
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

	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(" -> ");
		builder.append("[ ");

		for (Entry<Cluster, Integer> entry : valueByCluster.entrySet()) {
			builder.append(entry.getKey().getName());
			builder.append(",");
		}

		builder.append("]");

		return builder.toString();
	}

}
