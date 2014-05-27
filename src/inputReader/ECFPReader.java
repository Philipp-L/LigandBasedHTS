package inputReader;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.swing.text.StyledEditorKit.ItalicAction;

import de.jannlab.data.Sample;
import de.jannlab.data.SampleSet;
import tools.CustomIO;

/**
 * @author User
 *
 */
/**
 * @author User
 *
 */
/**
 * @author User
 *
 */
public class ECFPReader {

	static final int SPARSE_TARGET_SIZE = 200;

	int hashSpace;
	CustomIO inputReader;


	public ECFPReader(int hashSpace, String FileName) {
		inputReader = new CustomIO(FileName);
		this.hashSpace = hashSpace;
	}

	/**
	 * Extrahiert aus den ECFP's die SPARSE_TARGET_SIZE häufigten Features
	 * @return Binärvektor welcher die Anweseneheit des Features markiert.
	 */
	public SampleSet generateUnsparseSamples(){
		int[] counts = countSamples();

		int[] finalFeatures = new int[SPARSE_TARGET_SIZE];

		//Finde die SPARSE_TARGET_SIZE größten Features
		for(int i = 0; i < SPARSE_TARGET_SIZE; i++){
			int currentMax = 0;
			int currentIndex = 0;

			for(int j = 0; j < counts.length; j++){
				if(counts[j] > currentMax){
					currentIndex = j;
					currentMax = counts[j];
				}
				finalFeatures[i] = currentIndex;
				counts[currentIndex] = 0;
			}
		}
		return generateUnsparseSamples(finalFeatures);
	}
//TODO
	private SampleSet generateUnsparseSamples(int[] features){
		SampleSet sampleList = new SampleSet();

		inputReader.resetReader();
		while(inputReader.isReady()){
			double[] targets = new double[1];
			double[] inputs = new double[SPARSE_TARGET_SIZE];

			String currentLine = inputReader.readLine();
			String[] splittedLine = currentLine.split(" ");

			double target = Double.valueOf(splittedLine[0]); 
			if(target == -1 ){
				target = 0;
			}			
			targets[0] = target;
			double[] featuresOfCurrentLine = extractFeaturesFromLine(splittedLine);
			for(int i = 0; i < featuresOfCurrentLine.length; i++){
				double currentFeature = featuresOfCurrentLine[i];
				for(int j = 0; j < features.length; j++){
					if(currentFeature == features[j]){
						inputs[j]++;
					}
				}
			}
			sampleList.add(new Sample(inputs, targets));

		}
		return sampleList;
	}

	private int[] countSamples(){
		int[] counts = new int[hashSpace];

		
		while(inputReader.isReady()){
			String[] currentLine = inputReader.readLine().split(" ");
			double[] features = extractFeaturesFromLine(currentLine);
			for(int i = 1; i < features.length; i++){
				counts[(int) features[i]]++;
			}
		}
		
		return counts; 
	}

	private double[] extractFeaturesFromLine(String[] line){
		double[] features = new double[line.length-1];
		for (int i = 1; i < line.length; i++) {
			Integer currentValue = Integer.valueOf(line[i].split(":")[0]);
			features[i-1] = currentValue;
		}
		return features;
	}

	
	
/*
	public Sample[] generateSamples(){
		ArrayList<String[]>	splitedLines = new ArrayList<>();	
		Sample[] samples;

		//Split the Lines on " " 
		while(inputReader.isReady()){
			String currentLine = inputReader.readLine();
			String[] splitedLine = currentLine.split(" ");
			splitedLines.add(splitedLine);
		}

		samples = new Sample[splitedLines.size()];
		for (int i = 0; i < splitedLines.size(); i++){
			samples[i] = splittedLineToSample(splitedLines.get(i));
		}
		return samples;
	}


	private Sample splittedLineToSample(String[] splitedLine){
		double[] targets = {(Double.valueOf(splitedLine[0]))};
		double[] inputs =  new double[splitedLine.length-1];
		for (int i = 1; i < splitedLine.length; i++) {
			Integer currentValue = Integer.valueOf(splitedLine[i].split(":")[0]);
			inputs[i-1] = currentValue;
		}
		return new Sample(inputs, targets);
	}
	*/
}
