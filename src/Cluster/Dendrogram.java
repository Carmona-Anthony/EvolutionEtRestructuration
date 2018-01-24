"package Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import decorator.TypeDecorator;

public class Dendrogram {

	ClusterManager clusterManager;
	int index = 0;

	public Dendrogram(List<TypeDecorator> types, Integer[][] couplingArray) {
		
		System.out.println("Types : " + types);
		
		// Create all clusters
		List<Cluster> clusters = new ArrayList<>();
		// Get all clusters
		for (int index = 0; index < types.size(); index++) {

			Cluster cluster = new Cluster(types.get(index).getName());
			clusters.add(cluster);

			// Get value associated with the cluster
			HashMap<Cluster, Integer> values = new HashMap<>();

			int j = 0;
			while (j <= index) {
				//If not itself add
				if(couplingArray[index][j] != null) {
					if (index != j) {
						if(couplingArray[index][j] > 0) {
							values.put(clusters.get(j), couplingArray[index][j]);
							(clusters.get(j)).add(cluster, couplingArray[index][j]);
						}
					}
				}
				j++;
			}
			cluster.setValues(values);
		}
		
		/*for(Cluster cluster : clusters) {
			System.err.println(cluster);
		}*/
		// Create Manager
		clusterManager = new ClusterManager(clusters);
	}

	public Cluster createDendrogram() {
		
		Cluster A;
		MultipleCluster multipleCluster = null;
		
		while ((A = clusterManager.getNext()) != null) {

			Cluster B = A.getMaxCluster();

			System.out.println(A + " -> " + B);

			multipleCluster = new MultipleCluster("C" + index, A, B);
			clusterManager.add(multipleCluster);

			index++;

			clusterManager.remove(A);
			clusterManager.remove(B);
		}
		System.out.println("Root : " + multipleCluster);
		return multipleCluster;
	}
}
