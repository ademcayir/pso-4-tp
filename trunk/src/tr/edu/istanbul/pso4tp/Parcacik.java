package tr.edu.istanbul.pso4tp;

import java.util.Collections;


class Particle {
	private double x[];
	private double v[];
	private double best_x[];
	private int best_order[];
	private int current_order[];
	private int best_cost;
	private TPProblem tp_problem;
	public void init(TPProblem tp_problem,int x_min,int x_max,int v_min,int v_max){
		this.tp_problem = tp_problem;
		x = new double[tp_problem.getNumOfDecodeSeries()];
		v = new double[tp_problem.getNumOfDecodeSeries()];
		best_x = new double[tp_problem.getNumOfDecodeSeries()];
		best_order = new int[tp_problem.getNumOfDecodeSeries()];
		current_order = new int[tp_problem.getNumOfDecodeSeries()];
		for (int i = 0; i < x.length; i++) {
			x[i] = Algorithms.random.nextDouble()*(x_max - x_min) + x_min;			
			v[i] = Algorithms.random.nextDouble()*(v_min - v_max ) + v_min;			
		}
		if (UI.instance.isStartWithVogel()){
			for (int i = 0; i < x.length-1; i++) {
				for (int j = 0; j < x.length-1-i; j++) {
					if (x[j] > x[j+1]){
						double tmp = x[j];
						x[j] = x[j+1];
						x[j+1] = tmp;
					}
				}
			}
			int vogel[] = tp_problem.generateVogelsSolution();
			double tmp[] = new double[x.length];
			for (int i = 0; i < tmp.length; i++) {
				tmp[vogel[i]] = x[i];
			}
			x = tmp;
		}
		System.arraycopy(x, 0, best_x, 0, best_x.length);
		Algorithms.apply_SPV("init",best_x, best_order);
		tp_problem.orderWith(best_order);
		best_cost = tp_problem.getTotalCost();
	}
	public double[] getBestX(){
		return best_x;
	}
	public int[] getBestOrder(){
		return best_order;
	}
	public int getBestCost(){
		return best_cost;
	}
	public double[] getCurrentX(){
		return x;
	}
	public double[] getCurrentV(){
		return v;
	}
	public void nextMove(double global_best[], double inertia,double c1, double c2){
		for (int i = 0; i < global_best.length; i++) {
			v[i] = v[i] * inertia + c1 * Math.abs(Algorithms.random.nextDouble()) * (best_x[i] - x[i]) + c2 * Math.abs(Algorithms.random.nextDouble())*(global_best[i] - x[i]);
			x[i] = x[i] + v[i];
		}
		Algorithms.apply_SPV("nextMove",x,current_order);
		tp_problem.orderWith(current_order);
		int current_cost = tp_problem.getTotalCost();
		if (current_cost < best_cost ){
			best_cost = current_cost;
			System.arraycopy(x, 0, best_x, 0, x.length);
			System.arraycopy(current_order, 0, best_order, 0, current_order.length);
		}
	}
}
