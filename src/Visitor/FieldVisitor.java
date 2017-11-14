package Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldVisitor extends Visitor {

	
	List<FieldDeclaration> fields = new ArrayList<>();
	
	public boolean visit(FieldDeclaration node) {
		fields.add(node);
		return super.visit(node);
	}
	
	public List<FieldDeclaration> getFields() {
		return fields;
	}
}
