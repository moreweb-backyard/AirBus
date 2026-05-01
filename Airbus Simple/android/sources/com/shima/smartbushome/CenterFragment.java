package com.shima.smartbushome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import com.shima.smartbushome.assist.Adapter.CentralControlAdapter;
import com.shima.smartbushome.centercontrol.AllCurtainActivity;
import com.shima.smartbushome.centercontrol.AllFanActivity;
import com.shima.smartbushome.centercontrol.AllHVACActivity;
import com.shima.smartbushome.centercontrol.AllLightActivity;
import com.shima.smartbushome.centercontrol.AllMusicActivity;
import com.shima.smartbushome.centercontrol.AllOtherActivity;
import com.shima.smartbushome.centercontrol.EnergyActivity;
import com.shima.smartbushome.centercontrol.MarcoActivity;
import com.shima.smartbushome.centercontrol.NFCActivity;
import com.shima.smartbushome.centercontrol.Security;
import com.shima.smartbushome.centercontrol.StatusActivity;

/* loaded from: classes.dex */
public class CenterFragment extends Fragment {
    private GridView centralcontrol;
    public AdapterView.OnItemClickListener centralitemclick = new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.CenterFragment.1
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            switch (i) {
                case 0:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) MarcoActivity.class));
                    break;
                case 1:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) AllLightActivity.class));
                    break;
                case 2:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) AllHVACActivity.class));
                    break;
                case 3:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) AllMusicActivity.class));
                    break;
                case 4:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) AllCurtainActivity.class));
                    break;
                case 5:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) AllOtherActivity.class));
                    break;
                case 6:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) AllFanActivity.class));
                    break;
                case 7:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) Security.class));
                    break;
                case 8:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) EnergyActivity.class));
                    break;
                case 9:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) StatusActivity.class));
                    break;
                case 10:
                    CenterFragment.this.startActivity(new Intent("com.shima.smartbushome.vstarcam"));
                    break;
                case 11:
                    CenterFragment.this.startActivity(new Intent(CenterFragment.this.getActivity(), (Class<?>) NFCActivity.class));
                    break;
            }
        }
    };
    View view;

    public static CenterFragment newInstance(String str) {
        return new CenterFragment();
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_center, viewGroup, false);
        this.centralcontrol = (GridView) this.view.findViewById(R.id.centercontrol);
        this.centralcontrol.setAdapter((ListAdapter) new CentralControlAdapter(getActivity()));
        this.centralcontrol.setOnItemClickListener(this.centralitemclick);
        return this.view;
    }
}
