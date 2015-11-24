// Have used small proportions of 'R' to filter my Data. The code is given below.
// After downloding the fils from the link in ReadMe file, use the following code and use the test and train files
// from generated with this.

// Test Files
mydata1 = read.table("/Users/Dokuru/Desktop/Informatics_HW3/ml-100k/u1.test")
mydata1[4] <- NULL
write.table(mydata1, "/Users/Dokuru/Desktop/Informatics_HW3/test/test1.txt", sep="\t",row.names=FALSE,col.names=FALSE)

mydata1 = read.table("/Users/Dokuru/Desktop/Informatics_HW3/test/test1.txt")
itemList1 = duped.data <- unique(mydata1[,2])
itemList1 = sort(itemList1)
maxTest1 = tail(itemList1, n = 1)
write(maxTest1, "/Users/Dokuru/Desktop/Informatics_HW3/test/lengths.txt", append="false")


//Train Files

mydata1 = read.table("/Users/Dokuru/Desktop/Informatics HW3/ml-100k/u1.base")
mydata1[4] <- NULL
write.table(mydata1, "/Users/Dokuru/Desktop/train/train1.txt", sep=",",row.names=FALSE,col.names=FALSE)
