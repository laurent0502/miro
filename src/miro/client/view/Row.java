package miro.client.view;

import com.google.gwt.user.client.ui.TextBox;

/**
 * This class represents a row
 */
public class Row {
	private String title;
	private final int SIZE_OF_ARRAY = 14;
	protected TextBox[] arrayForARow;

	/**
	 * Defines a Row
	 */
	public Row() {
		arrayForARow = new TextBox[SIZE_OF_ARRAY];
		title = "";

		initArray();
	}

	/**
	 * Defines a row with the title specified
	 * 
	 * @param titleOfRow
	 *            Title of the row
	 */
	public Row(String titleOfRow) {
		arrayForARow = new TextBox[SIZE_OF_ARRAY];
		title = titleOfRow;

		initArray();
	}

	private void initArray() {

		for (int i = 0; i < arrayForARow.length; i++) {
			arrayForARow[i] = new TextBox();
		}

		arrayForARow[0].setText(title);
	}

	/**
	 * Returns the element of a column specified
	 * 
	 * @return The element
	 */
	public Object getElementAt(int column) {
		if (column < 0 || column >= arrayForARow.length)
			return null;

		return arrayForARow[column];
	}

	/**
	 * Changes the value of a column specified
	 * 
	 * @param column
	 *            The column
	 * @param value
	 *            The value
	 * @return true if the value has been changed
	 */
	public boolean setElementAt(int column, String value) {
		boolean isModified = column > 0 && column < arrayForARow.length;

		if (isModified)
			arrayForARow[column].setText(value);

		return isModified;
	}

	/**
	 * Returns the length of the row
	 * 
	 * @return The length
	 */
	public int length() {
		return arrayForARow.length;
	}

	/**
	 * Changes the title of the row
	 * 
	 * @param newTitle
	 *            New title of the row
	 */
	public void setTitle(String newTitle) {
		this.title = newTitle;
		arrayForARow[0].setText(newTitle);
	}

	/**
	 * Returns the title of the row
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}
}
