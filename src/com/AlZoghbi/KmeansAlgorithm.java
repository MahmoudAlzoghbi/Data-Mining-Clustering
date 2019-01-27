package com.AlZoghbi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class KmeansAlgorithm {

	public static void main(String[] args) throws IOException, Exception {
		boolean compare = true;
		int numOfStudent = 150;
		int numOfQuestion = 20;
		// Read Data From The Excel Sheet
		int[][] DBData = new int[numOfStudent][numOfQuestion];
		int[] StudID = new int[numOfStudent]; 
		File f = new File("Sheet1.xls");
		Workbook wb = Workbook.getWorkbook(f);
		Sheet s = wb.getSheet(0);
		int row = s.getRows();
		int col = s.getColumns();
		// System.out.println("Row " + row + "Col " + col);
		for (int i = 0, i1 = 1; i1 < row; i++, i1++) {
			for (int j = 0, j1 = 1; j1 < col; j++, j1++) {
				Cell c = s.getCell(j1, i1); // hena ana 3amel +1 3lshan awl row , col
				DBData[i][j] = Integer.parseInt(c.getContents().toString());
				// System.out.print(c.getContents().toString() + " ");
			}
			// System.out.println();
		}
		// Read Student ID's
		for (int i = 1, j = 0; i < row; i++, j++) {
			Cell c = s.getCell(0, i);
			StudID[j] = Integer.parseInt(c.getContents().toString());
			// System.out.println(StudID[j]);
		}
		// Get The Number of Cluster "K" From the User
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter The Number Of Clusters K");
		int K = sc.nextInt();
		System.out.println("Enter The Distance to ditect Outliers ");
		double D = sc.nextDouble();
		double[][] distance = new double[numOfStudent][K];
		double[][] old_Centroid = new double[K][numOfQuestion];
		double[][] New_Centroid = new double[K][numOfQuestion];
		double[] nearestCluster = new double[numOfStudent];
		int[][] Cluster = new int[K][numOfQuestion];
		// Get The Clusters Random
		Random rand = new Random();
		//System.out.println("OLD");
		for (int i = 0; i < K; i++) {
			int n = rand.nextInt(149) + 1;
			// System.out.print( n + "---> ");
			for (int j = 0; j < numOfQuestion; j++) {
				Cluster[i][j] = DBData[n][j];
				old_Centroid[i][j] = DBData[n][j];
				//System.out.print(old_Centroid[i][j] + " ");
			}
			//System.out.println();
		}
		// get the cluster without Random
		/*for (int i = 0 ; i < K ; i++ ) {
			for (int j = 0 ; j < numOfQuestion ; j++ ) {
				old_Centroid[i][j] = DBData[i][j];
				System.out.println(old_Centroid[i][j] + "  ");
			}
			System.out.println();
		}*/
		while(compare) {
			// compute Distance :D
			double result = 0, help = 0;
			for (int i = 0; i < K; i++) { // number of Clusters
				for (int j = 0; j < numOfStudent; j++) { // number of row
					for (int n = 0; n < numOfQuestion; n++) { // number of column
						help = DBData[j][n] - old_Centroid[i][n];
						// System.out.println(help);
						result += (help * help);
					}
					result = Math.sqrt(result);
					distance[j][i] = result;
					result = 0;
					// System.out.print("Distance of cluster " + " "+distance[i][j]);
				}
				// System.out.println();
			}
			// Print the Distances
			/*
			 * for (int i = 0 ; i < numOfStudent ; i++) {
			 * System.out.print("The Distance is: ----> "); for(int j = 0 ; j < K ; j++) {
			 * System.out.print( distance[i][j] + "   "); } System.out.println(); }
			 */
			// to get the nearest Cluster for every Student
			List<Double> sort = new ArrayList<>();
			for (int i = 0; i < numOfStudent; i++) {
				for (int j = 0; j < K; j++) {
					sort.add(distance[i][j]);
				}
				Collections.sort(sort);
				// System.out.println(sort);
				double smallest = sort.get(0);
				for (int j = 0; j < K; j++) { // keda ana 3rft el-Student fe anhy Cluster
					// System.out.println(distance[i][j] + " " + sort.get(0));
					// System.out.println("Ana hena Barah");
					if (distance[i][j] == smallest) {
						// System.out.println("Ana hena");
						nearestCluster[i] = j + 1;
						// System.out.println(nearestCluster[i]);
						break;
					}
				}
				sort.clear();
			}
			// Printing the Students with each Cluster
			 for (int i = 0; i < numOfStudent; i++) { 
				 System.out.println("The Student ID is " + StudID[i] + " and Belongs to Cluster Number " + nearestCluster[i]);
				 }
			// Compute new Clusters Mean
			List<Double> DataOfEachCluster = new ArrayList<>();
			for (int i = 0; i < K; i++) {
				for (int j = 0; j < numOfStudent; j++) {
					if (i + 1 == nearestCluster[j]) {
						for (int m = 0; m < numOfQuestion; m++) {
							DataOfEachCluster.add((double) DBData[j][m]);
						}
					}
				}
				double nw_mean = 0;
				//System.out.println("NEW");
				for (int q = 0, j = 0; q < numOfQuestion; q++) {
					for (; j < DataOfEachCluster.size(); j += 20) {
						nw_mean += DataOfEachCluster.get(j);
					}
					nw_mean /= (DataOfEachCluster.size() / 20);
					New_Centroid[i][q] = nw_mean;
					j = q;
					nw_mean = 0;
					System.out.print(New_Centroid[i][q] + " ");
				}
				System.out.println();
				// System.out.println(DataOfEachCluster);
				// System.out.println(DataOfEachCluster.size());
				DataOfEachCluster.clear();
			}
			List<Double> O_C = new ArrayList<>();
			List<Double> N_C = new ArrayList<>();
			for (int i = 0; i < K; i++) {
				for (int j = 0; j < numOfQuestion; j++) {
					O_C.add(old_Centroid[i][j]);
					N_C.add(New_Centroid[i][j]);
				}
			}
			System.out.println(O_C);
			System.out.println(N_C);
			// compare with the traditional way EL-Zoghbi
			for (int m = 0 ; m < O_C.size() ; m++) {
				//System.out.println(O_C.get(m) +"  " + N_C.get(m));
				if (O_C.equals(N_C)) {
					//if (counter == O_C.size()) {
						compare = false;
						break;
					//}
				}
				else if (!O_C.equals(N_C)) {
					int index = 0;
					for (int i = 0; i < K; i++) {
						for (int j = 0; j < numOfQuestion; j++) {
							//System.out.println(N_C.get(m));
							old_Centroid[i][j] = N_C.get(index);
							//System.out.print(old_Centroid[i][j] +" ");
							index++;
						}
						//System.out.println();
					}
					break;
				}
			}
		}
		// compute outLier insh
		int count = 0;
		for (int i = 0 ; i < numOfStudent ; i++ ) {
			int index = (int)nearestCluster[i];
			index -= 1;
			double result = 0 , help; 
			for (int j = 0 ; j < numOfQuestion ; j++) {
				help = DBData[i][j] - Cluster[index][j];
				help *= help;
				result += help;
			}
			result = Math.sqrt(result);
			if ( result > D) {
				count++;
				System.out.println("There is An OutLier Object or Tuple: " + StudID[i]);
			}
			result = 0;	
		}
		System.out.println("Number of OutLier are " + count );
	}
}