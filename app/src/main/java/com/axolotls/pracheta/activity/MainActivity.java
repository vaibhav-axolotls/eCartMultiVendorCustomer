package com.axolotls.pracheta.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.axolotls.pracheta.fragment.RechargeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.axolotls.pracheta.R;
import com.axolotls.pracheta.fragment.AddressListFragment;
import com.axolotls.pracheta.fragment.CartFragment;
import com.axolotls.pracheta.fragment.CategoryFragment;
import com.axolotls.pracheta.fragment.DrawerFragment;
import com.axolotls.pracheta.fragment.FavoriteFragment;
import com.axolotls.pracheta.fragment.HomeFragment;
import com.axolotls.pracheta.fragment.OrderPlacedFragment;
import com.axolotls.pracheta.fragment.PinCodeFragment;
import com.axolotls.pracheta.fragment.ProductDetailFragment;
import com.axolotls.pracheta.fragment.ProductListFragment;
import com.axolotls.pracheta.fragment.SubCategoryFragment;
import com.axolotls.pracheta.fragment.TrackOrderFragment;
import com.axolotls.pracheta.fragment.TrackerDetailFragment;
import com.axolotls.pracheta.fragment.WalletTransactionFragment;
import com.axolotls.pracheta.helper.ApiConfig;
import com.axolotls.pracheta.helper.Constant;
import com.axolotls.pracheta.helper.DatabaseHelper;
import com.axolotls.pracheta.helper.Session;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, PaymentResultListener {

    static final String TAG = "MAIN ACTIVITY";
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    public static BottomNavigationView bottomNavigationView;
    public static Fragment active;
    public static FragmentManager fm = null;
    public static Fragment homeFragment, categoryFragment, favoriteFragment, drawerFragment,rechargeFragment;
    public static boolean homeClicked = false, categoryClicked = false, favoriteClicked = false, drawerClicked = false, rechargeClicked = false;
    public Activity activity;
    public Session session;
    boolean doubleBackToExitPressedOnce = false;
    DatabaseHelper databaseHelper;
    String from;
    CardView cardViewHamburger;
    TextView toolbarTitle;
    public static PinCodeFragment pinCodeFragment;
    ImageView imageMenu, imageHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        cardViewHamburger = findViewById(R.id.cardViewHamburger);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageMenu = findViewById(R.id.imageMenu);
        imageHome = findViewById(R.id.imageHome);

        activity = MainActivity.this;
        session = new Session(activity);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        from = getIntent().getStringExtra(Constant.FROM);
        databaseHelper = new DatabaseHelper(activity);

        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
            ApiConfig.getCartItemCount(activity, session);
        } else {
            session.setData(Constant.STATUS, "1");
            databaseHelper.getTotalItemOfCart(activity);
        }

        setAppLocal("en"); //Change you language code here

        fm = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        favoriteFragment = new FavoriteFragment();
        drawerFragment = new DrawerFragment();
        rechargeFragment = new RechargeFragment();

        Bundle bundle = new Bundle();
        bottomNavigationView.setSelectedItemId(R.id.navMain);
        active = homeFragment;
        homeClicked = true;
        drawerClicked = false;
        favoriteClicked = false;
        categoryClicked = false;
        rechargeClicked = false;

        try {
            if (!getIntent().getStringExtra("json").isEmpty()) {
                bundle.putString("json", getIntent().getStringExtra("json"));
            }
            homeFragment.setArguments(bundle);
            fm.beginTransaction().add(R.id.container, homeFragment).commit();
        } catch (Exception e) {
            fm.beginTransaction().add(R.id.container, homeFragment).commit();
        }

        @SuppressLint("ResourceType") ColorStateList iconColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor(getResources().getString(R.color.text_unselected)),
                        Color.parseColor(getResources().getString(R.color.colorSecondary))
                });

        bottomNavigationView.setItemIconTintList(iconColorStates);
        bottomNavigationView.setItemTextColor(iconColorStates);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            {
                if (item.getItemId() == R.id.navMain) {
                    if (active != homeFragment) {
                        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
                            Constant.TOOLBAR_TITLE = getString(R.string.hi) + session.getData(Constant.NAME) + "!";
                        } else {
                            Constant.TOOLBAR_TITLE = getString(R.string.hi_user);
                        }
                        invalidateOptionsMenu();
                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        if (!homeClicked) {
                            fm.beginTransaction().add(R.id.container, homeFragment).show(homeFragment).hide(active).commit();
                            homeClicked = true;
                        } else {
                            fm.beginTransaction().show(homeFragment).hide(active).commit();
                        }
                        active = homeFragment;
                    }
                } else if (item.getItemId() == R.id.navCategory) {
                    Constant.TOOLBAR_TITLE = getString(R.string.title_category);
                    invalidateOptionsMenu();
                    if (active != categoryFragment) {
                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        if (!categoryClicked) {
                            fm.beginTransaction().add(R.id.container, categoryFragment).show(categoryFragment).hide(active).commit();
                            categoryClicked = true;
                        } else {
                            fm.beginTransaction().show(categoryFragment).hide(active).commit();
                        }
                        active = categoryFragment;
                    }
                } else if (item.getItemId() == R.id.navWishList) {
                    Constant.TOOLBAR_TITLE = getString(R.string.title_fav);
                    invalidateOptionsMenu();
                    if (active != favoriteFragment) {
                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        if (!favoriteClicked) {
                            fm.beginTransaction().add(R.id.container, favoriteFragment).show(favoriteFragment).hide(active).commit();
                            favoriteClicked = true;
                        } else {
                            fm.beginTransaction().show(favoriteFragment).hide(active).commit();
                        }
                        active = favoriteFragment;
                    }
                } else if (item.getItemId() == R.id.navProfile) {
                    Constant.TOOLBAR_TITLE = getString(R.string.title_profile);
                    invalidateOptionsMenu();
                    if (active != drawerFragment) {
                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        if (!drawerClicked) {
                            fm.beginTransaction().add(R.id.container, drawerFragment).show(drawerFragment).hide(active).commit();
                            drawerClicked = true;
                        } else {
                            fm.beginTransaction().show(drawerFragment).hide(active).commit();
                        }
                        active = drawerFragment;
                    }
                    } else if (item.getItemId() == R.id.navRecharge) {
                    Constant.TOOLBAR_TITLE = "Recharge";
                    invalidateOptionsMenu();
                    if (active != rechargeFragment) {
                        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
                        if (!rechargeClicked) {
                            fm.beginTransaction().add(R.id.container, rechargeFragment).show(rechargeFragment).hide(active).commit();
                            rechargeClicked = true;
                        } else {
                            fm.beginTransaction().show(rechargeFragment).hide(active).commit();
                        }
                        active = rechargeFragment;
                    }
                }
            }
            return false;
        });

        switch (from) {
            case "checkout":
                bottomNavigationView.setVisibility(View.GONE);
                ApiConfig.getCartItemCount(activity, session);
                Fragment fragment = new AddressListFragment();
                bundle = new Bundle();
                bundle.putString(Constant.FROM, "process");
                bundle.putDouble("total", Double.parseDouble(ApiConfig.StringFormat("" + Constant.FLOAT_TOTAL_AMOUNT)));
                fragment.setArguments(bundle);
                fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                break;
            case "share":
                Fragment fragment0 = new ProductDetailFragment();
                Bundle bundle0 = new Bundle();
                bundle0.putInt(Constant.VARIANT_POSITION, getIntent().getIntExtra(Constant.VARIANT_POSITION, 0));
                bundle0.putString(Constant.ID, getIntent().getStringExtra(Constant.ID));
                bundle0.putString(Constant.FROM, "share");
                fragment0.setArguments(bundle0);
                fm.beginTransaction().add(R.id.container, fragment0).addToBackStack(null).commit();
                break;
            case "product":
                Fragment fragment1 = new ProductDetailFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt(Constant.VARIANT_POSITION, getIntent().getIntExtra(Constant.VARIANT_POSITION, 0));
                bundle1.putString(Constant.ID, getIntent().getStringExtra(Constant.ID));
                bundle1.putString(Constant.FROM, "product");
                fragment1.setArguments(bundle1);
                fm.beginTransaction().add(R.id.container, fragment1).addToBackStack(null).commit();
                break;
            case "category":
                Fragment fragment2 = new SubCategoryFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString(Constant.ID, getIntent().getStringExtra(Constant.ID));
                bundle2.putString("name", getIntent().getStringExtra("name"));
                bundle2.putString(Constant.FROM, "category");
                fragment2.setArguments(bundle2);
                fm.beginTransaction().add(R.id.container, fragment2).addToBackStack(null).commit();
                break;
            case "order":
                Fragment fragment3 = new TrackerDetailFragment();
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("model", "");
                bundle3.putString(Constant.ID, getIntent().getStringExtra(Constant.ID));
                fragment3.setArguments(bundle3);
                fm.beginTransaction().add(R.id.container, fragment3).addToBackStack(null).commit();
                break;
            case "payment_success":
                fm.beginTransaction().add(R.id.container, new OrderPlacedFragment()).addToBackStack(null).commit();
                break;
            case "tracker":
                fm.beginTransaction().add(R.id.container, new TrackOrderFragment()).addToBackStack(null).commit();
                break;
            case "wallet":
                fm.beginTransaction().add(R.id.container, new WalletTransactionFragment()).addToBackStack(null).commit();
                break;
        }

        fm.addOnBackStackChangedListener(() ->

        {
            toolbar.setVisibility(View.VISIBLE);
            Fragment currentFragment = fm.findFragmentById(R.id.container);
            Objects.requireNonNull(currentFragment).onResume();
        });

        FirebaseMessaging.getInstance().

                getToken().

                addOnSuccessListener(token ->

                {
                    session.setData(Constant.FCM_ID, token);
                    Register_FCM(token);
                });

        GetProductsName();

        getUserData(activity, session);

    }

    public void setAppLocal(String languageCode) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(languageCode.toLowerCase()));
        resources.updateConfiguration(configuration, dm);
    }

    public void Register_FCM(String token) {
        Map<String, String> params = new HashMap<>();
        if (session.getBoolean(Constant.IS_USER_LOGIN)) {
            params.put(Constant.USER_ID, session.getData(Constant.ID));
        }
        params.put(Constant.FCM_ID, token);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        session.setData(Constant.FCM_ID, token);
                    }
                } catch (JSONException ignored) {

                }

            }
        }, activity, Constant.REGISTER_DEVICE_URL, params, false);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        if (fm.getBackStackEntryCount() == 0) {
            Toast.makeText(this, getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_cart:
                MainActivity.fm.beginTransaction().add(R.id.container, new CartFragment()).addToBackStack(null).commit();
                break;
            case R.id.toolbar_search:
                Fragment fragment = new ProductListFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.FROM, "search");
                bundle.putString(Constant.NAME, activity.getString(R.string.search));
                bundle.putString(Constant.ID, "");
                fragment.setArguments(bundle);
                MainActivity.fm.beginTransaction().add(R.id.container, fragment).addToBackStack(null).commit();
                break;
            case R.id.toolbar_logout:
                session.logoutUserConfirmation(activity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.toolbar_cart).setVisible(true);
        menu.findItem(R.id.toolbar_search).setVisible(true);
        menu.findItem(R.id.toolbar_cart).setIcon(ApiConfig.buildCounterDrawable(Constant.TOTAL_CART_ITEM, activity));

        if (fm.getBackStackEntryCount() > 0) {
            toolbarTitle.setText(Constant.TOOLBAR_TITLE);
            bottomNavigationView.setVisibility(View.GONE);

            cardViewHamburger.setCardBackgroundColor(getColor(R.color.colorPrimaryLight));
            imageMenu.setOnClickListener(v -> fm.popBackStack());

            imageMenu.setVisibility(View.VISIBLE);
            imageHome.setVisibility(View.GONE);
        } else {
            if (session.getBoolean(Constant.IS_USER_LOGIN)) {
                toolbarTitle.setText(getString(R.string.hi) + session.getData(Constant.NAME) + "!");
            } else {
                toolbarTitle.setText(getString(R.string.hi_user));
            }
            bottomNavigationView.setVisibility(View.VISIBLE);
            cardViewHamburger.setCardBackgroundColor(getColor(R.color.colorPrimaryLight));
            imageMenu.setVisibility(View.GONE);
            imageHome.setVisibility(View.VISIBLE);
        }

        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.clear();
        LatLng latLng = new LatLng(Double.parseDouble(session.getCoordinates(Constant.LATITUDE)), Double.parseDouble(session.getCoordinates(Constant.LONGITUDE)));
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Current Location"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(19));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        Objects.requireNonNull(fragment).onActivityResult(requestCode, resultCode, data);

    }


    @SuppressLint("SetTextI18n")
    public static void getUserData(final Activity activity, Session session) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(Constant.GET_USER_DATA, Constant.GetVal);
            params.put(Constant.USER_ID, session.getData(Constant.ID));
            ApiConfig.RequestToVolley((result, response) -> {
                if (result) {
                    try {
                        JSONObject jsonObject_ = new JSONObject(response);
                        if (!jsonObject_.getBoolean(Constant.ERROR)) {
                            JSONObject jsonObject = jsonObject_.getJSONArray(Constant.DATA).getJSONObject(0);
                            session.setUserData(jsonObject.getString(Constant.USER_ID), jsonObject.getString(Constant.NAME), jsonObject.getString(Constant.EMAIL), jsonObject.getString(Constant.COUNTRY_CODE), jsonObject.getString(Constant.PROFILE), jsonObject.getString(Constant.MOBILE), jsonObject.getString(Constant.BALANCE), jsonObject.getString(Constant.REFERRAL_CODE), jsonObject.getString(Constant.FRIEND_CODE), jsonObject.getString(Constant.FCM_ID), jsonObject.getString(Constant.STATUS));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, activity, Constant.USER_DATA_URL, params, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GetProductsName() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.GET_ALL_PRODUCTS_NAME, Constant.GetVal);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean(Constant.ERROR)) {
                        session.setData(Constant.GET_ALL_PRODUCTS_NAME, jsonObject.getString(Constant.DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.GET_PRODUCTS_URL, params, false);
    }


    @Override
    protected void onPause() {
        invalidateOptionsMenu();
        super.onPause();
    }

    @Override
    protected void onResume() {
        ApiConfig.getWalletBalance(activity, session);
        super.onResume();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            WalletTransactionFragment.payFromWallet = false;
            new WalletTransactionFragment().AddWalletBalance(activity, new Session(activity), WalletTransactionFragment.amount, WalletTransactionFragment.msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(activity, getString(R.string.order_cancel), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}