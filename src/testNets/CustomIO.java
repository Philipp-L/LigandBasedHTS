package testNets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * My Custom IO class. 
 * @author Philipp Leutz
 * 
 */
public abstract class CustomIO {

	/**
	 * File read() will reference to.
	 */
	protected String file;
	/**
	 * Reader used in the process
	 */
	protected BufferedReader reader;

	public CustomIO(final String inputFile) {

		file = inputFile;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (final FileNotFoundException e) {
			System.err.println("File not found in Initialistaion");
		}
	}

	public final boolean isReady(){
		try {
			return reader.ready();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Reads a single line of the given file.
	 * 
	 * It's return value is the line as character array.
	 * 
	 * @return The Line in its character array representation.
	 * @throws IOException
	 *             File not Found
	 */
	public final char[] readLineAsCharArray() {
		try {
			if (!reader.ready()) {
				reader.close();
				return null;
			}
			return reader.readLine().toCharArray();
		} catch (final IOException e) {
			System.err.print("Error in readLineAsCharArray()");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Reads a single line of the File as String,
	 *
	 * @return single line or null, if file ended.
	 */
	public final String readLine(){
		
		try {
			if(reader.ready()){
				return reader.readLine();
			}
			else{
				System.err.print("Failed to read line! - File ended");
				return null;
			}
		} catch (IOException e) {
			System.err.print("Failed to read line!");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the referenced File as String.
	 * 
	 * @return name of the referenced file.
	 */
	public final String getFile() {
		return file;
	}

	/**
	 * Resets the reader to start on the first line.
	 */
	public final void resetReader() {
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (final FileNotFoundException e) {
			System.err.print("could not Reset");
			e.printStackTrace();
		}
	}

}