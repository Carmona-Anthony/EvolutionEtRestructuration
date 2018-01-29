package Cluster;

import java.util.ArrayList;

public class ClusterVisitor {
	
	ArrayList<MultipleCluster> modules = new ArrayList<>();
	
	public void visit(Cluster cluster) {
		
	}
	
	public void visit(MultipleCluster cluster) {
		
		boolean findModule = false;
		
		if(cluster.similarityValue >= calculateAVGSimilarity(cluster.clusterA,cluster.clusterB)) {
			findModule = true;
			modules.add(cluster);
		}
		
		if(!findModule) {
			cluster.clusterA.accept(this);
			cluster.clusterB.accept(this);
		}
	}
	
	public float calculateAVGSimilarity(Cluster clusterA, Cluster clusterB) {
		
		int sum = clusterA.getSimi() + clusterB.getSimi();
		
		return (float) (sum / 2.0);
	}
	
	public ArrayList<MultipleCluster> getModules(){
		for(MultipleCluster multiple : modules) {
			System.out.println(multiple + " : " + multiple.clusterA.toString() + " , " + multiple.clusterB.toString() );
		}
		return modules;
	}

}
