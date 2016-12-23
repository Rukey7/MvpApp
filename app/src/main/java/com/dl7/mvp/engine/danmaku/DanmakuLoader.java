package com.dl7.mvp.engine.danmaku;

import android.net.Uri;

import java.io.InputStream;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;

/**
 * Created by long on 2016/12/22.
 * 自定义弹幕加载器,参考A站
 */
public class DanmakuLoader implements ILoader {

    private DanmakuLoader(){}
    private static volatile DanmakuLoader instance;
    private JsonStrSource dataSource;

    public static ILoader instance() {
        if(instance == null){
            synchronized (DanmakuLoader.class){
                if(instance == null)
                    instance = new DanmakuLoader();
            }
        }
        return instance;
    }

    @Override
    public JsonStrSource getDataSource() {
        return dataSource;
    }

    @Override
    public void load(String uri) throws IllegalDataException {
        try {
            dataSource = new JsonStrSource(Uri.parse(uri));
        } catch (Exception e) {
            throw new IllegalDataException(e);
        }
    }

    @Override
    public void load(InputStream in) throws IllegalDataException {
        try {
            dataSource = new JsonStrSource(in);
        } catch (Exception e) {
            throw new IllegalDataException(e);
        }
    }
}
