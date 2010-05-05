package miro.client.view;

import com.google.gwt.i18n.client.NumberFormat;

public class CalcRow extends Row {
    private final NumberFormat NUMBER_FORMAT_IN_POURCENT = NumberFormat.getFormat("000.00");
    private final NumberFormat NUMBER_FORMAT_IN_OTHER = NumberFormat.getFormat("00.00");

    private boolean printPourcentageSymbol;

    public CalcRow(String title, boolean printPourcentageSymbol) {
        super(title);

        this.printPourcentageSymbol = printPourcentageSymbol;

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

    public boolean setElementAt(int index, double value) {
        boolean isModified = index > 0 && index < arrayForARow.length;
        String stringFormatted;

        if (isModified) {

            if (printPourcentageSymbol) {
                stringFormatted = NUMBER_FORMAT_IN_POURCENT.format(value);
                stringFormatted += "%";
            } else {
                stringFormatted = NUMBER_FORMAT_IN_OTHER.format(value);
            }

            arrayForARow[index].setText(stringFormatted);
        }
        return isModified;
    }

    public double sumRow() {
        double sumOfRow = 0;

        for (int i = 2; i < arrayForARow.length; i++) {
            String txtNumberOfACell = arrayForARow[i].getText();
            txtNumberOfACell = txtNumberOfACell.substring(0, txtNumberOfACell.length() - 1);

            double valueOfTheCell = Double.valueOf(txtNumberOfACell);
            sumOfRow += valueOfTheCell;
        }

        if (printPourcentageSymbol) sumOfRow /= 12;

        return sumOfRow;
    }
}