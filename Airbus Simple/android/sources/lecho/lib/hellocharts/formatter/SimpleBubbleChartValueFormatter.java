package lecho.lib.hellocharts.formatter;

import lecho.lib.hellocharts.model.BubbleValue;

/* loaded from: classes.dex */
public class SimpleBubbleChartValueFormatter implements BubbleChartValueFormatter {
    private ValueFormatterHelper valueFormatterHelper;

    public SimpleBubbleChartValueFormatter() {
        this.valueFormatterHelper = new ValueFormatterHelper();
        this.valueFormatterHelper.determineDecimalSeparator();
    }

    public SimpleBubbleChartValueFormatter(int i) {
        this();
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
    }

    @Override // lecho.lib.hellocharts.formatter.BubbleChartValueFormatter
    public int formatChartValue(char[] cArr, BubbleValue bubbleValue) {
        return this.valueFormatterHelper.formatFloatValueWithPrependedAndAppendedText(cArr, bubbleValue.getZ(), bubbleValue.getLabelAsChars());
    }

    public int getDecimalDigitsNumber() {
        return this.valueFormatterHelper.getDecimalDigitsNumber();
    }

    public SimpleBubbleChartValueFormatter setDecimalDigitsNumber(int i) {
        this.valueFormatterHelper.setDecimalDigitsNumber(i);
        return this;
    }

    public char[] getAppendedText() {
        return this.valueFormatterHelper.getAppendedText();
    }

    public SimpleBubbleChartValueFormatter setAppendedText(char[] cArr) {
        this.valueFormatterHelper.setAppendedText(cArr);
        return this;
    }

    public char[] getPrependedText() {
        return this.valueFormatterHelper.getPrependedText();
    }

    public SimpleBubbleChartValueFormatter setPrependedText(char[] cArr) {
        this.valueFormatterHelper.setPrependedText(cArr);
        return this;
    }

    public char getDecimalSeparator() {
        return this.valueFormatterHelper.getDecimalSeparator();
    }

    public SimpleBubbleChartValueFormatter setDecimalSeparator(char c) {
        this.valueFormatterHelper.setDecimalSeparator(c);
        return this;
    }
}
