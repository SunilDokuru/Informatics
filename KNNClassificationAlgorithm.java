// Implementing KNN algorithm using weighted score and distance-weighted score model.
package assignment1;

import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.*;

public class KNNAlgorithm {
   static DecimalFormat df = new DecimalFormat("#.########");
   static String weightedResult = "\t\t\t\t[TestDataIndex,\t\tClass]\n";
   public static void main(String[] args) throws FileNotFoundException {

		Random rand = new Random();
		Scanner sc = null;
		boolean condition = false;
      
      
		// Loading the file into the system
		File iterator = new File("glass.txt");
		int size = 0, cols = 0, tracker = 0;
		try {
			sc = new Scanner(iterator);
         
         // Counting records in the file
	      while(sc.hasNextLine()) {
	    	  
			   String in = sc.nextLine();
            if(tracker == 0) {
               tracker = Integer.MAX_VALUE;
               cols = findCols(in);
            }
		      size++;
		   }
         
 
		} catch(FileNotFoundException ex) {
			System.out.println("File not found at the location");
		}
		
		// Storing the data into 2D Double array
		double[][] data = new double[size][cols];
	      int index = -1; String delimiter = ",";
	      sc = new Scanner(iterator);
         while(sc.hasNextLine()) {
	    	 
	         String line = sc.nextLine();
	         
	         String[] row = line.split(delimiter);
	         
	         index++;
	         for(int i = 0; i < row.length; i++)
	            data[index][i] = Double.parseDouble(row[i]);
	      }
         sc.close();
         
         // Finding the trainingData and the testData sets using Random for train data.
         int[] trainingDataLables = new int[(75*size)/ 100];
         int[] testDataLables = new int[size - trainingDataLables.length];
         double[][] trainingData, testData;
		
         ArrayList<Integer> train = new ArrayList<Integer>();
         
         int l = 0;
         // Randomizes the Training data set and the test data set, using Random().
         // Outer while loop is used to check if all the available classes are present in the test data set
      
        while(!condition) {
        // Generating Training Data Set which is 75% of the Original selection Random       
            while(l < trainingDataLables.length) {
	           int value = rand.nextInt(size) + 1;
	           if(!train.contains(value)) {
	              trainingDataLables[l++] = value;
	               train.add(value);
	            }
	         }
	  
		      Arrays.sort(trainingDataLables);
		
            // Generating Training set Data from the rest of the data after test data
		      // Generating values for Test Data
		      index = 0;
		      for(int i = 0; i < testDataLables.length; i++) {
		      	if(i != 0)	index = testDataLables[i - 1];
		      	testDataLables[i] = find(trainingDataLables, index);
		      }
		      Arrays.sort(testDataLables);
  
		      // Checking the availability of classes in the test data set.
		      condition = checkAvail(data, testDataLables, cols);
       }         
       
       // Retrieving Training Data and the Test Data from the Data Array
       trainingData = fillData(data, trainingDataLables);
       testData = fillData(data, testDataLables);
       //End retrieving data
              
       //Getting Classes of Training Data
       int[] trainClass = new int[trainingDataLables.length];
       for(int i = 0; i < trainClass.length; i++)
            trainClass[i] = (int)data[trainingDataLables[i] - 1][10];
       // END
         
       @SuppressWarnings("unused")
	double[][] copyTrainingData = trainingData;
       @SuppressWarnings("unused")
	double[][] copyTestData = testData;

         
       // Normalizing Data - Technique Used: x/ sqrt(x2 + y2)
       for(int i = 0; i < trainingData.length; i++)
         trainingData[i] = normMath(trainingData[i]);
         
       for(int i = 0; i < testData.length; i++)
         testData[i] = normMath(testData[i]);
         
       // End Normalization of Data
                
       //Finding the Eucledian Distance. Distance is calculated to each test record against all the training records.
       
       /*  The distances are stored along with the training record identifier and their classes are to be shown with
       		the distance, the training record id and the class.
       		We can track the testData index with the value of i in the for-loop which is to be sent along with the 
       		testDataLable to the Eucledian method */
       
       //The distances are then sorted accordingly. The top 10 smallest distances are stored in an result array.
       //This result array is then displayed for the required values of k.
  
       // Original Data[][] had been tested here before implementing the Eucledian distance and the data remains same
       
       condition = false;
       while(!condition) {
         String knnMessage = "\t\t\t\t[TestDataIndex,\t\tClass]\n";
         weightedResult = "\t\t\t\t[TestDataIndex,\t\tClass]\n";
         int flag = 0;
         int choice = Integer.parseInt(JOptionPane.showInputDialog(null, "Choose your value of K in (1, 5, 10)\nOnly integer values please","KNN User's K Selection", JOptionPane.QUESTION_MESSAGE));
         switch(choice) {
            case 1:
            case 5:
            case 10:
                     flag = 1;
                     knnMessage += eucledianMethod(testData, trainingData, data, testDataLables, trainClass, choice);
                     JOptionPane.showMessageDialog(null, knnMessage, "KNN Majority Classes Model", JOptionPane.PLAIN_MESSAGE);
                     JOptionPane.showMessageDialog(null, weightedResult, "KNN Weighted Scoring Model", JOptionPane.PLAIN_MESSAGE);
                     break;
            default:
                     int dialogResult = JOptionPane.showConfirmDialog (null, "\t\t\t\tWrong Choice of 'K'\nDo you Want to re-enter value of K?","Try Again!!!",JOptionPane.YES_NO_OPTION);
                     if(dialogResult == JOptionPane.NO_OPTION){
                        condition = true;
                        JOptionPane.showMessageDialog(null, "User Cancelled Operation", "KNN Majority Classes Model", JOptionPane.PLAIN_MESSAGE);
                     }
                     break;
 
           }
           if(flag == 1) {
               int dialogResult = JOptionPane.showConfirmDialog (null, "Do you want to continue with a different value of K?\nSame Training and Test Data sets are used!!!","Try Again!!!",JOptionPane.YES_NO_OPTION);
               if(dialogResult == JOptionPane.NO_OPTION){
                   condition = true;
                  JOptionPane.showMessageDialog (null, "\t\t\t\t\tThank you!!\n\t\t\tHave a great day","Bye Bye !!!",JOptionPane.PLAIN_MESSAGE);
               }
           }          
       }
       
   }// END MAIN
   
   //Find the number of columns in the data
	private static int findCols(String in) {
		String[] temp = in.split("\\,");
      	return temp.length;
	}
   //END FIND COLUMNS    
   
   //Get the labels for the training set data
	private static int find(int[] train, int index) {
		int result = 0, k;
		while(result == 0) {
			k = 0; index++;
			while(train[k++] != index && k < train.length - 1);
			if(k == train.length - 1 && train[k] != index) 
				result = index; 
		}
		return result;
	}
   // END FIND Function which is used to get the testDataLables
   
   // Checking all classes in Test Data
	private static boolean checkAvail(double[][] original, int[] test, int index) {
      
		ArrayList<Integer> holderOriginal = new ArrayList<Integer>();
		ArrayList<Integer> holderTest = new ArrayList<Integer>();
		for(int i = 0; i < original.length; i++) {
			int value = (int)original[i][index - 1];
			if(!holderOriginal.contains(value))
				holderOriginal.add(value);
		}
      
		for(int i = 0; i < test.length; i++) {
			int value = (int)original[test[i]][10];
			if(!holderTest.contains(value))
				holderTest.add(value);
		}
		
      return holderOriginal.size() == holderTest.size();
      }  
      
      // END Check Availability to get the Condition for the while in finding Test and Train data sets  
      
      // FILL Data Method: Data values are updated here
	   private static double[][] fillData(double[][] original, int[] index) {
	   	double[][] indexD = new double[index.length][9];
	   	for(int i = 0; i < indexD.length; i++){
             for(int j = 0; j < indexD[i].length; j++)
                  indexD[i][j] = original[index[i] - 1][j + 1];	
         }
      return indexD;
      }
      // END Fill Data Method
      
      //Normalize Row method. Normalize using (x - min)/ (max - min)
      @SuppressWarnings("unused")
	private static double[] normalizeRow(double[] row) {
         double max = Integer.MIN_VALUE;
         double min = Integer.MAX_VALUE;
         
         for(int i = 0; i < row.length; i++) {
            if(row[i] < min)
               min = row[i];
            if(row[i] > max)
               max = row[i];
               
         }
         
         for(int i = 0; i < row.length; i++)
            row[i] = Double.parseDouble(df.format((row[i] - min)/ (max - min)));
      
      return row;
      }
      // END Normalize Method
      
      //Normalization using Mathematical Formula
      private static double[] normMath(double[] row) {
         double sum = 0;
         for(int i = 0; i < row.length; i++)
            sum += Math.pow(row[i], 2);
         sum = Math.sqrt(sum);
         
         for(int i = 0; i < row.length; i++)
            row[i] = Double.parseDouble(df.format(row[i]/ sum));
            
      return row;
      }
      // END Normalization using Mathematical formula technique
      
      //Eucledian Distance Technique
      
      private static String eucledianMethod(double[][]testData, double[][]trainingData, double[][]data, int[]testDataLables, int[]trainClass, int k) {
         String output = ""; int matches = 0, wmatches = 0; double accuracy = 0;
         int[] foundClass = null; 
         
         for(int i = 0; i < testData.length; i++) {
            foundClass = calculateDistance(testData[i], trainingData, data, testDataLables, trainClass, k);
            
            if(foundClass[0] == (int)data[testDataLables[i]][10])   matches++;
            if(foundClass[1] == (int)data[testDataLables[i]][10])   wmatches++;
            
            if(i% 2 == 0) {
               output += "[" + testDataLables[i] + "\t\t" + foundClass[0] + "], \t\t\t";
               weightedResult += "[" + testDataLables[i] + "\t\t" + foundClass[1] + "], \t\t\t";
            }
            else {
               output += "[" + testDataLables[i] + "\t\t" + foundClass[0] + "]\n";
               weightedResult += "[" + testDataLables[i] + "\t\t" + foundClass[1] + "]\n";
            }
               
         }
         
         //Calculating Accuracy
         accuracy = ((int)(((double)wmatches/ testData.length)*100)*100)/100;
         weightedResult += "\n\n\t\t\t\t Accuracy: " + accuracy+"%";
         
         accuracy = ((int)(((double)matches/ testData.length)*100)*100)/100;
      return output + "\n\n\t\t\t\t Accuracy: " + accuracy+"%";   
      }
      // End Method Eucledian Distance
      
      // Method Calculate Distance called from Eucledian Distance
      private static int[] calculateDistance(double[] testData, double[][] trainingData, double[][] data, int[] testLables, int[] trainClass, int k) {
         double[][] derivedClasses = new double[trainingData.length][2];
         double sum = 0;
         
         double[][] weightedDistance = new double[trainingData.length][2];

         
         for(int i = 0; i < trainingData.length; i++) {
            for(int j = 0; j < testData.length; j++) {
               sum += Math.pow(trainingData[i][j] - testData[j], 2);
            sum = Math.sqrt(sum);
            derivedClasses[i][0] = trainClass[i];
            weightedDistance[i][0] = trainClass[i];
            
            derivedClasses[i][1] = Double.parseDouble(df.format(sum));
            weightedDistance[i][1] = Double.parseDouble(df.format(1/Math.pow(sum, 2)));
            }   
         }
         
        sort(derivedClasses);
        
        //Sorting Weights descending order
        Arrays.sort(weightedDistance, new java.util.Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
               return Double.compare(b[1], a[1]);
            }
        });
        
        double[][] kWeights = new double[k][2];
        for(int i = 0; i < kWeights.length; i++)
            kWeights[i] = weightedDistance[i];
                
        // K result sets required which are obtained from following
        int[] kClasses = new int[k];
        for(int i = 0; i < kClasses.length; i++)
            kClasses[i] = (int)derivedClasses[i][0];
        
        //Find Majority Classes
       int[] found = new int[2];
       found[0] = findMajorityClass(kClasses);
       found[1] = findWeight(kWeights);
       
       return found;
        
    }
      // END method calculateDistance
      
      //Method sort. Arrays.sort is used in this technique, this method is being called from calculateDistance method
      public static void sort(double[][] distances) {
         Arrays.sort(distances, new Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
                return Double.valueOf(a[1]).compareTo(Double.valueOf(b[1]));
            }
         });
      }
      // END sort method
      
      /* Find Majority Classes. Classes array is passed into this method, This method analyzes which are the most
      repeated classes and outputs the class. If there are two classes of equal occurrences, then only one class is
      returned by this function */
      
      private static int findMajorityClass(int[] classes) {
		int out = -1;
		int[] uniqueValues = classes;
		ArrayList<Integer> count = new ArrayList<Integer>();
		
		for (int i = 0; i < uniqueValues.length; i++)
			for (int j = 0; j < classes.length; j++)
				if(classes[j] == uniqueValues[i])	count.add(i, 1);
		
		int max = count.get(0);
		for (int counter = 1; counter < uniqueValues.length; counter++)
			if (count.get(counter) > max)		max = count.get(counter);
		
		int repeat = 0;
		for (int counter = 0; counter < uniqueValues.length; counter++)
			if (count.get(counter) == max)		repeat++;
		
		int index = -1;
		if(repeat==1){
			for (int counter = 0; counter < uniqueValues.length; counter++)
				if (count.get(counter) == max) {
					index = counter;
					break;
				}
			
			out = uniqueValues[index];
			
		} else{																	//we have multiple maximum occurrences
				int[] xIndex = new int[repeat];							//array of indices of occurrences
				int temp = 0;
				for (int counter = 0; counter < uniqueValues.length; counter++)
					if (count.get(counter) == max) {
						xIndex[temp] = counter;							//save index of each max count value
						temp++; 													// increase index of ix array
				}

				Random generator = new Random();        
				int rIndex = generator.nextInt(xIndex.length);
				int nIndex = xIndex[rIndex];
			
				out = uniqueValues[nIndex];
			}
		
		return out;
	}
   //END findMaorityClasses
   
   // Find Weights method called from Calculate Eucledian distance
   private static int findWeight(double[][] weightsDistances) {
      double[][] calculate = null;
      if(weightsDistances.length == 1)    return (int)weightsDistances[0][0];
      else {
            ArrayList<Integer> uniqueClasses = new ArrayList<Integer>();
            for(int i = 0; i < weightsDistances.length; i++)
                  if(!uniqueClasses.contains((int)weightsDistances[i][0]))   uniqueClasses.add((int)weightsDistances[i][0]);
      
            calculate = new double[uniqueClasses.size()][2];
            double sum = 0;
            for(int i =0 ; i < uniqueClasses.size(); i++) {
               calculate[i][0] = uniqueClasses.get(i);
               sum = 0;
               for(int j = 0; j < weightsDistances.length; j++) {
                  if(weightsDistances[j][0] == uniqueClasses.get(i))
                     sum += weightsDistances[j][1];
               }
               calculate[i][1] = sum;
            }
                  
         //Sorting Weights descending order
         Arrays.sort(calculate, new java.util.Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
               return Double.compare(b[1], a[1]);
            }
        });
      }
    return (int)calculate[0][0];
   }
   // End FindWeights
}
