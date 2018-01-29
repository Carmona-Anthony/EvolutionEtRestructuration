package Cluster;

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
							clusters.get(j).add(cluster, couplingArray[index][j]);
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

			multipleCluster = new MultipleCluster("C" + index, A, clusterPair.getCluster(),clusterPair.getValue());
			
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
		
		
		/*if(root.clusterA != null) {
			Cluster A = root.clusterA;
			if(visitor.visit(A)) {
				//Break
			}
			else {
				Cluster B = root.clusterB;
				if(visitor.visit(B)) {
					//Break
				}
				else {
					if(A instanceof MultipleCluster) {
						getModules(visitor,(MultipleCluster) A);
					}
					if(B instanceof MultipleCluster) {
						getModules(visitor,(MultipleCluster) B);
					}
				}
			}
		}*/
		return null;
	}
	
}
