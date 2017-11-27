package Visitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDeclarationVisitor extends Visitor {
	
	static int typesCount = 0;
	
	static List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	static HashMap<TypeDeclaration, List<MethodDeclaration>> methodsByType = new HashMap<>();
	static HashMap<TypeDeclaration, List<FieldDeclaration>> fieldsByType = new HashMap<>();
	static LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<MethodInvocation>>> fullPackage = new LinkedHashMap<>();

	public boolean visit(TypeDeclaration node) {
		
		MethodDeclarationVisitor methodVisitor = new MethodDeclarationVisitor();
		FieldVisitor fieldVisitor = new FieldVisitor();
		node.accept(methodVisitor);
		node.accept(fieldVisitor);
		
		for(MethodDeclaration method : methodVisitor.getMethods()) {
			 method.accept(new ASTVisitor() {
				 public boolean visit(MethodInvocation methodInvocation) {
					 fullPackage.computeIfAbsent(node.getName().toString(), k-> new LinkedHashMap<String, LinkedHashSet<MethodInvocation>>()).computeIfAbsent(method.getName().toString(), k-> new LinkedHashSet<MethodInvocation>()).add(methodInvocation);
					 return true;
				 }
			 });
		}
		
		methodsByType.put(node, methodVisitor.getMethods());
		fieldsByType.put(node, fieldVisitor.getFields());
		
		typesCount++;
		types.add(node);
		return super.visit(node);
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
	
	public List<TypeDeclaration> getTypes() {
		return types;
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
