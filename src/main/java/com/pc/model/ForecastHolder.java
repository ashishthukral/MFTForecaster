package com.pc.model;

public class ForecastHolder {

	private double percentile10;

	private double median;

	private double percentile90;

	public ForecastHolder(double percentile10, double median, double percentile90) {
		this.percentile10 = percentile10;
		this.median = median;
		this.percentile90 = percentile90;
	}

	public double getPercentile10() {

		return percentile10;
	}

	public void setPercentile10(double percentile10) {

		this.percentile10 = percentile10;
	}

	public double getMedian() {

		return median;
	}

	public void setMedian(double median) {

		this.median = median;
	}

	public double getPercentile90() {

		return percentile90;
	}

	public void setPercentile90(double percentile90) {

		this.percentile90 = percentile90;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("ForecastHolder [percentile10=");
		builder.append(percentile10);
		builder.append(", median=");
		builder.append(median);
		builder.append(", percentile90=");
		builder.append(percentile90);
		builder.append("]");
		return builder.toString();
	}

}
