package lecho.lib.hellocharts.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.litesuits.http.data.Consts;

/* loaded from: classes.dex */
public class Viewport implements Parcelable {
    public static final Parcelable.Creator<Viewport> CREATOR = new Parcelable.Creator<Viewport>() { // from class: lecho.lib.hellocharts.model.Viewport.1
        @Override // android.os.Parcelable.Creator
        public Viewport createFromParcel(Parcel parcel) {
            Viewport viewport = new Viewport();
            viewport.readFromParcel(parcel);
            return viewport;
        }

        @Override // android.os.Parcelable.Creator
        public Viewport[] newArray(int i) {
            return new Viewport[i];
        }
    };
    public float bottom;
    public float left;
    public float right;
    public float top;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Viewport() {
    }

    public Viewport(float f, float f2, float f3, float f4) {
        this.left = f;
        this.top = f2;
        this.right = f3;
        this.bottom = f4;
    }

    public Viewport(Viewport viewport) {
        if (viewport == null) {
            this.bottom = 0.0f;
            this.right = 0.0f;
            this.top = 0.0f;
            this.left = 0.0f;
            return;
        }
        this.left = viewport.left;
        this.top = viewport.top;
        this.right = viewport.right;
        this.bottom = viewport.bottom;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Viewport viewport = (Viewport) obj;
        return Float.floatToIntBits(this.bottom) == Float.floatToIntBits(viewport.bottom) && Float.floatToIntBits(this.left) == Float.floatToIntBits(viewport.left) && Float.floatToIntBits(this.right) == Float.floatToIntBits(viewport.right) && Float.floatToIntBits(this.top) == Float.floatToIntBits(viewport.top);
    }

    public final boolean isEmpty() {
        return this.left >= this.right || this.bottom >= this.top;
    }

    public void setEmpty() {
        this.bottom = 0.0f;
        this.top = 0.0f;
        this.right = 0.0f;
        this.left = 0.0f;
    }

    public final float width() {
        return this.right - this.left;
    }

    public final float height() {
        return this.top - this.bottom;
    }

    public final float centerX() {
        return (this.left + this.right) * 0.5f;
    }

    public final float centerY() {
        return (this.top + this.bottom) * 0.5f;
    }

    public void set(float f, float f2, float f3, float f4) {
        this.left = f;
        this.top = f2;
        this.right = f3;
        this.bottom = f4;
    }

    public void set(Viewport viewport) {
        this.left = viewport.left;
        this.top = viewport.top;
        this.right = viewport.right;
        this.bottom = viewport.bottom;
    }

    public void offset(float f, float f2) {
        this.left += f;
        this.top += f2;
        this.right += f;
        this.bottom += f2;
    }

    public void offsetTo(float f, float f2) {
        this.right += f - this.left;
        this.bottom += f2 - this.top;
        this.left = f;
        this.top = f2;
    }

    public void inset(float f, float f2) {
        this.left += f;
        this.top -= f2;
        this.right -= f;
        this.bottom += f2;
    }

    public boolean contains(float f, float f2) {
        return this.left < this.right && this.bottom < this.top && f >= this.left && f < this.right && f2 >= this.bottom && f2 < this.top;
    }

    public boolean contains(float f, float f2, float f3, float f4) {
        return this.left < this.right && this.bottom < this.top && this.left <= f && this.top >= f2 && this.right >= f3 && this.bottom <= f4;
    }

    public boolean contains(Viewport viewport) {
        return this.left < this.right && this.bottom < this.top && this.left <= viewport.left && this.top >= viewport.top && this.right >= viewport.right && this.bottom <= viewport.bottom;
    }

    public void union(float f, float f2, float f3, float f4) {
        if (f >= f3 || f4 >= f2) {
            return;
        }
        if (this.left >= this.right || this.bottom >= this.top) {
            this.left = f;
            this.top = f2;
            this.right = f3;
            this.bottom = f4;
            return;
        }
        if (this.left > f) {
            this.left = f;
        }
        if (this.top < f2) {
            this.top = f2;
        }
        if (this.right < f3) {
            this.right = f3;
        }
        if (this.bottom > f4) {
            this.bottom = f4;
        }
    }

    public void union(Viewport viewport) {
        union(viewport.left, viewport.top, viewport.right, viewport.bottom);
    }

    public boolean intersect(float f, float f2, float f3, float f4) {
        if (this.left >= f3 || f >= this.right || this.bottom >= f2 || f4 >= this.top) {
            return false;
        }
        if (this.left < f) {
            this.left = f;
        }
        if (this.top > f2) {
            this.top = f2;
        }
        if (this.right > f3) {
            this.right = f3;
        }
        if (this.bottom >= f4) {
            return true;
        }
        this.bottom = f4;
        return true;
    }

    public boolean intersect(Viewport viewport) {
        return intersect(viewport.left, viewport.top, viewport.right, viewport.bottom);
    }

    public String toString() {
        return "Viewport [left=" + this.left + ", top=" + this.top + ", right=" + this.right + ", bottom=" + this.bottom + Consts.ARRAY_ECLOSING_RIGHT;
    }

    public int hashCode() {
        return ((((((Float.floatToIntBits(this.bottom) + 31) * 31) + Float.floatToIntBits(this.left)) * 31) + Float.floatToIntBits(this.right)) * 31) + Float.floatToIntBits(this.top);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.left);
        parcel.writeFloat(this.top);
        parcel.writeFloat(this.right);
        parcel.writeFloat(this.bottom);
    }

    public void readFromParcel(Parcel parcel) {
        this.left = parcel.readFloat();
        this.top = parcel.readFloat();
        this.right = parcel.readFloat();
        this.bottom = parcel.readFloat();
    }
}
