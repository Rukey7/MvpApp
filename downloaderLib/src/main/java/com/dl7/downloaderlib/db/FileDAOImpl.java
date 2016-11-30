package com.dl7.downloaderlib.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dl7.downloaderlib.FileDownloader;
import com.dl7.downloaderlib.entity.FileInfo;

/**
 * Created by long on 2016/5/26.
 * 数据库访问实现类
 */
public class FileDAOImpl implements FileDAO {

    private static final String DB_NAME = "FileDownloader.db";
    private DbHelper mDbHelper = null;


    private FileDAOImpl() {
        // 创建数据库
        mDbHelper = new DbHelper(FileDownloader.getContext(), DB_NAME, null, 2);
    }

    private static class HolderClass {
        private static final FileDAOImpl instance = new FileDAOImpl();
    }

    public static FileDAOImpl getInstance() {
        return HolderClass.instance;
    }

    @Override
    public void insert(FileInfo info) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(DbHelper.TABLE_NAME, null, info.toValues());
    }

    @Override
    public void delete(String url) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DbHelper.TABLE_NAME, "url = ?", new String[] {url});
    }

    @Override
    public void update(FileInfo info) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.update(DbHelper.TABLE_NAME, info.toValues(), "url = ?", new String[] {info.getUrl()});
    }

    @Override
    public FileInfo query(String fileUrl) {
        FileInfo fileInfo = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_NAME, null, "url = ?", new String[]{fileUrl}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            int loadBytes = cursor.getInt(cursor.getColumnIndex("loadBytes"));
            int totalBytes = cursor.getInt(cursor.getColumnIndex("totalBytes"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            fileInfo = new FileInfo(url, name);
            fileInfo.setPath(path);
            fileInfo.setLoadBytes(loadBytes);
            fileInfo.setTotalBytes(totalBytes);
            fileInfo.setStatus(status);
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileInfo;
    }

    public FileInfo queryPkg(String pkgName) {
        FileInfo fileInfo = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_NAME, null, "name = ?", new String[]{pkgName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String path = cursor.getString(cursor.getColumnIndex("path"));
            int loadBytes = cursor.getInt(cursor.getColumnIndex("loadBytes"));
            int totalBytes = cursor.getInt(cursor.getColumnIndex("totalBytes"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            fileInfo = new FileInfo(url, name);
            fileInfo.setPath(path);
            fileInfo.setLoadBytes(loadBytes);
            fileInfo.setTotalBytes(totalBytes);
            fileInfo.setStatus(status);
        }
        if (cursor != null) {
            cursor.close();
        }
        return fileInfo;
    }

    @Override
    public boolean isExists(String url) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DbHelper.TABLE_NAME, null, "url = ?", new String[]{url}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }
}
