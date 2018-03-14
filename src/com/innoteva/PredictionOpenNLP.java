package com.innoteva;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.languagemodel.NGramLanguageModel;
import opennlp.tools.ngram.NGramGenerator;
import opennlp.tools.util.StringList;

public class PredictionOpenNLP {
	static String modelFile = "/home/samuel/Downloads/Archivos/leipzig/en-NgramLanguageModel.xml";
	static String dataFile = "/home/samuel/Downloads/Archivos/leipzig/mungedTrainSentenceModel/eng/eng.txt";
	static InputStream stream;
	
	public static void main(String[] args) throws Exception {
		int ngramSize = 3;
		String charset = "UTF-8";
	    NGramLanguageModel languageModel = new NGramLanguageModel(ngramSize);
	    
	     //stream = new Object() {}.getClass().getResourceAsStream(dataFile);
	     
	     //StringWriter writer = new StringWriter(); not working
	     //IOUtils.copy(stream, writer, "UTF-8");
	     //writer.toString()
	     
	     //IOUtils.toString(stream, StandardCharsets.UTF_8 not working
	    
	    // @SuppressWarnings("resource")
		//ByteArrayOutputStream result = new ByteArrayOutputStream();
	     //byte[] buffer = new byte[1024];
	     //int length = 0;
	    
	    File inFile = new File(dataFile);
		FileInputStream fileIn = new FileInputStream(inFile);
		InputStreamReader isReader = new InputStreamReader(fileIn,charset);
		BufferedReader bufReader = new BufferedReader(isReader);
	     
		String line;
		
	     while ((line = bufReader.readLine()) != null) {
	    	 if (line.length() == 0) continue;
	         String[] array = line.split(" ");
		      List<String> split = Arrays.asList(array);
		      List<String> generatedStrings = NGramGenerator.generate(split, ngramSize, " ");
		      for (String generatedString : generatedStrings) {
		        String[] tokens = generatedString.split(" ");
		        if (tokens.length > 0) {
		          languageModel.add(new StringList(tokens), 1, ngramSize);
		        }
		      }
	     }
	     // StandardCharsets.UTF_8.name() > JDK 7
	     
	    try{
		    OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			
		    languageModel.serialize(modelOut);
	    }	catch	(FileNotFoundException	ex)	{
				//	Handle	exception
		}	catch	(IOException	ex)	{
				//	Handle	exception
		}
	     
	    StringList tokens = languageModel.predictNextTokens(new StringList("neural",
	        "network", "language"));
	    System.out.println("Predicting the next words for the" + " neural network language, here: ");
	    
	    System.out.println(tokens.toString());
	    
	    bufReader.close();
	}
}
