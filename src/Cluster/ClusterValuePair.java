package Cluster;

public class ClusterValuePair {
	
	Cluster cluster;
	Integer value;
	
	ClusterValuePair(Cluster c, Integer v){
		cluster = c;
		value = v;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	
}
