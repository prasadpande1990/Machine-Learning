#Execute only once
install.packages('caret',dependencies=TRUE)
install.packages('rpart',dependencies=TRUE)
install.packages('e1071',dependencies=TRUE)
install.packages('neuralnet',dependencies=TRUE)
install.packages('monmlp',dependencies=TRUE)
install.packages('nnet',dependencies=TRUE)

# rpart for decsion tree learning
library(rpart)
library(caret)
library(e1071)
library(neuralnet)
library(nnet)
library(MASS)
library(monmlp)
# Load Data sets
data_sets = c("house-votes-84.data","car.data","SPECT.data","wine.data","heart.data")

train_factor = c(0.8,0.9)

output <- matrix(nrow = 10, ncol = 7)
i = 1
file_count <- 1
while(file_count <= length(data_sets))
{
    data1 <- read.csv(data_sets[file_count],header = TRUE,sep = ",")
    data1$class <- factor(data1$class)
    attach(data1)
    j = 1
    while(j<3)
    {
      output[i][1] <- paste(data_sets[file_count],j)
      # Considering the 80% of data as training data
      smp_size <- floor(train_factor[j] * nrow(data1))
      train_ind <- sample(seq_len(nrow(data1)), size = smp_size)
      train_data <- data1[train_ind, ]
      test_data <- data1[-train_ind, ]
    
      output[i,2] <- train_factor[j]
        
      # Decision Tree
      dc_fit <- rpart(class ~. ,data = train_data)
      pred <- predict(dc_fit,test_data, type="class")
      table(pred, test_data$class)
      accuracy = mean(pred == test_data$class)*100
      output[i,3] <- paste(round(accuracy,digits = 2),"%")
    
      # Perceptron    
#      pt_fit <- neuralnet(class ~., data = train_data,hidden = 0,threshold = 0.45)
      pt_fit <- nnet(class~.,data = train_data,hidden=0,linear.output=TRUE,size = 1)
      pred_pt <- predict(pt_fit,test_data,type="class")
      table(pred_pt, test_data$class)
      accuracy = mean(pred_pt == test_data$class)*100
      output[i,4] <- paste(round(accuracy,digits = 2),"%")
      
      # Neural Networks
      nn_fit <- nnet(class ~.,data =train_data, size=3,maxit=10000,decay=.001,trace=FALSE)
      pred_nn = predict(nn_fit,test_data, type = "class")
      table(pred_nn, test_data$class)
      accuracy = mean(pred_nn == test_data$class)*100
      output[i,5] <- paste(round(accuracy,digits = 2),"%")
  
      # Support Vector Machines
      svm_fit = svm(class ~.,data = train_data)
      pred_svm = predict(svm_fit,test_data, type = "class")
      table(pred_svm, test_data$class)
      accuracy = mean(pred_svm == test_data$class)*100
      output[i,6] <- paste(round(accuracy,digits = 2),"%")
  
      # Naive Bayesian Classifier
      nb_fit = naiveBayes(class ~.,data = train_data)
      pred_nb = predict(nb_fit,test_data,type=c("class"))
      table(pred_nb, test_data$class)
      accuracy = mean(pred_nb == test_data$class)*100
      output[i,7] <- paste(round(accuracy,digits = 2),"%")
      j = j + 1
      i = i + 1
    }
    file_count = file_count + 1
    search()
    detach(data1)
    #detach(data1)
}
output
