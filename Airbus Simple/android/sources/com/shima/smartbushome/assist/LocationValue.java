package com.shima.smartbushome.assist;

import java.util.ArrayList;

/* loaded from: classes.dex */
public class LocationValue {
    public ArrayList<locationdetail> results;
    public String status;

    public class locationdetail {
        public ArrayList<components> address_components;
        public String formatted_address;
        public String place_id;

        public locationdetail() {
        }
    }

    public class components {
        public String long_name;
        public String short_name;
        public ArrayList<String> types;

        public components() {
        }
    }
}
