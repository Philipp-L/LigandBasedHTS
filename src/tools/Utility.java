package tools;

import java.util.ArrayList;
import java.util.Iterator;

import de.jannlab.data.Sample;
import de.jannlab.data.SampleSet;


/**
 * Stellt zusätzliche Funktionen berreit, um ein SampleSet zu bearbeiten
 * @author User
 *
 */
public class Utility {
	
	/**
	 * Erwartet dass die positive Klasse unterrepräsentiert ist, kopiert entsprechend die samples und hängt sie an das Sampleset an.
	 * @param initialSet
	 * @return
	 */
	public static SampleSet compensateUnderrepresentedClass (SampleSet initialSet){

		int pos = 0;
		int neg = 0;		

		for (Iterator iterator = initialSet.iterator(); iterator.hasNext();) {
			Sample sample = (Sample) iterator.next();
			if(sample.target.get(0, 0)  == 0){
				neg++;
			}
			else pos++;
		}

		int quotient = neg/pos;

		ArrayList<Sample> additionalSamples = new ArrayList<>();
		for (Iterator iterator = initialSet.iterator(); iterator.hasNext();) {
			Sample sample = (Sample) iterator.next();
			if(sample.target.get(0,0) == 1){
				for(int i = 0; i < quotient; i++){
					Sample newSample  = sample.copy();
					additionalSamples.add(newSample);					
				}
			}
		}
		
		for (int i = 0; i < additionalSamples.size(); i++) {
			initialSet.add(additionalSamples.get(i));
		}
		
		return initialSet;
	}
	
}
