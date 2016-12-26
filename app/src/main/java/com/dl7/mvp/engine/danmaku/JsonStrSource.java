package com.dl7.mvp.engine.danmaku;

import android.net.Uri;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.util.IOUtils;

/**
 * Created by long on 2016/12/22.
 * 自定义弹幕数据源，参考A站
 */
public class JsonStrSource implements IDataSource<String> {

    private String mJsonStr;
    private InputStream mInput;
    public JsonStrSource(String json) throws JSONException{
        init(json);
    }

    public JsonStrSource(InputStream in) throws JSONException{
        init(in);
    }

    private void init(InputStream in) throws JSONException {
        if(in == null)
            throw new NullPointerException("input stream cannot be null!");
        mInput = in;
        Logger.e("init");
        String json = IOUtils.getString(mInput);
        Logger.w(json);
        init(json);
    }

    public JsonStrSource(URL url) throws JSONException, IOException {
        this(url.openStream());
    }

    public JsonStrSource(File file) throws FileNotFoundException, JSONException{
        init(new FileInputStream(file));
    }

    public JsonStrSource(Uri uri) throws IOException, JSONException {
        String scheme = uri.getScheme();
        if (SCHEME_HTTP_TAG.equalsIgnoreCase(scheme) || SCHEME_HTTPS_TAG.equalsIgnoreCase(scheme)) {
            init(new URL(uri.getPath()).openStream());
        } else if (SCHEME_FILE_TAG.equalsIgnoreCase(scheme)) {
            init(new FileInputStream(uri.getPath()));
        }
    }

    private void init(String json) throws JSONException {
        if(!TextUtils.isEmpty(json)){
            mJsonStr = json;
        }
    }

    @Override
    public String data(){
        return mJsonStr;
    }

    @Override
    public void release() {
        IOUtils.closeQuietly(mInput);
        mInput = null;
        mJsonStr = null;
    }
}
