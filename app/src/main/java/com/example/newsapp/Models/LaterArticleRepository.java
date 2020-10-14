package com.example.newsapp.Models;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LaterArticleRepository {

    private LaterArticleDao laterArticleDao;
    private LiveData<List<LaterArticle>> allArticle;

    public LaterArticleRepository(Application application){
        LaterArticleDatabase database = LaterArticleDatabase.getInstance(application);
        laterArticleDao = database.laterArticleDao();
        allArticle = laterArticleDao.getAllArticles();
    }

    public void insert(LaterArticle laterArticle){
        new InsertLaterArticleAsyncTask(laterArticleDao).execute(laterArticle);
    }

    public void update(LaterArticle laterArticle){
        new UpdateLaterArticleAsyncTask(laterArticleDao).execute(laterArticle);
    }

    public void delete(LaterArticle laterArticle){
        new DeleteLaterArticleAsyncTask(laterArticleDao).execute(laterArticle);
    }

    public void deleteAllArticles(){
        new DeleteAllLaterArticleAsyncTask(laterArticleDao).execute();
    }

    public LiveData<List<LaterArticle>> getAllArticle() {
        return allArticle;
    }

    private static class InsertLaterArticleAsyncTask extends AsyncTask<LaterArticle, Void, Void>{

        private LaterArticleDao laterArticleDao;

        private InsertLaterArticleAsyncTask(LaterArticleDao laterArticleDao){
            this.laterArticleDao = laterArticleDao;
        }

        @Override
        protected Void doInBackground(LaterArticle... laterArticles) {
            laterArticleDao.insert(laterArticles[0]);
            return null;
        }
    }

    private static class UpdateLaterArticleAsyncTask extends AsyncTask<LaterArticle, Void, Void>{

        private LaterArticleDao laterArticleDao;

        private UpdateLaterArticleAsyncTask(LaterArticleDao laterArticleDao){
            this.laterArticleDao = laterArticleDao;
        }

        @Override
        protected Void doInBackground(LaterArticle... laterArticles) {
            laterArticleDao.update(laterArticles[0]);
            return null;
        }
    }

    private static class DeleteLaterArticleAsyncTask extends AsyncTask<LaterArticle, Void, Void>{

        private LaterArticleDao laterArticleDao;

        private DeleteLaterArticleAsyncTask(LaterArticleDao laterArticleDao){
            this.laterArticleDao = laterArticleDao;
        }

        @Override
        protected Void doInBackground(LaterArticle... laterArticles) {
            laterArticleDao.delete(laterArticles[0]);
            return null;
        }
    }

    private static class DeleteAllLaterArticleAsyncTask extends AsyncTask<Void, Void, Void>{

        private LaterArticleDao laterArticleDao;

        private DeleteAllLaterArticleAsyncTask(LaterArticleDao laterArticleDao){
            this.laterArticleDao = laterArticleDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            laterArticleDao.deleteAllArticles();
            return null;
        }
    }
}
