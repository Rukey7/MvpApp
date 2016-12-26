package com.dl7.player.danmaku;

/**
 * Created by long on 2016/12/22.
 * 弹幕数据基类，用来限定弹幕类型
 */
public abstract class BaseDanmakuData {

    private
    @DanmakuType
    int type;
    private String content;
    private long time;
    private float textSize;
    private int textColor;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "BaseDanmakuData{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", textSize=" + textSize +
                ", textColor=" + textColor +
                '}';
    }
}
