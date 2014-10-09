IntrusionDetectionSystem
========================

<b>Evolutionary Computation class project for Michigan State University</b>

Purpose<br>
The purpose of this project is - given a data set, use a Genetic Algorithm to identify a good subset of features which can be used by an Ensemble classifier to classify network traffic as good or bad. 

Data Set<br>
The data set I plan to use for this project comes from the 1999 KDD intrusion detection contest[1]. The dataset consists of 9 weeks of raw TCP dump data of raw data for a local area network simulating a U.S. Air Force LAN. The raw training data consists of compressed binary TCP dump data from seven weeks of network traffic, processed into about five million records. A connection is a sequence of TCP packets starting and ending at some well defined times between which data flows to and from a source IP address to a destination IP address under some well defined protocol. Each connection is labeled as either normal, or as a particular type of attack. 
The attacks themselves fall into 4 main categories: DOS(denial of service), R2L(unauthorized remote access), U2R(unauthorized local superuser access), probing. 
The data set also consists of test data which is derived from a different probability distribution than the training data including certain attack types not included in the training data. 24 training attack types are given while 14 attack types are present only in the test data. 

Ensemble classifier<br>
Ensemble Classifiers do not learn a single classifier but learn a set of classifiers. They combine the predictions of multiple classifiers. This helps in reducing the dependence on the peculiarities of a single training set and reduces bias introduced by a single classifier. Different types of Ensemble methods are commonly used which involve manipulating the data distribution, manipulating the input features(something which the GA will be responsible for in this project), manipulating the class labels or introducing randomness into the learning algorithm. Bagging and boosting are commonly used to modify the data distribution. The base classifiers have to satisfy the criteria that the classification errors made by the classifiers have to be as uncorrelated as possible.  

Evolutionary Algorithm<br>
The purpose of the genetic algorithm will be to select a subset of the features to be used by the Ensemble classifier, train and test the Ensemble classifier and calculate its fitness. The search component would be a GA and the evaluation component will be a Ensemble classifier. The initial population would be randomly generated, each individual would have a subset of the 41 features present in the training data set. Each individual would be evaluated using a Ensemble classifier. Once the top individuals from a generation have been found, crossover from the parents would create the offspring and some mutation would be performed on the child to maintain some diversity in the population. The parameters of - initial population size, method of crossover, mutation, selection criteria - among other parameters are something I am hoping to also be able to investigate through the duration of this project. 

Library to be used<br>
The Weka library[2] is a collection of implementation for various data mining algorithms including classification algorithms. I plan to use this library to implement the Ensemble classifier.

References<br>
<ol>
<li>http://kdd.ics.uci.edu/databases/kddcup99/kddcup99.html</li>
<li>http://www.cs.waikato.ac.nz/ml/weka/ </li>
</ol>

