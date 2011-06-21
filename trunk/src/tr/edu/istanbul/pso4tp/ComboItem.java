package tr.edu.istanbul.pso4tp;

public class ComboItem {
	public ComboItem(int i[],int cost,String value){
		this.solution = i;
		this.i = cost;
		this.value = value;
	}
	public ComboItem(int i,String value){
		this.i = i;
		this.value = value;
	}
	int i;
	String value;
	int solution[];
	public String toString() {
		return value;
	}
}