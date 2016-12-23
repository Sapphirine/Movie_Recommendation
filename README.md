## Introduction
This is a recommendation system implemented on Hadoop. Various algorithms, including collaborative filtering based recommendation, content-based recommendation and hybrid recommendation are available.

* * *
## Prepare similarity matrix
Distributed precomputation of the similarity matrix. It runs RowSimilarityJob of Mahout, which is a completely distributed computation of the pairwise similarity of the row vectors of a DistributedRowMatrix as a series of mapreduces.

* * *
### Run
Run com.HybridRecommendation.PrepareSimilarityMatrix input-path-CB output-path-CF temp-path input-path-CF output-path-CF user-vec-tr input-path-tr user-vec-test.

* * *
### Parameters
1. input-path-CB: (tagID, itemId, relevance) comma delimited file.

2. output-path-CF: output path where similarity matrix of Content-based Filtering should be written.

3. temp-path: temporary directory for computing similarity matrix of Content-based Filtering.

4. input-path-CF: (userID, itemId, rating) comma delimited file.

5. output-path-CF: output path where similarity matrix of Collaborative Filtering should be written.

6. user-vec-tr: output path where training user vectors should be written.

7. input-path-tr: (userID, itemId, rating) comma delimited file.

8. user-vec-test: output path where test user vectors should be written.

* * *
### Results
1. Similarity matrix of Content-based Filtering (Hadoop SequenceFile).

2. Similarity matrix of Collaborative Filtering (Hadoop SequenceFile).

3. Training user vectors (Hadoop SequenceFile).

4. Test user vectors (Hadoop SequenceFile).

* * *
## Make Recommendation
Make recommendation for a given user using collaborative filtering based recommendation, content-based recommendation and hybrid recommendation. The hybrid recommendation uses a linear combination of the ratings obtained from collaborative filtering based recommendation and content-based recommendation. Recommendations can be made in real time by looking up the precomputed similarity matrix.

* * *
### Run
Run com.HybridRecommendation.MakeRecommendation input-path-CF input-path-CB user-vec-test.

* * *
### Parameters
1. input-path-CF: sequence File of similarity matrix of Collaborative Filtering.

2. input-path-CB: sequence File of similarity matrix of Content-based Filtering.

3. user-vec-test: sequence File of test user vectors.

* * *
### Results
1. Lists of items in sorted order of predicted ratings deduced by collaborative filtering based recommendation, content-based recommendation and hybrid recommendation, respectively.

* * *
## Evaluation
Compute the average absolute error of predicted ratings of collaborative filtering based recommendation, content-based recommendation and hybrid recommendation, respectively.

* * *
### Run
Run com.HybridRecommendation.Evaluation input-path-CF input-path-CB user-vec-test.

* * *
### Parameters
1. input-path-CF: sequence File of similarity matrix of Collaborative Filtering.

2. input-path-CB: sequence File of similarity matrix of Content-based Filtering.

3. user-vec-test: sequence File of test user vectors.

* * *
### Results
1. Average absolute error of predicted ratings of collaborative filtering based recommendation, content-based recommendation and hybrid recommendation, respectively.
