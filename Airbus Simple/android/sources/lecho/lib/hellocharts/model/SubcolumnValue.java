package lecho.lib.hellocharts.model;

import com.litesuits.http.data.Consts;
import java.util.Arrays;
import lecho.lib.hellocharts.util.ChartUtils;

/* loaded from: classes.dex */
public class SubcolumnValue {
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private float diff;
    private char[] label;
    private float originValue;
    private float value;

    public SubcolumnValue() {
        setValue(0.0f);
    }

    public SubcolumnValue(float f) {
        setValue(f);
    }

    public SubcolumnValue(float f, int i) {
        setValue(f);
        setColor(i);
    }

    public SubcolumnValue(SubcolumnValue subcolumnValue) {
        setValue(subcolumnValue.value);
        setColor(subcolumnValue.color);
        this.label = subcolumnValue.label;
    }

    public void update(float f) {
        this.value = this.originValue + (this.diff * f);
    }

    public void finish() {
        setValue(this.originValue + this.diff);
    }

    public float getValue() {
        return this.value;
    }

    public SubcolumnValue setValue(float f) {
        this.value = f;
        this.originValue = f;
        this.diff = 0.0f;
        return this;
    }

    public SubcolumnValue setTarget(float f) {
        setValue(this.value);
        this.diff = f - this.originValue;
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public SubcolumnValue setColor(int i) {
        this.color = i;
        this.darkenColor = ChartUtils.darkenColor(i);
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public SubcolumnValue setLabel(String str) {
        this.label = str.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public SubcolumnValue setLabel(char[] cArr) {
        this.label = cArr;
        return this;
    }

    public String toString() {
        return "ColumnValue [value=" + this.value + Consts.ARRAY_ECLOSING_RIGHT;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SubcolumnValue subcolumnValue = (SubcolumnValue) obj;
        return this.color == subcolumnValue.color && this.darkenColor == subcolumnValue.darkenColor && Float.compare(subcolumnValue.diff, this.diff) == 0 && Float.compare(subcolumnValue.originValue, this.originValue) == 0 && Float.compare(subcolumnValue.value, this.value) == 0 && Arrays.equals(this.label, subcolumnValue.label);
    }

    public int hashCode() {
        return ((((((((((this.value != 0.0f ? Float.floatToIntBits(this.value) : 0) * 31) + (this.originValue != 0.0f ? Float.floatToIntBits(this.originValue) : 0)) * 31) + (this.diff != 0.0f ? Float.floatToIntBits(this.diff) : 0)) * 31) + this.color) * 31) + this.darkenColor) * 31) + (this.label != null ? Arrays.hashCode(this.label) : 0);
    }
}
