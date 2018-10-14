package com.keby_lcs.lcs.kebitour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.keby_lcs.lcs.kebitour.KEBIAPI.ResponseError;
import com.keby_lcs.lcs.kebitour.KEBIAPI.ResponseResult;
import com.keby_lcs.lcs.kebitour.KEBIAPI.TourApiService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //취소버튼 구현
        Button cancelButton = (Button)findViewById(R.id.signup_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        //최소버튼 구현 끝
        Button signupButton = (Button) findViewById(R.id.signup_ok_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //텍스트 객체 연결
                EditText username = (EditText) findViewById(R.id.signup_username_edit_text);
                EditText password1 = (EditText) findViewById(R.id.signup_password_edit_text);
                EditText password2 = (EditText) findViewById(R.id.signup_password_check_edit_text);

                // 객체 내용 가져오기
                String user = username.getText().toString();
                String pwd1 = password1.getText().toString();
                String pwd2 = password2.getText().toString();
                if (user.getBytes().length <= 0 && user == user.trim())//빈값, 공백처리
                {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (pwd1.getBytes().length <= 0 && pwd1 == pwd1.trim())//빈값, 공백처리
                {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (pwd2.getBytes().length <= 0 && pwd2 == pwd2.trim())//빈값, 공백처리)
                {
                    Toast.makeText(getApplicationContext(), "비밀번호확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (!pwd1.equals(pwd2)){
                    Toast.makeText(getApplicationContext(), "비밀번호 확인이 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                } else{
                    TourApiService apiService = TourApiService.retrofit.create(TourApiService.class);
                    final Call<ResponseResult> repo = apiService.signupGo(user,pwd1);
                    repo.enqueue(new Callback<ResponseResult>() {
                        @Override
                        public void onResponse(Call<ResponseResult> call, Response<ResponseResult> response) {
                            if(response.code()==201) {
                                Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 로그인 하세요.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }else{
                                //에러바디는 GSON적용이 안되어있어서 수동으로 파싱해준다.
                                try {
                                    String re = response.errorBody().string();
                                    Gson gson = new Gson();
                                    ResponseError error = gson.fromJson(re,ResponseError.class);

                                    if(error.getUsername().get(0).getBytes().length >=0){ Toast.makeText(getApplicationContext(), error.getUsername().get(0), Toast.LENGTH_SHORT).show(); }
                                    else if(error.getPasswordd().get(0).getBytes().length >=0){ Toast.makeText(getApplicationContext(), error.getPasswordd().get(0), Toast.LENGTH_SHORT).show(); }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseResult> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });


                }
            }
        });

    }
}
