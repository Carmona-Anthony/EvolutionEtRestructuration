package Cluster;

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class ClusterManager {

	List<Cluster> clusters;
	PriorityQueue<Cluster> nextCluster;

	ClusterManager(List<Cluster> clusters) {

		this.clusters = clusters;

		// Init next Cluster list
		ClusterComparator comparator = new ClusterComparator();
		nextCluster = new PriorityQueue<Cluster>(clusters.size(), comparator);
		for (Cluster cluster : clusters) {
			nextCluster.add(cluster);
		}
	}

	public void remove(Cluster cluster) {

		clusters.remove(cluster);
		nextCluster.remove(cluster);
		for (Cluster c : clusters) {
			c.remove(cluster);
		}
	}

	public void add(Cluster cluster) {
		clusters.add(cluster);
		nextCluster.add(cluster);
	}
	
	public Cluster getNext() {
		if(!nextCluster.isEmpty()) {
			return nextCluster.peek();
		}
		return null;
	}
}


class ClusterComparator implements Comparator<Cluster> {

	@Override
	public int compare(Cluster c1, Cluster c2) {
		// TODO Auto-generated method stub
		return Integer.compare(c2.getMaxValue(), c1.getMaxValue());
	}

}