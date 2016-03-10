package assignment3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

public class movieRecommendation {
	
	private static int[][] trainingData;
	private static float[][] userSimilarity;
	private static float[] avgRating;
	
	public void readFromFile(String trainingFile) throws IOException {
		File taFile = new File(trainingFile);
		String line = null;
		
		Scanner scanner = new Scanner(taFile);
		try {
			while(scanner.hasNextLine())
			{
				line = scanner.nextLine();
				String rows[] = line.split("\t");
				int userId = Integer.parseInt(rows[0]) - 1;
				int itemId = Integer.parseInt(rows[1]) - 1;
				int rating = Integer.parseInt(rows[2]);
				
				trainingData[userId][itemId] = rating; 
			}
			scanner.close();			
		} catch(Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	public float similarity(int p, int q, int items) {				// Logic to calculate the userSimilarity values
		float numerator = 0;
		float[] R_ARp = new float[items];
		float[] R_ARq = new float[items];
		
		for(int i = 0; i < items; i++) {
			R_ARp[i] = trainingData[p][i] - avgRating[i];
			R_ARq[i] = trainingData[q][i] - avgRating[i]; 
			
			numerator += R_ARp[i] * R_ARq[i];
		}
		
		int loopCount = 0;
		if((R_ARp.length == R_ARq.length))		loopCount = R_ARp.length;		//Safe Check (99.99%)
		
		
		for(int i = 0; i < loopCount; i++) {						// Finding the squares of all items
			R_ARp[i] = (float) Math.pow(R_ARp[i],2);
			R_ARq[i] = (float) Math.pow(R_ARq[i],2);
		}
		
		float denominator = 0, temp2 = 0;					// Adding the similarity values
		for(int i = 0; i < loopCount; i++) {
			denominator += R_ARp[i];
			temp2 += R_ARq[i];
		}
		
		denominator = (float) (Math.sqrt(denominator) * Math.sqrt(temp2));		// Performing the Square root and multiplication
		
		return numerator/ denominator;
	}
	
	
	public void userSimilarityMatrix(int trainCols, int trainRows) {
		int sumOfRating, count;

		// This is calculating the Average ratings for all the items for all the users
		
		for(int i = 0; i < trainCols; i++) {				// This for loop fetches each item; total of 1642 items
			sumOfRating = 0;
			count = 0;
			for(int j = 0; j < trainRows; j++) {		// This for loop fetches all the 943 users for each item
				if(trainingData[j][i] != 0) {
					sumOfRating += trainingData[j][i];
					count ++;
				}
			}
			
		if(sumOfRating != 0 && count != 0)					// This is the place where we store the average item ratings.
			avgRating[i] = sumOfRating/count;
			
		}
		
		
		for(int i = 0; i < trainRows; i++) {
			for (int  j= 0; j < trainRows; j++) {
				
				if(i == j) {
					userSimilarity[i][j] = -5;
					//userSimilarity[j][i] = -5;
				}
				
				
				if(userSimilarity[i][j] == 0) {									// Safe check.
					userSimilarity[i][j] = similarity(i,j, trainCols);
					userSimilarity[j][i] = userSimilarity[i][j];
				}
				
			}
		}
	}

public static void main(String[] args) throws IOException {

		String trainingFile = "train1.txt";
		
		FileWriter filewrite = new FileWriter("userSimilarity.txt");
		
		int trainRows = 0, trainCols = Integer.MIN_VALUE;
		BufferedReader br = new BufferedReader(new FileReader(trainingFile));
		
		String line = null;
		while((line = br.readLine()) != null){
			String[] rows = line.split("\\t");
			trainRows = Integer.parseInt(rows[0]);
			int temp = Integer.parseInt(rows[1]);
			
			if(trainCols < temp)	trainCols = temp;
		}
		br.close();
		trainingData = new int[trainRows][trainCols];	
		userSimilarity = new float[trainRows][trainRows];
		avgRating = new float[trainCols];
		

		
		movieRecommendation mR = new movieRecommendation(); 
		mR.readFromFile(trainingFile);
		
		mR.userSimilarityMatrix(trainCols, trainRows);
		
		/*Writing User similarity Matrix values to a file*/
		for(int p1 = 0; p1 < userSimilarity.length; p1++) {
			for (int  q = 0; q < userSimilarity[p1].length; q++)
				filewrite.write(userSimilarity[p1][q]+"\t");
			filewrite.write("\n");
		}
		filewrite.close();
	}

}