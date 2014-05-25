package main;

import java.util.ArrayList;

import testNets.SigmoidBinomialMultilayerPerceptron;
import de.jannlab.data.Sample;
import inputReader.ECFPReader;

public class LBHTSNet {
	public static void main(String[] args) {
		ECFPReader ECFPSparse = new ECFPReader(1058576, "hiva_Train_ECFP_1048576.sdf");
		ArrayList<Sample> sparseSamples =  ECFPSparse.generateUnsparseSamples();
		SigmoidBinomialMultilayerPerceptron sbmlp = new SigmoidBinomialMultilayerPerceptron(sparseSamples);
		sbmlp.startLearning();
		
		//sbmlp.print();
	}

}
