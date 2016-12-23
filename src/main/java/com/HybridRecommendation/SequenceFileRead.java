package com.HybridRecommendation;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.math.VarLongWritable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

// Read all the vectors in a sequence file to a hashmap
public class SequenceFileRead {
  
private HashMap<Integer, Vector> map;
	
	public void ReadSequence(String uri, int isvar) throws IOException {
		map = new HashMap<Integer, Vector>();
	    Configuration conf = new Configuration();
	    FileSystem fs = FileSystem.get(URI.create(uri), conf);
	    Path path = new Path(uri);
	    SequenceFile.Reader reader = null;
	    try {
	        reader = new SequenceFile.Reader(fs, path, conf);
	        Writable key = (Writable)
	          ReflectionUtils.newInstance(reader.getKeyClass(), conf);
	        Writable value = (Writable)
	          ReflectionUtils.newInstance(reader.getValueClass(), conf);
	        while (reader.next(key, value)) {
	        	if(isvar == 0) {
	        		map.put(((IntWritable) key).get(), ((VectorWritable) value).get());
	        	}
	        	else {
	        		map.put((int) (((VarLongWritable) key).get()), ((VectorWritable) value).get());
	        	}
	        }
	    } finally {
	    	IOUtils.closeStream(reader);
	    }
	}
	
	public HashMap<Integer, Vector> get(){
		return map;
	}
}
