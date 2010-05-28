package miro.client.view;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * This class represents a row containing the project benefits
 */
public class ProjectRow extends Row {
	private final NumberFormat NUMBER_FORMAT_FOR_PROJECT = NumberFormat
			.getFormat("0.##");

	/**
	 * Defines a ProjectRow with its title specified
	 * 
	 * @param title
	 *            Title of the row
	 */
	public ProjectRow(String title) {
		super(title);

		initArray();
		disabledCells();
	}

	private void initArray() {
		for (int i = 1; i < arrayForARow.length; i++) {
			setElementAt(i, 0);
		}
	}

	private void disabledCells() {
		arrayForARow[0].setEnabled(true);
		arrayForARow[1].setReadOnly(true);
	}

	/**
	 * Changes a value of a column
	 * 
	 * @param column
	 *            The column
	 * @param value
	 *            The value
	 * @return true if the value has been changed
	 */
	public boolean setElementAt(int column, double value) {
		boolean isModified = column > 0 && column < arrayForARow.length;
		String stringFormatted = NUMBER_FORMAT_FOR_PROJECT.format(value);

		if (isModified) {
			arrayForARow[column].setText(stringFormatted);
		}

		return isModified;
	}

	/**
	 * Returns the values sum of the row
	 * 
	 * @return The sum
	 */
	public double sumRow() {
		double sumOfRow = 0;

		for (int i = 2; i < arrayForARow.length; i++) {
			String txtNumberOfACell = arrayForARow[i].getText();
			double valueOfACell = Double.valueOf(txtNumberOfACell);
			sumOfRow += valueOfACell;
		}
		String stringFormatted = NUMBER_FORMAT_FOR_PROJECT.format(sumOfRow);

		return Double.valueOf(stringFormatted);
	}
}
