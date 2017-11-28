import Visitor.MethodDeclarationVisitor;
import Visitor.TypeDeclarationVisitor;
import Visitor.Visitor;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Parser {

    public static final String projectPath = "C:\\Users\\Proprietaire\\eclipse-workspace\\JenaProject";
    public static final String projectSourcePath = projectPath + "\\src";
    public static String jrePath = System.getProperty("java.home") + "\\lib\\rt.jar";

    static ArrayList<Visitor> visitors = new ArrayList<>();
    static HashSet<String> packages = new HashSet<>();
    static HashMap<MethodDeclaration, Integer> methodlineCounter = new HashMap<>();
    static HashMap<TypeDeclaration, Integer> lineCounter = new HashMap<>();
    static LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<MethodInvocation>>> methodInvocByMethodsByType = new LinkedHashMap<>(); //C'est sur ca qu'il faut faire le graphe

    Parser() {
        visitors = new ArrayList<>();
    }

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

        String[] sources = {projectSourcePath};
        String[] classpath = {jrePath};

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }

    public void parse() throws IOException {

        // read java files
        final File folder = new File(projectSourcePath);
        ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

        //
        for (File fileEntry : javaFiles) {
            String content = FileUtils.readFileToString(fileEntry);
            // System.out.println(content);

            CompilationUnit parse = parse(content.toCharArray());
            lineNumber(parse);

            if (parse.getPackage() != null) {
                packages.add(parse.getPackage().getName().toString());
            }

            for (Visitor visitor : visitors) {
                parse.accept(visitor);
            }
        }
        for (Visitor visitor : visitors) {
            System.out.println(visitor.toString());
        }

        System.out.println("Nombre de packages : " + packages.size());
        printLineNumber();
        List<TypeDeclaration> classesByMaxMethods = TypeDeclarationVisitor.getMaxMethodsByTypes(0.9);
        List<TypeDeclaration> classesByMaxFields = TypeDeclarationVisitor.getMaxFieldByTypes(0.5);

        List<TypeDeclaration> intersect = new ArrayList<>(classesByMaxMethods);
        intersect.retainAll(classesByMaxFields);
        System.out.println("----------" + "Classes appartenant aux deux cat�gories" + "----------");
        for (TypeDeclaration type : intersect) {
            System.out.println(type.getName().toString());
        }

        TypeDeclarationVisitor.containsMoreMethods(3);
        methodsByLineNumber(0.4);

        System.out.println("----------" + " Nombre de parametres maximaux" + "----------");
        System.out.println(MethodDeclarationVisitor.getMaxParameter());


        methodInvocByMethodsByType = TypeDeclarationVisitor.getFullPackage();
        //System.out.println(methodInvocByMethodsByType);

        String jsonString = createJsonGraph(methodInvocByMethodsByType);

        System.out.println(jsonString);
    }

    private String createJsonGraph(
            LinkedHashMap<String, LinkedHashMap<String, LinkedHashSet<MethodInvocation>>> methodInvocByMethodsByType) {
        StringBuilder st = new StringBuilder();
        HashMap<String, Integer> nodes = new HashMap<>();
        HashMap<Integer, HashSet<Integer>> links = new HashMap<>();
        ArrayList<Integer> countParents = new ArrayList<>();
        ArrayList<Boolean> belongsToProject = new ArrayList<>();

        for (Entry<String, LinkedHashMap<String, LinkedHashSet<MethodInvocation>>> stringLinkedHashMapEntry : methodInvocByMethodsByType
                .entrySet()) {
            for (Entry<String, LinkedHashSet<MethodInvocation>> stringLinkedHashSetEntry : stringLinkedHashMapEntry
                    .getValue().entrySet()) {

                String caller = stringLinkedHashMapEntry.getKey() + "." + stringLinkedHashSetEntry.getKey();


                int callerId;
                if (nodes.containsKey(caller)) {
                    callerId = nodes.get(caller);
                } else {
                    callerId = nodes.size();
                    nodes.put(caller, callerId);
                    countParents.add(0);
                    belongsToProject.add(true);
                }


                HashSet<Integer> callees = new HashSet<>();
                //System.out.println(stringLinkedHashMapEntry.getKey() + "." + stringLinkedHashSetEntry.getKey());
                for (MethodInvocation methodInvocation : stringLinkedHashSetEntry.getValue()) {
                    IMethodBinding resolve = methodInvocation.resolveMethodBinding();
                    // partial only srcpath
                    // if (resolve != null  && methodInvocByMethodsByType.containsKey(resolve.getDeclaringClass().getName())) {
                    if (resolve != null) { // full with javaclasses

                        //System.out.println("\t: " + resolve.getDeclaringClass().getName() + "." +
                        //                          resolve.getMethodDeclaration().getName());

                        String callee = resolve.getDeclaringClass().getName() + "." +
                                resolve.getMethodDeclaration().getName();
                        int calleeId;

                        if (nodes.containsKey(callee)) {
                            calleeId = nodes.get(callee);
                            countParents.set(calleeId, countParents.get(calleeId) + 1); // incrementing parent count
                        } else {
                            calleeId = nodes.size();
                            nodes.put(callee, calleeId);
                            countParents.add(1);// he has a parent
                            belongsToProject
                                    .add(methodInvocByMethodsByType.containsKey(resolve.getDeclaringClass().getName()));
                        }


                        callees.add(calleeId);
                    }
                }

                links.put(callerId, callees);
            }
        }

        //yeah that json maggle
        st.append("{\"nodes\":[");
        for (Entry<String, Integer> nodesString : nodes.entrySet()) {
            st.append("{\"id\":").append(nodesString.getValue()).append(",")
              .append(" \"name\": \"").append(nodesString.getKey()).append("\",")
              .append(" \"own\": ").append(belongsToProject.get(nodesString.getValue())).append("},");
        }
        st.deleteCharAt(st.length() - 1);
        st.append("], \"links\":[");

        for (Entry<Integer, HashSet<Integer>> link : links.entrySet()) {
            int caller = link.getKey();

            for (int callee : link.getValue()) {
                int parentCount = countParents.get(callee);

                float weight = (float) (1 / (0.5 * parentCount + 0.5));
                st.append("{\"source\":").append(caller).append(",")
                  .append(" \"target\": ").append(callee).append(",")
                  .append(" \"str\": ").append(weight)
                  .append("},");
            }
        }
        st.deleteCharAt(st.length() - 1);
        st.append("]}");
        return st.toString();
    }

    public HashMap<TypeDeclaration, List<MethodDeclaration>> methodsByLineNumber(double percentage) {

        HashMap<TypeDeclaration, List<MethodDeclaration>> methodsByType = TypeDeclarationVisitor.getMethodsByType();
        HashMap<TypeDeclaration, List<MethodDeclaration>> results = new HashMap<>();

        for (Entry<TypeDeclaration, List<MethodDeclaration>> entry : methodsByType.entrySet()) {

            TypeDeclaration key = entry.getKey();
            List<MethodDeclaration> listMethods = entry.getValue();

            double nbMethodsByClass = listMethods.size() * percentage;
            if (nbMethodsByClass < 1 && nbMethodsByClass > 0) nbMethodsByClass = 1.0;
            Collections.sort(listMethods, new Comparator<MethodDeclaration>() {

                @Override
                public int compare(MethodDeclaration m1, MethodDeclaration m2) {
                    // TODO Auto-generated method stub
                    return Integer.compare(methodlineCounter.get(m1), methodlineCounter.get(m2));
                }
            });

            List<MethodDeclaration> methods = listMethods.subList(0, (int) nbMethodsByClass);
            results.put(key, methods);
        }

        System.out.println("----------" + "Methodes qui possedent le plus de lignes par classes" + "---------");
        for (Entry<TypeDeclaration, List<MethodDeclaration>> entry : results.entrySet()) {
            System.out.println(entry.getKey().getName().toString());

            List<MethodDeclaration> methods = entry.getValue();
            for (MethodDeclaration method : methods) {
                System.out.println("    " + method.getName().toString());
            }
        }
        return results;
    }

    public void lineNumber(CompilationUnit cu) {
        cu.accept(new ASTVisitor() {

            public boolean visit(TypeDeclaration node) {
                int startLine = cu.getLineNumber(node.getStartPosition());
                int length = node.getLength();
                int endLine = cu.getLineNumber(node.getStartPosition() + length);
                lineCounter.put(node, endLine - startLine);
                return true;
            }

            public boolean visit(MethodDeclaration node) {
                int startLine = cu.getLineNumber(node.getStartPosition());
                int length = node.getLength();
                int endLine = cu.getLineNumber(node.getStartPosition() + length);
                methodlineCounter.put(node, endLine - startLine);
                return true;
            }
        });
    }

    public void printLineNumber() {

        System.out.println("---------- Nombre de lignes par classes ----------");
        for (Entry<TypeDeclaration, Integer> entry : lineCounter.entrySet()) {

            String cle = entry.getKey().getName().toString();
            int valeur = entry.getValue();

            System.out.println(cle + " : " + valeur);
        }
        System.out.println("---------- Nombre de lignes par methodes ----------");
        for (Entry<MethodDeclaration, Integer> entry : methodlineCounter.entrySet()) {

            String cle = entry.getKey().getName().toString();
            int valeur = entry.getValue();

            System.out.println(cle + " : " + valeur);
        }
    }

    public void addVisitor(Visitor visitor) {
        visitors.add(visitor);
    }
}
