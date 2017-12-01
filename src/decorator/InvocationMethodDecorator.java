package decorator;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class InvocationMethodDecorator extends Decorator {
	
	IMethodBinding binding;
	String name;
	
	public InvocationMethodDecorator(MethodInvocation node){
		super(node);
		name = node.toString();
		binding = node.resolveMethodBinding();
	}
	
	public String getCallingClass() {
		if(binding != null) {
			return binding.getDeclaringClass().getName().toString();
		}
		return null;
	}
	
	public String toString() {
		return name;
	}

}
