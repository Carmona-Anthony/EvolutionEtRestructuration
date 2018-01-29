import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.IMethodBinding;

import decorator.InvocationMethodDecorator;
import decorator.TypeDecorator;

public class GraphCreator {

	 public static String createJsonGraph(
	        LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<InvocationMethodDecorator>>> methodInvocByMethodsByType) {
	        StringBuilder st = new StringBuilder();
	        HashMap<String, Integer> nodes = new HashMap<>();
	        HashMap<Integer, HashSet<Integer>> links = new HashMap<>();
	        ArrayList<Integer> countParents = new ArrayList<>();
	        ArrayList<Boolean> belongsToProject = new ArrayList<>();

	        for (Entry<String, LinkedHashMap<String, LinkedHashSet<InvocationMethodDecorator>>> stringLinkedHashMapEntry : methodInvocByMethodsByType
	                .entrySet()) {
	            for (Entry<String, LinkedHashSet<InvocationMethodDecorator>> stringLinkedHashSetEntry : stringLinkedHashMapEntry
	                    .getValue().entrySet()) {

	                String caller = stringLinkedHashMapEntry.getKey() + "." + stringLinkedHashSetEntry.getKey();


	                int callerId;
	                if (nodes.containsKey(caller)) {
	                    callerId = nodes.get(caller);
	                } else {
	                    callerId = nodes.size();
	                    nodes.put(caller, callerId);
	                    countParents.add(0);
	                    belongsToProject.add(true);
	                }


	                HashSet<Integer> callees = new HashSet<>();
	                //System.out.println(stringLinkedHashMapEntry.getKey() + "." + stringLinkedHashSetEntry.getKey());
	                for (InvocationMethodDecorator methodInvocation : stringLinkedHashSetEntry.getValue()) {
	                    IMethodBinding resolve = methodInvocation.getBinding();
	                    // partial only srcpath
	                    // if (resolve != null  && methodInvocByMethodsByType.containsKey(resolve.getDeclaringClass().getName())) {
	                    if (resolve != null) { // full with javaclasses

	                        //System.out.println("\t: " + resolve.getDeclaringClass().getName() + "." +
	                        //                          resolve.getMethodDeclaration().getName());

	                        String callee = resolve.getDeclaringClass().getName() + "." +
	                                resolve.getMethodDeclaration().getName();
	                        int calleeId;

	                        if (nodes.containsKey(callee)) {
	                            calleeId = nodes.get(callee);
	                            countParents.set(calleeId, countParents.get(calleeId) + 1); // incrementing parent count
	                        } else {
	                            calleeId = nodes.size();
	                            nodes.put(callee, calleeId);
	                            countParents.add(1);// he has a parent
	                            belongsToProject
	                                    .add(methodInvocByMethodsByType.containsKey(resolve.getDeclaringClass().getName()));
	                        }


	                        callees.add(calleeId);
	                    }
	                }

	                links.put(callerId, callees);
	            }
	        }

	        //yeah that json maggle
	        st.append("{\"nodes\":[");
	        for (Entry<String, Integer> nodesString : nodes.entrySet()) {
	            st.append("{\"id\":").append(nodesString.getValue()).append(",")
	              .append(" \"name\": \"").append(nodesString.getKey()).append("\",")
	              .append(" \"own\": ").append(belongsToProject.get(nodesString.getValue())).append("},");
	        }
	        st.deleteCharAt(st.length() - 1);
	        st.append("], \"links\":[");

	        for (Entry<Integer, HashSet<Integer>> link : links.entrySet()) {
	            int caller = link.getKey();

	            for (int callee : link.getValue()) {
	                int parentCount = countParents.get(callee);

	                float weight = (float) (1 / (0.5 * parentCount + 0.5));
	                st.append("{\"source\":").append(caller).append(",")
	                  .append(" \"target\": ").append(callee).append(",")
	                  .append(" \"str\": ").append(weight)
	                  .append("},");
	            }
	        }
	        st.deleteCharAt(st.length() - 1);
	        st.append("]}");
	        return st.toString();
	    }

	public static String createJsonGraphCouplage(Integer[][] couplingArray, ArrayList<TypeDecorator> types) {
		int min = 1;
		int max = 5;

		int yMax = 0;
		int yMin = 0;

		for(int i = 0; i<types.size(); i++) {
			for(int j = 0; j<i; j++ ) {
				if(yMax < couplingArray[i][j]) {
					yMax = couplingArray[i][j];
				}
			}
		}
		
		StringBuilder st = new StringBuilder();
		st.append("{\"nodes\":[");
		for(int i = 0; i<types.size(); i++) {
            st.append("{\"id\":").append(i).append(",")
              .append(" \"name\": \"").append(types.get(i).getName()).append("\"").append(",")
              .append(" \"own\": ").append("true").append("},");
        }
        st.deleteCharAt(st.length() - 1);
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
		return st.toString();
	}
}
