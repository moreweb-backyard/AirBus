package lecho.lib.hellocharts.formatter;

import lecho.lib.hellocharts.model.SubcolumnValue;

/* loaded from: classes.dex */
public class SimpleColumnChartValueFormatter implements ColumnChartValueFormatter {
    private ValueFormatterHelper valueFormatterHelper;

    public SimpleColumnChartValueFormatter() {
        this.valueFormatterHelper = new ValueFormatterHelper();
        this.valueFormatterHelper.determineDecimalSeparator();
    }

    public SimpleColumnChartValueFormatter(int i) {
        this();
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
    }

    @Override // lecho.lib.hellocharts.formatter.ColumnChartValueFormatter
    public int formatChartValue(char[] cArr, SubcolumnValue subcolumnValue) {
        return this.valueFormatterHelper.formatFloatValueWithPrependedAndAppendedText(cArr, subcolumnValue.getValue(), subcolumnValue.getLabelAsChars());
    }

    public int getDecimalDigitsNumber() {
        return this.valueFormatterHelper.getDecimalDigitsNumber();
    }

    public SimpleColumnChartValueFormatter setDecimalDigitsNumber(int i) {
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
        return this;
    }

    public char[] getAppendedText() {
        return this.valueFormatterHelper.getAppendedText();
    }

    public SimpleColumnChartValueFormatter setAppendedText(char[] cArr) {
        this.valueFormatterHelper.setAppendedText(cArr);
        return this;
    }

    public char[] getPrependedText() {
        return this.valueFormatterHelper.getPrependedText();
    }

    public SimpleColumnChartValueFormatter setPrependedText(char[] cArr) {
        this.valueFormatterHelper.setPrependedText(cArr);
        return this;
    }

    public char getDecimalSeparator() {
        return this.valueFormatterHelper.getDecimalSeparator();
    }

    public SimpleColumnChartValueFormatter setDecimalSeparator(char c) {
        this.valueFormatterHelper.setDecimalSeparator(c);
        return this;
    }
}
