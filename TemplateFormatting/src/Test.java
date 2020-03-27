import java.util.ArrayList;
import java.util.Arrays;
//small scale testing of substitution
public class Test {
	public static void main(String[] args) {
		String s = "Hello @sub";
		String[] wordArray = s.split("\\b");
		System.out.println(Arrays.toString(wordArray));
		
		ArrayList<String> words = new ArrayList<>(Arrays.asList(s.split("\\b")));
		int size = words.size();
		if(s.contains("@")) {
			for(int i = 0; i<size; i++) {
				String spaceless =words.get(i).replaceAll("\\s+","");
				//substitutes the @ word
				if(spaceless.equals("@")) {
					words.set(i," world");
					words.remove(i+1);
					size--;
				}
			}
		}
		String line_mod = "";
		for(String word : words) {
			line_mod += word;
		}
		//only output non terminal lines
		if(line_mod.charAt(0) != '!') {
			System.out.println(line_mod);
		}
	}
}
	
