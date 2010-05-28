package miro.client.view;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * This class represents a row with calculate column value
 */
public class CalcRow extends Row {
	private final NumberFormat NUMBER_FORMAT_IN_POURCENT = NumberFormat
			.getFormat("0.0");
	private boolean printPercentageSymbol;

	/**
	 * Defines a CalcRow
	 * 
	 * @param title
	 *            Title of the row
	 * @param printPercentageSymbol
	 *            The boolean indicates whether the values of the columns are
	 *            percentages
	 */
	public CalcRow(String title, boolean printPercentageSymbol) {
		super(title);

		this.printPercentageSymbol = printPercentageSymbol;

		initArray();
		disabledCells();
	}

	private void initArray() {

		for (int i = 1; i < arrayForARow.length; i++) {
			setElementAt(i, 0);
		}
	}

	private void disabledCells() {
		arrayForARow[0].setEnabled(false);

		for (int i = 1; i < arrayForARow.length; i++) {
			arrayForARow[i].setReadOnly(true);
		}
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
		String stringFormatted = NUMBER_FORMAT_IN_POURCENT.format(value);

		if (isModified) {
			if (printPercentageSymbol)
				stringFormatted += "%";

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
			txtNumberOfACell = txtNumberOfACell.substring(0, txtNumberOfACell
					.length() - 1);

			double valueOfTheCell = Double.valueOf(txtNumberOfACell);
			sumOfRow += valueOfTheCell;
		}

		if (printPercentageSymbol)
			sumOfRow /= 12;

		return sumOfRow;
	}
}