import Visitor.TypeDeclarationVisitor;
import decorator.InvocationMethodDecorator;
import decorator.MethodDecorator;
import decorator.TypeDecorator;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import Cluster.Dendrogram;
import FileHandling.Output;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {

	public static final String projectPath = "C:\\Users\\Proprietaire\\eclipse-workspace\\BDD";
	public static final String projectSourcePath = projectPath + "\\src";
	public static String jrePath = System.getProperty("java.home") + "\\lib\\rt.jar";
	
	static HashSet<String> packages = new HashSet<>();

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		parser.setUnitName("");

		String[] sources = { projectSourcePath };
		String[] classpath = { jrePath };

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		parser.setSource(classSource);

		return (CompilationUnit) parser.createAST(null); // create and parse
	}

	public void parse() throws IOException {

		// read java files
		final File folder = new File(projectSourcePath);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		ArrayList<TypeDecorator> types = new ArrayList<>();

		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());
			TypeDeclarationVisitor visitor = new TypeDeclarationVisitor(parse);
			// lineNumber(parse);

			if (parse.getPackage() != null) {
				packages.add(parse.getPackage().getName().toString());
			}
			parse.accept(visitor);
			// Add all class and subclasses to the types list
			types.addAll(visitor.get());
		}

		/**
		 * Number of classes
		 */
		Print.printTitle("Nombre de classes");
		Print.printValue(String.valueOf(types.size()));

		/**
		 * Number of lines
		 */
		Print.printTitle("Nombre de lignes de l'application");
		int nbLines = 0;
		for (TypeDecorator type : types) {
			nbLines += type.getNbLines();
		}
		Print.printValue(String.valueOf(nbLines));

		/**
		 * Number of methods
		 */
		Print.printTitle("Nombre de methodes");
		int nbMethodes = 0;
		for (TypeDecorator type : types) {
			nbMethodes += type.getMethods().size();
		}
		Print.printValue(String.valueOf(nbMethodes));

		/**
		 * Number of packages
		 */
		Print.printTitle("Nombre de packages");
		Print.printValue(String.valueOf(packages.size()));

		/**
		 * Average Number of methodes by classes
		 */
		Print.printTitle("Nombre moyen de methodes par classes");
		Print.printValue(String.valueOf(nbMethodes / types.size()));

		/**
		 * Average Number of code lines by methodes
		 */
		Print.printTitle("Nombre moyen de lignes de codes par methodes");
		int nbLinesForAllMethods = 0;
		for (TypeDecorator type : types) {
			List<MethodDecorator> listMethods = type.getMethods();
			for (MethodDecorator method : listMethods) {
				nbLinesForAllMethods += method.getNbLines();
			}
		}
		Print.printValue(String.valueOf(nbLinesForAllMethods / nbMethodes));

		/**
		 * Average Number of fields by types
		 */
		Print.printTitle("Nombre moyen d'attributs par classes");
		int nbFieldsForAllTypes = 0;
		for (TypeDecorator type : types) {
			nbFieldsForAllTypes += type.getFields().size();
		}
		Print.printValue(String.valueOf(nbFieldsForAllTypes / types.size()));

		/**
		 * % of types that have the most number of methods
		 */
		Print.printTitle("Classes qui possedent le plus de methodes");
		List<TypeDecorator> mostMethods = GenericCompute.withMost(types, 0.1, new Comparator<TypeDecorator>() {

			@Override
			public int compare(TypeDecorator t1, TypeDecorator t2) {
				// TODO Auto-generated method stub
				return t2.getMethods().size() - t1.getMethods().size();
			}

		});
		Print.printList(mostMethods);

		/**
		 * % of types that have the most number of attributes
		 */
		Print.printTitle("Classes qui possedent le plus d'attributs");
		List<TypeDecorator> mostAttributes = GenericCompute.withMost(types, 0.1, new Comparator<TypeDecorator>() {

			@Override
			public int compare(TypeDecorator t1, TypeDecorator t2) {
				// TODO Auto-generated method stub

				return t2.getFields().size() - t1.getFields().size();
			}

		});
		Print.printList(mostAttributes);

		/**
		 * Types that exists in both
		 */
		Print.printTitle("Classes avec le + d'attributs et de methodes");
		Print.printList(GenericCompute.intersectList(mostMethods, mostAttributes));

		/**
		 * Classes qui possedent plus de X methodes
		 */

		Print.printTitle("Classes qui possedent plus de N methodes");
		Print.printList(GenericCompute.moreThan(types, 2, TypeDecorator::getNumberMethods));

		/**
		 * % of methodes with most lines by classes
		 */
		Print.printTitle("Methodes avec le + de lignes par classes");
		for (TypeDecorator type : types) {

			List<MethodDecorator> mostLinesByClasses = GenericCompute.withMost(type.getMethods(), 0.2,
					new Comparator<MethodDecorator>() {
						@Override
						public int compare(MethodDecorator m1, MethodDecorator m2) {
							// TODO Auto-generated method stub
							return m2.getNbLines() - m1.getNbLines();
						}
					});
			Print.printValue(type.toString() + " : ");
			Print.printList(mostLinesByClasses, "    ");
		}

		/**
		 * Maximal number of parameters for all the methods in the app
		 */
		Print.printTitle("Nombre maximal de parametres dans l'application");
		List<MethodDecorator> allMethods = new ArrayList<>();
		for (TypeDecorator type : types) {
			for (MethodDecorator method : type.getMethods()) {
				allMethods.add(method);
			}
		}

		MethodDecorator[] arrayOfMethods = new MethodDecorator[allMethods.size()];
		allMethods.toArray(arrayOfMethods);
		int max = GenericCompute.max(MethodDecorator::getNbParameters, arrayOfMethods);
		Print.printValue(String.valueOf(max));

		/**
		 * Calculate Couplage between types
		 */
		Print.printTitle("Couplage entre classes");
		Coupleur.couple(types);

		LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<InvocationMethodDecorator>>> methodInvocByMethodsByType = new LinkedHashMap<>();
		for (TypeDecorator type : types) {
			for (MethodDecorator method : type.getMethods()) {
				for (InvocationMethodDecorator invoc : method.getInvocationMethods()) {
					methodInvocByMethodsByType.computeIfAbsent(type.getName(), k -> new LinkedHashMap<>())
							.computeIfAbsent(method.getName(), k -> new LinkedHashSet<>()).add(invoc);
				}
			}
		}

		Print.printTitle("Creation du graphe");
		String jsonString = GraphCreator.createJsonGraph(methodInvocByMethodsByType);
		Output output = new Output(".\\Visualisation\\input.json");
		output.write(jsonString);
		output.close();
		Print.printValue(jsonString);

		Print.printTitle("Creation du dendrogramme");
		Integer[][] couplingArray = Coupleur.couple(types);
		Dendrogram dendrogram = new Dendrogram(types, couplingArray);
		dendrogram.createDendrogram();
		// dendrogram.getNextMaxCluster();
		/*
		 * String jsonString = createJsonGraph(methodInvocByMethodsByType);
		 * 
		 * System.out.println(jsonString);
		 */
	}
}
