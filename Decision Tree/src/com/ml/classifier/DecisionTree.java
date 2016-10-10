package com.ml.classifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class DecisionTree {
	public static final int num_of_attributes=19;
	public static Node temp = null, temp_random = null;
	public static int nodeCount = 1;
	public static int nodeCount_random = 1;
	
	public static void main(String[] args) throws IOException {
		
		Record rec = new Record();
		if(args.length!=5){
			System.out.println("Enter the appropriate number of parameters as follows");
			System.out.println("java com.ml.classifier.DecisionTree <NoOfNodesToPrune> <TrainingDataFile> <ValidataionDataFile> <TestDataFile> <DisplayOrNot>");
			System.exit(0);
		}
		
		String train_data = args[1];
		String test_data = args[3];
		String validation_data = args[2];
		int no_of_nodes_to_prune = Integer.parseInt(args[0]);
		boolean isDisplay = Boolean.parseBoolean(args[4]);
		
		//Reading data sets
		ArrayList<Record> training_data = rec.populateData(train_data);
		ArrayList<Record> testing_data = rec.populateData(test_data);
		ArrayList<Record> validation_data_1 = rec.populateData(validation_data);
		
		//Apply ID3 Algorithm
		System.out.println("Using ID3 Algorithm");
		constuctDecisionTreeByID3(training_data,testing_data,validation_data_1,no_of_nodes_to_prune,isDisplay);
		System.out.println("------------------------------------------------------------------------------");
		//Apply Random attribute selection algorithm
		System.out.println("Using Random Attribute Selection Algorithm");
		constructDecisionTreeByRandom(training_data,testing_data,validation_data_1,no_of_nodes_to_prune,isDisplay);
		
	}
	
	/*
	 * Function Name: constructDecisionTreeByRandom()
	 * Description: Function to construct a decision Tree using random attribute selection algorithm
	 * Input: 
	 * training_data - Training data set
	 * testing_data - Testing data set
	 * Validation_data_1 - Validation data set
	 * no_of_nodes_to_prune - No of nodes selected for pruning
	 * Output: NONE
	 * */
	public static void constructDecisionTreeByRandom(ArrayList<Record> training_data,ArrayList<Record> testing_data,ArrayList<Record> validation_data_1,int no_of_nodes_to_prune, boolean isDisplay){		

		Queue<Node> queue = new LinkedList<Node>();
		Node root=null;
		queue.offer(root);
		int attribute_selected=0;
		int min = 0, max = 18;
		Random rand = new Random();
		while(!queue.isEmpty()){
			root = queue.poll();
			if(root==null){
				root = new Node();
				root.data = training_data;
				root.NodeID = nodeCount_random++;	
				root.classLabel = getClassLabel(root.data);
				temp_random = root;				
				
			}
			attribute_selected = rand.nextInt((max - min) + 1) + min;
			root.attribute = attribute_selected;
			root.classLabel = getClassLabel(root.data);
			root.attributes_not_allowed.add(attribute_selected);
			//System.out.println("Attribute Select"+ root.attribute);
			if(attribute_selected!=-1){
				//System.out.println("Node Id= "+ root.NodeID);
				root.leftChild = getclassifiedData(root,"0");
				//System.out.println(root.leftChild.data.size());
				root.leftChild.NodeID = nodeCount_random++;
				root.leftChild.attributes_not_allowed.addAll(0, root.attributes_not_allowed);
				if(calcClassEntropy(root.leftChild.data)!=0){
					queue.offer(root.leftChild);
				} else {
					root.leftChild.isLeaf = true;
					root.leftChild.classLabel = getClassLabel(root.leftChild.data);
				}
				
				root.rightChild = getclassifiedData(root, "1");
				//System.out.println(root.rightChild.data.size());
				root.rightChild.NodeID = nodeCount_random++;
				root.rightChild.attributes_not_allowed.addAll(0, root.attributes_not_allowed);
				if(calcClassEntropy(root.rightChild.data)!=0) {
					queue.offer(root.rightChild);				
				} else {
					root.rightChild.isLeaf = true;
					root.rightChild.classLabel = getClassLabel(root.rightChild.data);
					//System.out.println("Node Id = "+root.rightChild.NodeID);
				}
			} else {
				root.isLeaf = true;
				root.NodeID = nodeCount_random++;
				root.classLabel = getClassLabel(root.data);
				//System.out.println("Node Id = "+root.NodeID);
			}
		}
		if(isDisplay)
			displayTree(temp_random);
		
		System.out.println("Accuracy on testing data = "+calcAccuracy(temp_random,testing_data));
		int sumOfDepth = sumOfDepths(temp_random,0);
		System.out.println("Number of nodes = "+(nodeCount_random-1));
		System.out.println("Average Depth = "+(double)sumOfDepth/(nodeCount_random-1));
		pruneTree(temp_random,testing_data,validation_data_1,no_of_nodes_to_prune,(nodeCount_random-1));		

		
	}
	
	/*
	 * Function Name: constuctDecisionTreeByID3()
	 * Description: Function to construct a decision Tree using ID3 algorithm
	 * Input: 
	 * training_data - Training data set
	 * testing_data - Testing data set
	 * Validation_data_1 - Validation data set
	 * no_of_nodes_to_prune - No of nodes selected for pruning
	 * Returns : NONE
	 * */

	public static void constuctDecisionTreeByID3(ArrayList<Record> training_data,ArrayList<Record> testing_data,ArrayList<Record> validation_data_1,int no_of_nodes_to_prune, boolean isDisplay) throws IOException{
		
		Queue<Node> queue = new LinkedList<Node>();
		Node root=null;
		queue.offer(root);
		int attribute_selected=0;
		while(!queue.isEmpty()){
			root = queue.poll();
			if(root==null){
				root = new Node();
				root.data = training_data;
				root.NodeID = nodeCount++;	
				root.classLabel = getClassLabel(root.data);
				temp = root;				
				
			}
			attribute_selected = calcInformationGain(root.data,root.attributes_not_allowed);
			root.attribute = attribute_selected;
			root.classLabel = getClassLabel(root.data);
			root.attributes_not_allowed.add(attribute_selected);
			//System.out.println("Attribute Select"+ root.attribute);
			if(attribute_selected!=-1){
				//System.out.println("Node Id= "+ root.NodeID);
				root.leftChild = getclassifiedData(root,"0");
				//System.out.println(root.leftChild.data.size());
				root.leftChild.NodeID = nodeCount++;
				root.leftChild.attributes_not_allowed.addAll(0, root.attributes_not_allowed);
				if(calcClassEntropy(root.leftChild.data)!=0){
					queue.offer(root.leftChild);
				} else {
					root.leftChild.isLeaf = true;
					root.leftChild.classLabel = getClassLabel(root.leftChild.data);
				}
				
				root.rightChild = getclassifiedData(root, "1");
				//System.out.println(root.rightChild.data.size());
				root.rightChild.NodeID = nodeCount++;
				root.rightChild.attributes_not_allowed.addAll(0, root.attributes_not_allowed);
				if(calcClassEntropy(root.rightChild.data)!=0) {
					queue.offer(root.rightChild);				
				} else {
					root.rightChild.isLeaf = true;
					root.rightChild.classLabel = getClassLabel(root.rightChild.data);
					//System.out.println("Node Id = "+root.rightChild.NodeID);
				}
			} else {
				root.isLeaf = true;
				root.NodeID = nodeCount++;
				root.classLabel = getClassLabel(root.data);
				//System.out.println("Node Id = "+root.NodeID);
			}
		}
		if(isDisplay)
			displayTree(temp);
		
		System.out.println("Accuracy on testing data = "+calcAccuracy(temp,testing_data));
		int sumOfDepth = sumOfDepths(temp,0);
		System.out.println("Number of nodes = "+(nodeCount-1));
		System.out.println("Average Depth = "+(double)sumOfDepth/(nodeCount-1));
		pruneTree(temp,testing_data,validation_data_1,no_of_nodes_to_prune,(nodeCount-1));		
	}
	
	/*
	 * Function Name: pruneTree()
	 * Description: Function to prune the tree constructed by either of the algorithm
	 * Input: 
	 * Root - Root node of the tree
	 * testing_data - Testing data set
	 * Validation_data_1 - Validation data set
	 * no_of_nodes_to_prune - No of nodes selected for pruning
	 * countNode - No of nodes in the tree
	 * Output: NONE 
	 */
	public static void pruneTree(Node root,ArrayList<Record> testing_data,ArrayList<Record> validation_data_1,int no_of_nodes_to_prune,int node_Count){
		ArrayList<Integer> nodesToPrune = new ArrayList<Integer>();
		Random rand = new Random();
		int min = 1;
		int max = node_Count;
		int count = 0;
		while(count < no_of_nodes_to_prune){
			int randomNum = rand.nextInt((max - min) + 1) + min;
			if(!nodesToPrune.contains(randomNum)){
				Node temp = searchNodeById(root,randomNum);
				if(temp.leftChild!= null && temp.rightChild!= null){
					if(temp.leftChild!=null)
						temp.leftChild = null;
					if(temp.rightChild!=null)
						temp.rightChild=null;
					temp.classLabel = getClassLabel(temp.data);
					temp.isLeaf = true;
					System.out.println(" "+temp.NodeID);
					nodesToPrune.add(randomNum);
					count++;
				}
			}
		} 
		System.out.println("Accuracy after pruning on validation data = "+calcAccuracy(root,validation_data_1));
		System.out.println("Accuracy after pruning on testing data = "+calcAccuracy(root,testing_data));
	}	
	
	/*
	 * Function Name: searchNodeById()
	 * Description: Given a Root of a tree and a node id returns a node with given node id
	 * Input: 
	 * Root - Root node of the tree
	 * Node Id - Node Id to be searched
	 * Output: Returns Node with given Node Id
	 */
	public static Node searchNodeById(Node root,int nodeId){
		Node temp = null;
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(root);
		while(!queue.isEmpty()) {
			temp = queue.poll();
			if(temp.NodeID==nodeId){
				break;
			} else{
				if(temp.leftChild!= null)
					queue.offer(temp.leftChild);
				if(temp.rightChild!=null)
					queue.offer(temp.rightChild);
			}
		}
		return temp;
	}
	
	/*
	 * Function Name: calcAccuracy()
	 * Description: Function to calculate the accuracy of a given decision tree using test data
	 * Input: 
	 * Root - Root node of the tree
	 * test_Data - Test data set
	 * Output: Returns accuracy of a given decision tree model
	 */	
	public static double calcAccuracy(Node root, ArrayList<Record> test_data){
		
		double accuracy = 0.0;
		Node temp = null;
		Iterator<Record> iter = test_data.iterator();
		int count = 0;
		while(iter.hasNext()){
			Record record = iter.next();
			temp = root;
			while(!temp.isLeaf){
				if(record.attributes[temp.attribute].equals("0"))
					temp = temp.leftChild;
				else
					temp = temp.rightChild;
			}		
			if(String.valueOf(temp.classLabel).equals(record.Class)){
				count++;
			} 
		}
		System.out.println("Test data size = "+ test_data.size());
		accuracy = (double)(count*100)/test_data.size();		
		return accuracy;
	}
	
	/*
	 * Function Name: sumOfDepths()
	 * Description: Function to calculate sum of deapth of leaf nodes
	 * Input: 
	 * Root - Root node of the tree
	 * depth - initial depth of a node (i.e. 0)
	 * Output: Returns sum of depths of leaf nodes 
	 */	
	static int sumOfDepths(Node root, int depth ) {
	    if ( root == null ) {
	       return 0;
	    }
	    else if ( root.leftChild == null && root.rightChild == null) {
	       return depth;
	    }
	    else {
	       return sumOfDepths(root.leftChild, depth + 1) 
	                   + sumOfDepths(root.rightChild, depth + 1);
	    }
	}	
	
	/*
	 * Function Name: displayTree()
	 * Description: Function to Display the tree
	 * Input: 
	 * Root - Root node of the tree
	 * Output: Displays the tree with parent and its child recursively 
	 */	
	public static void displayTree(Node root){
		Queue<Node> queue =  new LinkedList<Node>();
		queue.offer(root);
		while(!queue.isEmpty()){
			Node temp1 = queue.poll();
			System.out.println(getAttributeLabel(temp1.attribute)+":"+ temp1.classLabel);
			if(temp1.leftChild!=null)
				System.out.print(getAttributeLabel(temp1.leftChild.attribute)+":"+temp1.classLabel);
			if(temp1.rightChild!=null)
				System.out.println(" "+getAttributeLabel(temp1.rightChild.attribute)+":"+temp1.classLabel);
			if(temp1.leftChild!=null && !temp1.leftChild.isLeaf && temp1.leftChild.attribute!=-1){
				queue.offer(temp1.leftChild);
			}
			if(temp1.rightChild!=null && !temp1.rightChild.isLeaf && temp1.rightChild.attribute!=-1){
				queue.offer(temp1.rightChild);
			}
		}
	}
	
	/*
	 * Function Name: getAttributeLabel()
	 * Description: Function to return a attribute label given a attribute number
	 * Input: 
	 * attributeNo - Integer value for the given attribute number
	 * Output: Returns the corresponding string representing the attribute 
	 */	
	public static String getAttributeLabel(int attributeNo){
		
		switch (attributeNo) {
		case 0:
			return "XB";
		case 1:
			return "XC"; 
		case 2:
			return "XD"; 
		case 3:
			return "XE"; 
		case 4:
			return "XF"; 
		case 5:
			return "XG"; 
		case 6:
			return "XH"; 
		case 7:
			return "XI"; 
		case 8:
			return "XJ"; 
		case 9:
			return "XK"; 
		case 10:
			return "XL"; 
		case 11:
			return "XM"; 
		case 12:
			return "XN"; 
		case 13:
			return "XO"; 
		case 14:
			return "XP"; 
		case 15:
			return "XQ"; 
		case 16:
			return "XR"; 
		case 17:
			return "XS"; 
		case 18:
			return "XT"; 
		case 19:
			return "XU"; 
		default:
			break;
		}
		return "-1";
	}
	
	/*
	 * Function Name: getClassLabel()
	 * Description: Function to determine the class label for given data
	 * Input: 
	 * data - data set
	 * Output: Returns the class label for a given data set 
	 */		
	public static char getClassLabel(ArrayList<Record> data){
		char label = '2';
		int label_0=0, label_1=0;
		Iterator<Record> iter = data.iterator();
		while(iter.hasNext()){
			Record record= (Record)iter.next();
			if(record.Class.equals("1"))
				label_1++;
			else
				label_0++;
		}
		
		return (label_1 > label_0)?'1':'0';
	}
	
	/*
	 * Function Name: getclassifiedData()
	 * Description: Function to Create a node based on the attribute selected in the parent node
	 * Input: 
	 * Root - Root node of the tree
	 * attribute_value - Value (i.e. 0 or 1) for which we need to classify or sagregate the data
	 * Output: REturns a child node of the given root node with sagregated data for given attribute_value
	 */	
	public static Node getclassifiedData(Node root,String attribute_value) {
		// TODO Auto-generated method stub
		Node temp2 = new Node(); 
		Iterator<Record> iter = root.data.iterator();
		while(iter.hasNext()){
			Record record = (Record)iter.next();
			if(record.attributes[root.attribute].equals(attribute_value)){
				temp2.data.add(record);
			}
		}
		return temp2;
	}

	/*
	 * Function Name: calcClassEntropy()
	 * Description: Function to calculate the entropy of the class variable for the given data set 
	 * Input: 
	 * data - Data for which entropy needs to be calculated
	 * Output: Returns the class Entropy 
	 */	
	public static double calcClassEntropy(ArrayList<Record> data){
	
		double entropy = 0.0;
		int pos_count = 0,neg_count=0;
		int data_size = data.size();
		double constant = Math.log10(2);
		double term1 = 0, term2 = 0;
		
		Iterator<Record> iter = data.iterator();
		if(data_size==0)
			return 0;
		else{
			while(iter.hasNext()){
				String value = iter.next().Class;
			    if(value.equals("1")){
			    	pos_count++;
			    } else{
			    	neg_count++;
			    }
	        }
			double class_1_probability = (double)pos_count/data_size;
			double class_0_probability = (double)neg_count/data_size;
			if(class_1_probability==class_0_probability)
				entropy = 1;
			else if(class_1_probability==1 || class_0_probability==1)
				entropy = 0;
			else {
				if(class_1_probability==0)
					term1 = 0;
				else 
					term1 = Math.log10(class_1_probability)/constant;
				if(class_0_probability==0)
					term2 = 0;
				else
					term2 = Math.log10(class_0_probability)/constant;
						
				entropy = - ((class_1_probability*term1) + (class_0_probability*term2));
			}
		}
		return entropy;
	}
	
	/*
	 * Function Name: calcInformationGain()
	 * Description: Function to calculate the information gain for given dataset
	 * Input: 
	 * data - Given data for the node
	 * attributes_fixed - Arraylist that contains the attributes that are not allowed to be selecte
	 * since they are already chosen by the ancestors
	 * Output: Return the attribute index with maximum information gain for the given data set 
	 */	
	public static int calcInformationGain(ArrayList<Record> data,ArrayList<Integer> attributes_fixed)
	{
		double classEntropy = calcClassEntropy(data);
		
		double num_pos=0;
		double num_neg=0;
		double num_pos_0=0;
		double num_neg_0=0;
		double num_pos_1=0;
		double num_neg_1=0;
		double gain=0,maxGain=0;
		double constant = Math.log10(2);
		int maxGainIndex=-1;
		int data_size = data.size();
		
		if(classEntropy==0)
			return -1;
		
		for(int j=0;j<num_of_attributes;j++)
		{
			for(int i=0;i<data.size();i++)
			{
				Record record= data.get(i);
				if(record.attributes[j].matches("1"))
				{
					num_pos++;
					if(record.Class.matches("1"))
						num_pos_1++;
					else
						num_neg_1++;
				}else{
					num_neg++;
					if(record.Class.matches("1"))
						num_pos_0++;
					else
						num_neg_0++;
				}
			}
			
			double prob_pos_0 = (double)num_pos_0/num_neg;
			double prob_pos_1 = (double)num_pos_1/num_pos;
			double prob_neg_0 = (double)num_neg_0/num_neg;
			double prob_neg_1 = (double)num_neg_1/num_pos;
			double term1 = 0, term2 = 0;
			if(prob_pos_1==0)
				term1 = 0;
			else
				term1 = Math.log10(prob_pos_1)/constant;
			
			if(prob_neg_1==0)
				term2 = 0;
			else 
				term2 = Math.log10(prob_neg_1)/constant;
			
			double entropy_1 = -((prob_pos_1*term1) + (prob_neg_1*term2));
			
			if(prob_pos_0==0)
				term1 = 0;
			else
				term1 = Math.log10(prob_pos_0)/constant;
			
			if(prob_neg_0==0)
				term2 = 0;
			else
				term2 = Math.log10(prob_neg_0)/constant;
			
			double entropy_0 = -((prob_pos_0*term1) + (prob_neg_0*term2));
			
			gain = classEntropy - (((double)num_pos/(double)data_size)*entropy_1) - (((double)num_neg/(double)data_size)*entropy_0);
				if(j==0 && !Double.isNaN(gain) && !attributes_fixed.contains(j))
				{
					maxGain = gain;
					maxGainIndex = j;
				}
				else
				{
					if(gain>maxGain && !Double.isNaN(gain) && !attributes_fixed.contains(j))
					{
						maxGain=gain;
						maxGainIndex=j;
					}
				}
				gain=0;
				num_pos_0=0;
				num_pos_1=0;
				num_neg_0=0;
				num_neg_1=0;
				num_pos=0;
				num_neg=0;		
		}
		return maxGainIndex;
		
	}
}
