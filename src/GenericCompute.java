import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

public class GenericCompute {

	public static <T> List<T> withMost(List<T> types, double percentage, Comparator<T> comparator){
		
		ArrayList<T> copiedList = new ArrayList<>(types);
		
		copiedList.sort(comparator);
		
		int nbMethods = (int) (types.size() * percentage);
		
		return copiedList.subList(0, nbMethods);
		
	}
	
	public static <T> ArrayList<T> intersectList(List<T> l1 , List<T> l2) {
		ArrayList<T> copiedList = new ArrayList<>(l1);
		copiedList.retainAll(l2);
		return new ArrayList<>(copiedList);
	}
	
	public static <T> List<T> moreThan(List<T> list, int number, ToIntFunction<T> value){
		List<T> filtered = new ArrayList<>();
		for (T element : list) {
			if (value.applyAsInt(element) > number) {
				filtered.add(element);
				}
	        }
	        return filtered;
	}
	
	public static <T> int max(ToIntFunction<T> value, T ... t) {
		
		int max = value.applyAsInt(t[0]);
		
		for(T element : t) {
			if(value.applyAsInt(element) > max) {
				max = value.applyAsInt(element);
				System.out.println(element);
			}
		}
		return max;	
	}
}
