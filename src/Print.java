import java.util.List;

public class Print {

	public static void printTitle(String title) {
		System.out.println("----------" + title + "----------");
	}
	
	public static void printValue(String value) {
		System.out.println(value);
	}
	
	public static <T> void printList(List<T> list) {
		for(T t : list) {
			System.out.println(t.toString());
		}
	}
}
