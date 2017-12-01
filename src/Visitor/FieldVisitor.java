package Visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;

import decorator.FieldDecorator;

public class FieldVisitor extends Visitor {
	
	FieldVisitor(CompilationUnit cu) {
		super(cu);
		// TODO Auto-generated constructor stub
	}

	List<FieldDecorator> fields = new ArrayList<>();
	
	public boolean visit(FieldDeclaration node) {
		
		fields.add(new FieldDecorator(node));
		return super.visit(node);
	}

	@Override
	public List<FieldDecorator> get() {
		// TODO Auto-generated method stub
		return fields;
	}
	
	
}
