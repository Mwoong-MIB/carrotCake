package com.scsa.android.selfManagement.helloWord;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scsa.android.selfManagement.R;

import java.util.Random;

public class HelloWordActivityStartView extends AppCompatActivity {

    FrameLayout f;
    FrameLayout.LayoutParams params;
    int count=0;
    int delay=1200;
    static boolean threadEndFlag=true; // 쓰레드 끄기
    WordTask mt;				// 쓰레드 구현

    int myWidth;  // 내 폰의 너비
    int myHeight; // 내 폰의 높이
    int imgWidth=80;  //그림 크기
    int imgHeight=600;//그림 크기
    Random r = new Random();  // 이미지 위치를 랜덤하게 발생시킬 객체

    SoundPool pool;   // 소리
    int liveMouse;    // 소리
    MediaPlayer mp;   // 소리

    int x=200;        //시작위치
    int y=200;        //시작위치

    int level=1;      // 게임 레벨
    int nums=0;

    TextView hView1;
    TextView hView2;
    TextView hView3;
    TextView hView4;
    TextView hView5;
    TextView hView6;
    TextView hView7;
    TextView hView8;
    TextView hView9;
    TextView[] texts;

    TextView helloSubtitletv1;
    TextView helloSubtitletv2;
    TextView helloSubtitletv3;
    TextView helloSubtitletv4;
    TextView helloSubtitletv5;
    TextView helloSubtitletv6;
    TextView helloSubtitletv7;
    TextView helloSubtitletv8;
    TextView helloSubtitletv9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_word_start_view2);


        f=(FrameLayout) findViewById(R.id.frame);
        params=new FrameLayout.LayoutParams(1, 1);

        //디스플레이 크기 체크
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        myWidth=metrics.widthPixels;
        myHeight=metrics.heightPixels;
        Log.d("game","My Window "+myWidth+" : "+myHeight);

        //사운드 셋팅
        pool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        liveMouse = pool.load(this, R.raw.slap, 1);
        mp=MediaPlayer.create(this, R.raw.bgm);
        mp.setLooping(true);

        //텍스트 뷰 세팅
        hView1 = findViewById(R.id.hView1);
        hView2 = findViewById(R.id.hView2);
        hView3 = findViewById(R.id.hView3);
        hView4 = findViewById(R.id.hView4);
        hView5 = findViewById(R.id.hView5);
        hView6 = findViewById(R.id.hView6);
        hView7 = findViewById(R.id.hView7);
        hView8 = findViewById(R.id.hView8);
        hView9 = findViewById(R.id.hView9);

        helloSubtitletv1 = findViewById(R.id.helloSubtitletv1);
        helloSubtitletv2 = findViewById(R.id.helloSubtitletv2);
        helloSubtitletv3 = findViewById(R.id.helloSubtitletv3);
        helloSubtitletv4 = findViewById(R.id.helloSubtitletv4);
        helloSubtitletv5 = findViewById(R.id.helloSubtitletv5);
        helloSubtitletv6 = findViewById(R.id.helloSubtitletv6);
        helloSubtitletv7 = findViewById(R.id.helloSubtitletv7);
        helloSubtitletv8 = findViewById(R.id.helloSubtitletv8);
        helloSubtitletv9 = findViewById(R.id.helloSubtitletv9);

        init(9);

    }
    public void init(int nums){
        //초기화
        count=0;
        threadEndFlag=true;
        this.nums=nums;
        delay=(int)(delay*(10-level)/10.);

        f.removeAllViews();

        //텍스트 뷰 담을 배열 생성
        texts = new TextView[9];
//        hView1.setText("I");
//        hView2.setText("never");
//        hView3.setText("dreamed");
//        hView4.setText("about");
//        hView5.setText("success");
//        hView6.setText("I");
//        hView7.setText("worked");
//        hView8.setText("for");
//        hView9.setText("it");
        f.addView(hView1, params);
        f.addView(hView2, params);
        f.addView(hView3, params);
        f.addView(hView4, params);
        f.addView(hView5, params);
        f.addView(hView6, params);
        f.addView(hView7, params);
        f.addView(hView8, params);
        f.addView(hView9, params);
        texts[0] = hView1;
        texts[1] = hView2;
        texts[2] = hView3;
        texts[3] = hView4;
        texts[4] = hView5;
        texts[5] = hView6;
        texts[6] = hView7;
        texts[7] = hView8;
        texts[8] = hView9;
        hView1.setOnClickListener(h1);
        hView2.setOnClickListener(h2);
        hView3.setOnClickListener(h3);
        hView4.setOnClickListener(h4);
        hView5.setOnClickListener(h5);
        hView6.setOnClickListener(h6);
        hView7.setOnClickListener(h7);
        hView8.setOnClickListener(h8);
        hView9.setOnClickListener(h9);


        mt = new WordTask();  //일정 간격으로 이미지 위치를 바꿀 쓰레드 실행
        mt.execute();
    }
    protected void onResume() {
        super.onResume();
        mp.start();
    };
    protected void onPause() {
        super.onPause();
        mp.stop();
    };

    protected void onDestroy() {
        super.onDestroy();
        mp.release();
        mt.cancel(true);
        threadEndFlag = false;

    };
    View.OnClickListener  h1 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv1.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "I", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h2 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv2.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "never", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h3 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv3.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "dreamed", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h4 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv1.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "about", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h5 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv5.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "success", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h6 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv6.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "I", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h7 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv7.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "worked", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h8 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv8.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "for", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };
    View.OnClickListener  h9 = new View.OnClickListener() {
        public void onClick(View v) {
            count++;
            TextView tv = (TextView) v;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helloSubtitletv9.setTextColor(getColor(R.color.heart));
            }
            pool.play(liveMouse, 1, 1, 0, 0, 1);  // 소리 내기
            tv.setVisibility(View.INVISIBLE);
            Toast.makeText(HelloWordActivityStartView.this, "it", Toast.LENGTH_LONG).show();
            if(count==nums){   // 쥐를 다 잡았을때
                threadEndFlag=false;
                mt.cancel(true);

                AlertDialog.Builder dia=new AlertDialog.Builder(HelloWordActivityStartView.this);
                dia.setMessage("나는 결코 성공을 꿈꾸지 않았다." + "\n" + "나는 꿈을 위해 행동했다.");
                dia.setPositiveButton("오늘도 화이팅", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dia.show();
            }
        }
    };

    public void update(){
        if(!threadEndFlag) return;
        Log.d("game", "update:");
        for(TextView txt:texts){
            x=r.nextInt(myWidth-imgWidth);
            y=r.nextInt(myHeight-imgHeight);

            txt.layout(x, y, x+imgWidth, y+imgHeight);
            txt.invalidate();
        }

    }
    class WordTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {// 다른 쓰레드
            while(threadEndFlag){
                //다른 쓰레드에서는 UI를 접근할 수 없으므로
                publishProgress();	//자동으로 onProgressUpdate() 가 호출된다.
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {e.printStackTrace();}
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            update();
        }
    };//end MouseTask
}