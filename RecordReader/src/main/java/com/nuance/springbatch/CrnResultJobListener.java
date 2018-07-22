/**
 * 
 */
package com.nuance.springbatch;

import static com.nuance.springbatch.constants.AppConstants.CRN_OUTPUT_COPY;
import static com.nuance.springbatch.constants.AppConstants.CRN_OUTPUT_ORIGINAL;
import static com.nuance.springbatch.constants.AppConstants.EN_IN_GRAM;
import static com.nuance.springbatch.constants.AppConstants.HI_IN_GRAM;
import static com.nuance.springbatch.constants.AppConstants.HI_IN_OUTPUT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @author praveen.rawat
 *
 */
public class CrnResultJobListener implements JobExecutionListener {

	private DateTime startTime, stopTime;

	public void beforeJob(JobExecution jobExecution) {
		startTime = new DateTime();
		System.out.println("RecordReader Job starts at :" + startTime);
	}

	public void afterJob(JobExecution jobExecution) {
		stopTime = new DateTime();
		System.out.println("RecordReader Job stops at :" + stopTime);
		System.out.println("Total time taken in millis :" + getTimeInMillis(startTime, stopTime));

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			System.out.println("RecordReader job completed successfully");

			outputGenerator();
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			System.out.println("ExamResult job failed with following exceptions ");
			List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
			for (Throwable th : exceptionList) {
				System.err.println("exception :" + th.getLocalizedMessage());
			}
		}
	}

	private long getTimeInMillis(DateTime start, DateTime stop) {
		return stop.getMillis() - start.getMillis();
	}

	private boolean outputGenerator() {
		Properties prop = getAppConfig();
		File inputFile3 = new File(prop.getProperty(CRN_OUTPUT_ORIGINAL));
		File output3 = new File(prop.getProperty(CRN_OUTPUT_COPY));

		try {

			InputStream is2 = getClass().getResourceAsStream(HI_IN_GRAM);

			OutputStream os2 = new FileOutputStream(prop.getProperty(HI_IN_OUTPUT));

			InputStream is1 = getClass().getResourceAsStream(EN_IN_GRAM);

			OutputStream os1 = new FileOutputStream(prop.getProperty(HI_IN_OUTPUT));

			readWriteFiles(is1, os1);
			readWriteFiles(is2, os2);

			FileUtils.copyFileToDirectory(inputFile3, output3);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private Properties getAppConfig() {

		InputStream is = null;
		Properties prop = null;
		try {
			prop = new Properties();
			is = getClass().getResourceAsStream("/RecordReader.properties");
			prop.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;
	}

	private boolean readWriteFiles(InputStream is, OutputStream os) throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		// read from is to buffer
		while ((bytesRead = is.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		is.close();
		// flush OutputStream to write any buffered data to file
		os.flush();
		os.close();

		return false;
	}
}
