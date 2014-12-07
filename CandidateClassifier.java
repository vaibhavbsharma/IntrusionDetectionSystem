import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.core.Instances;

public class CandidateClassifier {
	Instances train, test;

	double fitness1, fitness2;
	
	CandidateClassifier(String gene, Instances _train, Instances _test) {
		System.out.println("Creating CandidateClassifier with gene = "+gene);
		System.out.println("_train.numAttributes = "+_train.numAttributes());
		System.out.println("_test.numAttributes = "+_test.numAttributes());
		train = new Instances(_train);
		test = new Instances(_test);

		
		for(int i=0;i<gene.length();i++) {
			if(gene.charAt(i) =='0') {
				if(i>=train.numAttributes() || i==train.classIndex()) {
					System.out.println("Delete attribute fails, i = "+i+" numAttributes = "+train.numAttributes());
				}
				train.deleteAttributeAt(i);
				test.deleteAttributeAt(i);
			}
		}
		
	}
	
	
	public void classify() throws Exception {
		//NaiveBayes classifier = new NaiveBayes();
		//AdaBoostM1 classifier = new AdaBoostM1();
		Bagging classifier = new Bagging();
		String optionString = " -D -P 90 -S 1 -I 10 -W weka.classifiers.trees.J48 ";
		classifier.setOptions(weka.core.Utils.splitOptions(optionString));

		//J48 classifier = new J48();
		classifier.buildClassifier(train);
		Evaluation eval_train = new Evaluation(train);
		eval_train.crossValidateModel(classifier,test,10,new Random(1));
		System.out.println(eval_train.toSummaryString("\nResults\n",true));
		System.out.println(eval_train.fMeasure(1) + " " + eval_train.precision(1) + " " + eval_train.recall(1));
		double[][] cm_train = eval_train.confusionMatrix();
		System.out.println(cm_train[0][0]+" "+cm_train[0][1]);
		System.out.println(cm_train[1][0]+" "+cm_train[1][1]);
		
		 Evaluation eTest = new Evaluation(train);
		 eTest.evaluateModel(classifier, test);
		 // Print the result à la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
		 // Get the confusion matrix
		 double[][] cmMatrix = eTest.confusionMatrix();
		 System.out.println(cmMatrix[0][0]+" "+cmMatrix[0][1]);
		 System.out.println(cmMatrix[1][0]+" "+cmMatrix[1][1]);
		 
		 fitness1=eTest.kappa();
		 
		
	}
	
	public double getFitness1() {
		return fitness1;
	}
	
	public double getFitness2() {
		return fitness2;
	}
	
	/*public static void main(String args[]) throws Exception {
		
	}*/
}
