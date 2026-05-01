package com.shima.smartbushome.assist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.shima.smartbushome.MainActivity;
import com.shima.smartbushome.R;
import com.shima.smartbushome.centercontrol.MarcoAddActivity;
import com.shima.smartbushome.database.Savemarco;
import com.shima.smartbushome.database.Savesong;
import com.shima.smartbushome.udp.CONST;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class MarcoItemAdapter extends BaseAdapter {
    public static final int select = 1648509506;
    private Context context;
    AlertView deletealter;
    private LayoutInflater inflater;
    private List<Savemarco> marcoearray;
    private int size;
    private int selectItem = -1;
    int delete_item_position = 0;
    public OnItemClickListener deleteclick = new OnItemClickListener() { // from class: com.shima.smartbushome.assist.Adapter.MarcoItemAdapter.2
        @Override // com.bigkoo.alertview.OnItemClickListener
        public void onItemClick(Object obj, int i) {
            if (i != -1 && i == 0) {
                MainActivity.mgr.deletemarco("marco", ((Savemarco) MarcoItemAdapter.this.marcoearray.get(MarcoItemAdapter.this.delete_item_position)).marco_id, ((Savemarco) MarcoItemAdapter.this.marcoearray.get(MarcoItemAdapter.this.delete_item_position)).sentorder);
                Toast.makeText(MarcoItemAdapter.this.context, "delete succeed", 0).show();
                MarcoItemAdapter.this.broadcastUpdate(MarcoAddActivity.MISSION_REFLASH);
            }
        }
    };
    String[] radiobuttonlist = {CONST.CONST_STR_ON, CONST.CONST_STR_OFF, "VOLUME -", "VOLUME +", "VOLUME MUTE", "UP", "DOWN", "LEFT", "RIGHT", "OK", "VIEW1", "VIEW2", "VIEW3", "VIEW4", "BACK", "HOME", "SETTING", "NUM 1", "NUM 2", "NUM 3", "NUM 4", "NUM 5", "NUM 6", "NUM 7", "NUM 8", "NUM 9", "NUM *", "NUM 0", "NUM #"};
    private HashMap<Integer, View> mView = new HashMap<>();

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public MarcoItemAdapter(Context context, List<Savemarco> list) {
        this.inflater = null;
        this.context = context;
        this.marcoearray = list;
        this.size = list.size();
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.size;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.marcoearray.get(i);
    }

    public void remove(Savemarco savemarco) {
        this.marcoearray.remove(savemarco);
        notifyDataSetChanged();
    }

    public void update(Savemarco savemarco, int i) {
        if (savemarco.sentorder >= this.marcoearray.get(i).sentorder) {
            int i2 = this.marcoearray.get(i).sentorder;
            int i3 = savemarco.sentorder;
            int i4 = savemarco.marco_id;
            MainActivity.mgr.updatemarco(savemarco, 9999999);
            for (int size = this.marcoearray.size() - 1; size >= 0; size--) {
                if (this.marcoearray.get(size).sentorder >= i2 && this.marcoearray.get(size).sentorder < i3) {
                    MainActivity.mgr.updatemarco(this.marcoearray.get(size), this.marcoearray.get(size).sentorder + 1);
                }
            }
            savemarco.sentorder = 9999999;
            MainActivity.mgr.updatemarco(savemarco, i2);
        } else {
            int i5 = this.marcoearray.get(i).sentorder;
            int i6 = savemarco.sentorder;
            MainActivity.mgr.updatemarco(savemarco, 9999999);
            for (int i7 = 0; i7 < this.marcoearray.size(); i7++) {
                if (this.marcoearray.get(i7).sentorder <= i5 && this.marcoearray.get(i7).sentorder > i6) {
                    MainActivity.mgr.updatemarco(this.marcoearray.get(i7), this.marcoearray.get(i7).sentorder - 1);
                }
            }
            savemarco.sentorder = 9999999;
            MainActivity.mgr.updatemarco(savemarco, i5);
        }
        broadcastUpdate(MarcoAddActivity.MISSION_REFLASH);
    }

    @Override // android.widget.Adapter
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View viewInflate = this.mView.get(Integer.valueOf(i));
        if (viewInflate == null) {
            viewInflate = this.inflater.inflate(R.layout.marcoitem, (ViewGroup) null);
            ImageView imageView = (ImageView) viewInflate.findViewById(R.id.marcoitem_type);
            TextView textView = (TextView) viewInflate.findViewById(R.id.marcoitem_room);
            TextView textView2 = (TextView) viewInflate.findViewById(R.id.marcoitem_action);
            TextView textView3 = (TextView) viewInflate.findViewById(R.id.marcoitem_value);
            ((TextView) viewInflate.findViewById(R.id.marcoitem_delete)).setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.assist.Adapter.MarcoItemAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    MarcoItemAdapter.this.delete_item_position = i;
                    MarcoItemAdapter.this.deletealter = new AlertView("Warning", " Are you sure to delete the Action ? ", "CANCEL", new String[]{"YES"}, null, MarcoItemAdapter.this.context, AlertView.Style.Alert, MarcoItemAdapter.this.deleteclick);
                    MarcoItemAdapter.this.deletealter.setCancelable(false);
                    MarcoItemAdapter.this.deletealter.show();
                }
            });
            switch (this.marcoearray.get(i).control_type) {
                case 1:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "light")));
                    break;
                case 2:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "hvac")));
                    break;
                case 3:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "curtain")));
                    break;
                case 4:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "music")));
                    break;
                case 5:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "other")));
                    break;
                case 6:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "fan")));
                    break;
                case 7:
                    imageView.setImageDrawable(this.context.getResources().getDrawable(getResourdIdByResourdName(this.context, "media")));
                    break;
            }
            textView.setText(this.marcoearray.get(i).room + "-" + this.marcoearray.get(i).device);
            StringBuilder sb = new StringBuilder();
            sb.append(getAction(this.marcoearray.get(i)));
            sb.append(" :");
            textView2.setText(sb.toString());
            if (getvalue(this.marcoearray.get(i)).equals("LED")) {
                textView3.setText("");
                textView3.setBackgroundColor(this.marcoearray.get(i).value2);
            } else {
                textView3.setText(getvalue(this.marcoearray.get(i)));
            }
            this.mView.put(Integer.valueOf(i), viewInflate);
        }
        return viewInflate;
    }

    public void setSelectItem(int i) {
        this.selectItem = i;
    }

    public static int getResourdIdByResourdName(Context context, String str) throws NoSuchFieldException {
        try {
            Field field = R.mipmap.class.getField(str);
            field.setAccessible(true);
            return field.getInt(null);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException unused) {
            return 0;
        }
    }

    public int getSelectItem() {
        return this.selectItem;
    }

    public String getAction(Savemarco savemarco) {
        String str;
        switch (savemarco.control_type) {
            case 1:
                switch (savemarco.value1) {
                    case 0:
                        str = "Power";
                        break;
                    case 1:
                        str = "Dimmer";
                        break;
                    case 2:
                        str = "LED Color";
                        break;
                    default:
                        return "";
                }
            case 2:
                switch (savemarco.value1) {
                    case 0:
                        str = "Power";
                        break;
                    case 1:
                        str = "Temperature";
                        break;
                    case 2:
                        str = "Fan Speed";
                        break;
                    case 3:
                        str = "Mode";
                        break;
                    default:
                        return "";
                }
            case 3:
                return "Power";
            case 4:
                int i = savemarco.value1;
                if (i == 1) {
                    str = "Source";
                    break;
                } else {
                    switch (i) {
                        case 3:
                            str = "Radio Channel";
                            break;
                        case 4:
                            str = "Play control";
                            break;
                        case 5:
                            str = "Volume";
                            break;
                        case 6:
                            str = "SD Song";
                            break;
                        default:
                            return "";
                    }
                }
            case 5:
                switch (savemarco.value1) {
                    case 0:
                        str = "Other Type1";
                        break;
                    case 1:
                        str = "Other Type2";
                        break;
                    default:
                        return "";
                }
            case 6:
                return "Fan Power";
            case 7:
                return "Media Control";
            default:
                return "";
        }
        return str;
    }

    public String getvalue(Savemarco savemarco) {
        String str;
        switch (savemarco.control_type) {
            case 1:
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                return CONST.CONST_STR_OFF;
                            case 1:
                                return CONST.CONST_STR_ON;
                            default:
                                return "";
                        }
                    case 1:
                        str = savemarco.value2 + "%";
                        break;
                    case 2:
                        str = "LED";
                        break;
                    default:
                        return "";
                }
            case 2:
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                return CONST.CONST_STR_OFF;
                            case 1:
                                return CONST.CONST_STR_ON;
                            default:
                                return "";
                        }
                    case 1:
                        switch (savemarco.value3) {
                            case 0:
                                return savemarco.value2 + " ℃ in Cool Mode";
                            case 1:
                                return savemarco.value2 + " ℃ in Heat Mode";
                            case 2:
                                return savemarco.value2 + " ℃ in Fan Mode";
                            case 3:
                                return savemarco.value2 + " ℃ in Auto Mode";
                            default:
                                return "";
                        }
                    case 2:
                        switch (savemarco.value2) {
                            case 0:
                                str = "Auto";
                                break;
                            case 1:
                                str = "High";
                                break;
                            case 2:
                                str = "Medium";
                                break;
                            case 3:
                                str = "Low";
                                break;
                            default:
                                return "";
                        }
                    default:
                        return "";
                }
            case 3:
                switch (savemarco.value2) {
                    case 0:
                        str = CONST.CONST_STR_OFF;
                        break;
                    case 1:
                        str = CONST.CONST_STR_ON;
                        break;
                    default:
                        return "";
                }
            case 4:
                int i = savemarco.value1;
                if (i == 1) {
                    int i2 = savemarco.value2;
                    if (i2 == 4) {
                        return "Radio";
                    }
                    switch (i2) {
                        case 1:
                            return "Music";
                        case 2:
                            return "Audio-In";
                        default:
                            return "";
                    }
                }
                switch (i) {
                    case 3:
                        str = "Channel-" + savemarco.value3;
                        break;
                    case 4:
                        switch (savemarco.value2) {
                            case 1:
                                return "Back";
                            case 2:
                                return "Next";
                            case 3:
                                return "Play";
                            case 4:
                                return "Pause";
                            default:
                                return "";
                        }
                    case 5:
                        str = savemarco.value2 + "%";
                        break;
                    case 6:
                        new ArrayList();
                        ArrayList arrayList = new ArrayList();
                        List<Savesong> listQuerysong = MainActivity.mgr.querysong();
                        for (int i3 = 0; i3 < listQuerysong.size(); i3++) {
                            if (listQuerysong.get(i3).room_id == savemarco.room_id) {
                                arrayList.add(listQuerysong.get(i3));
                            }
                        }
                        for (int i4 = 0; i4 < arrayList.size(); i4++) {
                            if (((Savesong) arrayList.get(i4)).album_num == savemarco.value2 && ((Savesong) arrayList.get(i4)).song_num == savemarco.value3) {
                                str = ((Savesong) arrayList.get(i4)).song_name;
                                break;
                            }
                        }
                        return "";
                    default:
                        return "";
                }
            case 5:
                switch (savemarco.value1) {
                    case 0:
                        switch (savemarco.value2) {
                            case 0:
                                return CONST.CONST_STR_OFF;
                            case 1:
                                return CONST.CONST_STR_ON;
                            default:
                                return "";
                        }
                    case 1:
                        switch (savemarco.value2) {
                            case 0:
                                str = CONST.CONST_STR_OFF;
                                break;
                            case 1:
                                str = CONST.CONST_STR_ON;
                                break;
                            default:
                                return "";
                        }
                    default:
                        return "";
                }
            case 6:
                switch (savemarco.value2) {
                    case 0:
                        str = "Fan off";
                        break;
                    case 1:
                        str = "Fan low Speed";
                        break;
                    case 2:
                        str = "Fan Middle Speed";
                        break;
                    case 3:
                        str = "Fan High Speed";
                        break;
                    case 4:
                        str = "Fan Full Speed";
                        break;
                    default:
                        return "";
                }
            case 7:
                return this.radiobuttonlist[savemarco.value2 - 1];
            default:
                return "";
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str) {
        this.context.sendBroadcast(new Intent(str));
    }
}
