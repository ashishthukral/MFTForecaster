package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForecasterUtil {

	private static final int NO_OF_ITERATIONS = 10_000;

	// Conservative Return
	private static final double MEAN_CONSERVATIVE = 6.189;

	// Conservative Risk
	private static final double SD_CONSERVATIVE = 6.3438;

	// Aggressive Return
	private static final double MEAN_AGGRESSIVE = 9.4324;

	// Aggressive Risk
	private static final double SD_AGGRESSIVE = 15.675;

	/**
	 * Uses {@link #calculateProjections(double, double, double, int, double)}
	 * 
	 * @param inflationPercentage
	 * @param noOfYears
	 * @param baseAmount
	 * @return ForecastHolder
	 */
	public static ForecastHolder calculateConservativeProjections(final double inflationPercentage, final int noOfYears, final double baseAmount) {

		HelperUtil.print("*** Calculating Conservative Profile *** Mean={0} , Standard Deviation={1} , Inflation Percentage={2} , No Of Years={3} , Base Amount={4}", MEAN_CONSERVATIVE,
				SD_CONSERVATIVE, inflationPercentage, noOfYears, baseAmount);
		ForecastHolder forecastHolder = calculateProjections(MEAN_CONSERVATIVE, SD_CONSERVATIVE, inflationPercentage, noOfYears, baseAmount);
		return forecastHolder;
	}

	/**
	 * Uses {@link #calculateProjections(double, double, double, int, double)}
	 * 
	 * @param inflationPercentage
	 * @param noOfYears
	 * @param baseAmount
	 * @return ForecastHolder
	 */
	public static ForecastHolder calculateAggressiveProjections(final double inflationPercentage, final int noOfYears, final double baseAmount) {

		HelperUtil.print("\n*** Calculating Aggressive Profile *** Mean={0} , Standard Deviation={1} , Inflation Percentage={2} , No Of Years={3} , Base Amount={4}", MEAN_AGGRESSIVE, SD_AGGRESSIVE,
				inflationPercentage, noOfYears, baseAmount);
		ForecastHolder forecastHolder = calculateProjections(MEAN_AGGRESSIVE, SD_AGGRESSIVE, inflationPercentage, noOfYears, baseAmount);
		return forecastHolder;
	}

	/**
	 * Uses {@link #calculateProjections(double, double, double, int, double)}
	 * 
	 * @param mean
	 * @param sd
	 * @param inflationPercentage
	 * @param noOfYears
	 * @param baseAmount
	 * @return ForecastHolder
	 */
	public static ForecastHolder calculateCustomProjections(final double mean, final double sd, final double inflationPercentage, final int noOfYears, final double baseAmount) {

		// HelperUtil.print("\n*** Calculating Custom Profile *** Mean={0} , Standard Deviation={1} , Inflation Percentage={2} , No Of
		// Years={3} , Base Amount={4}", mean, sd, inflationPercentage,
		// noOfYears, baseAmount);
		ForecastHolder forecastHolder = calculateProjections(mean, sd, inflationPercentage, noOfYears, baseAmount);
		return forecastHolder;
	}

	/**
	 * Run iterations for projecting returns. Returns ForecastHolder containing Inflation Adjusted return forecasts.
	 * 
	 * @param mean
	 * @param sd
	 * @param inflationPercentage
	 * @param noOfYears
	 * @param baseAmount
	 * @return ForecastHolder
	 */
	private static ForecastHolder calculateProjections(final double mean, final double sd, final double inflationPercentage, final int noOfYears, final double baseAmount) {

		final List<Double> results = new ArrayList<>(NO_OF_ITERATIONS);

		/*
		 * NOTE - Using serial operations here as earlier using ExecutorService lead to higher running times probably due to more overheard
		 * of maintaining threads than actual effort of projecting returns.
		 * For thread pool size of even 100, time taken was around 5 times more than a simple for loop.
		 */
		for (int i = 0; i < NO_OF_ITERATIONS; i++) {
			results.add(project(mean, sd, noOfYears, baseAmount));
		}
		// Sort to get percentiles, median. O(n log n) complexity.
		Collections.sort(results);

		final Double percentile10 = results.get(HelperUtil.calculatePercentileIndex(10, results.size()));
		final double median = HelperUtil.calculateMedian(results);
		final Double percentile90 = results.get(HelperUtil.calculatePercentileIndex(90, results.size()));

		// HelperUtil.print("10% percentile={0}", HelperUtil.round(percentile10));
		// HelperUtil.print("Median={0}", HelperUtil.round(median));
		// HelperUtil.print("90% percentile={0}", HelperUtil.round(percentile90));

		final Double percentile10_inflationAdjusted = HelperUtil.round(HelperUtil.adjustBackwardInflation(inflationPercentage, noOfYears, percentile10));
		final double median_inflationAdjusted = HelperUtil.round(HelperUtil.adjustBackwardInflation(inflationPercentage, noOfYears, median));
		final Double percentile90_inflationAdjusted = HelperUtil.round(HelperUtil.adjustBackwardInflation(inflationPercentage, noOfYears, percentile90));

		final ForecastHolder forecastHolder = new ForecastHolder(percentile10_inflationAdjusted, median_inflationAdjusted, percentile90_inflationAdjusted);

		return forecastHolder;
	}

	/**
	 * Project for {@link #NO_OF_YEARS} starting from the {@link #BASE_AMOUNT}. Adjusts mean every year by a random SD value and returns the
	 * Expected Return at end of term.
	 *
	 * @param mean
	 * @param sd
	 * @return double
	 */
	private static double project(final double mean, final double sd, final int noOfYears, final double baseAmount) {

		double amount = baseAmount;

		for (short i = 0; i < noOfYears; i++) {
			final double randomSD = HelperUtil.generateRandomSD(sd);
			final double randomMean = mean + randomSD;
			amount *= ((100 + randomMean) / 100);
		}
		return amount;
	}

}
