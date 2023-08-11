package com.example.appit.fragment.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appit.R;

import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;



public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder>{

    private ArrayList<ScanResult> mDeviceList = new ArrayList<>();

    public interface OnItemClick {
        void onItemClick(ScanResult device);
    }


//    private SortedSet<ScanResult> mDeviceList = new TreeSet<>();

    private OnItemClick onItemClick;

    public DeviceAdapter(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        setHasStableIds(true);
    }

    public void addDevice(ScanResult scanResult, boolean notify) {

//        mDeviceList.add(scanResult);

        int existingPosition = getPosition(scanResult.getDevice().getAddress());

        if (existingPosition >= 0) {
            // Device is already in list, update its record.
            mDeviceList.set(existingPosition, scanResult);
        } else {
            // Add new Device's ScanResult to list.
            mDeviceList.add(scanResult);
        }

        if (notify) {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_adapter_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mDeviceList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onItemClick(mDeviceList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDeviceList == null ? 0 : mDeviceList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private int getPosition(String address) {
        int position = -1;
        for (int i = 0; i < mDeviceList.size(); i++) {
            if (mDeviceList.get(i).getDevice().getAddress().equals(address)) {
                position = i;
                break;
            }
        }
        return position;
    }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        View convertView;

        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;
        TextView deviceData;

        public ViewHolder(@NonNull View convertView) {
            super(convertView);
            this.convertView = convertView;
                 deviceName = (TextView) convertView.findViewById(R.id.tvDeviceName);
                 deviceAddress = (TextView) convertView.findViewById(R.id.tvDeviceAddress);
                 deviceRssi = (TextView) convertView.findViewById(R.id.tvRssi);
                 deviceData = (TextView) convertView.findViewById(R.id.tvData);
        }

        public void bind(ScanResult result) {
            BluetoothDevice device = result.getDevice();
//            if (TextUtils.isEmpty(device.getName())) {
//                deviceName.setText("IQPANEL-BTD");
//            } else {
                deviceName.setText(device.getName());
//            }
                deviceAddress.setText(device.getAddress());
                deviceRssi.setText(String.valueOf(result.getRssi()));
                ScanRecord record = result.getScanRecord();
            final ParcelUuid pUuid = new ParcelUuid(Constants.MAIN_SERVICE);
                byte[] serviceData = record.getServiceData(pUuid);
                if (serviceData != null) {
                    float data = (serviceData[0] & 0xFF);
                    deviceData.setText(String.valueOf(data));
                }
        }
    }
}
