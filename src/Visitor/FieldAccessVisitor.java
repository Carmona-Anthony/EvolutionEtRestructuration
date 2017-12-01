package Visitor;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class FieldAccessVisitor extends Visitor {

	FieldAccessVisitor(CompilationUnit cu) {
		super(cu);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <T> List<T> get() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*List<FieldAccessVisitor> fields = new ArrayList<SimpleName>();

	public boolean visit(SimpleName node) {
		
		if (!node.isDeclaration() && node.resolveBinding() instanceof IVariableBinding) {
			fields.add(node);
		}
		return super.visit(node);
	}

	public List<SimpleName> getFields() {
		return fields;
	}
	
	public String toString() {
		String result = "";
		for(SimpleName name : fields) {
			result += name.toString() + "\n";
		}
		return result;
	}*/
}
