/**
 * 
 */
package com.nuance.springbatch;

import org.springframework.batch.item.ItemProcessor;

import com.nuance.springbatch.model.ModelCRN;

/**
 * @author praveen.rawat
 *
 */
public class CrnResultItemProcessor implements ItemProcessor<ModelCRN, ModelCRN> {

	public ModelCRN process(ModelCRN result) throws Exception {
		System.out.println("Processing result :" + result);
		return result;
	}

}
