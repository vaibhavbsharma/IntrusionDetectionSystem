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
			breader_train = new BufferedReader(new FileReader("/Users/vaibhavsharma/datasets/KDDTrain.arff"));
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
			breader_test = new BufferedReader(new FileReader("/Users/vaibhavsharma/datasets/KDDTest.arff"));
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
	        return generator_.nextInt(n+1);
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
		for(int i=0;i<population_.size();i++){
			//TODO commenting out for dry run
			//CandidateClassifier temp_classifier = 
					new CandidateClassifier(population_.elementAt(i).gene,training_set_,test_set_);
			//temp_classifier.classify();
			//population_.elementAt(i).fitness=temp_classifier.getFitness1();
			
			population_.elementAt(i).fitness=randomGenerator(1000);//TODO temporary 
		}
		System.out.println("Complete fitness calculation for population");
	}
	
	void sortPopulationByFitness() {
		Collections.sort(population_);
	}
	
	void crossoverPopulation() {
		System.out.println("Starting crossover");
		int popSize = population_.size();
		for(int i=0;i<popSize/2;i+=2) {
			Vector<Individual> tmp_vec = 
					performCrossover(population_.elementAt(i),
							population_.elementAt(i+1));
			population_.set(popSize-i-1, tmp_vec.elementAt(0));
			population_.set(popSize-i-2, tmp_vec.elementAt(1));
		}
		System.out.println("Completed crossover");
		
	}
	
	Vector<Individual> performCrossover(Individual i1, Individual i2) {
		System.out.println("Starting crossover for "+i1.gene+" and "+i2.gene);
		Vector<Individual> ret_vec = new Vector<Individual>();
		Individual child1,child2;
		child1 = new Individual();
		child2 = new Individual();
		int point1,point2;
		point1 = randomGenerator(feature_size_-1);
		point2 = randomGenerator(feature_size_-1);
		if(point1>point2) {
			int tmp=point1;
			point1=point2;
			point2=tmp;
		}
		int len1 = i1.gene.length();
		int len2 = i2.gene.length();
		if(len1 != len2 || len2 != feature_size_) {
			System.out.println("performCrossover found individuals with sizes = "+len1+" "+len2+" feature_size_ = "
					+feature_size_);
		}
		System.out.println("Using point1 = "+point1+" point2= "+point2);
		child1.gene=i1.gene.substring(0,point1) + i2.gene.substring(point1,point2+1);
		if(point2+1<=len1)
				child1.gene+= i1.gene.substring(point2+1,len1);
		child2.gene=i2.gene.substring(0,point1) + i1.gene.substring(point1,point2+1);
		if(point2+1<=len2)
			child2.gene+=i2.gene.substring(point2+1,len2);
		if(child1.gene.length()!=feature_size_ || child2.gene.length()!=feature_size_){
			System.out.println("Incorrect crossover performed "+child1.gene.length()+" "+child2.gene.length());
			System.exit(2);
		}
		
		ret_vec.add(child1);
		ret_vec.add(child2);
		System.out.println("child1 = "+child1.gene+" child2 = "+child2.gene);
		return ret_vec;
	}
	
	double getBestFitness() {
		return population_.elementAt(0).fitness;
	}
	
	void mutatePopulation() {
		System.out.println("Mutating population");
		int rand_pop_ind = randomGenerator(population_size_);
		Individual individual = population_.elementAt(rand_pop_ind);
		int rand_feature_ind = randomGenerator(feature_size_-1);
		String s;
		int len = individual.gene.length();
		System.out.println("Changing "+individual.gene+" to ");
		s = individual.gene.substring(0,rand_feature_ind);
		if(individual.gene.charAt(rand_feature_ind)=='0')
			s+='1';
		else s+='0';
		s+=individual.gene.substring(rand_feature_ind+1,len);
		individual.gene = s;
		population_.setElementAt(individual,rand_pop_ind);
		System.out.println(individual.gene);
	}
	
	public static void main(String args[]) throws Exception {
		PrintWriter writer = new PrintWriter("dry_run-1.log","UTF-8");
		FeatureSubsetGA ga = new FeatureSubsetGA(writer,100,6);
		
		int maxIterations=1000;
		for(int iter=1;iter<=maxIterations;iter++){
			writer.println("Starting generation: "+iter);
			ga.calcFitness();
			ga.sortPopulationByFitness();
			ga.crossoverPopulation();
			ga.mutatePopulation();
			writer.println("Generation: "+iter+" Best Fitness = "+ga.getBestFitness());
		}
		writer.close();
		
		
	}
}
