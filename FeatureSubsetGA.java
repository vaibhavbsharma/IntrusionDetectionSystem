import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

import weka.core.Instances;



public class FeatureSubsetGA {
	Instances training_set_, test_set_;
	Vector<Individual> population_;
	Random generator_;
	int feature_size_;
	int population_size_;
	PrintWriter writer_;
	
	
	
	FeatureSubsetGA(PrintWriter _writer, int population_size, int _feature_size) throws FileNotFoundException, UnsupportedEncodingException {
		writer_ = _writer;
		System.out.println("Creating FeatureSubsetGA");
		feature_size_=_feature_size;
		generator_ = new Random(System.currentTimeMillis());
		int training_set_feature_size=0, test_set_feature_size=-1;

		BufferedReader breader_train = null;
		try {
			breader_train = new BufferedReader(new FileReader("/Users/vaibhav/datasets/KDDTrain.arff"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			training_set_ = new Instances(breader_train);
			training_set_.setClassIndex(training_set_.numAttributes()-1);
			System.out.println("Number of original training attributes: "+training_set_.numAttributes());
			training_set_feature_size = training_set_.numAttributes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			breader_train.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Created training set");
		BufferedReader breader_test = null;
		try {
			breader_test = new BufferedReader(new FileReader("/Users/vaibhav/datasets/KDDTest.arff"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			test_set_ = new Instances(breader_test);
			test_set_.setClassIndex(test_set_.numAttributes()-1);
			System.out.println("Number of original testing attributes: "+test_set_.numAttributes());
			test_set_feature_size = test_set_.numAttributes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Created test set");
		population_ = new Vector<Individual>();
		initializePopulation(population_size);
		if(training_set_feature_size == test_set_feature_size)
			feature_size_ = _feature_size;//training_set_feature_size;
		else {
			System.out.println("Feature size mismatch between training set and test set");
			System.exit(1);
		}
	}
	
	int randomGenerator(int n) {
	        int ret= generator_.nextInt(n+1);
	        //System.out.println("Returning random number = "+ret+" bound within "+n);
	        return ret;
	}
	
	void initializePopulation(int num) {
		System.out.println("Initiliazing population");
		population_size_ = num;
		for(int i=0;i<population_size_;i++) {
			String s = new String();
			for(int j=0;j<feature_size_;j++)
				s+=(char)(randomGenerator(1)+'0');
			population_.add(new Individual(s));
		}
		System.out.println("Completed population initialization");
	}
	
	void calcFitness() throws Exception{
		System.out.println("Calculating fitness");
		for(int i=0;i<population_size_;i++){
			if(population_.elementAt(i).fitness_==-1) {
				CandidateClassifier temp_classifier = 
						new CandidateClassifier(population_.elementAt(i).gene_,training_set_,test_set_);
				temp_classifier.classify();
				population_.elementAt(i).fitness_=temp_classifier.getFitness1();
				population_.elementAt(i).cMatrix=temp_classifier.getCMatrix();
			}
			else {
				System.out.println(population_.elementAt(i).fitness_+" "+population_.elementAt(i).gene_);
			}
		}
		System.out.println("Complete fitness calculation for population");
	}
	
	void sortPopulationByFitness() {
		Collections.sort(population_);
		Collections.reverse(population_);
	}
	
	void crossoverPopulation() {
		System.out.println("Starting crossover");
		for(int i=0;i<population_size_/2;i+=1) {
			Individual tmp_ind = 
					performCrossover(population_.elementAt(i),
							population_.elementAt(population_size_-i-1));
			//population_.set(i, tmp_vec.elementAt(0));
			population_.set(population_size_-i-1, tmp_ind);
		}
		System.out.println("Completed crossover");
		
	}
	
	Individual performCrossover(Individual i1, Individual i2) {
		//System.out.println("Starting crossover"); for "+i1.gene+" and "+i2.gene);
		Individual child1;
		child1 = new Individual();
		int point1,point2;
		point1 = randomGenerator(feature_size_-1);
		point2 = randomGenerator(feature_size_-1);
		if(point1>point2) {
			int tmp=point1;
			point1=point2;
			point2=tmp;
		}
		int len1 = i1.gene_.length();
		int len2 = i2.gene_.length();
		if(len1 != len2 || len2 != feature_size_) {
			System.out.println("performCrossover found individuals with sizes = "+len1+" "+len2+" feature_size_ = "
					+feature_size_);
		}
		//System.out.println("Using point1 = "+point1+" point2= "+point2);
		child1.gene_=i1.gene_.substring(0,point1) + i2.gene_.substring(point1,point2+1);
		if(point2+1<=len1)
				child1.gene_+= i1.gene_.substring(point2+1,len1);
		
		
		//Mutating 1 bit in each child randomly
		child1.gene_ = mutateGene(child1.gene_);
		if(i1.gene_==i2.gene_)//Mutate twice if parents were the same to increase diversity
			child1.gene_ = mutateGene(child1.gene_);
		
		//System.out.println("child1 = "+child1.gene+" child2 = "+child2.gene);
		return child1;
	}
	
	double getBestFitness() {
		return population_.elementAt(0).fitness_;
	}
	
	String getBestGene() {
		return population_.elementAt(0).gene_;
	}
	
	double[][] getBestCMatrix() {
		return population_.elementAt(0).cMatrix;
	}
	
	String mutateGene(String s) {
		//System.out.println("Mutating population");
		int rand_feature_ind = randomGenerator(feature_size_-1);
		int len = s.length();
		String ret=new String();
		//System.out.println("Changing "+individual.gene+" to ");
		ret = s.substring(0,rand_feature_ind);
		if(s.charAt(rand_feature_ind)=='0')
			ret+='1';
		else ret+='0';
		ret+=s.substring(rand_feature_ind+1,len);
		//System.out.println(individual.gene);
		//System.out.println("Completed mutation");
		return ret;
	}

	
	public static void main(String args[]) throws Exception {
		PrintWriter writer = new PrintWriter("dry_run-1.log","UTF-8");
		FeatureSubsetGA ga = new FeatureSubsetGA(writer,20,41);
		
		int maxIterations=150;
		for(int iter=1;iter<=maxIterations;iter++){
			System.out.println("Starting generation: "+iter);
			ga.calcFitness();
			ga.sortPopulationByFitness();
			ga.crossoverPopulation();
			//ga.mutatePopulation();
			ga.sortPopulationByFitness();
			System.out.println("Generation: "+iter+" Best Fitness = "+ga.getBestFitness());
			System.out.println("Best Gene =  "+ga.getBestGene());
			double[][] cm = ga.getBestCMatrix();
			System.out.println("cMatrix=  "+cm[0][0]+" "+cm[0][1]+" "+cm[1][0]+" "+cm[1][1]);
		}
		System.out.println("Best candidate gene: "+ ga.getBestGene());
		writer.close();
		
		
	}
}
