package com.example.gson;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<NextWeatherBean> MmessageList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        View msgView;
        ImageView dayImage;
        TextView WeekName;
        TextView NextName;
        TextView MaxName;
        TextView MinName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            msgView=itemView;
            dayImage=(ImageView) itemView.findViewById(R.id.day_img);
            WeekName=(TextView) itemView.findViewById(R.id.weekday_text);
            NextName=(TextView) itemView.findViewById(R.id.nextday_text);
            MaxName=(TextView) itemView.findViewById(R.id.maxoc_text);
            MinName=(TextView) itemView.findViewById(R.id.minoc_text);
        }
    }

    public MsgAdapter(List<NextWeatherBean> messages){MmessageList=messages;}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wheatherlayout,parent,false);
            final ViewHolder holder=new ViewHolder(view);

            return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NextWeatherBean message=MmessageList.get(position);
        holder.dayImage.setImageResource(message.getDayImageID());
        holder.WeekName.setText(message.getWeekName());
        holder.NextName.setText(message.getNextName());
        holder.MaxName.setText(message.getMaxName());
        holder.MinName.setText(message.getMinName());
    }

    @Override
    public int getItemCount() {
       return MmessageList.size();
    }
}
