import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import decorator.MethodDecorator;
import decorator.TypeDecorator;

public class Coupleur {
	
	public static void couple(List<TypeDecorator> types) {
		
		Integer [][] couplingArray = new Integer [types.size()-1][types.size()-1];
		
		int sum = 0;
		
		for(int i=0; i<types.size()-1; i++) {
			for(int j=0;j<types.size()-1;j++) {
				int calls = handleCouplage(types.get(i),types.get(j));
				couplingArray[i][j] = calls;
				//couplingArray[j][i] = calls + "/" + (nbAppels/2);
				sum += calls;
			}
		}
		for (int i = 0; i < couplingArray.length; i++) {
			  Arrays.sort(couplingArray[i], new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					// TODO Auto-generated method stub
					return o1.compareTo(o2);
				}
				  
			  });
		}
		
		for(int i=0;i<couplingArray.length;i++) {
			for(int j=0;j<couplingArray[i].length;j++) {
				System.out.print(couplingArray[i][j] + "|");
			}
			System.out.println("");
		}
	}
	
	static int handleCouplage(TypeDecorator A, TypeDecorator B) {
		
		List<MethodDecorator> methodsA = A.getMethods();
		List<MethodDecorator> methodsB = B.getMethods();
		
		int nbCallsA = getCallsNumber(methodsA, B);
		int nbCallsB = getCallsNumber(methodsB, A);
		
		return nbCallsA + nbCallsB;
		
	}
	
	static int getCallsNumber(List<MethodDecorator> listMethod, TypeDecorator type) {
		
		int nbCalls = 0;
		for(MethodDecorator methodA : listMethod) {
			HashMap<String, Integer> couplageA = methodA.getCouplage();
			if(couplageA.containsKey(type.getName())) nbCalls += couplageA.get(type.getName());
		}
		
		return nbCalls;
	}
}
