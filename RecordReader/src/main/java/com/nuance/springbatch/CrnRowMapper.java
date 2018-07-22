/**
 * 
 */
package com.nuance.springbatch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.nuance.springbatch.model.ModelCRN;

/**
 * @author praveen.rawat
 *
 */
public class CrnRowMapper implements RowMapper<ModelCRN> {

	public ModelCRN mapRow(ResultSet rs, int rowNum) throws SQLException {

		ModelCRN result = new ModelCRN();
		result.setCrn(rs.getString("CRN"));

		return result;
	}

}