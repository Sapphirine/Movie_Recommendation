package com.HybridRecommendation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.mahout.math.Vector;

public class HybridRecommender {
	
	public HashMap<Integer, Vector> CFSimilarity;
	public HashMap<Integer, Vector> CBSimilarity;
	public HashMap<Integer, Vector> UserVecTest;
	public List<ItemSimilarity> CFl;
	public List<ItemSimilarity> CBl;
	public List<ItemSimilarity> Hl;
	
	public double CFScore;
	public double CBScore;
	public double HybridScore;
	
	// Read the data from sequence files to hashmaps
	public void GetInputMap(String[] args) throws Exception {
		SequenceFileRead r = new SequenceFileRead();
		CFSimilarity = new HashMap<Integer, Vector>();
		CBSimilarity = new HashMap<Integer, Vector>();
		UserVecTest = new HashMap<Integer, Vector>();
		r.ReadSequence(args[0], 0);
		CFSimilarity = r.get();
		r.ReadSequence(args[1], 0);
		CBSimilarity = r.get();
		r.ReadSequence(args[2], 1);
		UserVecTest = r.get();
	}
	
	// Make a prediction for the rating of (userID,itemID)
	// Using Collaborative Filtering if isCF = 1, else using Content-based Filtering
	public void Recommend(int userID, int itemID, int isCF) throws Exception {
		Vector vec;
		if(isCF == 1) {
			if(CFSimilarity.containsKey(itemID)) {
				vec = CFSimilarity.get(itemID);
			}
			else {
				CFScore = -1;
				return;
			}
		}
		else {
			if(CBSimilarity.containsKey(itemID)) {
				vec = CBSimilarity.get(itemID);
			}
			else {
				CBScore = -1;
				return;
			}
		}
		List<ItemSimilarity> list = new ArrayList<ItemSimilarity>();
		for (Vector.Element element : vec.nonZeroes()) {
			ItemSimilarity s = new ItemSimilarity();
			s.ItemID = element.index();
			s.Similarity = element.get();
			list.add(s);
        }
		Collections.sort(list, new Comparator<ItemSimilarity>() {
            public int compare(ItemSimilarity a, ItemSimilarity b) {
                if(a.Similarity < b.Similarity){  
                    return 1;  
                }  
                if(a.Similarity == b.Similarity){  
                    return 0;
                }  
                return -1;
            }
        });
		double score = 0;
		double weight = 0;
		int sum = 0;
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		vec = UserVecTest.get(userID);
		for (Vector.Element element : vec.nonZeroes()) {
			map.put(element.index(), element.get());
        }
		for (ItemSimilarity s : list) {
			if(map.containsKey(s.ItemID)) {
				score += map.get(s.ItemID) * s.Similarity;
				weight += s.Similarity;
				sum ++;
			}
			if(sum == 30) {
				break;
			}
        }
		if(isCF == 1) {
			if(sum >= 1) {
				CFScore = score/weight;
			}
			else {
				CFScore = -1;
			}
		}
		else {
			if(sum >= 1) {
				CBScore = score/weight;
			}
			else {
				CBScore = -1;
			}
		}
	}
	
	// Make a prediction using the hybrid recommender
	public void HybridRecommend(int userID, int itemID, double p) throws Exception {
		Recommend(userID, itemID, 0);
		Recommend(userID, itemID, 1);
		if(CFScore > 0 && CBScore > 0) {
			HybridScore = p * CFScore + (1 - p) * CBScore;
		}
		else {
			HybridScore = -1;
		}
	}

	// Find the most similar items to the given item
	// Using Collaborative Filtering if isCF = 1, else using Content-based Filtering
	public int MostSimilar(int itemID, int isCF) throws Exception {
		Vector vec;
		if(isCF == 1) {
			if(CFSimilarity.containsKey(itemID)) {
				vec = CFSimilarity.get(itemID);
			}
			else {
				return -1;
			}
		}
		else {
			if(CBSimilarity.containsKey(itemID)) {
				vec = CBSimilarity.get(itemID);
			}
			else {
				return -1;
			}
		}
		List<ItemSimilarity> list = new ArrayList<ItemSimilarity>();
		for (Vector.Element element : vec.nonZeroes()) {
			ItemSimilarity s = new ItemSimilarity();
			s.ItemID = element.index();
			s.Similarity = element.get();
			list.add(s);
        }
		Collections.sort(list, new Comparator<ItemSimilarity>() {
            public int compare(ItemSimilarity a, ItemSimilarity b) {
                if(a.Similarity < b.Similarity){  
                    return 1;  
                }  
                if(a.Similarity == b.Similarity){  
                    return 0;
                }  
                return -1;
            }
        });
		if(isCF == 1) {
			CFl = list;
		}
		else {
			CBl = list;
		}
		return 0;
	}
	
	// Make a recommendation for the given user
	// Using Collaborative Filtering if method = 1, using Content-based Filtering if method = 2
	// else using the hybrid recommender
	public void MakeRecommendation(int userID, int method) throws Exception {
		Vector vec = UserVecTest.get(userID);
		List<ItemSimilarity> list = new ArrayList<ItemSimilarity>();
		if(method == 2 || method == 3) {
			for (Integer k : CBSimilarity.keySet()) {
				for (Vector.Element element : vec.nonZeroes()) {
					if(element.index() == k)
						continue;
				}
				Recommend(userID, k, 0);
				if(CBScore < 0) {
					continue;
				}
				if(method == 3) {
					Recommend(userID, k, 1);
					if(CFScore < 0) {
						continue;
					}
				}
				ItemSimilarity s = new ItemSimilarity();
				s.ItemID = k;
				if(method == 2)
					s.Similarity = CBScore;
				else
					s.Similarity = CFScore * 0.8 + CBScore * 0.2;
				list.add(s);
			}
			Collections.sort(list, new Comparator<ItemSimilarity>() {
	            public int compare(ItemSimilarity a, ItemSimilarity b) {
	                if(a.Similarity < b.Similarity){  
	                    return 1;  
	                }  
	                if(a.Similarity == b.Similarity){  
	                    return 0;
	                }  
	                return -1;
	            }
	        });
			if(method == 2) {
				CBl = list;
			}
			else {
				Hl = list;
			}
        }
		else {
			for (Integer k : CFSimilarity.keySet()) {
				for (Vector.Element element : vec.nonZeroes()) {
					if(element.index() == k)
						continue;
				}
				Recommend(userID, k, 1);
				if(CFScore < 0) {
					continue;
				}
				ItemSimilarity s = new ItemSimilarity();
				s.ItemID = k;
				s.Similarity = CFScore;
				list.add(s);
			}
			Collections.sort(list, new Comparator<ItemSimilarity>() {
	            public int compare(ItemSimilarity a, ItemSimilarity b) {
	                if(a.Similarity < b.Similarity){  
	                    return 1;  
	                }  
	                if(a.Similarity == b.Similarity){  
	                    return 0;
	                }  
	                return -1;
	            }
	        });
			CFl = list;
		}
	}
	
	public class ItemSimilarity {
		public int ItemID;
		public double Similarity;
	}
}