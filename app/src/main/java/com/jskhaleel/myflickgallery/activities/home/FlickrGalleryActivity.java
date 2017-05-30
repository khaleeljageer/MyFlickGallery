package com.jskhaleel.myflickgallery.activities.home;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jskhaleel.myflickgallery.R;
import com.jskhaleel.myflickgallery.utils.DeviceUtils;
import com.jskhaleel.myflickgallery.webservice.Webservice;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;


public class FlickrGalleryActivity extends AppCompatActivity {

    private RecyclerView rvGalleryList;
    private ArrayList<GalleryBean> galleryBeenList;
    private TextView txtNoResult;
    private static final int SPAN_COUNT = 2;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FlickrListAdapter imageListAdapter;
    private boolean isWebServiceLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setupDefaults();
        setupEvents();
    }

    private void init() {
        rvGalleryList = (RecyclerView) findViewById(R.id.rv_gallery_list);
        txtNoResult = (TextView) findViewById(R.id.txt_no_result);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        galleryBeenList = new ArrayList<>();
    }

    private void setupDefaults() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary_dark, R.color.primary, R.color.primary_light);
        imageListAdapter = new FlickrListAdapter(this, galleryBeenList);

        rvGalleryList.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGalleryList.setLayoutManager(layoutManager);
        rvGalleryList.setAdapter(imageListAdapter);

        if(DeviceUtils.isInternetConnected(this)) {
            getPhotoList(true);
        }else {
            Snackbar.make(rvGalleryList, R.string.no_internet_alert, Snackbar.LENGTH_LONG).show();
        }
    }

    private void setupEvents() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!DeviceUtils.isInternetConnected(FlickrGalleryActivity.this)) {
                    Snackbar.make(rvGalleryList, R.string.no_internet_alert, Snackbar.LENGTH_LONG).show();
                    hideSwipeRefresh();
                    return;
                }
                getPhotoList(false);
            }
        });
    }

    private void getPhotoList(final boolean isShowLoader) {
        isWebServiceLoading = true;
        ProgressDialog pg = null;
        if (isShowLoader) {
            pg = new ProgressDialog(this);
            pg.setCancelable(false);
            pg.setMessage(getString(R.string.loading));
            pg.show();
        }

        final ProgressDialog finalPg = pg;
        new TaskGetFlickrGalleryList(new OnResponseListener() {

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                FlickrGalleryParser flickParser = gson.fromJson(response, FlickrGalleryParser.class);
                if (flickParser != null && flickParser.photos != null) {
                    if (flickParser.photos.photo != null && flickParser.photos.photo.size() > 0) {
                        txtNoResult.setVisibility(View.GONE);
                        rvGalleryList.setVisibility(View.VISIBLE);
                        galleryBeenList.clear();
                        for (FlickrGalleryParser.FPhotos.ListPhoto item : flickParser.photos.photo) {
                            String url = String.format(Webservice.FLICKR_PHOTO, item.farm, item.server, item.id, item.secret);
                            galleryBeenList.add(new GalleryBean(url, item.title, 0));
                        }
                        imageListAdapter.updateList(galleryBeenList);
                    } else {
                        txtNoResult.setVisibility(View.VISIBLE);
                        rvGalleryList.setVisibility(View.GONE);
                    }
                } else {
                    txtNoResult.setVisibility(View.VISIBLE);
                    rvGalleryList.setVisibility(View.GONE);
                }
                if (isShowLoader && finalPg.isShowing()) {
                    finalPg.dismiss();
                } else {
                    hideSwipeRefresh();
                }
                isWebServiceLoading = false;
            }
        }).execute();
    }

    private void hideSwipeRefresh() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private class TaskGetFlickrGalleryList extends AsyncTask<Void, Void, String> {
        private OnResponseListener onResponseListener;

        public TaskGetFlickrGalleryList(OnResponseListener onResponseListener) {
            this.onResponseListener = onResponseListener;
        }

        @Override
        protected String doInBackground(Void... param) {
            InputStream is = null;

            try {
                URL url = new URL(Webservice.FLICKR_GALLARY);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, null, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());

                conn.setReadTimeout(30000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("GET");

                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                return readIt(is);
            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (onResponseListener != null) {
                onResponseListener.onResponse(response);
            }
        }
    }

    public String readIt(InputStream stream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    public class GalleryBean implements Serializable {
        private String url, title;
        private int visibleView;

        public GalleryBean(String url, String title, int visibleView) {
            this.url = url;
            this.title = title;
            this.visibleView = visibleView;
        }

        public int getVisibleView() {
            return visibleView;
        }

        public void setVisibleView(int visibleView) {
            this.visibleView = visibleView;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_sync:
                if(DeviceUtils.isInternetConnected(this)) {
                    if(!isWebServiceLoading) getPhotoList(true);
                }else {
                    Snackbar.make(rvGalleryList, R.string.no_internet_alert, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
