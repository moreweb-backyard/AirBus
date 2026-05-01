package lecho.lib.hellocharts.formatter;

import lecho.lib.hellocharts.model.SliceValue;

/* loaded from: classes.dex */
public class SimplePieChartValueFormatter implements PieChartValueFormatter {
    private ValueFormatterHelper valueFormatterHelper;

    public SimplePieChartValueFormatter() {
        this.valueFormatterHelper = new ValueFormatterHelper();
        this.valueFormatterHelper.determineDecimalSeparator();
    }

    public SimplePieChartValueFormatter(int i) {
        this();
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
    }

    @Override // lecho.lib.hellocharts.formatter.PieChartValueFormatter
    public int formatChartValue(char[] cArr, SliceValue sliceValue) {
        return this.valueFormatterHelper.formatFloatValueWithPrependedAndAppendedText(cArr, sliceValue.getValue(), sliceValue.getLabelAsChars());
    }

    public int getDecimalDigitsNumber() {
        return this.valueFormatterHelper.getDecimalDigitsNumber();
    }

    public SimplePieChartValueFormatter setDecimalDigitsNumber(int i) {
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
        return this;
    }

    public char[] getAppendedText() {
        return this.valueFormatterHelper.getAppendedText();
    }

    public SimplePieChartValueFormatter setAppendedText(char[] cArr) {
        this.valueFormatterHelper.setAppendedText(cArr);
        return this;
    }

    public char[] getPrependedText() {
        return this.valueFormatterHelper.getPrependedText();
    }

    public SimplePieChartValueFormatter setPrependedText(char[] cArr) {
        this.valueFormatterHelper.setPrependedText(cArr);
        return this;
    }

    public char getDecimalSeparator() {
        return this.valueFormatterHelper.getDecimalSeparator();
    }

    public SimplePieChartValueFormatter setDecimalSeparator(char c) {
        this.valueFormatterHelper.setDecimalSeparator(c);
        return this;
    }
}
