package com.example.boiwala;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragmentt extends Fragment {
    public HomeFragmentt() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModels;
    private FirebaseFirestore firebaseFirestore;

    //////////Banner Slider
    private ViewPager bannerSliderViewPager;
    private List<SilderModel> silderModelList;
    private int currentPage = 2;
    private Timer timer;
    private final long DELAY_TIME = 3000, PERIOD_TIME = 3000;
    //////////

    //////////Horizontal product Layout
    private TextView horizontalLayoutTitle;
    private Button btnHorizontalViewAll;
    private RecyclerView horizontalRecyclerView;

    //////////Horizontal product Layout

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmentt_home, container, false);

        categoryRecyclerView = view.findViewById(R.id.catagoryRecView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);

        categoryModels = new ArrayList<CategoryModel>();

        categoryAdapter = new CategoryAdapter(categoryModels);
        categoryRecyclerView.setAdapter(categoryAdapter);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        categoryModels.clear();
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String icon = documentSnapshot.getString("icon");
                                String categoryName = documentSnapshot.getString("categoryName");
                                categoryModels.add(new CategoryModel(icon, categoryName));
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        //////////Banner slider
        bannerSliderViewPager = view.findViewById(R.id.bannerSliderViewPager);
        silderModelList = new ArrayList<>();

        silderModelList.add(new SilderModel(R.drawable.banner_3));
        silderModelList.add(new SilderModel(R.drawable.banner_6));
        silderModelList.add(new SilderModel(R.drawable.banner_1));
        silderModelList.add(new SilderModel(R.drawable.banner_3));
        silderModelList.add(new SilderModel(R.drawable.banner_4));
        silderModelList.add(new SilderModel(R.drawable.banner_6));
        silderModelList.add(new SilderModel(R.drawable.banner_5));
        silderModelList.add(new SilderModel(R.drawable.banner_1));
        silderModelList.add(new SilderModel(R.drawable.banner_3));

        SliderAdapter sliderAdapter = new SliderAdapter(silderModelList);
        bannerSliderViewPager.setAdapter(sliderAdapter);
        bannerSliderViewPager.setClipToPadding(false);
        bannerSliderViewPager.setPageMargin(20);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    pageLooper();
                }
            }
        };

        bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

        startBannerSlideShow();

        bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pageLooper();
                stopBannerSlideShow();
                if(event.getAction()==MotionEvent.ACTION_UP) {
                    startBannerSlideShow();
                }
                return false;
            }
        });

        //////////Banner slider


        //////////Horizontal product Layout

        horizontalRecyclerView = view.findViewById(R.id.horizontalScrollLayoutRecyclerView);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(linearLayoutManager1);

        List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();

        HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductScrollModelList);
        horizontalRecyclerView.setAdapter(horizontalProductScrollAdapter);

        firebaseFirestore.collection("Home").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                long noOfProducts = documentSnapshot.getLong("no_of_products");
                                for(long x = 1; x<=noOfProducts; x++) {
                                    horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.getString("product_id_" + x),
                                            documentSnapshot.getString("product_image_"+x), documentSnapshot.getString("product_title_"+x),
                                            documentSnapshot.getString("product_subtitle_"+x), documentSnapshot.getString("product_price_"+x)));
                                }
                            }
                            /////////Grid product layout
                            GridView gridView = view.findViewById(R.id.gridProductLayoutGridView);

                            if(horizontalProductScrollModelList.size()>0) {
                                gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList));
                            }
                            //////////Grid Product layout
                            horizontalProductScrollAdapter.notifyDataSetChanged();
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //////////Horizontal product Layout

        /////////GridProductLayout
        List<HorizontalProductScrollModel> horizontalProductScrollModelList2 = new ArrayList<>();

        firebaseFirestore.collection("PopularProducts").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                long noOfProducts = documentSnapshot.getLong("no_of_products");
                                for(long x = 1; x<=noOfProducts; x++) {
                                    horizontalProductScrollModelList2.add(new HorizontalProductScrollModel(documentSnapshot.getString("product_id_" + x),
                                            documentSnapshot.getString("product_image_"+x), documentSnapshot.getString("product_title_"+x),
                                            documentSnapshot.getString("product_subtitle_"+x), documentSnapshot.getString("product_price_"+x)));
                                }
                            }
                            /////////Grid product layout
                            GridView gridView = view.findViewById(R.id.gridProductLayoutGridView);

                            if(horizontalProductScrollModelList2.size()>0) {
                                gridView.setAdapter(new GridProductLayoutAdapter(horizontalProductScrollModelList2));
                            }
                            //////////Grid Product layout
                        }
                        else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        /////////GridProductLayout



        return view;
    }

    //////////Banner slider
    private void pageLooper() {
        if(currentPage==silderModelList.size()-2) {
            currentPage = 2;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }
        if(currentPage==1) {
            currentPage = silderModelList.size()-3;
            bannerSliderViewPager.setCurrentItem(currentPage, false);
        }
    }

    private void startBannerSlideShow() {
        Handler handler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage>=silderModelList.size()) {
                    currentPage = 1;
                }
                bannerSliderViewPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_TIME, PERIOD_TIME);
    }

    private void stopBannerSlideShow() {
        timer.cancel();
    }


    /////////
}