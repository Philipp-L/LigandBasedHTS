package main;

import inputReader.ECFPReader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import nets.SigmoidBinomialMultilayerPerceptron;
import tools.Utility;
import de.jannlab.data.SampleSet;

public class LBHTSNet {
	private static final int HASH_SPACE = 1058576;
	private final static int    EPOCHS       = 500;

/*	
	private final static int 	HIDDEN_CELLS = 100; 
	private final static double LEARNING_RATE = 0.001;
	private final static double MOMENTUM     = 0.9;
	private final static double WEIGHT_DECAY = 0.00;
	*/
	private static PrintWriter writer;
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		writer  = new PrintWriter("results\\290514_3.txt", "UTF-8");
		writer.write("All with" +"EPOCHS"+ "Epochs \n Accuracy" + "," + "HiddenCells" + "," + "Epochs" + "," + "LearningRate" + "," + "Momentum" + "," + "WeightDecay" + "\n");
		ECFPReader ECFPSparseTrain = new ECFPReader(HASH_SPACE, "hiva_Train_ECFP_1048576.sdf");
		SampleSet UnsparseSamples =  ECFPSparseTrain.generateUnsparseSamples();
		SampleSet equalisedSamples = Utility.compensateUnderrepresentedClass(UnsparseSamples);
		
		ECFPReader ECFPSparseEvaluate = new ECFPReader(HASH_SPACE, "hiva_Valid_ECFP_1048576.sdf");
		SampleSet evluationSet = Utility.compensateUnderrepresentedClass(ECFPSparseEvaluate.generateUnsparseSamples());
		
		/*
		 * Setup a Batch of experiments
		 */
		
		int[] hiddenSizes = {100};
		double[] learningRates = {0.00005};
		double[] momentums = {0.9,0.1, 0.001};
		double[] weightDecays = {0};
		
		for(int currenthiddens : hiddenSizes){
			for(double currentLearning : learningRates){
				for(double currentMomentum : momentums){
					for(double currentDecay : weightDecays){
						SigmoidBinomialMultilayerPerceptron sbmlp = new SigmoidBinomialMultilayerPerceptron(equalisedSamples, evluationSet, currenthiddens, EPOCHS, currentLearning, currentMomentum, currentDecay, writer);
						sbmlp.startLearning();
					}
				}
			}
		}
		writer.close();
	}
}
