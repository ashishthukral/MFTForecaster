package test;

import org.junit.Test;

import main.ForecasterUtil;
import model.ForecastHolder;

public class ForecasterUtilTest {

	@Test
	public void test() {

		// 7.4% annual return and 8.0% 127,300 ,,, 197,502
		final double expectedPercentile10 = 127_300;
		final double expectedMedian = 197_502;

		final double mean = 7.4;
		final double sd = 8.0;
		ForecastHolder forecastHolder = ForecasterUtil.calculateCustomProjections(mean, sd, 3.5, 20, 100_000);
		double error_Percentile10 = Math.abs((1.0 - (forecastHolder.getPercentile10() / expectedPercentile10))) * 100;
		System.out.println(error_Percentile10);

		double error_Median = Math.abs((1.0 - (forecastHolder.getMedian() / expectedMedian))) * 100;
		System.out.println(error_Median);

	}

}
