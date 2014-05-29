package nets;

import java.io.PrintWriter;
import java.util.Random;

import de.jannlab.Net;
import de.jannlab.core.CellType;
import de.jannlab.data.Sample;
import de.jannlab.data.SampleSet;
import de.jannlab.generator.MLPGenerator;
import de.jannlab.learning.BasicNetLearningListener;
import de.jannlab.learning.NetLearning;
import de.jannlab.learning.Sampling;
import de.jannlab.misc.DoubleTools;
import de.jannlab.optimization.optimizer.GradientDescent;
import de.jannlab.tools.EvaluationTools;


/**
 * @author User
 *	Erstellt ein Mutlilayerperzeptron mit den angegebenen Konstanten und einer versteckten Schicht
 */
public class SigmoidBinomialMultilayerPerceptron {

	public static double[] array(double ...x) { return x; }

	Random rnd = new Random(0L);

/*
	private final static int 	HIDDEN_CELLS = 100; 
	private final static int    EPOCHS       = 200;
	private final static double LEARNING_RATE = 0.001;
	private final static double MOMENTUM     = 0.9;
	private final static double WEIGHT_DECAY = 0.00;
*/
	
	public PrintWriter writer;
	
	private SampleSet trainingSet;
	private SampleSet evaluation;
	private NetLearning learning;
	
	private int hiddenCells;
	private int epochs;
	private double learningRate;
	private double momentum;
	private double weightDecay;

	private Net mlp;
	
	public SigmoidBinomialMultilayerPerceptron(SampleSet _trainingSet, SampleSet _evaluationSet, int _hiddenCells, int _epochs, double _learningRate, double _momentum, double _weightDecay, PrintWriter _writer) {

		this.writer = _writer;
		this.trainingSet = _trainingSet;
		this.evaluation = _evaluationSet;
		
		this.epochs = _epochs;
		this.learningRate = _learningRate;
		this.momentum = _momentum;
		this.weightDecay = _weightDecay; 
		this.hiddenCells = _hiddenCells;
		
		// generate Sigmoid Binomial MLP
		//
		MLPGenerator gen = new MLPGenerator();

		gen.inputLayer(trainingSet.get(0).input.cols); 

		gen.hiddenLayer(hiddenCells, CellType.TANH);
		gen.outputLayer(1, CellType.SIGMOID_BINOMIAL, true, 1.0);

		mlp = gen.generate();
		//
		// setup optimizer.
		//
		GradientDescent optimizer = new GradientDescent();
		optimizer.setLearningRate(learningRate);
		optimizer.setRnd(rnd);
		optimizer.setParameters(mlp.getWeightsNum());
		optimizer.setMomentum(momentum);
		optimizer.setDecay(weightDecay);
		//
		// setup learning.
		//
		learning = new NetLearning();
		learning.addListener(new BasicNetLearningListener());
		learning.setRnd(rnd);
		learning.setNet(mlp);
		learning.setSampling(Sampling.STOCHASTIC);
		learning.setTrainingSamples(trainingSet);
		learning.setEpochs(epochs);
		learning.setOptimizer(optimizer);
		//
		// perform training and print final error.
	}

	public void startLearning(){
		learning.learn();
		
		double rate = EvaluationTools.evaluateClassifcation(mlp, evaluation);
		//System.out.println("Accuracy: " + DoubleTools.asString(rate * 100, 2));
		writer.write(DoubleTools.asString(rate * 100, 2) + "," + hiddenCells+ "," + epochs + "," + learningRate + "," + momentum + "," + weightDecay + "\n");
	}

	public void printToFile(){
		
	}
	public void print(){
		//
		// print computation results.
		//
		System.out.println();
		final double[] out = new double[1];
		for (Sample s : trainingSet) {
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

