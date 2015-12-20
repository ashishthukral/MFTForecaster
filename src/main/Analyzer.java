package main;

import model.ForecastHolder;
import util.HelperUtil;

public class Analyzer {

	public static void main(String[] args) {

		// base params
		final double inflationPercentage = 3.5;
		final int noOfYears = 20;
		final double baseAmount = 100_000;
		ForecastHolder forecastHolder_Conservative = ForecasterUtil.calculateConservativeProjections(inflationPercentage, noOfYears, baseAmount);
		printForecast(forecastHolder_Conservative);

		ForecastHolder forecastHolder_Aggressive = ForecasterUtil.calculateAggressiveProjections(inflationPercentage, noOfYears, baseAmount);
		printForecast(forecastHolder_Aggressive);

	}

	private static void printForecast(ForecastHolder forecastHolder) {

		System.out.println("\nAfter Adjusting Inflation\n");
		HelperUtil.print("10% percentile={0}", forecastHolder.getPercentile10());
		HelperUtil.print("Median={0}", forecastHolder.getMedian());
		HelperUtil.print("90% percentile={0}", forecastHolder.getPercentile90());
	}

}
