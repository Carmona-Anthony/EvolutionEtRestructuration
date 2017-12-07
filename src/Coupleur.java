import java.util.HashMap;
import java.util.List;
import decorator.MethodDecorator;
import decorator.TypeDecorator;

public class Coupleur {

	static Integer[][] couplingArray;

	public static Integer[][] couple(List<TypeDecorator> types) {

		couplingArray = new Integer[types.size()][types.size()];

		int sum = 0;
		for (int i = 0; i < types.size(); i++) {
			for (int j = 0; j <= i; j++) {
				int calls = handleCouplage(types.get(i), types.get(j));
				couplingArray[i][j] = calls;
				// couplingArray[j][i] = calls + "/" + (nbAppels/2);
				sum += calls;
			}
		}
		
		for (int i = 0; i < couplingArray.length; i++) {
			for (int j = 0; j < couplingArray[i].length; j++) {
				System.out.print(couplingArray[i][j] + "|");
			}
			System.out.println("");
		}
		
		return couplingArray;
	}

	static int handleCouplage(TypeDecorator A, TypeDecorator B) {

		List<MethodDecorator> methodsA = A.getMethods();
		List<MethodDecorator> methodsB = B.getMethods();

		int nbCallsA = getCallsNumber(methodsA, B);
		int nbCallsB = getCallsNumber(methodsB, A);
		
		//System.out.println( "(" + A.getName() + "," + B.getName() + ")" + "-> " + (nbCallsA+nbCallsB));
		
		if(A.equals(B)) {
			return nbCallsA;
		}
		
		return nbCallsA + nbCallsB;

	}

	static int getCallsNumber(List<MethodDecorator> listMethod, TypeDecorator type) {

		int nbCalls = 0;
		for (MethodDecorator methodA : listMethod) {
			HashMap<String, Integer> couplageA = methodA.getCouplage();
			if (couplageA.containsKey(type.getName()))
				nbCalls += couplageA.get(type.getName());
		}

		return nbCalls;
	}

/*	static MultipleCluster getMaxCluster(
			LinkedHashMap<Cluster, LinkedHashMap<Cluster, Integer>> hashHierarchiq) {
		
		int maxValue = 0;
		Cluster	A = null;
		Cluster B = null;
		
		for (Entry<Cluster, LinkedHashMap<Cluster, Integer>> e : hashHierarchiq.entrySet()) {
			Cluster key = e.getKey();
			LinkedHashMap<Cluster, Integer> value = e.getValue();

			for (Entry<Cluster, Integer> entry : value.entrySet()) {
				if(maxValue < entry.getValue()) {
					A = key;
					B = entry.getKey();
					maxValue = entry.getValue();
				}
			}
		}
		if(A != null && B != null) {
			return new MultipleCluster(A,B);
		}
		else return null;
	}

	static void initHierarchiqDendrogram(List<TypeDecorator> types, int[][] couplingArray) {
		//L'enfer sur terre
		LinkedHashMap<Cluster, LinkedHashMap<Cluster, Integer>> hashHierarchiq = new LinkedHashMap<>();
		for (int i = 0; i < couplingArray.length; i++) {
			for (int j = 0; j < couplingArray[i].length; j++) {
				hashHierarchiq.put(new UniqueCluster(types.get(i)), new LinkedHashMap<>())
						.put(new UniqueCluster(types.get(j)), couplingArray[i][j]);
			}
		}
		
		MultipleCluster pair;
		while((pair = getMaxCluster(hashHierarchiq)) != null) {
			hashHierarchiq.remove(pair.getClusterA());
			hashHierarchiq.remove(pair.getClusterB());
			for(Entry<Cluster, LinkedHashMap<Cluster,Integer>> entry : hashHierarchiq.entrySet()) {
				hashHierarchiq.get(entry.getKey()).remove(pair.getClusterA());
				hashHierarchiq.get(entry.getKey()).remove(pair.getClusterB());
			}
		}
	}*/
}
