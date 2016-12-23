package com.HybridRecommendation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.HybridRecommendation.HybridRecommender.ItemSimilarity;

//Make a recommendation for the given user
//args[0]: Sequence File of similarity matrix of Collaborative Filtering
//args[1]: Sequence File of similarity matrix of Content-based Filtering
//args[2]: Sequence File of test user vectors
public class MakeRecommendation {
	public static void main(String[] args) throws Exception {
		FileOutputStream fs = new FileOutputStream(new File("/home/sxc/Recommendation.txt"));
		PrintStream p = new PrintStream(fs);
		HybridRecommender recommender = new HybridRecommender();
		recommender.GetInputMap(args);
		int userID = 200006;
		int key = userID;
		recommender.MakeRecommendation(key,1);
		recommender.MakeRecommendation(key,2);
		recommender.MakeRecommendation(key,3);
		List<ItemSimilarity> list = recommender.CFl;
		p.print(key);
		p.print("\t");
		int num = 0;
		for(ItemSimilarity s : list) {
			p.print(s.ItemID);
			p.print("\t");
			p.print(s.Similarity);
			p.print("\t");
			num ++;
			if(num == 5) {
				break;
			}
		}
		p.print("\n");
		p.print(key);
		p.print("\t");
		list = recommender.CBl;
		num = 0;
		for(ItemSimilarity s : list) {
			p.print(s.ItemID);
			p.print("\t");
			p.print(s.Similarity);
			p.print("\t");
			num ++;
			if(num == 5) {
				break;
			}
		}
		p.print("\n");
		p.print(key);
		p.print("\t");
		list = recommender.Hl;
		num = 0;
		for(ItemSimilarity s : list) {
			p.print(s.ItemID);
			p.print("\t");
			p.print(s.Similarity);
			p.print("\t");
			num ++;
			if(num == 5) {
				break;
			}
		}
		p.print("\n");
	p.close();
	}
}
