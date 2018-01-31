package DendrogramCreator;

import Cluster.Cluster;
import Cluster.MultipleCluster;

public class DendrogramGraphVisitor {
	
	StringBuilder nodes = new StringBuilder();
	StringBuilder links = new StringBuilder();
	
	public DendrogramGraphVisitor(){
		nodes.append("{\"nodes\":[");
		links.append("], \"links\":[");
	}
	public void visit(MultipleCluster multipleCluster) {
		
		nodes.append("{\"id\":").append(multipleCluster.getId()).append(",")
			.append(" \"name\": \"").append(multipleCluster.getName()).append("\"").append(",")
			.append(" \"own\": ").append("true").append("},");
		
		multipleCluster.getClusterA().accept(this);
		multipleCluster.getClusterB().accept(this);
		
		
		links.append("{\"source\":").append(multipleCluster.getId()).append(",")
        .append(" \"target\": ").append(multipleCluster.getClusterA().getId()).append(",")
        .append(" \"str\": ").append(0.5)
        .append("},");	
		
		links.append("{\"source\":").append(multipleCluster.getId()).append(",")
        .append(" \"target\": ").append(multipleCluster.getClusterB().getId()).append(",")
        .append(" \"str\": ").append(0.5)
        .append("},");	
		
        /*st.deleteCharAt(st.length() - 1);
        st.append("], \"links\":[");
        for(int i = 0; i<types.size(); i++) {
        	for(int j = 0; j<=i; j++) {
        		if(couplingArray[i][j] != 0) {
        			
        			double rawValue = (double) couplingArray[i][j];
        			
        			double percent = (rawValue - yMin) / (yMax - yMin);
        			double weight = percent * (max - min) + min;
        			
        			st.append("{\"source\":").append(i).append(",")
                    .append(" \"target\": ").append(j).append(",")
                    .append(" \"str\": ").append(0.5).append(",")
                    .append(" \"weight\": ").append(weight)
                    .append("},");	
        		}
        	}
        }
        st.deleteCharAt(st.length() - 1);
        st.append("]}");
		return st.toString();*/
	}
	
	public void visit(Cluster cluster) {
		nodes.append("{\"id\":").append(cluster.getId()).append(",")
		.append(" \"name\": \"").append(cluster.getName()).append("\"").append(",")
		.append(" \"own\": ").append("true").append("},");
	
	}
	
	public StringBuilder getNodes() {
		return nodes;
	}
	
	public StringBuilder getLinks() {
		return links;
	}
	
	public StringBuilder getJson() {
		nodes.deleteCharAt(nodes.length() - 1);
		links.deleteCharAt(links.length() - 1);
	    links.append("]}");
	    
	    return nodes.append(links);
	}

}
