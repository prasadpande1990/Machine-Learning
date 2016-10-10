
# Text Classification

Trained classifiers to represent topics represented by different documents given in training data

## Dataset

News group data is available online at [this](http://qwone.com/~jason/20Newsgroups/) link. There are 3 different versions available for download. I used "bydate" version for this program. It has documents split up into training and testing sets.

I have calculated the result for the following five categories

1. talk.politics.misc
2. talk.religion.misc
3. sci.electronics
4. sci.space
5. rec.motorcycles
	
Sample dataset is available in ~/Text Classification/Data directory.

## Classifiers
I used following classifiers for this experiment.

1. SVM Classifier
2. Maximum Entropy Classifier
3. Bagging Ensemble Method
4. Boosting Ensemble Method
5. Random Forest Classifier
6. GLMNET Algorithm

## Language and Library
For this project, **RTextTools** R package is used. RTextTools is a Machine Learning package for automatic text classification that makes it simple.

## Conclusion

Ensemble summary in the report refers to whether n different algorithms make the same prediction concerning the class of a particular test data event.

In our summary we tested the same for different n values using RTextTools package. Summary table consists of 2 columns Coverage which tells us that percentage of documents that matches the criteria of the recall threshold. As we can see from the summary table for n >= 3 we are getting the maximum coverage that means maximum data points are over the threshold of 0.92 for n=3.

Precision tells us how much confidence we have on relevancy of our classifier result. More precision gives us more confidence. Of all the predicted values, how much of the values actually has that predicted class label gives us precision. Here for maximum entropy classifier we are getting maximum precision which is good for us.

Recall tells us the sensitivity of the classifier. Out of total true class labels, how much fraction of class labels we predicted correctly tells us the recall. Higher is the recall better is the classifier. For MaxEnt model, recall is high.
Therefore, based on the precision and the recall values we can say that for the given dataset, among the 6 classifiers we evaluated MaxEnt classifier gives us the best performance in terms of the precision and recall.

Value of F-score is based on both precision and recall. We used F-score because precision and recall are biased parameters. Precision and recall are more biased terms. With high precision and recall, MaxEnt classifier has maximum F-score.

