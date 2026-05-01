package lecho.lib.hellocharts.model;

import com.litesuits.http.data.Consts;
import java.util.Arrays;

/* loaded from: classes.dex */
public class PointValue {
    private float diffX;
    private float diffY;
    private char[] label;
    private float originX;
    private float originY;
    private float x;
    private float y;

    public PointValue() {
        set(0.0f, 0.0f);
    }

    public PointValue(float f, float f2) {
        set(f, f2);
    }

    public PointValue(PointValue pointValue) {
        set(pointValue.x, pointValue.y);
        this.label = pointValue.label;
    }

    public void update(float f) {
        this.x = this.originX + (this.diffX * f);
        this.y = this.originY + (this.diffY * f);
    }

    public void finish() {
        set(this.originX + this.diffX, this.originY + this.diffY);
    }

    public PointValue set(float f, float f2) {
        this.x = f;
        this.y = f2;
        this.originX = f;
        this.originY = f2;
        this.diffX = 0.0f;
        this.diffY = 0.0f;
        return this;
    }

    public PointValue setTarget(float f, float f2) {
        set(this.x, this.y);
        this.diffX = f - this.originX;
        this.diffY = f2 - this.originY;
        return this;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Deprecated
    public char[] getLabel() {
        return this.label;
    }

    public PointValue setLabel(String str) {
        this.label = str.toCharArray();
        return this;
    }

    public char[] getLabelAsChars() {
        return this.label;
    }

    @Deprecated
    public PointValue setLabel(char[] cArr) {
        this.label = cArr;
        return this;
    }

    public String toString() {
        return "PointValue [x=" + this.x + ", y=" + this.y + Consts.ARRAY_ECLOSING_RIGHT;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PointValue pointValue = (PointValue) obj;
        return Float.compare(pointValue.diffX, this.diffX) == 0 && Float.compare(pointValue.diffY, this.diffY) == 0 && Float.compare(pointValue.originX, this.originX) == 0 && Float.compare(pointValue.originY, this.originY) == 0 && Float.compare(pointValue.x, this.x) == 0 && Float.compare(pointValue.y, this.y) == 0 && Arrays.equals(this.label, pointValue.label);
    }

    public int hashCode() {
        return ((((((((((((this.x != 0.0f ? Float.floatToIntBits(this.x) : 0) * 31) + (this.y != 0.0f ? Float.floatToIntBits(this.y) : 0)) * 31) + (this.originX != 0.0f ? Float.floatToIntBits(this.originX) : 0)) * 31) + (this.originY != 0.0f ? Float.floatToIntBits(this.originY) : 0)) * 31) + (this.diffX != 0.0f ? Float.floatToIntBits(this.diffX) : 0)) * 31) + (this.diffY != 0.0f ? Float.floatToIntBits(this.diffY) : 0)) * 31) + (this.label != null ? Arrays.hashCode(this.label) : 0);
    }
}
