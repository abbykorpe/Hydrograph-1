/*******************************************************************************
 * Copyright 2016 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.engine.transformation.userfunctions.partition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import hydrograph.engine.transformation.userfunctions.base.CustomPartitionExpression;
import hydrograph.engine.transformation.userfunctions.base.ReusableRow;

public class RandomSplit implements CustomPartitionExpression,Serializable{

	private static final long serialVersionUID = 8602448204592615430L;

	private Map<String,Double> weightPortMap=new HashMap<String,Double>();
	private Random randomVal;
	
	/**
	 * This method will populate a hashmap which will be used in getPartition() method for determining the partition.
	 * For populating the hashmap, it will use two properties "WEIGHTS" and "PORTS".
	 * User can specify how many records should go to which port using these properties.
	 * For example, if user wants 70% of records in out0 socket and 30% in out1, he/she should specify WEIGHTS as "0.7,0.3" and PORTS as "out0,out1" (the outSocketId).
	 * Sum of all weights should be less than or equal to 1. If the sum is greater than one, this method will throw exception.
	 * If the sum of weights is less than 1, this method will adjusts weights to make the sum equal to 1.
	 * Also, number of weights and ports passed to this method should match. If they don't match, this method will throw exception.
	 * These are required properties for this class. If user does not pass these properties, this method will throw exception.
	 * Apart from "WEIGHTS" and "PORTS", user can also pass "SEED" property (optional) which will be used to seed the random number generation.
	 */
	@Override
	public void prepare(Properties props) {
		String[] weightProp;
		String[] outSocketProp;
		double sum=0;
		randomVal=new Random();
		
		if(props.getProperty("WEIGHTS")!=null)
			weightProp=props.getProperty("WEIGHTS").split(",");
		else
			throw new PartitionByExpressionException("WEIGHTS property not passed in partition by expression");
		
		if(props.getProperty("PORTS")!=null)
			outSocketProp=props.getProperty("PORTS").split(",");
		else
			throw new PartitionByExpressionException("PORTS property not passed in partition by expression");
		
		if(weightProp.length!=outSocketProp.length)
			throw new PartitionByExpressionException("weights and ports are not mapping correctly");
		
		if(props.getProperty("SEED")!=null){
			randomVal.setSeed(Long.parseLong(props.getProperty("SEED")));
		}
		
		for(int i=0;i<weightProp.length;i++){
			double parsedWeight=Double.parseDouble(weightProp[i]);
			weightPortMap.put(outSocketProp[i], parsedWeight);
			sum=sum+parsedWeight;
		}
		
		if(sum>1)
			throw new PartitionByExpressionException("Sum of weights is greater than zero");
		
		if(sum<1)
		{
			for(String portId:weightPortMap.keySet()){
				weightPortMap.put(portId, weightPortMap.get(portId)/sum);
			}
		}
	}
	
	/**
	 * This method will use the hashmap populated in prepare() method.
	 * This method will create weight offsets and see where the generated random belongs.
	 * For example, for 20-50-30% random split, it will create weight offsets of 0.2,0.2+0.5,0.2+0.5+0.3.
	 * That is 0.2 for partition 0, 0.7 for partition 1 and 1 for partition 2.
	 * It will generate a random number (between 0 and 1) and put the record in the partition respective to the weight offset in which the generated random number belongs.
	 * The partitions generated by this method will randomly split the record into the percentage which is approx. equal to the weights passed.
	 */
	@Override
	public String getPartition(ReusableRow keys, int numOfPartitions) {
		double randomValue=randomVal.nextDouble();
		double weightOffset=0;
		String partitionPortId=null;

		for(String portId:weightPortMap.keySet()){
			weightOffset+=weightPortMap.get(portId);
			if(randomValue<weightOffset){
				partitionPortId=portId;
				break;
			}
		}

		return partitionPortId;
	}
	
	private class PartitionByExpressionException extends RuntimeException {

		private static final long serialVersionUID = -5625425804007046046L;

		public PartitionByExpressionException(String msg) {
			super(msg);
		}
	}

}