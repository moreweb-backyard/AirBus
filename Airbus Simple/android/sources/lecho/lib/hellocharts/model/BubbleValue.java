package lecho.lib.hellocharts.model;

import com.litesuits.http.data.Consts;
import java.util.Arrays;
import lecho.lib.hellocharts.util.ChartUtils;

/* loaded from: classes.dex */
public class BubbleValue {
    private float diffX;
    private float diffY;
    private float diffZ;
    private char[] label;
    private float originX;
    private float originY;
    private float originZ;
    private float x;
    private float y;
    private float z;
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private ValueShape shape = ValueShape.CIRCLE;

    public BubbleValue() {
        set(0.0f, 0.0f, 0.0f);
    }

    public BubbleValue(float f, float f2, float f3) {
        set(f, f2, f3);
    }

    public BubbleValue(float f, float f2, float f3, int i) {
        set(f, f2, f3);
        setColor(i);
    }

    public BubbleValue(BubbleValue bubbleValue) {
        set(bubbleValue.x, bubbleValue.y, bubbleValue.z);
        setColor(bubbleValue.color);
        this.label = bubbleValue.label;
    }

    public void update(float f) {
        this.x = this.originX + (this.diffX * f);
        this.y = this.originY + (this.diffY * f);
        this.z = this.originZ + (this.diffZ * f);
    }

    public void finish() {
        set(this.originX + this.diffX, this.originY + this.diffY, this.originZ + this.diffZ);
    }

    public BubbleValue set(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
        this.originX = f;
        this.originY = f2;
        this.originZ = f3;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        this.diffZ = 0.0f;
        return this;
    }

    public BubbleValue setTarget(float f, float f2, float f3) {
        set(this.x, this.y, this.z);
        this.diffX = f - this.originX;
        this.diffY = f2 - this.originY;
        this.diffZ = f3 - this.originZ;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public int getColor() {
        return this.color;
    }

    public BubbleValue setColor(int i) {
        this.color = i;
        this.darkenColor = ChartUtils.darkenColor(i);
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    public ValueShape getShape() {
        return this.shape;
    }

    public BubbleValue setShape(ValueShape valueShape) {
        this.shape = valueShape;
        return this;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public BubbleValue setLabel(String str) {
        this.label = str.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public BubbleValue setLabel(char[] cArr) {
        this.label = cArr;
        return this;
    }

    public String toString() {
        return "BubbleValue [x=" + this.x + ", y=" + this.y + ", z=" + this.z + Consts.ARRAY_ECLOSING_RIGHT;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        BubbleValue bubbleValue = (BubbleValue) obj;
        return this.color == bubbleValue.color && this.darkenColor == bubbleValue.darkenColor && Float.compare(bubbleValue.diffX, this.diffX) == 0 && Float.compare(bubbleValue.diffY, this.diffY) == 0 && Float.compare(bubbleValue.diffZ, this.diffZ) == 0 && Float.compare(bubbleValue.originX, this.originX) == 0 && Float.compare(bubbleValue.originY, this.originY) == 0 && Float.compare(bubbleValue.originZ, this.originZ) == 0 && Float.compare(bubbleValue.x, this.x) == 0 && Float.compare(bubbleValue.y, this.y) == 0 && Float.compare(bubbleValue.z, this.z) == 0 && Arrays.equals(this.label, bubbleValue.label) && this.shape == bubbleValue.shape;
    }

    public int hashCode() {
        return ((((((((((((((((((((((((this.x != 0.0f ? Float.floatToIntBits(this.x) : 0) * 31) + (this.y != 0.0f ? Float.floatToIntBits(this.y) : 0)) * 31) + (this.z != 0.0f ? Float.floatToIntBits(this.z) : 0)) * 31) + (this.originX != 0.0f ? Float.floatToIntBits(this.originX) : 0)) * 31) + (this.originY != 0.0f ? Float.floatToIntBits(this.originY) : 0)) * 31) + (this.originZ != 0.0f ? Float.floatToIntBits(this.originZ) : 0)) * 31) + (this.diffX != 0.0f ? Float.floatToIntBits(this.diffX) : 0)) * 31) + (this.diffY != 0.0f ? Float.floatToIntBits(this.diffY) : 0)) * 31) + (this.diffZ != 0.0f ? Float.floatToIntBits(this.diffZ) : 0)) * 31) + this.color) * 31) + this.darkenColor) * 31) + (this.shape != null ? this.shape.hashCode() : 0)) * 31) + (this.label != null ? Arrays.hashCode(this.label) : 0);
    }
}
