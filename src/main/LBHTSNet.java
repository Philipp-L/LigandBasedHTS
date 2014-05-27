package main;

import java.util.ArrayList;

import testNets.SigmoidBinomialMultilayerPerceptron;
import de.jannlab.data.Sample;
import de.jannlab.data.SampleSet;
import inputReader.ECFPReader;
import inputReader.Utility;

public class LBHTSNet {
	public static void main(String[] args) {
		ECFPReader ECFPSparse = new ECFPReader(1058576, "hiva_Train_ECFP_1048576.sdf");
		SampleSet UnsparseSamples =  ECFPSparse.generateUnsparseSamples();
		SampleSet equalisedSamples = Utility.compensateUnderrepresentedClass(UnsparseSamples);
		
		SigmoidBinomialMultilayerPerceptron sbmlp = new SigmoidBinomialMultilayerPerceptron(equalisedSamples);
		sbmlp.startLearning();
		sbmlp.print();
	}

}
