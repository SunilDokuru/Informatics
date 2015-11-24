//User Similairy Algorithm implementation in Java

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class UserSimilairyAlgorithm {
	static HashMap<Integer, Double> errorReadings = new HashMap<>();
	static HashMap<Integer, Double> runningTimes = new HashMap<>();
	
	public static void main(String[] args) throws FileNotFoundException, NumberFormatException, IOException{
		
	      BufferedReader similar = null;
	      BufferedReader original = null;
	      BufferedReader testData = null;
	      BufferedReader maxItem = null;

	      // Loading the file into program
			try {
				similar = new BufferedReader(new FileReader("SimilarityMatrix_For_u5.base"));
				original = new BufferedReader(new FileReader("train5.txt"));
				testData = new BufferedReader(new FileReader("test5.txt"));
				maxItem = new BufferedReader(new FileReader("maxItems.txt"));
	        
				//Reading Each test File length into testRows array
				// Index 1 represents number of records in testFile1 and is assigned to testingData1
				
				int testRows[] = new int[6]; int t = 1; String values;
				while((values=maxItem.readLine()) != null) {
					testRows[t++] = Integer.parseInt(values);
				}  
				
				int[][] trainingData = new int[943][1682];
				double[][] userSimilarity = new double[943][943];
				int[][] testingData = new int[943][testRows[5]]; 
		
				
				String delimiter = "\\t", line = null;;
		    
				trainingData = getTrainingData(line, original, delimiter, trainingData);
				testingData = getTestingData(line, testData, delimiter, testingData);
				userSimilarity = getSimilarityMatrix(line, similar, delimiter, userSimilarity);
				
				for(int i = 0; i < userSimilarity.length; i++) 
					sortUsers(userSimilarity[i], i, trainingData, testingData);
			
			} catch(FileNotFoundException ex) {
				System.out.println("File not found at the location");
			}
			
			finally {
				System.out.println("MAE for DataSet 1");
				System.out.println(errorReadings.toString());
				
				System.out.println("Running for DataSet");
				System.out.println(runningTimes.toString());
				
				similar.close();
				original.close();
				testData.close();
				maxItem.close();
			} // End Finally
	}
	
	// Method to calculate MeanAbsoluteError
   	private static double findMeanAbsoluteError(double[][] storeRatings, int[][] testingData, int userIndex) {
   			
   		double MAE = 0;
   				for(int i = 0; i < storeRatings.length; i++) {
   					if(storeRatings[i][1] > 0 && storeRatings[i][0] < testingData.length && testingData[userIndex][(int) storeRatings[i][0]] > 0) {
   						MAE += Math.abs((storeRatings[i][1] - testingData[userIndex][(int)storeRatings[i][0]]));
   					}
   				}
   				return MAE/20000;
   	}
	
	private static void getPredictions(double[][] sortedUsers, int[][] trainingData, ArrayList<Integer> notEmptyItems, int[][] testingData, int userIndex) {
   		
   		DecimalFormat df = new DecimalFormat("##.######");
   		
   		int[] users = null;
   		double[] similarities = null;
   		double startTime, endTime, runningTime = 0;
   		
   		int[] kValues = {1, 10, 50, 100};
   		
   		for(int i = 0; i < kValues.length; i++) {
   			int k = kValues[i];
   			startTime = System.currentTimeMillis();
   			calculatePredictions(runningTime, k, users, similarities, sortedUsers, trainingData, notEmptyItems, testingData, userIndex);
   			endTime = System.currentTimeMillis();
			
   			runningTime  = Double.parseDouble(df.format(endTime - startTime)); 
			
			
			if(runningTimes.containsKey(k)) {
				double temp = runningTimes.get(k);
				runningTimes.replace(k, temp + runningTime);
			} else {
				runningTimes.put(k, runningTime);
			}
   		}
   		
	}
	
	private static void calculatePredictions(double runningTime, int kValue, int[] users,
			double[] similarities, double[][] sortedUsers, int[][] trainingData, ArrayList<Integer> notEmptyItems,
			int[][] testingData, int userIndex) {
		
			double[][] storeRatings = new double[notEmptyItems.size()][2];
			double numerator = 0, denominator = 0, prediction = Double.NaN, MAE = 0;;
			runningTime = 0;
			int k = kValue;
			prediction = Double.NaN;
			
			users = new int[k];
			similarities = new double[k];
			for(int i = 0; i <k; i++) {
					users[i] = (int) sortedUsers[i][0]; 
					similarities[i] = sortedUsers[i][1];
			}
			for(int i = 0; i < notEmptyItems.size(); i++) {
				prediction = Double.NaN;
				
				for( int j = 0; j < k; j++) {
					if(trainingData[users[j]][notEmptyItems.get(i)] > 0 && trainingData[users[j]][notEmptyItems.get(i)] <= 5) {
						numerator += similarities[j] * trainingData[users[j]][notEmptyItems.get(i)];
						denominator += Math.abs(similarities[j]);
					} 
				}
				
				if(denominator != 0)		prediction = numerator/ denominator;
				storeRatings[i][0] = notEmptyItems.get(i);
				storeRatings[i][1] = prediction;
			}
		
			MAE = findMeanAbsoluteError(storeRatings, testingData, userIndex);
			
			if(errorReadings.containsKey(k)) {
				double temp = errorReadings.get(k);
				errorReadings.replace(k, temp + MAE);
			}
			else {
				errorReadings.put(k, MAE);
			}

		
	}



	// Method to find the predictions of missing movie ratings
	   private static void findPredictions(double[][] sortedUsers, int[][] trainingData, int userIndex, int[][] testingData) {
		   ArrayList<Integer> notEmptyItems = new ArrayList<>();
	  		
		   		
		   		// This finds the empty movie ratings
		   		for(int i = 0; i < testingData[userIndex].length; i++) {
		   			if(testingData[userIndex][i] > 0 && testingData[userIndex][i] <= 5)		
		   				notEmptyItems.add(i);
		   		}
		   		// predict for the test data
		   		//Find the predictions
		   		getPredictions(sortedUsers, trainingData, notEmptyItems, testingData, userIndex);
	   }

	
	// Sort method for individual Users
	   private static void sortUsers(double[] individualUser, int userIndex, int[][] trainingData, int[][] testingData)  {
		   double[][] sortedUsers = new double[individualUser.length][2];
		   
		   // Insert index positions to the users similarity values
		   for(int i = 0; i < sortedUsers.length; i++) {
			   sortedUsers[i][0] = i;
			   sortedUsers[i][1] = individualUser[i];
		   }
		   
		   // Sorting user similarity values preserving the indices
		   Arrays.sort(sortedUsers, new java.util.Comparator<double[]>() {
	           public int compare(double[] a, double[] b) {
	              return Double.compare(b[1], a[1]);
	           }
	       });
		   
		   
		   // Copying top 100 values to nearestNeighbors array as maximum value of K asked is 100
		   double[][] nearestNeighbors = new double[100][2];
		   for(int i = 0; i < nearestNeighbors.length; i++)
			   		System.arraycopy(sortedUsers[i], 0, nearestNeighbors[i], 0, nearestNeighbors[i].length);
		   
		   findPredictions(nearestNeighbors, trainingData, userIndex, testingData);
		   
	   }
	   // End sortUsers Method


	private static double[][] getSimilarityMatrix(String line, BufferedReader similar, String delimiter,
			double[][] userSimilarity) throws NumberFormatException, IOException {
		
		double[][] similarity = userSimilarity;
		int index = -1, temp = 0;          
        line = null;
		
		while((line=similar.readLine()) != null) {
				String[] row = line.split(delimiter);
			
				if(temp != 0){
						index++;
						for(int i = 1; i < row.length; i++) {
                
							// This is to ensure the diagonal element has the value -10
							if ( (index + 1) == i)
								similarity[index][i - 1] = -10;
            		
							if(similarity[index][i-1] == 0)
								similarity[index][i-1] = Double.parseDouble(row[i]);
               
							if(similarity[i-1][index] == 0)
								similarity[i-1][index] = similarity[index][i-1];
                  
            	}
            }temp++;
		}

		return similarity;
	}

	private static int[][] getTestingData(String line, BufferedReader testData, String delimiter, int[][] testingData) throws NumberFormatException, IOException {
		int[][] test = testingData;
		
		line = null;
		while((line = testData.readLine()) != null) {
				String[] rows = line.split(delimiter);
        
				int row = -1, col = -1, value = -1;
        

				col = Integer.parseInt(rows[0]) - 1;
				row = Integer.parseInt(rows[1]) - 1;
				value = Integer.parseInt(rows[2]);

            
			test[col][row] = value;
		}

		return test;
	}

	private static int[][] getTrainingData(String line, BufferedReader original, String delimiter,
			int[][] trainingData) throws NumberFormatException, IOException {
			
		int[][] train = trainingData;
		while((line = original.readLine()) != null) {
				String[] rows = line.split(delimiter);
        
				int row = -1, col = -1, value = -1;
        
		
				col = Integer.parseInt(rows[0]) - 1;
				row = Integer.parseInt(rows[1]) - 1;
				value = Integer.parseInt(rows[2]);
		    
            
			train[col][row] = value;
		}
		return train;
	}
}
