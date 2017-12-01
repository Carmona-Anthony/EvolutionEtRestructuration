package decorator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDecorator extends Decorator{
	
	List<FieldDecorator> fields;
	List<MethodDecorator> methods;
	
	String name;
	int nbLines;
	
	public TypeDecorator(TypeDeclaration type){
		
		super(type);
		
		name = type.getName().toString();
		/**
		 * Get All fields of the currentType
		 */
		fields = new ArrayList<>();
		FieldDeclaration[] fieldDeclarations = type.getFields();
		for(FieldDeclaration currentField : fieldDeclarations) {
			fields.add(new FieldDecorator(currentField));
		}
		
		/**
		 * Get all methods of the currentType
		 */
		methods = new ArrayList<>();
		MethodDeclaration[] methodDeclarations = type.getMethods();
		for(MethodDeclaration currentMethod : methodDeclarations) {
			methods.add(new MethodDecorator(currentMethod));
		}
	}

	public List<FieldDecorator> getFields() {
		return fields;
	}

	public void setFields(List<FieldDecorator> fields) {
		this.fields = fields;
	}

	public List<MethodDecorator> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodDecorator> methods) {
		this.methods = methods;
	}

	public int getNbLines() {
		return nbLines;
	}

	public void setNbLines(int nbLines) {
		this.nbLines = nbLines;
	}
	
	public int getNumberFields() {
		return fields.size();
	}
	
	public int getNumberMethods() {
		return methods.size();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
