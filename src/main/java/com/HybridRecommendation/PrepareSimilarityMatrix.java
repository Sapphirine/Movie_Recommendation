package com.HybridRecommendation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.cf.taste.hadoop.preparation.PreparePreferenceMatrixJob;

/*
 * Precompute the similarity matrix and user vectors
 * 
 * args[0]: File of tag relevance of each movie
 * args[1]: Output path where similarity of Content-based Filtering should be written
 * args[2]: Temporary directory for computing similarity of Content-based Filtering
 * args[3]: File of ratings of each movie in training set
 * args[4]: Output path where similarity of Collaborative Filtering should be written
 * args[5]: Output path where training user vectors should be written
 * args[6]: File of ratings of each movie in test set
 * args[7]: Output path where test user vectors should be written
 * 
 * The input files should look like {userID(or tagID),movieID,rating(or relevance)}
 */
public class PrepareSimilarityMatrix {

	public static void main(String[] args) throws Exception {
		
		ToolRunner.run(new Configuration(), new DistributedRecommenderReasonJob(), new String[] {
		  "--input", args[0],
		  "--output", args[1],
		  "--similarityClassname", "SIMILARITY_COSINE",
		  "--maxSimilaritiesPerItem", "500",
		  "--tempDir", args[2],
		});
		
		ToolRunner.run(new Configuration(), new DistributedRecommenderReasonJob(), new String[] {
		  "--input", args[3],
		  "--output", args[4],
		  "--similarityClassname", "SIMILARITY_COSINE",
		  "--maxSimilaritiesPerItem", "500",
		  "--tempDir", args[5],
		});
		
		ToolRunner.run(new Configuration(), new PreparePreferenceMatrixJob(), new String[] {
		  "--input", args[6],
		  "--output", args[7] + "prepareRatingMatrix/",
		  "--tempDir", args[7],
		});
	}
}
