package com.ml.classifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Record {
	public String Class;
	public String[] attributes;
	public int RecotdId;
	
	/*
	 * Function Name: populateData()
	 * Description: Function to populate the data from input files into the ArrayList
	 * Input: 
	 * train_data - String consisting of the path to an input file
	 * Output: Returns an ArrayList with complete dataset
	 * */
	public ArrayList<Record> populateData(String train_data) throws IOException{
		String line = "";
		int count = 0;
		ArrayList<Record> data = new ArrayList<Record>();
		BufferedReader br = new BufferedReader(new FileReader(train_data));
		while ((line = br.readLine()) != null) {
			 Record record = new Record();
			 record.attributes = new String[line.split(",").length-1];
			 for(int i=0;i<line.split(",").length-1;i++){
				 record.attributes[i]=line.split(",")[i];
			 }
			 record.Class= line.split(",")[20];
			 //System.out.println(record.Class);
			 record.RecotdId = count++;
			 data.add(record);
		}		
		br.close();
		return data;
	}

}
