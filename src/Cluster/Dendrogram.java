package Cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import decorator.TypeDecorator;

public class Dendrogram {
	
	ClusterManager clusterManager;
	int index = 0;

	public Dendrogram(List<TypeDecorator> types, Integer[][] couplingArray){
		//Create all clusters
		List <Cluster> clusters = new ArrayList<>();
		//Get all clusters
		for(int index = 0; index < types.size()-1; index++) {
			
			Cluster cluster = new Cluster(types.get(index).getName());
			clusters.add(cluster);
			
			//Get value associated with the cluster
			HashMap<Cluster,Integer> values = new HashMap<>();
			
			int j=0;
			while(couplingArray[index][j] != null) {
				if(index != j) {
					values.put(clusters.get(j), couplingArray[index][j]);
				}
				j++;
			}
			
			cluster.setValues(values);
			
			//Create Manager
			clusterManager = new ClusterManager(clusters);
		}
	}
	
	public void createDendrogram() {
		Cluster A;
		while( (A = clusterManager.getNext()) != null) {
			System.out.println(A);
			Cluster B = A.getMaxCluster();
			
			MultipleCluster multipleCluster = new MultipleCluster("C"+index,A,B);
			index++;
			
			clusterManager.remove(A);
			clusterManager.remove(B);
			
			clusterManager.add(multipleCluster);
		}
	}
}
