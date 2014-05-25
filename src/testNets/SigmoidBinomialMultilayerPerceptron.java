package testNets;

import java.util.ArrayList;
import java.util.Random;

import de.jannlab.Net;
import de.jannlab.core.CellType;
import de.jannlab.data.Sample;
import de.jannlab.data.SampleSet;
import de.jannlab.data.SampleTools;
import de.jannlab.generator.MLPGenerator;
import de.jannlab.learning.BasicNetLearningListener;
import de.jannlab.learning.NetLearning;
import de.jannlab.learning.NetLearningListener;
import de.jannlab.learning.Sampling;
import de.jannlab.misc.DoubleTools;
import de.jannlab.optimization.optimizer.GradientDescent;
import de.jannlab.tools.ClassificationValidator;
import de.jannlab.tools.EvaluationTools;


/**
 * @author User
 *	Erstellt ein Mutlilayerperzeptron mit den angegebenen Konstanten und einer versteckten Schicht
 */
public class SigmoidBinomialMultilayerPerceptron {

	public static double[] array(double ...x) { return x; }

	Random rnd = new Random(0L);

	private final static int 	HIDDEN_CELLS = 50; 
	private final static int    EPOCHS       = 1000;
	private final static double LEARNING_RATE = 0.001;
	private final static double MOMENTUM     = 0.9;
	private final static double WEIGHT_DECAY = 0.00;

	SampleSet set;
	Net mlp;
	NetLearning learning;

	public SigmoidBinomialMultilayerPerceptron(ArrayList<Sample> samples) {

		//generate a SampleSet
		set = new SampleSet(); 
		
		for (Sample currentSample : samples) {
			set.add(currentSample);
		}
		/*
		int[] idx = new int[set.get(0).input.data.length];

		for (int i = 0; i < idx.length; i++) {
			idx[i] = i;
		}

		SampleTools.normalize(set, idx);
		 */


		// generate Sigmoid Binomial MLP
		//
		MLPGenerator gen = new MLPGenerator();

		gen.inputLayer(set.get(0).input.cols); 
		//gen.hiddenLayer(hiddenCells, CellType.TANH);
		//gen.outputLayer(1, CellType.TANH);

		gen.hiddenLayer(HIDDEN_CELLS, CellType.TANH);
		gen.outputLayer(1, CellType.SIGMOID_BINOMIAL, true, 1.0);

		mlp = gen.generate();
		//
		// setup optimizer.
		//
		GradientDescent optimizer = new GradientDescent();
		optimizer.setLearningRate(LEARNING_RATE);
		optimizer.setRnd(rnd);
		optimizer.setParameters(mlp.getWeightsNum());
		optimizer.setMomentum(MOMENTUM);
		optimizer.setDecay(WEIGHT_DECAY);
		//
		// setup learning.
		//
		learning = new NetLearning();
		learning.addListener(new BasicNetLearningListener());
		learning.setRnd(rnd);
		learning.setNet(mlp);
		learning.setSampling(Sampling.STOCHASTIC);
		learning.setTrainingSamples(set);
		learning.setEpochs(EPOCHS);
		learning.setOptimizer(optimizer);
		//
		// perform training and print final error.
	}

	public void startLearning(){
		learning.learn();

		double rate = EvaluationTools.evaluateClassifcation(mlp, set);
		System.out.println("Accuracy: " + DoubleTools.asString(rate * 100, 2));
	}

	public void print(){
		//
		// print computation results.
		//
		System.out.println();
		final double[] out = new double[1];
		for (Sample s : set) {
			mlp.reset();
			mlp.input(s.input.data, 0);
			mlp.compute();
			mlp.output(out,  0);
			System.out.println(
					DoubleTools.asString(s.input.data, 1) +
					" ==> " + 
					DoubleTools.asString(out, 1)
					);
		}
	}


}

