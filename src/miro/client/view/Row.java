package miro.client.view;

import com.google.gwt.user.client.ui.TextBox;

public class Row {
    private String title;
    private final int SIZE_OF_ARRAY = 14;
    protected TextBox[] arrayForARow;

    public Row() {
        arrayForARow = new TextBox[SIZE_OF_ARRAY];
        title = "";

        initArray();
    }

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

    public Object getElementAt(int column) {
        if (column < 0 || column >= arrayForARow.length) return "";

        return arrayForARow[column];
    }

    public boolean setElementAt(int index,String value){
        boolean isModified = index > 0 && index < arrayForARow.length;

        if(isModified) arrayForARow[index].setText(value);

        return isModified;
    }

    public int length() {
        return arrayForARow.length;
    }

    public void setTitle(String title){
        this.title = title;
        arrayForARow[0].setText(title);
    }

    public String getTitle(){
        return title;
    }
}
