package com.ml.classifier;

import java.util.ArrayList;

public class Node {
	public ArrayList<Record> data;
	public Node leftChild;
	public Node rightChild;
	public Node parent;
	public int NodeID;
	public int attribute;
	public char classLabel;
	public boolean isLeftChild,isRightChild,isLeaf;
	public ArrayList<Integer> attributes_not_allowed;
	
	Node(){
		attributes_not_allowed = new ArrayList<Integer>();
		leftChild = null;
		rightChild = null;
		isLeaf = false;
		attribute = -1;
		classLabel = '2';
		data = new ArrayList<Record>();
	}

}
