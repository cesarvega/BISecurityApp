package com.brandinstitute.bi.bisecurity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {

    private Context context;

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout el;
        ImageView expensePicture;
        TextView txtDescription;
        TextView txtSpenseType;
        TextView txtAmount;

        ExpenseViewHolder(View itemView) {
            super(itemView);
            el = (ConstraintLayout)itemView.findViewById(R.id.el);
            expensePicture = (ImageView)itemView.findViewById(R.id.expense_picture);
            txtDescription = (TextView)itemView.findViewById(R.id.text_description);
            txtDescription.setKeyListener(null);
            txtDescription.setOnTouchListener(new View.OnTouchListener(){
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });
            txtSpenseType = (TextView)itemView.findViewById(R.id.txt_spense_type);
            txtAmount = (TextView)itemView.findViewById(R.id.text_amount);
        }
    }

    List<ExpenseList> expenses;

    ExpenseListAdapter(List<ExpenseList> expenses, Context context){
        this.expenses = expenses;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_item_view, viewGroup, false);
        ExpenseViewHolder evh = new ExpenseViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder expenseViewHolder, int i) {

//        expenseViewHolder.expensePicture.setImageResource(R.drawable.ic_info_white_24dp);
        expenseViewHolder.expensePicture.setImageURI(Uri.parse(expenses.get(i).expensePhoto));
        expenseViewHolder.txtSpenseType.setText(expenses.get(i).expenseType);
        expenseViewHolder.txtDescription.setText(expenses.get(i).description);
        expenseViewHolder.txtAmount.setText(expenses.get(i).amount);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView expenseListView) {
        super.onAttachedToRecyclerView(expenseListView);
    }
}