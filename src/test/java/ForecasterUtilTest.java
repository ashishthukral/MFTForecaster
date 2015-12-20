package test.java;

import org.junit.Assert;
import org.junit.Test;

import main.java.ForecastHolder;
import main.java.ForecasterUtil;

public class ForecasterUtilTest {

	@Test
	public void test1() {

		// 7.4% annual return and 8.0%
		final double expectedPercentile10 = 127_300;
		final double expectedMedian = 197_502;

		final double mean = 7.4;
		final double sd = 8.0;
		for (int i = 0; i < 100; i++) {
			ForecastHolder forecastHolder = ForecasterUtil.calculateCustomProjections(mean, sd, 3.5, 20, 100_000);
			double proximity_Percentile10 = (expectedPercentile10 / forecastHolder.getPercentile10()) * 100;
			Assert.assertTrue("Percentile10 proximity should be >= than 89% in relation to expected. Found=" + proximity_Percentile10, proximity_Percentile10 >= 89);

			double proximity_Median = (expectedMedian / forecastHolder.getMedian()) * 100;
			Assert.assertTrue("Median proximity should be >= than 96% in relation to expected. Found=" + proximity_Median, proximity_Median >= 96);
		}
	}

	@Test
	public void test2() {

		// 9.4% annual return and 15.6%
		final double expectedPercentile10 = 106_041;
		final double expectedMedian = 248_564;

		final double mean = 9.4;
		final double sd = 15.6;
		for (int i = 0; i < 100; i++) {
			ForecastHolder forecastHolder = ForecasterUtil.calculateCustomProjections(mean, sd, 3.5, 20, 100_000);
			double proximity_Percentile10 = (expectedPercentile10 / forecastHolder.getPercentile10()) * 100;
			Assert.assertTrue("Percentile10 proximity should be >= than 80% in relation to expected. Found=" + proximity_Percentile10, proximity_Percentile10 >= 80);

			double proximity_Median = (expectedMedian / forecastHolder.getMedian()) * 100;
			Assert.assertTrue("Median proximity should be >= than 92% in relation to expected. Found=" + proximity_Median, proximity_Median >= 92);
		}
	}

}
