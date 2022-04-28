package com.axolotls.pracheta.activity;

import static com.axolotls.pracheta.helper.ApiConfig.createJWT;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.axolotls.pracheta.R;
import com.axolotls.pracheta.adapter.RechargeSpinnerAdapter;
import com.axolotls.pracheta.helper.Constant;
import com.axolotls.pracheta.helper.Session;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MobileRechargeActivity extends AppCompatActivity {

    Spinner edtOperator;
    TextView payButton;
    private ProgressBar loadingPB;
    EditText edtphone_number,edtAmount;
    Switch rechargeSwitch;
    String recharge_type = "1";
    ImageView image;
    AlertDialog.Builder builder;
    Button btnCheckStatus;
    String Status,TransId,ErrorMsg;
    TextView tvStatus;
    String phone_number,recharge_amount;
    static String operator_code,CircleCode;
    ImageView imageMenu;
    TextView textView;
    Spinner Circlespinner;
//    String JSON_STRING = "{\"Status\":\"xyz\"}";
//    String JSON_STRING;

    // create a string of all characters
    Session session;

    String UserMobileNumber,WalletId;
    static double walletbalance, minamount = 15;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_recharge_activity);
        edtOperator = findViewById(R.id.edtOperator);
        payButton = findViewById(R.id.payButton);
        loadingPB = findViewById(R.id.idLoadingPB);
        edtphone_number = findViewById(R.id.edtphone_number);
        edtAmount = findViewById(R.id.edtAmount);
        loadingPB.setVisibility(View.GONE);
        Circlespinner = findViewById(R.id.CircleSpinner);
        imageMenu = findViewById(R.id.imageMenu);
        rechargeSwitch = (Switch) findViewById(R.id.rechargeSwitch);
        btnCheckStatus = findViewById(R.id.btnCheckStatus);
        tvStatus = findViewById(R.id.tvStatus);
        session = new Session(getApplicationContext());
        textView = new TextView(this);
//        minamount = 99;
        walletbalance = Double.parseDouble(session.getData(Constant.WALLET_BALANCE));
//        try {
//            // get JSONObject from JSON file
//            JSONObject obj = new JSONObject(JSON_STRING);
//            // fetch JSONObject named employee
////            JSONObject employee = obj.getJSONObject("Status");
//            // get employee name and salary
//            Status = obj.getString("Status");
//            // set employee name and salary in TextView's
//            tvStatus.setText("Name: "+Status);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        builder = new AlertDialog.Builder(MobileRechargeActivity.this);
        image = new ImageView(MobileRechargeActivity.this);

        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.india_states));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Circlespinner.setAdapter(aa);

        Integer[] integers=new Integer[]{R.drawable.ic_airtel, R.drawable.ic_vi,R.drawable.ic_jio};
        String[] strings=new String[]{"Airtel Prepaid","VI Prepaid","Jio Prepaid"};
        RechargeSpinnerAdapter adapter = new RechargeSpinnerAdapter(getApplicationContext(),integers,strings);
        edtOperator.setAdapter(adapter);

//        btnCheckStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckRechargeStatus();
//            }
//        });
        edtOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String strOperatorCode = String.valueOf(edtOperator.getSelectedItemPosition());
                if(strOperatorCode.equals("0")){
                    operator_code = "AT";

                }if(strOperatorCode.equals("1")){
                    operator_code = "VF";

                }if(strOperatorCode.equals("2")){
                    operator_code = "JIO";

                }
                System.out.println("OPERATOR_CODE=" + operator_code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rechargeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                Log.v("Switch State=", "" + isChecked);

                if (isChecked) {
                    recharge_type = "2";
//                    minamount = 150;
                    Integer[] integers=new Integer[]{R.drawable.ic_airtel, R.drawable.ic_vi,R.drawable.ic_jio};
                    String[] strings=new String[]{"Airtel Postpaid","VI Postpaid","Jio Postpaid"};
                    RechargeSpinnerAdapter adapter = new RechargeSpinnerAdapter(getApplicationContext(),integers,strings);
                    edtOperator.setAdapter(adapter);

                    edtOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            String strOperatorCode = String.valueOf(edtOperator.getSelectedItemPosition());
                            if(strOperatorCode.equals("0")){
                                operator_code = "ATP";

                            }if(strOperatorCode.equals("1")){
                                operator_code = "VFP";

                            }if(strOperatorCode.equals("2")){
                                operator_code = "RJC";

                            }
                            System.out.println("OPERATOR_CODE=" + operator_code);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    recharge_type = "1";
                    recreate();
//                    minamount = 99;

                }
            }
        });

        Circlespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CircleCode = String.valueOf(Circlespinner.getSelectedItemPosition());

                System.out.println("CircleCode=" + CircleCode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recharge_amount = edtAmount.getText().toString();
                phone_number = edtphone_number.getText().toString();
                if (phone_number.length() != 10) {
                    edtphone_number.setError("Enter mobile number");
                    edtphone_number.setFocusable(true);
                }else if (CircleCode.equals("0")){
                    ((TextView)Circlespinner.getSelectedView()).setError("Select Circle");
                }else if (recharge_amount.length() == 0 || Double.valueOf(recharge_amount) < minamount) {
                    edtAmount.setError("For Prepaid / Postpaid recharge Amount should be greater than 15 Rs.");
                    edtAmount.setFocusable(true);
                }else{

                    Log.e("Your Response", "Else part execute"+walletbalance);
                    Log.e("Your Response", "Else part execute"+Double.valueOf(recharge_amount));

                    if(Double.valueOf(recharge_amount) > walletbalance){

                        Toast.makeText(getApplicationContext(), "Insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        postDataUsingVolley1();
//                        Toast.makeText(getApplicationContext(), "Insufficient Wallet Balance1", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Your Response", "Else part execute");

                }
// calling a method to post the data and passing our name and job.
//                postDataUsingVolley();
            }
        });
    }
    public void postDataUsingVolley1() {

        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
            loadingPB.setVisibility(View.VISIBLE);

            UserMobileNumber = session.getData(Constant.MOBILE);
//            WalletId = .setText(activity.getResources().getString(R.string.wallet_balance) + "\t:\t" + session.getData(Constant.CURRENCY) + session.getData(Constant.WALLET_BALANCE));
            WalletId = session.getData(Constant.ID);

            Log.d("new Log","log data=="+UserMobileNumber+"\t"+WalletId);

            phone_number = edtphone_number.getText().toString();
            recharge_amount = edtAmount.getText().toString();

            Log.d("data==","data=="+phone_number+"\t"+recharge_amount+"\t"+operator_code+"\t"+CircleCode);
            String YourUrl = "https://pracheta.co/admin/api-firebase/recharge.php";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest request = new StringRequest(Request.Method.POST, YourUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loadingPB.setVisibility(View.GONE);

                    if (!response.equals(null)) {

                        Log.e("Your Array Response", response);
                        try {
                            // get JSONObject from JSON file
                            JSONObject obj = new JSONObject(response);
                            // fetch JSONObject named employee
//            JSONObject employee = obj.getJSONObject("Status");
                            // get employee name and salary
                            Status = obj.getString("status");

                            if(Status.equals("Success")){
                                TransId = obj.getString("usertx");
                                textView.setText("Status: "+Status+"\n"+"Transaction Id: "+TransId);
                                // set employee name and salary in TextView's

                                //Uncomment the below code to Set the message and title from the strings.xml file
//                builder.setMessage("HI") .setTitle("Hello");
//                        image.setImageResource(R.drawable.ic_thank_you);
//                        textView.setText(response);
                                //Setting message manually and performing action on button click
                                builder.setMessage("Your recharge request is in process...")
                                        .setCancelable(false)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        }).setView(textView);
//
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Thank You !!!");
                                alert.setIcon(R.mipmap.ic_launcher);
                                alert.show();
                            }

                            if(Status.equals("Pending")){
                                TransId = obj.getString("usertx");
                                ErrorMsg = obj.getString("ErrorMessage");
                                textView.setText("Status: "+Status+"\n"+"Transaction Id: "+TransId+"\n"+"ErrorMessage: "+ErrorMsg);
                                // set employee name and salary in TextView's

                                //Uncomment the below code to Set the message and title from the strings.xml file
//                builder.setMessage("HI") .setTitle("Hello");
//                        image.setImageResource(R.drawable.ic_thank_you);
//                        textView.setText(response);
                                //Setting message manually and performing action on button click
                                builder.setMessage("Your recharge request is Placed")
                                        .setCancelable(false)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        }).setView(textView);
//
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Thank You !!!");
                                alert.setIcon(R.mipmap.ic_launcher);
                                alert.show();
                            }
                            if(Status.equals("Failure")){
                                TransId = obj.getString("usertx");
                                ErrorMsg = obj.getString("ErrorMessage");
                                textView.setText("Status: "+Status+"\n"+"Transaction Id: "+TransId+"\n"+"ErrorMessage: "+ErrorMsg);
                                // set employee name and salary in TextView's

                                //Uncomment the below code to Set the message and title from the strings.xml file
//                builder.setMessage("HI") .setTitle("Hello");
//                        image.setImageResource(R.drawable.ic_thank_you);
//                        textView.setText(response);
                                //Setting message manually and performing action on button click
                                builder.setMessage("Your recharge request is Failed")
                                        .setCancelable(false)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        }).setView(textView);
//
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Oops!!!");
                                alert.setIcon(R.mipmap.ic_launcher);
                                alert.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("Your Array Response", "Data Null");
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error is ", "" + error);
                }
            }) {
                //This is for Headers If You Needed
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put(Constant.AUTHORIZATION, "Bearer " + createJWT("eKart", "eKart Authentication"));
                    return params;
                }
                //Pass Your Parameters here
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id",UserMobileNumber);
                    params.put("wallet_id",WalletId);
                    params.put("number", phone_number);
                    params.put("accesskey", Constant.AccessKeyVal);
                    params.put("recharge_amount",recharge_amount);
                    params.put("operator_code",operator_code);
                    params.put("circle_code",CircleCode);
                    params.put("recharge_type","1");
                    Log.d("Params","params=="+params);
                    return params;
                }
            };
            queue.add(request).setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 0;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 0; //retry turn off
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            queue.getCache().remove(YourUrl);

        } else {
            Toast.makeText(getApplicationContext(), "Login Or Register First", Toast.LENGTH_SHORT).show();
        }
    }
}
