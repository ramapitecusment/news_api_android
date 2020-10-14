package com.example.newsapp.Models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = LaterArticle.class, version = 1)
public abstract class LaterArticleDatabase extends RoomDatabase {

    private static LaterArticleDatabase instance;

    public abstract LaterArticleDao laterArticleDao();

    public static synchronized LaterArticleDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    LaterArticleDatabase.class, "later_article_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private LaterArticleDao laterArticleDao;
        private PopulateDbAsyncTask(LaterArticleDatabase db) {
            laterArticleDao = db.laterArticleDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            laterArticleDao.insert(new LaterArticle("Eddie Van Halen's Brother Alex Remembers " +
                    "Late Rocker: See More Tributes - Entertainment Tonight",
                    "Entertainment Tonight",
                    "The Van Halen founder died on Oct. 6 after a years-long battle " +
                            "with cancer. He was 65.",
                    "2020-10-08T14:30:30Z",
                    "https://www.etonline.com/sites/default/files/styles/max_1280x720/public/images/2020-10/gettyimages-635762173_0.jpg?h=b2d3ced3&itok=NB42Y_lU",
                    "https://www.etonline.com/eddie-van-halen-dead-at-65-david-lee-roth-jon-bon-jovi-and-more-pay-tribute-154284"

                    ));
            laterArticleDao.insert(new LaterArticle("Presidential debate: Trump refuses to take " +
                    "part in virtual TV event - BBC News",
                    "BBC News",
                    "The president says a commission's demand for it to be held remotely " +
                            "is to \\\"protect\\\" Joe Biden.",
                    "2020-10-08T14:15:00Z",
                    "https://ichef.bbci.co.uk/news/1024/branded_news/E24B/production/_114813975_2_trump-biden_comp_getty.jpg\"",
                    "https://www.bbc.com/news/election-us-2020-54465139"

            ));
            laterArticleDao.insert(new LaterArticle("Eddie Van Halen's Brother Alex Remembers " +
                    "Late Rocker: See More Tributes - Entertainment Tonight",
                    "Economic relief talks are back on, Trump asserts, two days after he cut them off - The Washington Post",
                    "",
                    "2020-10-08T13:18:00Z",
                    "https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/TLBANBQJMEI6XBYZBXYVTUKHSQ.jpg&w=1440",
                    "https://www.washingtonpost.com/us-policy/2020/10/08/trump-stimulus-pelosi/"

            ));
            return null;
        }
    }
}
