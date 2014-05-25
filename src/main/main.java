package main;

import testNets.sigmoidBinomialMultilayerPerceptron;
import de.jannlab.data.Sample;
import inputReader.ECFPReader;

public class main {
	public static void main(String[] args) {
		ECFPReader ECFPSparse = new ECFPReader(1058576, "hiva_Train_ECFP_1048576.sdf");
		Sample[] sparseSamples =  ECFPSparse.generateUnsparseSamples();
		sigmoidBinomialMultilayerPerceptron sbmlp = new sigmoidBinomialMultilayerPerceptron(sparseSamples);
		sbmlp.startLearning();
		sbmlp.print();
	}

}
