package tr.edu.istanbul.pso4tp;
import java.util.Random;





public class Algorithms {
	
	public static Random random = new Random();
	private static boolean temp_full_list[];
	
	public static void cozum_sira_goster(int cozum[]){
		System.out.print("sira>");
		for (int i = 0; i < cozum.length; i++) {
			System.out.print(cozum[i]+",");
		}
		System.out.println();
	}
	
	public static void apply_SPV(String from,double x[],int order[]){

		if (temp_full_list == null || temp_full_list.length != x.length){
			temp_full_list = new boolean[x.length];
		}
		for (int i = 0; i < temp_full_list.length; i++) {
			temp_full_list[i] = false;
		}
		
		boolean min_set;
		for (int i = 0; i < order.length; i++) {
			double min = 1000;
			int target=0;
			min_set = false;
			for (int j = 0; j < x.length; j++) {
				if (!temp_full_list[j] && (!min_set || x[j] < min)){
					min_set = true;
					target = j;
					min = x[j];
				}
			}
			order[i] = target;
			temp_full_list[target] = true;
		}
	}
	
	private static void swap(double a[],int i,int j){
		double tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	private static void swap(int a[],int i,int j){
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}
	private static int[] clone(int a[]){
		int t[] = new int[a.length];
		System.arraycopy(a, 0, t, 0, t.length);
		return t;
	}
	private static double[] clone(double a[]){
		double t[] = new double[a.length];
		System.arraycopy(a, 0, t, 0, t.length);
		return t;
	}
	
	
	
	public static String getArrayString(double array[]){
		return getArrayString(array,5);
	}
	public static String getArrayString(double array[],int hassasiyet){
		StringBuffer buf = new StringBuffer();
		buf.append('[');
		for (int i = 0; i < array.length; i++) {
			buf.append(doubleToString(array[i],hassasiyet));
			if(array.length != i + 1){
				buf.append(',');
			}
		}
		buf.append(']');
		return buf.toString();
	}
	public static String doubleToString(double d,int hassasiyet){
		if (hassasiyet == -1){
			return ""+d;
		}
		String s = ""+d;
		if (d >= 0){
			s = "+"+s;
		}
		while (s.length() < hassasiyet){
			s += "0";
		}
		return s.substring(0,hassasiyet);
	}
	public static String getArrayString(int array[]){
		StringBuffer buf = new StringBuffer();
		buf.append('[');
		for (int i = 0; i < array.length; i++) {
			buf.append(array[i]);
			if(array.length != i + 1){
				buf.append(',');
			}
		}
		buf.append(']');
		return buf.toString();
	}
}
