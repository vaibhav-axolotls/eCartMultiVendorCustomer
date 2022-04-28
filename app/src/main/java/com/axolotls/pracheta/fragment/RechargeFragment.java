package com.axolotls.pracheta.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.axolotls.pracheta.activity.BroadbandRechargeActivity;
import com.axolotls.pracheta.activity.DTHRechargeActivity;
import com.axolotls.pracheta.activity.ElectricityBillPaymentActivity;
import com.axolotls.pracheta.activity.GasBillPaymentActivity;
import com.axolotls.pracheta.activity.LandlineBillPayment;
import com.axolotls.pracheta.activity.MobileRechargeActivity;
import com.axolotls.pracheta.activity.WaterBillPaymentActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.axolotls.pracheta.R;
import com.axolotls.pracheta.activity.LoginActivity;
import com.axolotls.pracheta.activity.MainActivity;
import com.axolotls.pracheta.helper.ApiConfig;
import com.axolotls.pracheta.helper.Constant;
import com.axolotls.pracheta.helper.Session;
import com.axolotls.pracheta.helper.Utils;

public class RechargeFragment extends Fragment {
    View root;
    public TextView tvName, tvMobile, tvMenuHome, tvMenuCart, tvMenuOrders, tvMenuChangePassword, tvMenuManageAddresses, tvMenuReferEarn, tvMenuContactUs, tvMenuAboutUs, tvMenuRateUs, tvMenuShareApp, tvMenuFAQ, tvMenuTermsConditions, tvMenuPrivacyPolicy, tvMenuLogout;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imgProfile;
    @SuppressLint("StaticFieldLeak")
    public static TextView tvMenuNotification, tvMenuTransactionHistory, tvMenuWalletHistory;
    public ImageView imgEditProfile;
    Session session;
    Activity activity;
    LinearLayout lytMenuGroup, lytProfile;
    Fragment fragment;
    Bundle bundle;
    AlertDialog.Builder builder;

    public static TextView tvWallet;
    ImageView MobRecharge;
    LinearLayout llRecharge,llDth,llGas,llWater,llBroadband,llElectricity,llLandline;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recharge, container, false);
        activity = getActivity();
        session = new Session(activity);

        MobRecharge = root.findViewById(R.id.MobRecharge);
        llRecharge = root.findViewById(R.id.llRecharge);
        llDth = root.findViewById(R.id.llDth);
        llGas = root.findViewById(R.id.llGas);
        llWater = root.findViewById(R.id.llWater);
        llBroadband = root.findViewById(R.id.llBroadband);
        llElectricity = root.findViewById(R.id.llElectricity);
        llLandline = root.findViewById(R.id.llLandline);

        session = new Session(getActivity());
        builder = new AlertDialog.Builder(getActivity());

        if (session.getBoolean(Constant.IS_USER_LOGIN)) {


            llRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MobileRechargeActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(),"Mobile Recharge",Toast.LENGTH_SHORT).show();
            }
        });

        llDth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), DTHRechargeActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(),"DTH Recharge",Toast.LENGTH_SHORT).show();
            }
        });

        llGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GasBillPaymentActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"Gas Recharge",Toast.LENGTH_SHORT).show();
            }
        });

        llWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WaterBillPaymentActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"Water Bill Pay",Toast.LENGTH_SHORT).show();

            }
        });

        llBroadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BroadbandRechargeActivity.class);
                startActivity(intent);
            }
        });

        llElectricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ElectricityBillPaymentActivity.class);
                startActivity(intent);

            }
        });

        llLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LandlineBillPayment.class);
                startActivity(intent);

            }
        });

        } else {
//            builder.setMessage("HI") .setTitle("Hello");
            //Setting message manually and performing action on button click
//            builder.setMessage("Login / Signup First")
//                    .setCancelable(false)
//                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
////
//            AlertDialog alert = builder.create();
//            //Setting the title manually
//            alert.setTitle("Hello User!!!");
//            alert.setIcon(R.mipmap.ic_launcher);
//            alert.show();

            llRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();
                }
            });

            llDth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();

                }
            });

            llGas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();

                }
            });

            llWater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();


                }
            });

            llBroadband.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();

                }
            });

            llElectricity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();


                }
            });

            llLandline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"Login / Signup First",Toast.LENGTH_SHORT).show();


                }
            });
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}