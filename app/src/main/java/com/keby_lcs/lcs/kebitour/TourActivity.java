package com.keby_lcs.lcs.kebitour;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.keby_lcs.lcs.kebitour.KEBIAPI.ResponsePost;
import com.keby_lcs.lcs.kebitour.KEBIAPI.ResponseUserPk;
import com.keby_lcs.lcs.kebitour.KEBIAPI.TourApiService;
import com.keby_lcs.lcs.kebitour.TourAPI.ApiService;
import com.keby_lcs.lcs.kebitour.TourAPI.TourModel.Type25.Item;
import com.keby_lcs.lcs.kebitour.TourAPI.TourModel.Type25.ResponseType;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TourActivity extends AppCompatActivity {
    Intent intent;
    ApiService apiService;
    Item responseContent;
    String contentid;
    String token;
    String username;
    String apiContentid;
    ArrayList<CommentItem> itemList = new ArrayList<CommentItem>();
    static CommentAdapter adapter;
    String imagePath;  //이미지저장 uri
    String msg; //댓글저장

    private static final int PICK_FROM_CAMERA = 0; //카메라 사진가져오기
    private static final int PICK_FROM_ALBUM = 1; //앨범에서 가져오기
    private static final int CROP_FROM_CAMERA = 2; //크롭하기?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        //시작하기전에 값 받아오기
        intent = getIntent();
        //contentid받기
        final int idTemp = (int) intent.getExtras().get("contentid");
        final String mainImageUrl = (String) intent.getExtras().get("image");
        //작성을 위해 문자열화
        contentid = String.valueOf(idTemp);
        //간편 저장소에 저장된 값 초기화
        token = getPreferences("token");
        username = getPreferences("username");

        savePreferences("sendImage", "null");  //댓글 이미지 업로드 기능을 위한 초기화

        final ListView listView = (ListView) findViewById(R.id.jrv_comment_list);
        final View header = getLayoutInflater().inflate(R.layout.header, null, false);
        final View footer = getLayoutInflater().inflate(R.layout.footer, null, false);
        listView.addHeaderView(header);
        listView.addFooterView(footer);
        // listView에 header, footer 추가.

        //시작하기전에 컨텐츠 있는지 확인하기.
        TourApiService postService = TourApiService.retrofit.create(TourApiService.class);
        Call<ResponseBody> postRepo = postService.postGet(contentid);
        postRepo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String text = response.body().string();
                        Gson gson = new Gson();
                        ResponsePost[] post = gson.fromJson(text, ResponsePost[].class);
                        List<ResponsePost> postRe = Arrays.asList(post);
                        if (postRe.size() > 0) {
                            if (contentid.equals(postRe.get(0).getContentid())) {
                                RelativeLayout rv = (RelativeLayout) findViewById(R.id.list_relative_view);
                                Button cButton = (Button) findViewById(R.id.create_button);
                                rv.removeView(cButton);
                                savePreferences("contentYN", 1);
                                savePreferences("postPk", postRe.get(0).getPk());
                            } else {
                                savePreferences("contentYN", 1);


                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    savePreferences("contentYN", 0);
                }
            }//제보제거 완료

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //컨텐츠가 있다면 아래 작업을 수행한다.
        if (getIntPreferences("contentYN") == 1) {
            //아이템이 들어갈 리스트뷰 찾는다.
            TourApiService commentApiService = TourApiService.retrofit.create(TourApiService.class);
            Call<ResponseBody> tourConvert = commentApiService.commentGet(getIntPreferences("postPk"));

            tourConvert.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            //가져오기
                            String text = response.body().string();
                            Gson gson = new Gson();
                            CommentItem[] comRepo = gson.fromJson(text, CommentItem[].class);

                            ArrayList<CommentItem> comRe = new ArrayList<CommentItem>(Arrays.asList(comRepo));
                            ;
                            itemList = comRe;
                            //서버에서 가져오는 부분 끝
                            //어댑터 설정

                            adapter = new CommentAdapter(getApplicationContext(), itemList);
                            listView.setAdapter(adapter);
                            LinearLayout linearView = (LinearLayout) findViewById(R.id.omment_image_linear_layout);
                            for (int i = 0; i < itemList.size(); i++) {
                                CommentItem myItem = itemList.get(i);
                                if (itemList.get(i).getImage() == null) {
                                } //널일경우 이미지 안넣고
                                else {
                                    ImageView imageView = new ImageView(getApplicationContext());
                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                    Picasso.with(getApplicationContext()).load(myItem.getImage()).into(imageView);
                                    linearView.addView(imageView);
                                }
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else

                    {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }

            });
            //나가기전에 0으로초기화
            //댓글달기!!
            ImageView sendView = (ImageView) findViewById(R.id.comment_send);
            ImageView selectImageView = (ImageView) findViewById(R.id.image_file_upload);

            //이미지 버튼을 클릭시 액션
            selectImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchPickPictureIntent();
                }
            });
            //댓글 처리
            sendView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText commentText = (EditText) findViewById(R.id.commenttext);
                    msg = commentText.getText().toString();
                    if (msg.getBytes().length <= 0 && msg == msg.trim()) {
                        Toast.makeText(getApplicationContext(), "댓글을 작성해주세요.", Toast.LENGTH_SHORT).show();
                    } else {

                        if (!getPreferences("sendImage").equals("null")) {
                            //이미지가 있다면 아래 작업 시작

                            if (null != getPreferences("sendImage")) {
                                Uri uri = Uri.parse(getPreferences("sendImage"));
                                Log.v("uri잘가져왔는지", "문의:" + uri.toString());
                                try {
                                    InputStream is = getContentResolver().openInputStream(uri);
                                    sendImageComment(getBytes(is));
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            //댓글 달기 시작
                            TourApiService sendResponse = TourApiService.retrofit.create(TourApiService.class);
                            Call<ResponseBody> sendRepo = sendResponse.commentPost(getIntPreferences("userPk"), getIntPreferences("postPk"), msg);
                            //비동기 접속시작
                            sendRepo.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        //비동기 접속 성공한다면 즉 postPK해당하는 정보들을 가져왔다면
                                        if (response.isSuccessful()) {
                                            String text = response.body().string();

                                            //댓글 내용 다시 받아오기 시작
                                            TourApiService commentApiService = TourApiService.retrofit.create(TourApiService.class);
                                            Call<ResponseBody> tourConvert = commentApiService.commentGet(getIntPreferences("postPk"));

                                            tourConvert.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        try {
                                                            //가져오기
                                                            Log.v("이미지 주소 잘가져왔는지 문의", "" + imagePath);
                                                            String text = response.body().string();
                                                            Gson gson = new Gson();
                                                            CommentItem[] comRepo = gson.fromJson(text, CommentItem[].class);

                                                            ArrayList<CommentItem> comRe = new ArrayList<CommentItem>(Arrays.asList(comRepo));
                                                            ;
                                                            itemList = comRe;
                                                            //서버에서 가져오는 부분 끝
                                                            //어댑터 설정

                                                            adapter = new CommentAdapter(getApplicationContext(), itemList);
                                                            listView.setAdapter(adapter);
                                                            LinearLayout linearView = (LinearLayout) findViewById(R.id.omment_image_linear_layout);
                                                            for (int i = 0; i < itemList.size(); i++) {
                                                                CommentItem myItem = itemList.get(i);
                                                                if (itemList.get(i).getImage() == null) {
                                                                } //널일경우 이미지 안넣고
                                                                else {
                                                                    ImageView imageView = new ImageView(getApplicationContext());
                                                                    LinearLayout.LayoutParams params=(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                                                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                                                    imageView.setLayoutParams(params);
                                                                    Picasso.with(getApplicationContext()).load(myItem.getImage()).into(imageView);
                                                                    linearView.addView(imageView);
                                                                }
                                                            }


                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    } else

                                                    {
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                }

                                            });//댓글내용 가져오기 끝
                                            adapter.notifyDataSetChanged();
                                        } else {
                                            String text = response.errorBody().string();
                                            Log.v("에러내용보기", text);
                                        }

                                    } catch (
                                            IOException e)

                                    {
                                        e.printStackTrace();
                                    }
                                }


                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }

                    }
                }
            });

            savePreferences("postPk", 0);
        }


        //시작하기전에 contentid비교해서 post에 있는지 없는지 확인하기
        //시작하기전에 pk있는지 보고 없으면 가져오기
        if (getIntPreferences("userPk") == -1) {
            Log.v("userPk없으니까 들어왔는지 문의", "들어왔다 이상.");
            TourApiService userGet = TourApiService.retrofit.create(TourApiService.class);
            Call<ResponseBody> user = userGet.getUserPktest(getPreferences("username"));
            user.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String test = null;
                    if (response.isSuccessful()) {
                        try {
                            //Json List이기 때문에 아래와 같은 작업을 진행해 줘야한다.
                            test = response.body().string();
                            Gson gson = new Gson();
                            //배열로 받아서
                            ResponseUserPk[] re = gson.fromJson(test, ResponseUserPk[].class);
                            //List화 시켜준다.
                            List<ResponseUserPk> list = Arrays.asList(re);
                            savePreferences("userPk", list.get(0).pk);
                            Intent intent = new Intent(getApplicationContext(), TourActivity.class);
                            intent.putExtra("contentid", idTemp);
                            intent.putExtra("image", mainImageUrl);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }// pk가져오기 끝!


        //화면 그려주기


        //탭 호스트뷰 셋팅
        TabHost tabHost2 = (TabHost) findViewById(R.id.tabHost2);
        tabHost2.setup(); // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost2.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("Default");
        tabHost2.addTab(ts1); // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost2.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.list_relative_view);
        ts2.setIndicator("USER");
        tabHost2.addTab(ts2); // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")

        //텍스트뷰
        final TextView textView = (TextView) findViewById(R.id.over_view);

        //받아온 내용 보여주기
        apiService = ApiService.retrofit.create(ApiService.class);
        Call<ResponseBody> detail = apiService.getDetail("detailCommon", idTemp, "Y", "Y", "Y", "AND", "AppTesting", "json");
        detail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String ty = response.body().string();
                    Gson gson = new Gson();
                    ResponseType rs = gson.fromJson(ty, ResponseType.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        textView.setText(Html.fromHtml(rs.response.body.items.item.overview, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        textView.setText(Html.fromHtml(rs.response.body.items.item.overview));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        //내용 보여주기 끝

        //사진 크게 보여주기

        //메인 이미지 부분 이미지가 널인경우 이미지 없음을 출력하고
        ImageView mainImageView = (ImageView) findViewById(R.id.main_image_view);
        if (String.valueOf(mainImageUrl) == "null") {
            mainImageView.setImageResource(R.drawable.noimage);
        } else { //널이 아닌경우 이미지를 넣는다.
            Picasso.with(this).load(mainImageUrl).into(mainImageView);
        }

        /////////////////////////제보시작

        //제보하기 버튼
        Button tourEdit = (Button) findViewById(R.id.create_button);
        //제보하기 제한시작
        if (getPreferences("token") == null) {
            //비회원이 클릭시
            tourEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "비회원은 제보할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //회원이 클릭시 해당 주제로 제보가 시작된다.
            tourEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //제보시작
                    TourApiService postService = TourApiService.retrofit.create(TourApiService.class);
                    Call<ResponsePost> post = postService.postCreate(getIntPreferences("userPk"), contentid, contentid);
                    post.enqueue(new Callback<ResponsePost>() {
                        @Override
                        public void onResponse(Call<ResponsePost> call, Response<ResponsePost> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "생성완료! 댓글 및 사진을 남겨주세요,", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), TourActivity.class);
                                intent.putExtra("contentid", idTemp);
                                intent.putExtra("image", mainImageUrl);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "생성실패", Toast.LENGTH_SHORT).show();
                                try {
                                    Log.v("asd", response.errorBody().string().toString());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponsePost> call, Throwable t) {

                        }
                    });//제보하기 끝
                }//클릭 내용 끝
            });
        }


    }

    // 값 불러오기

    private String getPreferences(String key) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, null);//저장했던 key, 두번째는 없을경우 디폴트
    }

    private int getIntPreferences(String key) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getInt(key, -1);//저장했던 key, 두번째는 없을경우 디폴트
    }

    // 값 저장하기
    private void savePreferences(String key, String value) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void savePreferences(String key, int value) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // 값(Key Data) 삭제하기
    private void removePreferences(String key) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    private void removeAllPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    //갤러리 선택하기
    private void dispatchPickPictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            savePreferences("sendImage", "null");
            return;
        } else {
            savePreferences("sendImage", data.getDataString());
        }
    }

    //사진 첨부 댓글달기
    private void sendImageComment(byte[] imageBytes) {
        TourApiService apiService = TourApiService.retrofit.create(TourApiService.class);

        RequestBody postRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getIntPreferences("postPk")));
        RequestBody authorRequest = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getIntPreferences("userPk")));
        RequestBody msgRequest = RequestBody.create(MediaType.parse("text/plain"), msg);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);

        Call<ResponseBody> call = apiService.commentPostImage(authorRequest, postRequest, msgRequest, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String text = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //전송성공 화면 갱신
                    //댓글 내용 다시 받아오기 시작
                    TourApiService commentApiService = TourApiService.retrofit.create(TourApiService.class);
                    Call<ResponseBody> tourConvert = commentApiService.commentGet(getIntPreferences("postPk"));

                    tourConvert.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                try {
                                    //가져오기
                                    ListView listView = (ListView)findViewById(R.id.jrv_comment_list);
                                    Log.v("이미지 주소 잘가져왔는지 문의", "" + imagePath);
                                    String text = response.body().string();
                                    Gson gson = new Gson();
                                    CommentItem[] comRepo = gson.fromJson(text, CommentItem[].class);

                                    ArrayList<CommentItem> comRe = new ArrayList<CommentItem>(Arrays.asList(comRepo));
                                    ;
                                    itemList = comRe;
                                    //서버에서 가져오는 부분 끝
                                    //어댑터 설정
                                    adapter = new CommentAdapter(getApplicationContext(), itemList);
                                    listView.setAdapter(adapter);
                                    LinearLayout linearView = (LinearLayout) findViewById(R.id.omment_image_linear_layout);
                                    for (int i = 0; i < itemList.size(); i++) {
                                        CommentItem myItem = itemList.get(i);
                                        if (itemList.get(i).getImage() == null) {
                                        } //널일경우 이미지 안넣고
                                        else {
                                            ImageView imageView = new ImageView(getApplicationContext());
                                            LinearLayout.LayoutParams params=(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                            imageView.setLayoutParams(params);
                                            Picasso.with(getApplicationContext()).load(myItem.getImage()).into(imageView);
                                            linearView.addView(imageView);
                                        }
                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else

                            {
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }

                    });//댓글내용 가져오기 끝
                    adapter.notifyDataSetChanged();

                } else {
                    String text = null;
                    try {
                        text = response.errorBody().string();
                        Toast.makeText(getApplicationContext(), "전송에러..", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }
}
