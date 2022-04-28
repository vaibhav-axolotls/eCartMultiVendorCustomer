package com.axolotls.pracheta.activity;

import static com.axolotls.pracheta.helper.ApiConfig.createJWT;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BroadbandRechargeActivity extends AppCompatActivity {
    Spinner Circlespinner;
    Spinner edtOperator;
    TextView payButton;
    private ProgressBar loadingPB;
    EditText edtphone_number,edtAmount;
    ImageView image;
    AlertDialog.Builder builder;
    Button btnCheckStatus;
    String Status,TransId;
    TextView tvStatus,toolbarTitle;
    String phone_number,recharge_amount;
    static String operator_code,CircleCode;
    ImageView imageMenu;
    String randomString;

    //    String JSON_STRING = "{\"Status\":\"xyz\"}";
    // create a string of all characters
    Session session;
    String UserMobileNumber,WalletId;
    double walletbalance;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.electricity_bill_pay_activity);

        edtOperator = findViewById(R.id.edtOperator);
        payButton = findViewById(R.id.payButton);
        loadingPB = findViewById(R.id.idLoadingPB);
        edtphone_number = findViewById(R.id.edtphone_number);
        edtAmount = findViewById(R.id.edtAmount);
        loadingPB.setVisibility(View.GONE);
        Circlespinner = findViewById(R.id.CircleSpinner);
        imageMenu = findViewById(R.id.imageMenu);
        btnCheckStatus = findViewById(R.id.btnCheckStatus);
        tvStatus = findViewById(R.id.tvStatus);
        session = new Session(getApplicationContext());
        toolbarTitle = findViewById(R.id.toolbarTitle);
        walletbalance = Double.parseDouble(session.getData(Constant.WALLET_BALANCE));
        toolbarTitle.setText("Electricity Biil Payment");
        edtphone_number.setHint("Broadband Number");
        textView = new TextView(this);

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

        builder = new AlertDialog.Builder(BroadbandRechargeActivity.this);
        image = new ImageView(BroadbandRechargeActivity.this);

        ArrayAdapter aa = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.india_states));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Circlespinner.setAdapter(aa);

        Integer[] integers=new Integer[]{R.drawable.ic_tikona,R.drawable.ic_hathway};
        String[] strings=new String[]{"Tikona Broadband","Hathway Broadband"};
        RechargeSpinnerAdapter adapter = new RechargeSpinnerAdapter(getApplicationContext(),integers,strings);
        edtOperator.setAdapter(adapter);

        btnCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CheckRechargeStatus();
            }
        });
        edtOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String strOperatorCode = String.valueOf(edtOperator.getSelectedItemPosition());
                if(strOperatorCode.equals("0")) {
                    operator_code = "TBB";
                }if(strOperatorCode.equals("1")){
                    operator_code = "HBB";
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
                }else if (recharge_amount.length() == 0) {
                    edtAmount.setError("Enter the amount");
                    edtAmount.setFocusable(true);
                }else{
                    if(Double.valueOf(recharge_amount) > walletbalance){

                        Toast.makeText(getApplicationContext(), "Insufficient Wallet Balance", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        postDataUsingVolley1();
//                        Toast.makeText(getApplicationContext(), "Insufficient Wallet Balance1", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("Your Response", "Else part execute");

                }
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
            String YourUrl = "https://pracheta.co/admin/api-firebase/billpay_api.php";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest request = new StringRequest(Request.Method.POST, YourUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    loadingPB.setVisibility(View.GONE);

                    if (!response.equals(null)) {
                        Log.e("Your Array Response==","gdfg="+response);

                        try {
                            // get JSONObject from JSON file
                            JSONObject obj = new JSONObject(response);
                            // fetch JSONObject named employee
//            JSONObject employee = obj.getJSONObject("Status");
                            // get employee name and salary
                            Status = obj.getString("status");
                            TransId = obj.getString("usertx");
                            String Status1 = obj.getString("Status");
                            // set employee name and salary in TextView's
                            textView.setText("Status: "+Status+"\n"+"Transaction Id: "+TransId);
                            Log.e("Your Array Response==","gdfgfd="+Status1);

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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("Your Array Response", "Data Null==");
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
                    params.put("transaction_id",randomString);
                    params.put("recharge_type","2");
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
