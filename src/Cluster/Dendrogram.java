package Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DendrogramCreator.DendrogramGraphVisitor;
import decorator.TypeDecorator;

public class Dendrogram {

	ClusterManager clusterManager;
	int index = 0;
	
	int nbIds = 0;

	public Dendrogram(List<TypeDecorator> types, Integer[][] couplingArray) {
		
		System.out.println("Types : " + types);
		
		nbIds = types.size();
		// Create all clusters
		List<Cluster> clusters = new ArrayList<>();
		// Get all clusters
		for (int i = 0; i < types.size(); i++) {

			Cluster cluster = new Cluster(types.get(i).getName(),i);
			clusters.add(cluster);

			// Get value associated with the cluster
			HashMap<Cluster, Integer> values = new HashMap<>();

			int j = 0;
			while (j <= i) {
				//If not itself add
				if(couplingArray[i][j] != null) {
					if (i != j) {
						if(couplingArray[i][j] > 0) {
							values.put(clusters.get(j), couplingArray[i][j]);
							clusters.get(j).add(cluster, couplingArray[i][j]);
						}
					}
				}
				j++;
			}
			cluster.setValues(values);
		}
		
		clusterManager = new ClusterManager(clusters);
	}

	public Cluster createDendrogram() {
		
		Cluster A;
		MultipleCluster multipleCluster = null;
		
		while ((A = clusterManager.getNext()) != null) {
			
			ClusterValuePair clusterPair = A.getMaxCluster();
			
			System.out.println(A + " -> " + clusterPair.getCluster());

			multipleCluster = new MultipleCluster("C" + index, A, clusterPair.getCluster(),nbIds,clusterPair.getValue());
			
			nbIds++;
			//Will only be called for non multiple cluster
			A.setSimi(clusterPair.getValue());
			clusterPair.getCluster().setSimi(clusterPair.getValue());
			
			clusterManager.add(multipleCluster);

			index++;

			clusterManager.remove(A);
			clusterManager.remove(clusterPair.getCluster());
		}
		System.out.println("Root : " + multipleCluster);
		return multipleCluster;
	}
	
	public ArrayList<Cluster> getModules(ClusterVisitor visitor , Cluster root){
		
		if(root != null) {
			root.accept(visitor);
		}
		return null;
	}
	
	public static void createVisualisation(DendrogramGraphVisitor visitor, Cluster root) {
		if(root != null) {
			root.accept(visitor);
		}
	}
}
