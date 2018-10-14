package com.keby_lcs.lcs.kebitour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.keby_lcs.lcs.kebitour.KEBIAPI.Token;
import com.keby_lcs.lcs.kebitour.KEBIAPI.TourApiService;
import com.keby_lcs.lcs.kebitour.TourAPI.ApiService;
import com.keby_lcs.lcs.kebitour.TourAPI.TourModel.Item;
import com.keby_lcs.lcs.kebitour.TourAPI.TourModel.ResponseTour;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    String user;
    private ArrayList<Item> itemList;
    private Item item;
    TourApiService tourApiService;
    ApiService apiService;
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //탭 구현 부분
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup(); //호스트 셋업
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.tab1);
        ts1.setIndicator("Home");
        tabHost1.addTab(ts1); //첫 번째 탭 추가
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.tab2);
        ts2.setIndicator("Quest");
        tabHost1.addTab(ts2); // 두번째 탭 추가
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3");
        ts3.setContent(R.id.tab3);
        ts3.setIndicator("Setting");
        tabHost1.addTab(ts3); //세번째 탭 추가


        //로그인 관련 버튼뷰
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button signUpButton = (Button) findViewById(R.id.signup_go_button);

        //로그인 분기문 시작
        if(getPreferences("token") != null){
            //토큰값이 있다면 단순 레이아웃 작업만 진행하고
            //로그인 화면 변화를 위해 해당 객체 포인터를 연결하고
            LinearLayout layout = (LinearLayout) findViewById(R.id.tab3);

            //레이아웃 주소와, 컨텍스트 값을 넘겨줘서 로그인 완료 뷰를 보여준다.
            loginTextView(layout, getApplication());


        }else{
            //토큰값이 없다면 회원가입과 로그인 구현
            //여행 리스트 구현 끝
            Button tourSignupButton = (Button)findViewById(R.id.signup_go_button);
            tourSignupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(intent);
                }
            });

            //회원가입끝
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText username = (EditText) findViewById(R.id.username);
                    EditText password = (EditText) findViewById(R.id.password);
                    //공백(스페이스바)만 눌러서 넘기는 경우도 안된다고 할때에는 아래코드도 살림
                    //getEdit = getEdit.trim();

                    //문자열화 시키기
                    user = username.getText().toString();
                    String pwd = password.getText().toString();

                    //토큰 받아오기위해 필요한 클라이언트 id와 시크릿 키 입력
                    String client_id = "Q6xGpGMM8k0uBcQPfNWgFU8ISnO2pwBMbdf65i9G";
                    String client_secret = "Jn6lkbdssI9fvPzRVFUdxrTSd1DR4ONpVlLA0NP3O34F6TGMYC5SZgHgzx7Cq4MCMXWuRphujUM4q0sKsTQ4vr6JKqH8IPkdCelSOcmPZRU4s5NBbvsogXBs16Ag3Hzn";

                    if (user.getBytes().length <= 0 && user == user.trim())//빈값, 공백처리
                    {
                        Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else if (pwd.getBytes().length <= 0 && pwd == pwd.trim())//빈값, 공백처리
                    {
                        Toast.makeText(getApplicationContext(), "계정을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {//둘다 해당되지 않을경우
                        tourApiService = TourApiService.retrofit.create(TourApiService.class);
                        Call<Token> repoLogin = tourApiService.login(user, pwd, client_id, client_secret, "password");
                        repoLogin.enqueue(new Callback<Token>() {
                            @Override
                            public void onResponse(Call<Token> call, Response<Token> response) {
                                if(response.isSuccessful()){
                                    //웹 서버에서 토큰을 받아온다.
                                    String token = response.body().getAccess_token();

                                    //안드로이드 간단 저장 공간에 두 정보를 저장한다.
                                    savePreferences("username",user);
                                    savePreferences("token",token);

                                    //로그인 완료 토스트 메시지 출력한다.
                                    Toast.makeText(getApplicationContext(), "로그인 완료되었습니다.", Toast.LENGTH_SHORT).show();

                                    //로그인 화면 변화를 위해 해당 객체 포인터를 연결하고
                                    LinearLayout layout = (LinearLayout) findViewById(R.id.tab3);

                                    //레이아웃 주소와, 컨텍스트 값을 넘겨줘서 로그인 완료 뷰를 보여준다.
                                    loginTextView(layout, getApplication());
                                }else {
                                    Toast.makeText(getApplicationContext(),"제대로 입력해주세요!",Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Call<Token> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "에러 발생...!", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


                }
            });
            //로그인 구현 끝
        }// 로그인 분기문 끝

        //여행 리스트 구현
        final ListView listView = (ListView) findViewById(R.id.listView1);
        itemList = new ArrayList<Item>();

        apiService = ApiService.retrofit.create(ApiService.class);
        Call<ResponseTour> tourConvert = apiService.getList("areaBasedList", 25, 37, 1, "AND", "AppTesting", "json");
        tourConvert.enqueue(new Callback<ResponseTour>() {
            @Override
            public void onResponse(Call<ResponseTour> call, Response<ResponseTour> response) {
                if (response.isSuccessful()) {
                    itemList = response.body().getResponse().getBody().getItems().getItem();
                    adapter = new MyAdapter(MainActivity.this, itemList);
                    listView.setAdapter(adapter);
                } else

                {
                }
            }

            @Override
            public void onFailure(Call<ResponseTour> call, Throwable t) {
            }

        });



        //리스트 아이템 하나 클릭시 동작.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TourActivity.class);
                intent.putExtra("contentid", adapter.getItem(position).getContentid());
                intent.putExtra("image", adapter.getItem(position).getFirstimage());
                startActivity(intent);
            }
        });
    }
    //로그인후 뷰처리
    public void loginTextView(LinearLayout linearLayout, Context c){
        //텍스트 뷰 만들기
        TextView userView = new TextView(getApplicationContext());
        LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pram.setMargins(5,10,5,20);
        pram.gravity=Gravity.CENTER;
        pram.width= ActionBar.LayoutParams.WRAP_CONTENT;
        userView.setLayoutParams(pram);
        userView.setTextSize(18);
        userView.setGravity(Gravity.CENTER);
        userView.getLayoutParams();
        userView.setText(getPreferences("username")+"님 환영합니다.");
        linearLayout.removeAllViews();
        linearLayout.addView(userView);

        Button logoutButton = new Button(getApplicationContext());
        logoutButton.setGravity(Gravity.BOTTOM);
        logoutButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        logoutButton.setBackgroundResource(R.drawable.press);
        logoutButton.setGravity(Gravity.CENTER);
        logoutButton.setText("로그아웃");
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePreferences("token");
                removePreferences("username");
                removePreferences("userPk");
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        linearLayout.addView(logoutButton);

    }

    // 값 불러오기
    private String getPreferences(String key){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getString(key, null);//저장했던 key, 두번째는 없을경우 디폴트
    }

    // 값 저장하기
    private void savePreferences(String key, String value){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // 값(Key Data) 삭제하기
    private void removePreferences(String key){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    private void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
