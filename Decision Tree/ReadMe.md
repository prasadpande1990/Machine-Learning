
# Decision Tree Implementation

Implemented Decision Tree using ID3 algorithm in Java

## Building a Decision Tree model

Implemented the ID3 decision tree algorithm presented in . 
Used the training dataset to learn the tree, validation dataset to validate and prune the tree, and finally the test dataset to test the tree. 

The terminating condition is one of the following of the following:

• No more attributes to split on. In this case, you should find the majority class, and label it as that class. 
•The node is pure i.e. it has only one class. In this case, it is easy to infer the class of instances.

## Pruning your model

After constructing model i.e. decision tree, pruning is implemented to check if tree is overfitted to training data.

Implemented by randomly deleting the subtree below a node including all the child nodes of that node i.e. the sub-tree starting at that node, and not that node. 

In that case, that node becomes a leaf node. The classification at that node will be done by taking the majority vote e.g. if that node contains 10 instances of class 1 and 5 instances of class 2, then it will classify instances as


## Testing Model Accuracy

Accuracy of the model on test dataset is calculated by implementing following formula:

                 Accuracy=  (Number of instances correctly classified)/(Total number of instances)




