package br.com.meupedidoapp.meupedidokt.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class Tema implements Parcelable{
    private String corFonte;
    private String corPrincipal;
    private String corStatusBar;
    private String corLight;

    public Tema(){}

    protected Tema(Parcel in) {
        corFonte = in.readString();
        corPrincipal = in.readString();
        corStatusBar = in.readString();
        corLight = in.readString();
    }

    public static final Creator<Tema> CREATOR = new Creator<Tema>() {
        @Override
        public Tema createFromParcel(Parcel in) {
            return new Tema(in);
        }

        @Override
        public Tema[] newArray(int size) {
            return new Tema[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(corFonte);
        dest.writeString(corPrincipal);
        dest.writeString(corStatusBar);
        dest.writeString(corLight);
    }

    public String getCorFonte() {
        return corFonte;
    }

    public String getCorPrincipal() {
        return corPrincipal;
    }

    public String getCorStatusBar() {
        return corStatusBar;
    }

    public String getCorLight() {
        return corLight;
    }
}
