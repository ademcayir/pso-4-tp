package tr.edu.istanbul.pso4tp;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;


public class TPProblem {
	private int num_of_sources;
	private int num_of_destinations;
	private int sources[];
	private int destinations[];
	private int costs[][];
	private int sources_temp[];
	private int destinations_temp[];
	private int amounts[][];
	private int solution[];
	
	public TPProblem(){
		
	}
	public TPProblem(int num_of_sources,int num_of_destinations){
		this.num_of_sources = num_of_sources;
		this.num_of_destinations = num_of_destinations;
		sources = new int[num_of_sources];
		destinations = new int[num_of_destinations];
		costs = new int[num_of_sources][num_of_destinations];
		sources_temp = new int[num_of_sources];
		destinations_temp = new int[num_of_destinations];
		amounts = new int[num_of_sources][num_of_destinations];
	}
	public void save(OutputStream output) throws IOException {
		String str = generate_represent_string(false);
		output.write(str.getBytes());
	}
	public void load(InputStream input){
		Scanner scanner = new Scanner(input);
		Vector<String[]> lines = new Vector<String[]>();
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			if (line.startsWith("--")){
				continue;
			}
			String pieces[] = parse(line);
			lines.add(pieces);
		}
		
		num_of_destinations = lines.elementAt(0).length;
		num_of_sources = lines.size() - 1;
		sources = new int[num_of_sources];
		destinations = new int[num_of_destinations];
		costs = new int[num_of_sources][num_of_destinations];
		sources_temp = new int[num_of_sources];
		destinations_temp = new int[num_of_destinations];
		amounts = new int[num_of_sources][num_of_destinations];
		for (int i = 0; i < sources.length; i++) {
			sources[i] = Integer.parseInt(lines.elementAt(i+1)[0]);
		}
		for (int i = 0; i < destinations.length; i++) {
			destinations[i] = Integer.parseInt(lines.elementAt(0)[i]);
		}
		for (int i = 0; i < num_of_sources; i++) {
			for (int j = 0; j < num_of_destinations; j++) {
				String piece = lines.elementAt(i+1)[j+1];
				StringBuffer amount = new StringBuffer(); 
				StringBuffer cost = new StringBuffer();
				if (piece.indexOf('(') == -1){
					cost.append(piece);
					amount.append('0');
				} else {
					boolean add_to_amount = true;
					for (int k = 0; k < piece.length(); k++) {
						if (add_to_amount){
							if (piece.charAt(k) != '('){
								amount.append(piece.charAt(k));
							} else {
								add_to_amount = false;
							}
						} else {
							if (piece.charAt(k) != ')'){
								cost.append(piece.charAt(k));
							} else {
								break;
							}
						}
					}
				}
				
				amounts[i][j] = Integer.parseInt(amount.toString().trim());
				costs[i][j] = Integer.parseInt(cost.toString().trim());
			}
		}
	}
	private String[] parse(String line){
		Vector<String> v = new Vector<String>();
		int loc0 = 0;
		int loc1 = line.indexOf('|');
		while (loc1 != -1){
			String piece = line.substring(loc0,loc1).trim();
			if (piece.length() > 0){
				v.add(piece);
			}
			loc0 = loc1+1;
			loc1 = line.indexOf('|',loc0);
		}
		if (loc0 < line.length()){
			String piece = line.substring(loc0,line.length()).trim();
			if (piece.length() > 0){
				v.add(piece);
			}
		}
		String arr[] = new String[v.size()];
		v.copyInto(arr);
		return arr;
	}
	public int[] generateVogelsSolution(){
		int sources_temp[] = new int[sources.length];
		int destinations_temp[] = new int[destinations.length];
		int amounts[][] = new int[sources.length][destinations.length];
		System.arraycopy(sources, 0, sources_temp, 0, sources.length);
		System.arraycopy(destinations, 0, destinations_temp, 0, destinations.length);
		for (int i = 0; i < amounts.length; i++) {
			for (int j = 0; j < amounts[i].length; j++) {
				amounts[i][j] = -1;
			}
		}
		int solution[] = new int[getNumOfDecodeSeries()];
		int solution_count = 0;
		while (true){
			int max_penalty = 0;
			int penalty_i=-1;
			int penalty_j=-1;
			for (int i = 0; i < costs.length; i++) {
				int min_j=-1;
				int min_j2 = -1;
				for (int j = 0; j < costs[i].length; j++) {
					if (amounts[i][j] == -1){
						if (min_j == -1){
							min_j = j;
						} else if (min_j2 == -1){
							if ( costs[i][j] < costs[i][min_j] ){
								 min_j2 = min_j;
								 min_j = j;
							} else {
								min_j2 = j;
							}
						} else {
							if ( costs[i][j] < costs[i][min_j] ){
								min_j2 = min_j;
								min_j = j;
							} else if ( costs[i][j] < costs[i][min_j2] ){
								min_j2 = j;
							}
						}
					}
				}
				if (min_j != -1 && min_j2 != -1){
					int penalty = costs[i][min_j2] - costs[i][min_j];
					if (penalty > max_penalty){
						max_penalty = penalty;
						penalty_i = i;
						penalty_j = min_j;
					}
				}
			}
			
			for (int j = 0; j < costs[0].length; j++) {
				int min_i=-1;
				int min_i2 = -1;
				for (int i = 0; i < costs.length; i++) {
					if (amounts[i][j] == -1){
						if (min_i == -1){
							min_i = i;
						} else if (min_i2 == -1){
							if ( costs[i][j] < costs[min_i][j] ){
								 min_i2 = min_i;
								 min_i = i;
							} else {
								min_i2 = i;
							}
						} else {
							if ( costs[i][j] < costs[min_i][j] ){
								min_i2 = min_i;
								min_i = i;
							} else if ( costs[i][j] < costs[min_i2][j] ){
								min_i2 = i;
							}
						}
					}
				}
				if (min_i != -1 && min_i2 != -1){
					int penalty = costs[min_i2][j] - costs[min_i][j];
					if (penalty > max_penalty){
						max_penalty = penalty;
						penalty_i = min_i;
						penalty_j = j;
					}
				}
				
			}
			if (penalty_i != -1 && penalty_j != -1){
				int min = Math.min(sources_temp[penalty_i], destinations_temp[penalty_j]);
				sources_temp[penalty_i] -= min;
				destinations_temp[penalty_j] -= min;
				amounts[penalty_i][penalty_j] = min;
				solution[solution_count] = penalty_i * num_of_destinations + penalty_j;
				solution_count++;
			} else {
				break;
			}
		}
		
		for (int i = 0; i < amounts.length; i++) {
			for (int j = 0; j < amounts[i].length; j++) {
				if (amounts[i][j] == -1){
					solution[solution_count] = i * num_of_destinations + j;
					solution_count++;
				}
			}
		}
		return solution;
	}
	public void generate(int min_amount,int max_amount,int min_cost,int max_cost){
		Random random = new Random();
		int total = 0;
		for (int i = 0; i < sources.length; i++) {
			sources[i] = min_amount + Math.abs(random.nextInt()%(max_amount - min_amount));
			total += sources[i];
		}
		int total2 = 0;
		for (int i = 0; i < destinations.length; i++) {
			destinations[i] = min_amount + Math.abs(random.nextInt()%(max_amount - min_amount));
			total2 += destinations[i];
		}
		while (total2 > total || total > total2){
			
			int from = Math.abs(random.nextInt()%destinations.length);
			int amount_modulus = (destinations[from]/3);
			if (amount_modulus < 1){
				amount_modulus = 1;
			}
			int amount = Math.abs(random.nextInt()%amount_modulus);
			if (amount == 0){
				amount++;
			}
			if (total2 > total){
				if (destinations[from] - amount < 1){
					continue;
				}
				destinations[from] -= amount;
				total2 -= amount;
			} else if (total2 < total){
				destinations[from] += amount;
				total2 += amount;
			} 
		}
		max_cost++;
		Vector<Integer> v = new Vector<Integer>();
		int t = getNumOfDecodeSeries();
		int cost_range = max_cost - min_cost;
		int fill_amount = (t/cost_range)*cost_range;
		int random_amount = t - fill_amount;
		for (int i = 0; i < random_amount; i++) {
			int rand = Math.abs(random.nextInt()%(max_cost-min_cost))+min_cost;
			if (v.contains(rand)){
				i--;
				continue;
			}
			v.add(rand);
		}
		for (int i = 0; i < fill_amount; i++) {
			int rand = i % (max_cost - min_cost)+min_cost;
			v.add(rand);
		}
		
		
		for (int i = 0; i < v.size(); i++) {
			int i1 = Math.abs(random.nextInt()%v.size());
			int i2 = Math.abs(random.nextInt()%(v.size()-1) );
			int tmp = v.elementAt(i1);
			v.removeElementAt(i1);
			v.insertElementAt(tmp, i2);
		}
		for (int i = 0; i < num_of_sources; i++) {
			for (int j = 0; j < num_of_destinations; j++) {
				costs[i][j] = v.firstElement();
				v.removeElementAt(0);
			} 
		}
	}
	public int getAmountOfDestination(int dest_num){
		return destinations[dest_num];
	}
	public int getAmountOfSource(int source_num){
		return sources[source_num];
	}
	public int getCellOfCost(int source,int destinations){
		return costs[source][destinations];
	}
	public int getCellOfAmount(int source,int destinations){
		return amounts[source][destinations];
	}
	public void clear(){
		for (int i = 0; i < sources_temp.length; i++) {
			sources_temp[i] = sources[i];
		}
		for (int i = 0; i < destinations.length; i++) {
			destinations_temp[i] = destinations[i];
		}
		for (int i = 0; i < num_of_sources; i++) {
			for (int j = 0; j < num_of_destinations; j++) {
				amounts[i][j] = 0;
			}
		}
	}
	
	public void show(boolean dont_show_zero_amount){
		String str = generate_represent_string(dont_show_zero_amount);
		System.out.println(str);
	}
	private String generate_represent_string(boolean dont_show_zero_amount){
		StringBuffer buf = new StringBuffer();
		int max_amount = 0;
		int max_cost = 0;
		for (int i = 0; i < num_of_sources; i++) {
			for (int j = 0; j < num_of_destinations; j++) {
				if (amounts[i][j] > max_amount){
					max_amount = amounts[i][j];
				}
				if (costs[i][j] > max_cost){
					max_cost = costs[i][j];
				}
			} 
		}
		
		int digit_amount = (""+max_amount).length();
		int digit_cost = (""+max_cost).length();
		for (int i = 0; i < digit_amount; i++) {
			buf.append(' ');
		}
		buf.append(" | ");
		for (int i = 0;i < num_of_destinations ; i++) {
			buf.append(string_represent(destinations[i], digit_amount+digit_cost+2));
			buf.append(" |");
			if (i < num_of_destinations-1){
				buf.append(' ');
			}
		}
		buf.append("\n");
		int char_nums = num_of_destinations * ( digit_amount + digit_cost + 5 )+digit_amount+2;
		for (int i = 0; i < char_nums; i++) {
			buf.append('-');
		}
		buf.append("\n");
		for (int i = 0; i < num_of_sources; i++) {
			buf.append(string_represent(sources[i], digit_amount));
			buf.append(" | ");
			for (int j = 0; j < num_of_destinations; j++) {
				if (dont_show_zero_amount && amounts[i][j] == 0){
					for  (int t =0;t< digit_amount+digit_cost+2;t++){
						buf.append(' ');
					}
					buf.append(" |");
				} else {
					buf.append(string_represent(amounts[i][j], digit_amount));
					buf.append('(');
					buf.append(string_represent(costs[i][j], digit_cost));
					buf.append(") |");
				}
				if (j < num_of_destinations -1 ){
					buf.append(' ');
				}
			}
			buf.append("\n");
			for (int j = 0; j < char_nums; j++) {
				buf.append('-');
			}
			buf.append("\n");
		}
		return buf.toString();
	}
	private String string_represent(int number,int digit){
		StringBuffer buf = new StringBuffer();
		buf.append(number);
		while (buf.length() < digit){
			buf.insert(0, ' ');
		}
		return buf.toString();
	}
	public int getTotalCost(){
		int total = 0;
		for (int i = 0; i < num_of_sources; i++) {
			for (int j = 0; j < num_of_destinations; j++) {
				total += amounts[i][j] * costs[i][j];
			}
		}
		return total;
	}
	public int[] getSolution(){
		return solution;
	}
	public void orderWith(int series[]){
		if (series == null || series.length != getNumOfDestinations()*getNumOfSources()){
			return;
		}
		solution = new int[series.length];
		System.arraycopy(series, 0, solution, 0, solution.length);
		clear();
		for (int i = 0; i < series.length; i++) {
			int target_source = series[i]/num_of_destinations;
			int target_destinations = series[i] % num_of_destinations;
			if (sources_temp[target_source] > destinations_temp[target_destinations]){
				amounts[target_source][target_destinations] += destinations_temp[target_destinations];
				sources_temp[target_source] -= destinations_temp[target_destinations];
				destinations_temp[target_destinations] = 0;
			} else if (sources_temp[target_source] < destinations_temp[target_destinations]){
				amounts[target_source][target_destinations] += sources_temp[target_source];
				destinations_temp[target_destinations] -= sources_temp[target_source] ;
				sources_temp[target_source] = 0;
			} else if (sources_temp[target_source] == destinations_temp[target_destinations]){
				amounts[target_source][target_destinations] += sources_temp[target_source];
				destinations_temp[target_destinations] = 0;
				sources_temp[target_source] = 0;
			}
		}
	}
	public String getLingoFormat(){
		StringBuffer f = new StringBuffer();
		f.append("MIN=");
		for (int i = 0; i < costs.length; i++) {
			for (int j = 0; j < costs[i].length; j++) {
				f.append(costs[i][j]);
				f.append('*');
				appendX(f, i, j);
				f.append('+');
			}
		}
		f.delete(f.length()-1, f.length());
		f.append(";\r\n");
		for (int i = 0; i < num_of_sources; i++) {
			for (int j = 0; j < num_of_destinations; j++) {
				appendX(f, i, j);
				if (j != num_of_destinations-1){
					f.append('+');
				}
			}			
			
			f.append('=');
			f.append(sources[i]);
			f.append(";\r\n");
		}
		for (int j = 0; j < num_of_destinations; j++) {
			for (int i = 0; i < num_of_sources; i++) {
				appendX(f, i, j);
				if (i != num_of_sources-1){
					f.append('+');
				}
			}
			f.append('=');
			f.append(destinations[j]);
			f.append(";\r\n");
		}
		
		f.append("END");
		return f.toString();
	}
	private void appendX(StringBuffer b,int i,int j){
		b.append('X');
		int tmp = num_of_sources;
		if (tmp < num_of_destinations){
			tmp = num_of_destinations;
		}
		if (tmp < 10){
			b.append(i);
			b.append(j);
		} else if (tmp < 100){
			if (i < 10){
				b.append(0);
			}
			b.append(i);
			if (j < 10){
				b.append(0);
			}
			b.append(j);
		} else {
			if (i < 10){
				b.append(00);
			} else if (i < 100){
				b.append(0);
			}
			b.append(i);
			
			if (j < 10){
				b.append(00);
			} else if (j < 100){
				b.append(0);
			}
			b.append(j);
		}
	}
	public int getNumOfSources(){
		return num_of_sources;
	}
	public int getNumOfDestinations(){
		return num_of_destinations;
	}
	public int getNumOfDecodeSeries(){
		return num_of_destinations*num_of_sources;
	}
}
