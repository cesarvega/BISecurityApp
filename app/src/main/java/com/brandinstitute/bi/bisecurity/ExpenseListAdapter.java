package com.brandinstitute.bi.bisecurity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.internal.bind.ObjectTypeAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;


public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
    private Context context;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private String phoneIdType;
    private String appid;
    private String recType;
    private String recTotal;
    private String imgType;
    private String imgString;
    private String amount;
//    private String description;
    private String expenseType;
    private String phoneId;
   private String img;

//    private String phoneId = "15555218135";
//    private String phoneId = "3057427989";

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout el;
        ImageView expensePicture;
//        TextView txtDescription;
        TextView txtSpenseType;
        TextView txtAmount;


        ExpenseViewHolder(View itemView) {
            super(itemView);
            el = (ConstraintLayout)itemView.findViewById(R.id.el);
            expensePicture = (ImageView)itemView.findViewById(R.id.expense_picture);
            expensePicture.setRotation(90);
//            txtDescription = (TextView)itemView.findViewById(R.id.text_description);
//            txtDescription.setKeyListener(null);
//            txtDescription.setOnTouchListener(new View.OnTouchListener(){
//                public boolean onTouch(View v, MotionEvent event) {
//                    // Disallow the touch request for parent scroll on touch of child view
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//            });
            txtSpenseType = (TextView)itemView.findViewById(R.id.txt_spense_type);
            txtAmount = (TextView)itemView.findViewById(R.id.text_amount);
        }
    }

    List<ExpenseList> expenses;

    ExpenseListAdapter(List<ExpenseList> expenses,   Context context){
        this.context = context;
        this.expenses = expenses;
        this.context = context;
        TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        phoneId = tMgr.getLine1Number();

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

        this.imgString = getStringImage(expenses.get(i).expensePhoto);
        amount =  expenses.get(i).amount;
//        description = expenses.get(i).description;
        expenseType =expenses.get(i).expenseType;

        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Uploading, please wait...");
//        progressDialog.show();
        //sending image to server
        if(expenses.get(i).whatToDo == "saveToDataBase"){
            StringRequest request = new StringRequest(Request.Method.POST, "https://tools.brandinstitute.com/wsbi/bimobile.asmx/addAppReceiptString", new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Uploaded Successful", Toast.LENGTH_LONG).show();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                }
            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    phoneIdType ="1";
                    appid =((DetailActivityView) context).appointmentId.getText().subSequence(15, ((DetailActivityView) context).appointmentId.getText().length()).toString();
                    recType = "1";
//                    recType = expenseType;
                    recTotal = amount;
                    imgType = "base64";
                    img = imgString;

                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("phoneId",phoneId);
                    parameters.put("phoneIdType",phoneIdType);
                    parameters.put("appid",appid);
                    parameters.put("recType",recType);
                    parameters.put("recTotal",recTotal);
                    parameters.put("imgType",imgType);
                    parameters.put("img",img);
                    return parameters;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(context);
            rQueue.add(request);
        }


        expenseViewHolder.expensePicture.setImageURI(Uri.parse(expenses.get(i).expensePhoto));
        expenseViewHolder.txtSpenseType.setText(expenses.get(i).expenseType);
//        expenseViewHolder.txtDescription.setText(expenses.get(i).description);
        expenseViewHolder.txtAmount.setText(expenses.get(i).amount);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView expenseListView) {
        super.onAttachedToRecyclerView(expenseListView);
    }

    public String getStringImage(String path) {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=150;

            // Find the correct scale value. It should be the power of 2.
            int scale = 8;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeFile(path,o2);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}