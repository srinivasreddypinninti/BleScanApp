package com.example.appit.fragment.service;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {

    private String tag;
    private String text;

    protected Message(Parcel in) {
        tag = in.readString();
        text = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeString(text);
    }
}
