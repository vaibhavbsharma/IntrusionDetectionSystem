
public class Individual implements Comparable<Individual>{
	String gene;
	double fitness;
	Individual(){}
	Individual(String _gene){
		gene = _gene;
	}
	@Override
	public int compareTo(Individual otherIndividual) {
		if(fitness > otherIndividual.fitness)
			return 1;
		if(fitness < otherIndividual.fitness)
			return -1;
		return 0;
	}
}
