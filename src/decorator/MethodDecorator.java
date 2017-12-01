package decorator;

import java.util.HashMap;
import java.util.List;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class MethodDecorator extends Decorator {
	
	String name;
	int nbLines;
	List<SingleVariableDeclaration> parameters;
	List<InvocationMethodDecorator> invocations;
	
	public MethodDecorator(MethodDeclaration method){
		super(method);
		this.name = method.getName().toString();
	}

	public List<InvocationMethodDecorator> getInvocationMethods() {
		return invocations;
	}

	public void setInvocationMethods(List<InvocationMethodDecorator> invocationMethods) {
		this.invocations = invocationMethods;
	}

	public int getNbLines() {
		return nbLines;
	}

	public void setNbLines(int nbLines) {
		this.nbLines = nbLines;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getNbParameters() {
		return parameters.size();
	}
	
	public List<SingleVariableDeclaration> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<SingleVariableDeclaration> list) {
		this.parameters = list;
	}
	
	public List<InvocationMethodDecorator> getInvocations() {
		return invocations;
	}

	public void setInvocations(List<InvocationMethodDecorator> invocations) {
		this.invocations = invocations;
	}
	
	public HashMap<String,Integer> getCouplage(){
		
		HashMap<String,Integer> couplage = new HashMap<>();
		for(InvocationMethodDecorator invocation : invocations) {
			
			String callingClass = invocation.getCallingClass();
			
			if(couplage.containsKey(callingClass)) couplage.put(callingClass, couplage.get(callingClass) + 1);
			else couplage.put(callingClass, 1);
			
		}
		return couplage;
	}

	public String toString() {
		return name;
	}
}
