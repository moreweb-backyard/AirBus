package com.bigkoo.pickerview.view;

import android.content.Context;
import android.view.View;
import com.bigkoo.pickerview.R;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.shima.smartbushome.assist.CrashHandle.DateUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/* loaded from: classes.dex */
public class WheelTime {
    public static final int DEFULT_END_DAY = 31;
    public static final int DEFULT_END_MONTH = 12;
    public static final int DEFULT_END_YEAR = 2100;
    public static final int DEFULT_START_DAY = 1;
    public static final int DEFULT_START_MONTH = 1;
    public static final int DEFULT_START_YEAR = 1900;
    public static DateFormat dateFormat = new SimpleDateFormat(DateUtil.DEFAULT_DATE_TIME_FORMAT);
    private int currentYear;
    int dividerColor;
    private WheelView.DividerType dividerType;
    private int endDay;
    private int endMonth;
    private int endYear;
    private int gravity;
    float lineSpacingMultiplier;
    private int startDay;
    private int startMonth;
    private int startYear;
    int textColorCenter;
    int textColorOut;
    private int textSize;
    private TimePickerView.Type type;
    private View view;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    private WheelView wv_month;
    private WheelView wv_seconds;
    private WheelView wv_year;

    public WheelTime(View view) {
        this.startYear = DEFULT_START_YEAR;
        this.endYear = DEFULT_END_YEAR;
        this.startMonth = 1;
        this.endMonth = 12;
        this.startDay = 1;
        this.endDay = 31;
        this.textSize = 18;
        this.lineSpacingMultiplier = 1.6f;
        this.view = view;
        this.type = TimePickerView.Type.ALL;
        setView(view);
    }

    public WheelTime(View view, TimePickerView.Type type, int i, int i2) {
        this.startYear = DEFULT_START_YEAR;
        this.endYear = DEFULT_END_YEAR;
        this.startMonth = 1;
        this.endMonth = 12;
        this.startDay = 1;
        this.endDay = 31;
        this.textSize = 18;
        this.lineSpacingMultiplier = 1.6f;
        this.view = view;
        this.type = type;
        this.gravity = i;
        this.textSize = i2;
        setView(view);
    }

    public void setPicker(int i, int i2, int i3) {
        setPicker(i, i2, i3, 0, 0, 0);
    }

    public void setPicker(int i, int i2, int i3, int i4, int i5, int i6) {
        int i7;
        int i8;
        final List listAsList = Arrays.asList("1", "3", "5", "7", "8", "10", "12");
        final List listAsList2 = Arrays.asList("4", "6", "9", "11");
        Context context = this.view.getContext();
        this.currentYear = i;
        this.wv_year = (WheelView) this.view.findViewById(R.id.year);
        this.wv_year.setAdapter(new NumericWheelAdapter(this.startYear, this.endYear));
        this.wv_year.setLabel(context.getString(R.string.pickerview_year));
        this.wv_year.setCurrentItem(i - this.startYear);
        this.wv_year.setGravity(this.gravity);
        this.wv_month = (WheelView) this.view.findViewById(R.id.month);
        if (this.startYear == this.endYear) {
            this.wv_month.setAdapter(new NumericWheelAdapter(this.startMonth, this.endMonth));
            this.wv_month.setCurrentItem((i2 + 1) - this.startMonth);
        } else if (i == this.startYear) {
            this.wv_month.setAdapter(new NumericWheelAdapter(this.startMonth, 12));
            this.wv_month.setCurrentItem((i2 + 1) - this.startMonth);
        } else if (i == this.endYear) {
            this.wv_month.setAdapter(new NumericWheelAdapter(1, this.endMonth));
            this.wv_month.setCurrentItem(i2);
        } else {
            this.wv_month.setAdapter(new NumericWheelAdapter(1, 12));
            this.wv_month.setCurrentItem(i2);
        }
        this.wv_month.setLabel(context.getString(R.string.pickerview_month));
        this.wv_month.setGravity(this.gravity);
        this.wv_day = (WheelView) this.view.findViewById(R.id.day);
        if (this.startYear == this.endYear && this.startMonth == this.endMonth) {
            int i9 = i2 + 1;
            if (listAsList.contains(String.valueOf(i9))) {
                if (this.endDay > 31) {
                    this.endDay = 31;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, this.endDay));
            } else if (listAsList2.contains(String.valueOf(i9))) {
                if (this.endDay > 30) {
                    this.endDay = 30;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, this.endDay));
            } else if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {
                if (this.endDay > 29) {
                    this.endDay = 29;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, this.endDay));
            } else {
                if (this.endDay > 28) {
                    this.endDay = 28;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, this.endDay));
            }
            this.wv_day.setCurrentItem(i3 - this.startDay);
        } else if (i == this.startYear && (i8 = i2 + 1) == this.startMonth) {
            if (listAsList.contains(String.valueOf(i8))) {
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, 31));
            } else if (listAsList2.contains(String.valueOf(i8))) {
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, 30));
            } else if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, 29));
            } else {
                this.wv_day.setAdapter(new NumericWheelAdapter(this.startDay, 28));
            }
            this.wv_day.setCurrentItem(i3 - this.startDay);
        } else if (i == this.endYear && (i7 = i2 + 1) == this.endMonth) {
            if (listAsList.contains(String.valueOf(i7))) {
                if (this.endDay > 31) {
                    this.endDay = 31;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(1, this.endDay));
            } else if (listAsList2.contains(String.valueOf(i7))) {
                if (this.endDay > 30) {
                    this.endDay = 30;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(1, this.endDay));
            } else if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {
                if (this.endDay > 29) {
                    this.endDay = 29;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(1, this.endDay));
            } else {
                if (this.endDay > 28) {
                    this.endDay = 28;
                }
                this.wv_day.setAdapter(new NumericWheelAdapter(1, this.endDay));
            }
            this.wv_day.setCurrentItem(i3 - 1);
        } else {
            int i10 = i2 + 1;
            if (listAsList.contains(String.valueOf(i10))) {
                this.wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (listAsList2.contains(String.valueOf(i10))) {
                this.wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {
                this.wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            } else {
                this.wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
            this.wv_day.setCurrentItem(i3 - 1);
        }
        this.wv_day.setLabel(context.getString(R.string.pickerview_day));
        this.wv_day.setGravity(this.gravity);
        this.wv_hours = (WheelView) this.view.findViewById(R.id.hour);
        this.wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        this.wv_hours.setLabel(context.getString(R.string.pickerview_hours));
        this.wv_hours.setCurrentItem(i4);
        this.wv_hours.setGravity(this.gravity);
        this.wv_mins = (WheelView) this.view.findViewById(R.id.min);
        this.wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
        this.wv_mins.setLabel(context.getString(R.string.pickerview_minutes));
        this.wv_mins.setCurrentItem(i5);
        this.wv_mins.setGravity(this.gravity);
        this.wv_seconds = (WheelView) this.view.findViewById(R.id.second);
        this.wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));
        this.wv_seconds.setLabel(context.getString(R.string.pickerview_seconds));
        this.wv_seconds.setCurrentItem(i6);
        this.wv_seconds.setGravity(this.gravity);
        OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() { // from class: com.bigkoo.pickerview.view.WheelTime.1
            @Override // com.bigkoo.pickerview.listener.OnItemSelectedListener
            public void onItemSelected(int i11) {
                int i12 = i11 + WheelTime.this.startYear;
                WheelTime.this.currentYear = i12;
                int currentItem = WheelTime.this.wv_month.getCurrentItem();
                if (WheelTime.this.startYear == WheelTime.this.endYear) {
                    WheelTime.this.wv_month.setAdapter(new NumericWheelAdapter(WheelTime.this.startMonth, WheelTime.this.endMonth));
                    if (currentItem > WheelTime.this.wv_month.getAdapter().getItemsCount() - 1) {
                        currentItem = WheelTime.this.wv_month.getAdapter().getItemsCount() - 1;
                        WheelTime.this.wv_month.setCurrentItem(currentItem);
                    }
                    int i13 = currentItem + WheelTime.this.startMonth;
                    if (WheelTime.this.startMonth == WheelTime.this.endMonth) {
                        WheelTime.this.setReDay(i12, i13, WheelTime.this.startDay, WheelTime.this.endDay, listAsList, listAsList2);
                        return;
                    } else if (i13 == WheelTime.this.startMonth) {
                        WheelTime.this.setReDay(i12, i13, WheelTime.this.startDay, 31, listAsList, listAsList2);
                        return;
                    } else {
                        WheelTime.this.setReDay(i12, i13, 1, 31, listAsList, listAsList2);
                        return;
                    }
                }
                if (i12 == WheelTime.this.startYear) {
                    WheelTime.this.wv_month.setAdapter(new NumericWheelAdapter(WheelTime.this.startMonth, 12));
                    if (currentItem > WheelTime.this.wv_month.getAdapter().getItemsCount() - 1) {
                        currentItem = WheelTime.this.wv_month.getAdapter().getItemsCount() - 1;
                        WheelTime.this.wv_month.setCurrentItem(currentItem);
                    }
                    int i14 = currentItem + WheelTime.this.startMonth;
                    if (i14 == WheelTime.this.startMonth) {
                        WheelTime.this.setReDay(i12, i14, WheelTime.this.startDay, 31, listAsList, listAsList2);
                        return;
                    } else {
                        WheelTime.this.setReDay(i12, i14, 1, 31, listAsList, listAsList2);
                        return;
                    }
                }
                if (i12 == WheelTime.this.endYear) {
                    WheelTime.this.wv_month.setAdapter(new NumericWheelAdapter(1, WheelTime.this.endMonth));
                    if (currentItem > WheelTime.this.wv_month.getAdapter().getItemsCount() - 1) {
                        currentItem = WheelTime.this.wv_month.getAdapter().getItemsCount() - 1;
                        WheelTime.this.wv_month.setCurrentItem(currentItem);
                    }
                    int i15 = 1 + currentItem;
                    if (i15 == WheelTime.this.endMonth) {
                        WheelTime.this.setReDay(i12, i15, 1, WheelTime.this.endDay, listAsList, listAsList2);
                        return;
                    } else {
                        WheelTime.this.setReDay(i12, i15, 1, 31, listAsList, listAsList2);
                        return;
                    }
                }
                WheelTime.this.wv_month.setAdapter(new NumericWheelAdapter(1, 12));
                WheelTime.this.setReDay(i12, 1 + WheelTime.this.wv_month.getCurrentItem(), 1, 31, listAsList, listAsList2);
            }
        };
        OnItemSelectedListener onItemSelectedListener2 = new OnItemSelectedListener() { // from class: com.bigkoo.pickerview.view.WheelTime.2
            @Override // com.bigkoo.pickerview.listener.OnItemSelectedListener
            public void onItemSelected(int i11) {
                int i12 = i11 + 1;
                if (WheelTime.this.startYear == WheelTime.this.endYear) {
                    int i13 = (i12 + WheelTime.this.startMonth) - 1;
                    if (WheelTime.this.startMonth == WheelTime.this.endMonth) {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, i13, WheelTime.this.startDay, WheelTime.this.endDay, listAsList, listAsList2);
                        return;
                    }
                    if (WheelTime.this.startMonth == i13) {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, i13, WheelTime.this.startDay, 31, listAsList, listAsList2);
                        return;
                    } else if (WheelTime.this.endMonth == i13) {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, i13, 1, WheelTime.this.endDay, listAsList, listAsList2);
                        return;
                    } else {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, i13, 1, 31, listAsList, listAsList2);
                        return;
                    }
                }
                if (WheelTime.this.currentYear == WheelTime.this.startYear) {
                    int i14 = (i12 + WheelTime.this.startMonth) - 1;
                    if (i14 == WheelTime.this.startMonth) {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, i14, WheelTime.this.startDay, 31, listAsList, listAsList2);
                        return;
                    } else {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, i14, 1, 31, listAsList, listAsList2);
                        return;
                    }
                }
                if (WheelTime.this.currentYear == WheelTime.this.endYear) {
                    if (i12 == WheelTime.this.endMonth) {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, WheelTime.this.wv_month.getCurrentItem() + 1, 1, WheelTime.this.endDay, listAsList, listAsList2);
                        return;
                    } else {
                        WheelTime.this.setReDay(WheelTime.this.currentYear, WheelTime.this.wv_month.getCurrentItem() + 1, 1, 31, listAsList, listAsList2);
                        return;
                    }
                }
                WheelTime.this.setReDay(WheelTime.this.currentYear, i12, 1, 31, listAsList, listAsList2);
            }
        };
        this.wv_year.setOnItemSelectedListener(onItemSelectedListener);
        this.wv_month.setOnItemSelectedListener(onItemSelectedListener2);
        switch (this.type) {
            case YEAR_MONTH_DAY:
                this.wv_hours.setVisibility(8);
                this.wv_mins.setVisibility(8);
                this.wv_seconds.setVisibility(8);
                break;
            case HOURS_MINS:
                this.wv_year.setVisibility(8);
                this.wv_month.setVisibility(8);
                this.wv_day.setVisibility(8);
                this.wv_seconds.setVisibility(8);
                break;
            case MONTH_DAY_HOUR_MIN:
                this.wv_year.setVisibility(8);
                this.wv_seconds.setVisibility(8);
                break;
            case YEAR_MONTH:
                this.wv_day.setVisibility(8);
                this.wv_hours.setVisibility(8);
                this.wv_mins.setVisibility(8);
                this.wv_seconds.setVisibility(8);
            case YEAR_MONTH_DAY_HOUR_MIN:
                this.wv_seconds.setVisibility(8);
                break;
        }
        setContentTextSize();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setReDay(int i, int i2, int i3, int i4, List<String> list, List<String> list2) {
        int currentItem = this.wv_day.getCurrentItem();
        if (list.contains(String.valueOf(i2))) {
            this.wv_day.setAdapter(new NumericWheelAdapter(i3, i4 <= 31 ? i4 : 31));
        } else if (list2.contains(String.valueOf(i2))) {
            this.wv_day.setAdapter(new NumericWheelAdapter(i3, i4 <= 30 ? i4 : 30));
        } else if ((i % 4 == 0 && i % 100 != 0) || i % 400 == 0) {
            this.wv_day.setAdapter(new NumericWheelAdapter(i3, i4 <= 29 ? i4 : 29));
        } else {
            this.wv_day.setAdapter(new NumericWheelAdapter(i3, i4 <= 28 ? i4 : 28));
        }
        if (currentItem > this.wv_day.getAdapter().getItemsCount() - 1) {
            this.wv_day.setCurrentItem(this.wv_day.getAdapter().getItemsCount() - 1);
        }
    }

    private void setContentTextSize() {
        this.wv_day.setTextSize(this.textSize);
        this.wv_month.setTextSize(this.textSize);
        this.wv_year.setTextSize(this.textSize);
        this.wv_hours.setTextSize(this.textSize);
        this.wv_mins.setTextSize(this.textSize);
        this.wv_seconds.setTextSize(this.textSize);
    }

    private void setTextColorOut() {
        this.wv_day.setTextColorOut(this.textColorOut);
        this.wv_month.setTextColorOut(this.textColorOut);
        this.wv_year.setTextColorOut(this.textColorOut);
        this.wv_hours.setTextColorOut(this.textColorOut);
        this.wv_mins.setTextColorOut(this.textColorOut);
        this.wv_seconds.setTextColorOut(this.textColorOut);
    }

    private void setTextColorCenter() {
        this.wv_day.setTextColorCenter(this.textColorCenter);
        this.wv_month.setTextColorCenter(this.textColorCenter);
        this.wv_year.setTextColorCenter(this.textColorCenter);
        this.wv_hours.setTextColorCenter(this.textColorCenter);
        this.wv_mins.setTextColorCenter(this.textColorCenter);
        this.wv_seconds.setTextColorCenter(this.textColorCenter);
    }

    private void setDividerColor() {
        this.wv_day.setDividerColor(this.dividerColor);
        this.wv_month.setDividerColor(this.dividerColor);
        this.wv_year.setDividerColor(this.dividerColor);
        this.wv_hours.setDividerColor(this.dividerColor);
        this.wv_mins.setDividerColor(this.dividerColor);
        this.wv_seconds.setDividerColor(this.dividerColor);
    }

    private void setDividerType() {
        this.wv_day.setDividerType(this.dividerType);
        this.wv_month.setDividerType(this.dividerType);
        this.wv_year.setDividerType(this.dividerType);
        this.wv_hours.setDividerType(this.dividerType);
        this.wv_mins.setDividerType(this.dividerType);
        this.wv_seconds.setDividerType(this.dividerType);
    }

    private void setLineSpacingMultiplier() {
        this.wv_day.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wv_month.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wv_year.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wv_hours.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wv_mins.setLineSpacingMultiplier(this.lineSpacingMultiplier);
        this.wv_seconds.setLineSpacingMultiplier(this.lineSpacingMultiplier);
    }

    public void setLabels(String str, String str2, String str3, String str4, String str5, String str6) {
        if (str != null) {
            this.wv_year.setLabel(str);
        }
        if (str2 != null) {
            this.wv_month.setLabel(str2);
        }
        if (str3 != null) {
            this.wv_day.setLabel(str3);
        }
        if (str4 != null) {
            this.wv_hours.setLabel(str4);
        }
        if (str5 != null) {
            this.wv_mins.setLabel(str5);
        }
        if (str6 != null) {
            this.wv_seconds.setLabel(str6);
        }
    }

    public void setCyclic(boolean z) {
        this.wv_year.setCyclic(z);
        this.wv_month.setCyclic(z);
        this.wv_day.setCyclic(z);
        this.wv_hours.setCyclic(z);
        this.wv_mins.setCyclic(z);
        this.wv_seconds.setCyclic(z);
    }

    public String getTime() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.currentYear == this.startYear) {
            int currentItem = this.wv_month.getCurrentItem() + this.startMonth;
            System.out.println("i:" + currentItem);
            if (this.wv_month.getCurrentItem() + this.startMonth == this.startMonth) {
                stringBuffer.append(this.wv_year.getCurrentItem() + this.startYear);
                stringBuffer.append("-");
                stringBuffer.append(this.wv_month.getCurrentItem() + this.startMonth);
                stringBuffer.append("-");
                stringBuffer.append(this.wv_day.getCurrentItem() + this.startDay);
                stringBuffer.append(" ");
                stringBuffer.append(this.wv_hours.getCurrentItem());
                stringBuffer.append(":");
                stringBuffer.append(this.wv_mins.getCurrentItem());
                stringBuffer.append(":");
                stringBuffer.append(this.wv_seconds.getCurrentItem());
            } else {
                stringBuffer.append(this.wv_year.getCurrentItem() + this.startYear);
                stringBuffer.append("-");
                stringBuffer.append(this.wv_month.getCurrentItem() + this.startMonth);
                stringBuffer.append("-");
                stringBuffer.append(this.wv_day.getCurrentItem() + 1);
                stringBuffer.append(" ");
                stringBuffer.append(this.wv_hours.getCurrentItem());
                stringBuffer.append(":");
                stringBuffer.append(this.wv_mins.getCurrentItem());
                stringBuffer.append(":");
                stringBuffer.append(this.wv_seconds.getCurrentItem());
            }
        } else {
            stringBuffer.append(this.wv_year.getCurrentItem() + this.startYear);
            stringBuffer.append("-");
            stringBuffer.append(this.wv_month.getCurrentItem() + 1);
            stringBuffer.append("-");
            stringBuffer.append(this.wv_day.getCurrentItem() + 1);
            stringBuffer.append(" ");
            stringBuffer.append(this.wv_hours.getCurrentItem());
            stringBuffer.append(":");
            stringBuffer.append(this.wv_mins.getCurrentItem());
            stringBuffer.append(":");
            stringBuffer.append(this.wv_seconds.getCurrentItem());
        }
        return stringBuffer.toString();
    }

    public View getView() {
        return this.view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getStartYear() {
        return this.startYear;
    }

    public void setStartYear(int i) {
        this.startYear = i;
    }

    public int getEndYear() {
        return this.endYear;
    }

    public void setEndYear(int i) {
        this.endYear = i;
    }

    public void setRangDate(Calendar calendar, Calendar calendar2) {
        if (calendar == null && calendar2 != null) {
            int i = calendar2.get(1);
            int i2 = calendar2.get(2) + 1;
            int i3 = calendar2.get(5);
            if (i > this.startYear) {
                this.endYear = i;
                this.endMonth = i2;
                this.endDay = i3;
            } else if (i == this.startYear) {
                if (i2 > this.startMonth) {
                    this.endYear = i;
                    this.endMonth = i2;
                    this.endDay = i3;
                } else if (i2 == this.startMonth && i2 > this.startDay) {
                    this.endYear = i;
                    this.endMonth = i2;
                    this.endDay = i3;
                }
            }
        } else if (calendar != null && calendar2 == null) {
            int i4 = calendar.get(1);
            int i5 = calendar.get(2) + 1;
            int i6 = calendar.get(5);
            if (i4 < this.endYear) {
                this.startMonth = i5;
                this.startDay = i6;
                this.startYear = i4;
            } else if (i4 == this.endYear) {
                if (i5 < this.endMonth) {
                    this.startMonth = i5;
                    this.startDay = i6;
                    this.startYear = i4;
                } else if (i5 == this.endMonth && i6 < this.endDay) {
                    this.startMonth = i5;
                    this.startDay = i6;
                    this.startYear = i4;
                }
            }
        } else if (calendar != null && calendar2 != null) {
            this.startYear = calendar.get(1);
            this.endYear = calendar2.get(1);
            this.startMonth = calendar.get(2) + 1;
            this.endMonth = calendar2.get(2) + 1;
            this.startDay = calendar.get(5);
            this.endDay = calendar2.get(5);
        }
        System.out.println("endyear:" + this.endYear);
        System.out.println("endMonth:" + this.endMonth);
        System.out.println("endDay:" + this.endDay);
    }

    public void setLineSpacingMultiplier(float f) {
        this.lineSpacingMultiplier = f;
        setLineSpacingMultiplier();
    }

    public void setDividerColor(int i) {
        this.dividerColor = i;
        setDividerColor();
    }

    public void setDividerType(WheelView.DividerType dividerType) {
        this.dividerType = dividerType;
        setDividerType();
    }

    public void setTextColorCenter(int i) {
        this.textColorCenter = i;
        setTextColorCenter();
    }

    public void setTextColorOut(int i) {
        this.textColorOut = i;
        setTextColorOut();
    }
}
