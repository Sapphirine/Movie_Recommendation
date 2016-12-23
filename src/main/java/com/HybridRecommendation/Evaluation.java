package com.HybridRecommendation;

import org.apache.mahout.math.Vector;

// Evaluate the recommender of different weights
// args[0]: Sequence File of similarity matrix of Collaborative Filtering
// args[1]: Sequence File of similarity matrix of Content-based Filtering
// args[2]: Sequence File of test user vectors
public class Evaluation {

	public static void main(String[] args) throws Exception {
		int sum = 0;
		double score;
		double err[];
		err = new double[11];
		for(int i = 0; i < 11; i ++) {
			err[i] = 0;
		}  
		HybridRecommender recommender = new HybridRecommender();
		recommender.GetInputMap(args);
		for(Integer key : recommender.UserVecTest.keySet()) {
			Vector vec = recommender.UserVecTest.get(key);
			for (Vector.Element element : vec.nonZeroes()) {
				recommender.Recommend(key,element.index(),0);
				if(recommender.CBScore < 0) {
					continue;
				}
				recommender.Recommend(key,element.index(),1);
				if(recommender.CFScore > 0 && recommender.CBScore > 0) {
					for(int i = 0; i < 11; i ++) {
						score = recommender.CFScore * 0.1 * i + recommender.CBScore * (1 - 0.1 * i);
						err[i] += Math.abs(score - element.get());
					}
					sum ++;
				}
	        }
		}
		for(int i = 0; i < 11; i ++) {
			err[i] = err[i] / sum;
			System.out.print(err[i]);
			System.out.print("\t");
		}
		System.out.print("\n");
		System.out.print(sum);
	}
}
