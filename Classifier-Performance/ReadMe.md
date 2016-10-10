
# Performance Analysis of Classifiers

Implemented R program to classify data using different classifiers and compare the accuracy of models for different datasets.

## Classifiers comapred as a part of this project:
1) Decision Tree classifier

2) Perceptron classifier

3) Neural Network classifier

4) Support Vector Machine(SVM) classifier

5) Naive Bayesian classifier

## Conclusion

As per the experiment, it is difficult to decide which dataset it best for all kind of datasets. This depends on many factors important of which is the kind of data set we are using for the experiment.

If we are using data set which is linearly separable, a single unit of perceptron can do our job. It is very easily implementable if you have only binary classes.

For non-linear and complex dataset one can go with the multi-layer perceptrons i.e. Neural Networks or Support vector machines.

For categorical dataset decision tree can give you best accuracy but decision tree is susceptible to noise.

All these classifiers except Naïve Bayesian are discriminative classifiers because they learn boundary between them while Naïve Bayesian is a probabilistic classifier which finds the distribution of the datasets for individual classes. Naïve Bayesian can be used for any categorical dataset which gives us the distribution of data over the class variables.
