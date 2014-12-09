
public class Individual implements Comparable<Individual>{
	String gene_;
	double fitness_;
	double [][] cMatrix;
	Individual(){
		fitness_=-1;
	}
	Individual(String _gene){
		gene_ = _gene;
		fitness_=-1;
	}
	public int compareTo(Individual otherIndividual) {
		if(fitness_ > otherIndividual.fitness_)
			return 1;
		if(fitness_ < otherIndividual.fitness_)
			return -1;
		return 0;
	}
}
