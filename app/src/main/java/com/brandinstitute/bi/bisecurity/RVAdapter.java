package com.brandinstitute.bi.bisecurity;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {
    public String cliAddress1;
    public String cliAddress2;
    public String cliCity;
    public String cliState;
    public String cliZip;
    public String cliCountry;
    public String cliPhone;
    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView companyName;
        TextView clientContact;
        TextView txtMonth;
        TextView txtDay;
        TextView txtTime;
        ImageView appointmentIcon;
        TextView appointmentId;

//        TextView cliAddress1;
        TextView cliAddress2;
        TextView cliCity;
        TextView cliState;
        TextView cliZip;
        TextView cliCountry;
        TextView cliPhone;



        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);

            cv.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), DetailActivityView.class);
                    i.putExtra("companyName", companyName.getText().toString());
                    i.putExtra("clientContact", clientContact.getText().toString());
                    i.putExtra("txtMonth", txtMonth.getText().toString());
                    i.putExtra("txtDay", txtDay.getText().toString());
                    i.putExtra("txtTime", txtTime.getText().toString());
                    i.putExtra("appointmentId", appointmentId.getText().toString());
                    i.putExtra("cliAddress1",RVAdapter.this.cliAddress1);
                    i.putExtra("cliAddress2" ,RVAdapter.this.cliAddress2);
                    i.putExtra("cliCity" ,RVAdapter.this.cliCity);
                    i.putExtra("cliState" ,RVAdapter.this.cliState);
                    i.putExtra("cliZip" ,RVAdapter.this.cliZip);
                    i.putExtra("cliCountry" ,RVAdapter.this.cliCountry);
                    i.putExtra("cliPhone" ,RVAdapter.this.cliPhone);
                    view.getContext().startActivity(i);
                }
            });

            companyName = (TextView)itemView.findViewById(R.id.company_name);
            clientContact = (TextView)itemView.findViewById(R.id.client_contact);
            txtMonth = (TextView)itemView.findViewById(R.id.month);
            txtDay = (TextView)itemView.findViewById(R.id.day);
            txtTime = (TextView)itemView.findViewById(R.id.time);
            appointmentIcon = (ImageView)itemView.findViewById(R.id.appointment_icon);
            appointmentId = (TextView)itemView.findViewById(R.id.appointment_id);
        }
    }

    List<AppointmentList> appointments;

    RVAdapter(List<AppointmentList> appointments){
        this.appointments = appointments;
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        String[] date = appointments.get(i).AppDate.split(" ");
        personViewHolder.appointmentId.setText(appointments.get(i).AppId);
        personViewHolder.txtMonth.setText(date[0]);
        personViewHolder.txtDay.setText(date[1]);
        personViewHolder.txtTime.setText(date[3]);
        personViewHolder.companyName.setText(appointments.get(i).cliCompany);
        personViewHolder.clientContact.setText(appointments.get(i).cliContact);
        this.cliAddress1 = appointments.get(i).cliAddress1;
        this.cliAddress2= appointments.get(i).cliAddress2;
        this.cliCity= appointments.get(i).cliCity;
        this.cliState= appointments.get(i).cliState;
        this.cliZip= appointments.get(i).cliZip;
        this.cliCountry= appointments.get(i).cliCountry;
        this.cliPhone= appointments.get(i).cliPhone;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}