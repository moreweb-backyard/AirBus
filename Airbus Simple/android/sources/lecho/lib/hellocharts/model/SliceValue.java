package lecho.lib.hellocharts.model;

import com.litesuits.http.data.Consts;
import java.util.Arrays;
import lecho.lib.hellocharts.util.ChartUtils;

/* loaded from: classes.dex */
public class SliceValue {
    private static final int DEFAULT_SLICE_SPACING_DP = 2;
    private int color;
    private int darkenColor;
    private float diff;
    private char[] label;
    private float originValue;

    @Deprecated
    private int sliceSpacing;
    private float value;

    public SliceValue() {
        this.sliceSpacing = 2;
        this.color = ChartUtils.DEFAULT_COLOR;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        setValue(0.0f);
    }

    public SliceValue(float f) {
        this.sliceSpacing = 2;
        this.color = ChartUtils.DEFAULT_COLOR;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        setValue(f);
    }

    public SliceValue(float f, int i) {
        this.sliceSpacing = 2;
        this.color = ChartUtils.DEFAULT_COLOR;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        setValue(f);
        setColor(i);
    }

    public SliceValue(float f, int i, int i2) {
        this.sliceSpacing = 2;
        this.color = ChartUtils.DEFAULT_COLOR;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        setValue(f);
        setColor(i);
        this.sliceSpacing = i2;
    }

    public SliceValue(SliceValue sliceValue) {
        this.sliceSpacing = 2;
        this.color = ChartUtils.DEFAULT_COLOR;
        this.darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
        setValue(sliceValue.value);
        setColor(sliceValue.color);
        this.sliceSpacing = sliceValue.sliceSpacing;
        this.label = sliceValue.label;
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

    public SliceValue setValue(float f) {
        this.value = f;
        this.originValue = f;
        this.diff = 0.0f;
        return this;
    }

    public SliceValue setTarget(float f) {
        setValue(this.value);
        this.diff = f - this.originValue;
        return this;
    }

    public int getColor() {
        return this.color;
    }

    public SliceValue setColor(int i) {
        this.color = i;
        this.darkenColor = ChartUtils.darkenColor(i);
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    @Deprecated
    public int getSliceSpacing() {
        return this.sliceSpacing;
    }

    @Deprecated
    public SliceValue setSliceSpacing(int i) {
        this.sliceSpacing = i;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    @Deprecated
    public SliceValue setLabel(char[] cArr) {
        this.label = cArr;
        return this;
    }

    public SliceValue setLabel(String str) {
        this.label = str.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    public String toString() {
        return "SliceValue [value=" + this.value + Consts.ARRAY_ECLOSING_RIGHT;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SliceValue sliceValue = (SliceValue) obj;
        return this.color == sliceValue.color && this.darkenColor == sliceValue.darkenColor && Float.compare(sliceValue.diff, this.diff) == 0 && Float.compare(sliceValue.originValue, this.originValue) == 0 && this.sliceSpacing == sliceValue.sliceSpacing && Float.compare(sliceValue.value, this.value) == 0 && Arrays.equals(this.label, sliceValue.label);
    }

    public int hashCode() {
        return ((((((((((((this.value != 0.0f ? Float.floatToIntBits(this.value) : 0) * 31) + (this.originValue != 0.0f ? Float.floatToIntBits(this.originValue) : 0)) * 31) + (this.diff != 0.0f ? Float.floatToIntBits(this.diff) : 0)) * 31) + this.color) * 31) + this.darkenColor) * 31) + this.sliceSpacing) * 31) + (this.label != null ? Arrays.hashCode(this.label) : 0);
    }
}
