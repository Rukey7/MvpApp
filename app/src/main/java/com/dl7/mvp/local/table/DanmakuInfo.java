package com.dl7.mvp.local.table;

import com.dl7.player.danmaku.BaseDanmakuData;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by long on 2016/12/23.
 * 弹幕数据
 */
@Entity
public class DanmakuInfo extends BaseDanmakuData {

    // 为了数据库字段生成，把基类的字段拷过来，没找到GreenDao生成父类字段的方式
    private int type;
    private String content;
    private long time;
    private float textSize;
    private int textColor;
    // 用户名
    private String userName;
    // 对应一个视频
    private String vid;

    @Generated(hash = 297831740)
    public DanmakuInfo(int type, String content, long time, float textSize,
            int textColor, String userName, String vid) {
        this.type = type;
        this.content = content;
        this.time = time;
        this.textSize = textSize;
        this.textColor = textColor;
        this.userName = userName;
        this.vid = vid;
    }

    @Generated(hash = 2104047588)
    public DanmakuInfo() {
    }

    public String getVid() {
        return this.vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DanmakuInfo{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", textSize=" + textSize +
                ", textColor=" + textColor +
                ", userName='" + userName + '\'' +
                ", vid='" + vid + '\'' +
                '}';
    }
}
