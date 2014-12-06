import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;

import weka.core.Instances;

public class TestKDD {
	
	
	public static void main(String[] args) throws Exception {
		
		
		BufferedReader breader_train = null;
		breader_train = new BufferedReader(new FileReader("/Users/vaibhavsharma/datasets/KDDTrain.arff"));
		Instances train = new Instances(breader_train);
		train.setClassIndex(train.numAttributes()-1);
		breader_train.close();
		BufferedReader breader_test = null;
		breader_test = new BufferedReader(new FileReader("/Users/vaibhavsharma/datasets/KDDTest.arff"));
		Instances test = new Instances(breader_test);
		test.setClassIndex(test.numAttributes()-1);

		//NaiveBayes classifier = new NaiveBayes();
		//AdaBoostM1 classifier = new AdaBoostM1();
		Bagging classifier = new Bagging();
		String optionString = " -D -P 90 -S 1 -I 10 -W weka.classifiers.trees.J48 ";
		classifier.setOptions(weka.core.Utils.splitOptions(optionString));

		//J48 classifier = new J48();
		classifier.buildClassifier(train);
		Evaluation eval = new Evaluation(train);
		//eval.crossValidateModel(classifier,test,10,new Random(1));
		//System.out.println(eval.toSummaryString("\nResults\n",true));
		//System.out.println(eval.fMeasure(1) + " " + eval.precision(1) + " " + eval.recall(1));
		
		 Evaluation eTest = new Evaluation(train);
		 eTest.evaluateModel(classifier, test);
		 // Print the result à la Weka explorer:
		 String strSummary = eTest.toSummaryString();
		 System.out.println(strSummary);
		 
		 // Get the confusion matrix
		 double[][] cmMatrix = eTest.confusionMatrix();
		
	}
}
