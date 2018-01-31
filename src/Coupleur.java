import java.util.HashMap;
import java.util.List;
import decorator.MethodDecorator;
import decorator.TypeDecorator;

public class Coupleur {

	static Integer[][] couplingArray;

	public static Integer[][] callsMatrix(List<TypeDecorator> types){
		 Integer[][] callsMatrix = new Integer[types.size()][types.size()];
		 
		 for (int i = 0; i < types.size(); i++) {
				for (int j = 0; j <= i; j++) {
					List<MethodDecorator> methodsA = types.get(i).getMethods();
					int calls = getCallsNumber(methodsA, types.get(j));
					callsMatrix[i][j] = calls;
				}
			}
		
		return callsMatrix;
		
	}
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
		
		if(A.getName().equals(B.getName())) {
			return nbCallsA;
		}
		
		return nbCallsA + nbCallsB;

	}

	static int getCallsNumber(List<MethodDecorator> listMethod, TypeDecorator type) {

		int nbCalls = 0;
		for (MethodDecorator methodA : listMethod) {
			HashMap<String, Integer> couplageA = methodA.getCouplage();

			if (couplageA.containsKey(type.getName())) {
				nbCalls += couplageA.get(type.getName());
			}
			else couplageA.put(type.getName(), 1);	
		}

		return nbCalls;
	}
}
