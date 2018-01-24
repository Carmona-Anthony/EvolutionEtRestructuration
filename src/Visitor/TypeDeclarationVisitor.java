package Visitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import decorator.TypeDecorator;

public class TypeDeclarationVisitor extends Visitor {
	
	static int typesCount = 0;
	
	List<TypeDecorator> types = new ArrayList<>();
	static HashMap<TypeDeclaration, List<MethodDeclaration>> methodsByType = new HashMap<>();
	static HashMap<TypeDeclaration, List<FieldDeclaration>> fieldsByType = new HashMap<>();
	static LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<MethodInvocation>>> fullPackage = new LinkedHashMap<>();
	
	CompilationUnit compilationUnit;
	TypeDeclaration currentType;
	TypeDecorator currentDecorator;
	
	/**
	 * Constructor
	 * @param cu
	 */
	public TypeDeclarationVisitor(CompilationUnit cu) {
		super(cu);
	}

	
	public boolean visit(CompilationUnit cu) {
		//Save the root of the file
		compilationUnit = cu;
		
		return super.visit(cu);
	}

	public boolean visit(TypeDeclaration node) {
		
		TypeDecorator typeDecorator = new TypeDecorator(node);
		
		types.add(typeDecorator);
		
		currentType = node;
		currentDecorator = typeDecorator;
		
		MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor(compilationUnit);
		FieldVisitor fieldVisitor = new FieldVisitor(compilationUnit);
		
		node.accept(methodVisitor);
		node.accept(fieldVisitor);
		
		currentDecorator.setFields(fieldVisitor.get());
		currentDecorator.setMethods(methodVisitor.get());
		
		return super.visit(node);
	}
	
	public void endVisit(TypeDeclaration node) {
		int startLine = compilationUnit.getLineNumber(currentType.getStartPosition());
		int endLine = compilationUnit.getLineNumber(currentType.getStartPosition() + currentType.getLength() - 1);
		currentDecorator.setNbLines(endLine - startLine);
	}
	
	
	public void endVisit(CompilationUnit cu) {
		super.root = null;
	}
	
	@Override
	public List<TypeDecorator> get() {
		// TODO Auto-generated method stub
		return types;
	}
	
	public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<MethodInvocation>>> getFullPackage(){
		return fullPackage;
	}
	public static HashMap<TypeDeclaration, List<MethodDeclaration>> getMethodsByType(){
		return methodsByType;
	}
	
	public static List<TypeDeclaration> containsMoreMethods(int nbMethod){
		List<TypeDeclaration> results = new ArrayList<>();
		for(Entry<TypeDeclaration, List<MethodDeclaration>> entry : methodsByType.entrySet()) {
			
		    TypeDeclaration cle = entry.getKey();
		    List<MethodDeclaration> valeur = entry.getValue();
		    
		    if(valeur.size() >= nbMethod) {
		    	results.add(cle);
		    }
		}
		System.out.println("----------" +" Classes qui contiennent plus que " + nbMethod+ " methodes" + "----------");
		for(TypeDeclaration type : results) {
			System.out.println(type.getName().toString());
		}
		return results;
	}

	public static List<TypeDeclaration> getMaxMethodsByTypes(double percentage){
		double nbClassesToFound = typesCount * percentage;
		List<TypeDeclaration> results = new ArrayList<>();
		List<TypeDeclaration> keys = methodsByType.entrySet().stream()
				.sorted((left, right) -> Integer.compare(right.getValue().size(), left.getValue().size()))
				.map(entry -> entry.getKey()).collect(Collectors.toList());

		System.out.println("----------" + percentage + " classes qui contiennent le plus de methodes" + "----------");
		for (int i = 0; i < nbClassesToFound; i++) {
			System.out.println(keys.get(i).getName().toString());
			results.add(keys.get(i));
		}
		return results;
	}
	
	public static List<TypeDeclaration> getMaxFieldByTypes(double percentage){
		double nbClassesToFound = typesCount * percentage;
		List<TypeDeclaration> results = new ArrayList<>();
		List<TypeDeclaration> keys = fieldsByType.entrySet().stream()
				.sorted((left, right) -> Integer.compare(right.getValue().size(), left.getValue().size()))
				.map(entry -> entry.getKey()).collect(Collectors.toList());

		System.out.println("----------" + percentage + " classes qui contiennent le plus de fields" + "----------");
		for (int i = 0; i < nbClassesToFound; i++) {
			System.out.println(keys.get(i).getName().toString());
			results.add(keys.get(i));
		}
		return results;
	}
	
	public int getNbFields() {
		int totalFieldCount = 0;
		
		for (Entry<TypeDeclaration, List<FieldDeclaration>> entry : fieldsByType.entrySet()) {
		    String key = entry.getKey().toString();
		    List<FieldDeclaration> value = entry.getValue();
		    
		    totalFieldCount += value.size();
		}
		
		return totalFieldCount;
	}
	
	public int getNbTypes() {
		return types.size();
	}
	
	public int averageNumberOfMethods() {
		
		int totalMethodCount = 0;
		
		for (Entry<TypeDeclaration, List<MethodDeclaration>> entry : methodsByType.entrySet()) {
		    String key = entry.getKey().toString();
		    List<MethodDeclaration> value = entry.getValue();
		    
		    totalMethodCount += value.size();
		}
		
		return totalMethodCount/typesCount;
	}
	
	public String toString() {
		String result = "";
		for(Entry<TypeDeclaration, List<MethodDeclaration>> entry : methodsByType.entrySet()) {
			
		    String cle = entry.getKey().getName().toString();
		    List<MethodDeclaration> valeur = entry.getValue();
		  
		    result += cle +" : \n";
		    
		    for(MethodDeclaration method : valeur) {
		    	result += method.getName() + "\n";
		    }
		}
		
		result += "Nombre moyen de methodes : " + averageNumberOfMethods() + "\n";
		result += "Nombre de fields : " + getNbFields() + "\n";
		return result;
	}
}
