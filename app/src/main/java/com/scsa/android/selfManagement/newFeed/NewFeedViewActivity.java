package com.scsa.android.selfManagement.newFeed;


import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scsa.android.selfManagement.R;

import org.xmlpull.v1.XmlPullParser;

import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class NewFeedViewActivity extends AppCompatActivity {

    private static final int ADD_TTS = 101;
    static private String rootNews;
    static private String s;
    private TextToSpeech tts;
    ListView newsFeedList;
    NewsAdapter<Check> newsAda;
    LayoutInflater inflater;
    TextView newFeedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feed_view);
        newsFeedList = findViewById(R.id.result);
        newFeedTv = findViewById(R.id.newFeedTv);

        newsFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adview, View view, int position, long id) {

                Check selItem = (Check) adview.getAdapter().getItem(position);
                Toast.makeText(NewFeedViewActivity.this, "자세한 뉴스로 연결돼요", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(selItem.getLink()));
                startActivity(i);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, ADD_TTS, Menu.NONE, "TTS 뉴스");
        tts = new TextToSpeech(this , new TextToSpeech.OnInitListener(){
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(Locale.KOREA);
                    //int result = tts.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(NewFeedViewActivity.this, "Language is not available.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NewFeedViewActivity.this, "Could not initialize TextToSpeech.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case ADD_TTS:
                for(int i =0; i < 10; i++) {
                    s = newsAda.getItem(i).toString();
                    tts.speak(s, TextToSpeech.QUEUE_ADD, null);
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            // 음성 출력을 중단하고 대기 Queue 의 데이터를 비운다.
            tts.stop();
            // TTS 엔진에 할당된 리소스를 해제하기위해 반드시 호출한다.
            tts.shutdown();
        }
    }

    public void Chosun(View view) {
        rootNews = "https://www.chosun.com/arc/outboundfeeds/rss/?outputType=xml";
        getNews();
        newFeedTv.setText("조선일보");
    }

    public void joongang(View view) {
        rootNews = "https://rss.joins.com/joins_homenews_list.xml";
        getNews();
        newFeedTv.setText("중앙일보");
    }

    public void mk(View view) {
        rootNews = "https://file.mk.co.kr/news/rss/rss_30100041.xml";
        getNews();
        newFeedTv.setText("매일경제");
    }

    public void jj(View view) {
        rootNews = "https://rss.donga.com/total.xml";
        getNews();
        newFeedTv.setText("동아일보");
    }

    public void getNews() {
        new XMLParserTask().execute(rootNews);
    }



    class XMLParserTask extends AsyncTask<String, String, ArrayList<Check>> {

        ArrayList<Check> list = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();

        protected ArrayList<Check> doInBackground(String... arg) {
            try {

                parser.setInput(new URL(arg[0]).openConnection()
                        .getInputStream(), null);
                int eventType = parser.getEventType();
                Check ch = null;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = null;
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("item")) {
                                ch = new Check();
                            } else if (ch != null) {
                                if (name.equalsIgnoreCase("title")) {
                                    ch.setTitle(parser.nextText());
                                } else if (name.equalsIgnoreCase("description")) {
                                    ch.setDesc(parser.nextText());
                                } else if (name.equalsIgnoreCase("link")) {
                                    ch.setLink(parser.nextText());
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            name = parser.getName();
                            if (name.equalsIgnoreCase("item") && ch != null) {
                                list.add(ch);
                            }
                            break;
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                Log.e("NewFeedViewActivity", e.getMessage(), e);
                throw new RuntimeException(e);
            }
            return list;
        }

        protected void onPostExecute(ArrayList<Check> result) {
            newsAda = new NewsAdapter<Check>(result, R.layout.newsfeed_item);
            newsFeedList.setAdapter(newsAda);
            inflater = LayoutInflater.from(NewFeedViewActivity.this);
        }

    }

    class NewsAdapter<M> extends BaseAdapter {

        ArrayList<Check> chkList;
        int layout_id;

        public NewsAdapter(ArrayList<Check> cList, int layout) {
            this.chkList = cList;
            this.layout_id = layout;
        }


        // 리스트 표출할 데이터의 전체 크기를 리턴
        @Override
        public int getCount() {
            return chkList.size();
        }

        // 특정 포지션을 통해 선택된 객체 정보를 가져옴
        @Override
        public Object getItem(int position) {
            return chkList.get(position);
        }

        // 포지션값을 그대로 리턴
        @Override
        public long getItemId(int position) {
            return position;
        }

        int i = 0;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(layout_id, null);

                // ImageView, TextView 객체를 가져온다.
                TextView newsTitleTv = convertView.findViewById(R.id.newsTitleTv);
                TextView newsDescTv = convertView.findViewById(R.id.newsDescTv);

                holder = new ViewHolder();

                holder.newsTitleTv = newsTitleTv;
                holder.newsDescTv = newsDescTv;

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // position 해당하는 Movie 객체를 가져온다.
            Check check = (Check) newsAda.getItem(position);

            // 각각의 View에 Movie 객체 안의 데이터를 설정한다.
            holder.newsTitleTv.setText(check.getTitle());
            holder.newsDescTv.setText(check.getDesc());
            return convertView;
        }
    }
    static class ViewHolder {
        TextView newsTitleTv;
        TextView newsDescTv;

    }

    // 리소스 파일에 정의된 음의 빠르기(Speech Rate) 에 대한 해당 ID 의 버튼을 얻고,
    public void speechQuick(View view){
        tts.setSpeechRate( (float)2 );
    }
    public void speechNormal(View view){
        tts.setSpeechRate( (float)1 );
    }
    public void speechSlow(View view){
        tts.setSpeechRate( (float)0.5 );
    }
    // 리소스 파일에 정의된 음의 높낮이(Pitch) 에 대한 해당 ID 의 버튼을 얻고,
    public void pitchHigh(View view){
        tts.setPitch( (float)2 );
    }
    public void pitchNormal(View view){
        tts.setPitch( (float)1 );
    }
    public void pitchLow(View view){
        tts.setPitch( (float)0.5 );
    }
}

