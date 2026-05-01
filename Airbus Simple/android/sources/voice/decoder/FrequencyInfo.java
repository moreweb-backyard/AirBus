package voice.decoder;

import android.support.v7.widget.ActivityChooserView;
import java.util.Set;

/* loaded from: classes.dex */
class FrequencyInfo implements Comparable<FrequencyInfo> {
    int frequencyY;
    boolean isNested = false;
    int nestedBy = -1;
    int nestReason = 0;
    int preciseFreqCount = 0;
    float avgMatchDistance = -1.0f;
    int shortFreqCount = 0;
    int prePeakType = 1;
    int preOrderType = 1;
    int idx = 0;
    int bigMatchCount = 0;
    int orderMatchCount = 0;
    MyArrayList<FrequencyMatch> times = new MyArrayList<>();
    MyArrayList<FrequencyMatch> realTimes = this.times;
    int maxAmplitudeIdx = -1;
    int maxRelativeIdx = -1;
    boolean preDowning = false;
    int endReason = 0;

    FrequencyInfo(int i, long j, FreqAmplitude freqAmplitude) {
        this.frequencyY = i;
        addTime(j, freqAmplitude);
    }

    FrequencyInfo(int i) {
        this.frequencyY = i;
    }

    @Override // java.lang.Comparable
    public int compareTo(FrequencyInfo frequencyInfo) {
        return this.frequencyY - frequencyInfo.frequencyY;
    }

    public long howLongTime() {
        return (this.times.last().timeIdx - this.times.first().timeIdx) + 1;
    }

    public long realHowLongTime() {
        return (this.realTimes.last().timeIdx - this.realTimes.first().timeIdx) + 1;
    }

    public float avgMatchDistance() {
        float fAbs = 0.0f;
        if (this.avgMatchDistance >= 0.0f) {
            return this.avgMatchDistance;
        }
        for (int i = 0; i < this.times.size(); i++) {
            fAbs += Math.abs(this.frequencyY - this.times.get(i).frequency.frequencyY);
        }
        float size = fAbs / this.times.size();
        this.avgMatchDistance = size;
        return size;
    }

    public boolean checkFreq(long j, Set<FreqAmplitude> set) {
        int i = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        FreqAmplitude freqAmplitude = null;
        for (FreqAmplitude freqAmplitude2 : set) {
            int iAbs = Math.abs(this.frequencyY - freqAmplitude2.frequencyY);
            if (iAbs <= 5 && iAbs < i) {
                freqAmplitude = freqAmplitude2;
                i = iAbs;
            }
        }
        if (freqAmplitude != null) {
            if (this.times.size() > 0 && Math.abs(this.times.last().frequency.frequencyY - freqAmplitude.frequencyY) > 5) {
                this.endReason = 8;
                return false;
            }
            boolean z = this.times.size() >= 2 && this.times.get(this.times.size() - 2).frequency.amplitude > this.times.get(this.times.size() - 1).frequency.amplitude && (this.times.get(this.times.size() - 1).frequency.amplitude > freqAmplitude.amplitude || this.times.get(this.times.size() - 2).frequency.amplitude > freqAmplitude.amplitude);
            if (z && this.orderMatchCount == 0 && this.times.size() == 2 && freqAmplitude.peakType == 2 && this.times.last().frequency.peakType == 2 && this.times.first().frequency.peakType == 2) {
                this.endReason = 2;
                return false;
            }
            if (freqAmplitude.peakType == 2 && this.prePeakType == 2) {
                if (this.bigMatchCount >= 2) {
                    addTime(j, freqAmplitude);
                    this.endReason = 3;
                    return false;
                }
                if (this.orderMatchCount >= 2 && this.preOrderType == 2 && freqAmplitude.order == 2) {
                    addTime(j, freqAmplitude);
                    this.endReason = 4;
                    return false;
                }
            }
            if (this.times.size() > 0 && this.preDowning && this.times.last().frequency.amplitude < freqAmplitude.amplitude && ((this.bigMatchCount > 0 && this.times.last().frequency.peakType == 2) || (this.bigMatchCount == 0 && this.orderMatchCount > 0 && this.times.last().frequency.order == 2))) {
                this.endReason = 5;
                return false;
            }
            if (this.times.size() >= 6 && this.times.last().frequency.amplitude < this.times.get(this.times.size() - 2).frequency.amplitude && this.times.last().frequency.amplitude < freqAmplitude.amplitude && freqAmplitude.peakType == 2) {
                if (this.bigMatchCount > 0) {
                    int i2 = this.times.last().frequency.peakType;
                }
                this.endReason = 5;
                return false;
            }
            if (this.times.size() > 3 && freqAmplitude.peakType == 1 && this.times.last().frequency.peakType == 2 && this.times.get(this.times.size() - 2).frequency.peakType == 1 && this.times.get(this.times.size() - 3).frequency.peakType == 1) {
                this.endReason = 6;
                return false;
            }
            addTime(j, freqAmplitude);
            this.preDowning = z;
            return true;
        }
        this.preDowning = true;
        this.shortFreqCount++;
        if (this.shortFreqCount < 2) {
            return true;
        }
        this.endReason = 1;
        return false;
    }

    public void addTime(long j, FreqAmplitude freqAmplitude) {
        addTime(new FrequencyMatch(j, freqAmplitude));
    }

    public FrequencyInfo removeSmallMatch() {
        if ((this.times.last().timeIdx - this.times.first().timeIdx) + 1 <= 5) {
            return this;
        }
        int i = this.maxAmplitudeIdx;
        int i2 = this.maxAmplitudeIdx;
        if (this.times.get(this.maxAmplitudeIdx).frequency.order == 2 && this.orderMatchCount > 0) {
            i = this.maxRelativeIdx;
            i2 = i;
        }
        do {
            if (i < 0 && i2 >= this.times.size()) {
                break;
            }
            int i3 = i - 1;
            FreqAmplitude freqAmplitude = null;
            FreqAmplitude freqAmplitude2 = (i3 < 0 || this.times.get(i).timeIdx - this.times.get(i3).timeIdx != 1) ? null : this.times.get(i3).frequency;
            int i4 = i2 + 1;
            if (i4 < this.times.size() && this.times.get(i4).timeIdx - this.times.get(i2).timeIdx == 1) {
                freqAmplitude = this.times.get(i4).frequency;
            }
            if (freqAmplitude2 == null || freqAmplitude != null) {
                if (freqAmplitude2 != null || freqAmplitude == null) {
                    if (freqAmplitude2 == null || freqAmplitude == null) {
                        break;
                    }
                    if (freqAmplitude2.order != 1 || freqAmplitude.order != 2) {
                        if (freqAmplitude2.order != 2 || freqAmplitude.order != 1) {
                            float f = freqAmplitude2.relative;
                            float f2 = freqAmplitude.relative;
                            if (f < 1.0f || f2 < 1.0f) {
                                float f3 = freqAmplitude2.relative;
                                float f4 = freqAmplitude.relative;
                            }
                            if (freqAmplitude2.relative >= freqAmplitude.relative) {
                            }
                        }
                    }
                    i = i3;
                }
                i2 = i4;
            } else {
                i = i3;
            }
        } while ((this.times.get(i2).timeIdx - this.times.get(i).timeIdx) + 1 < 5);
        MyArrayList<FrequencyMatch> myArrayList = new MyArrayList<>((i2 - i) + 1);
        while (i <= i2) {
            myArrayList.add(this.times.get(i));
            i++;
        }
        this.times = myArrayList;
        return this;
    }

    public void addTime(FrequencyMatch frequencyMatch) {
        this.times.add(frequencyMatch);
        if (this.maxAmplitudeIdx < 0 || this.maxAmplitudeIdx >= this.times.size() || frequencyMatch.frequency.amplitude > this.times.get(this.maxAmplitudeIdx).frequency.amplitude) {
            this.maxAmplitudeIdx = this.times.size() - 1;
        }
        if (this.maxRelativeIdx < 0 || this.maxRelativeIdx >= this.times.size() || frequencyMatch.frequency.relative > this.times.get(this.maxRelativeIdx).frequency.relative) {
            this.maxRelativeIdx = this.times.size() - 1;
        }
        FreqAmplitude freqAmplitude = frequencyMatch.frequency;
        if (freqAmplitude.peakType == 1) {
            this.bigMatchCount++;
        }
        if (freqAmplitude.order == 1) {
            this.orderMatchCount++;
        }
        this.shortFreqCount = 0;
        this.prePeakType = freqAmplitude.peakType;
        this.preOrderType = freqAmplitude.order;
        if (this.frequencyY == freqAmplitude.frequencyY) {
            this.preciseFreqCount++;
        }
    }
}
