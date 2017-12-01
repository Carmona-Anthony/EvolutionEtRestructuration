package Visitor;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public abstract class Visitor extends ASTVisitor{
	
	CompilationUnit root;
	
	Visitor(CompilationUnit cu){
		root = cu;
	}
	
	public abstract <T> List<T> get();
	
}
