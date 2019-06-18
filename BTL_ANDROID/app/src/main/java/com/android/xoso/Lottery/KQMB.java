package com.android.xoso.Lottery;

class KQMB {

    private String tenGiai;
    private String ketqua;

    KQMB(String title, String info) {
        this.tenGiai = title;
        this.ketqua = info;
    }

    String getTenGiai() {
        return tenGiai;
    }

    String getKetqua() {
        return ketqua;
    }
}
