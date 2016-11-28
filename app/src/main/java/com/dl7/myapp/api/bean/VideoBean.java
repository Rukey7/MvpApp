package com.dl7.myapp.api.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by long on 2016/10/11.
 * 视频实体
 */
public class VideoBean implements Parcelable {

    /**
     * mp4Hd_url : http://flv2.bn.netease.com/videolib3/1501/28/wlncJ2098/HD/wlncJ2098-mobile.mp4
     * cover : http://img3.cache.netease.com/3g/2015/1/12/20150112103113e10a2.png
     * title : 袁腾飞《这个历史挺靠谱》
     * replyBoard : videonews_bbs
     * playCount : 1024733
     * sectiontitle : 第15集:科举制让屌丝逆袭
     * replyid : AG4JHJUR008535RB
     * mp4_url : http://flv2.bn.netease.com/videolib3/1501/28/wlncJ2098/SD/wlncJ2098-mobile.mp4
     * length : 591
     * m3u8Hd_url : http://flv2.bn.netease.com/videolib3/1501/28/wlncJ2098/HD/movie_index.m3u8
     * latest :
     * vid : VAG4JHJUR
     * ptime : 2015-01-28 15:59:31
     * m3u8_url : http://flv2.bn.netease.com/videolib3/1501/28/wlncJ2098/SD/movie_index.m3u8
     */

    private String mp4Hd_url;
    private String cover;
    private String title;
    private String replyBoard;
    private int playCount;
    private String sectiontitle;
    private String replyid;
    private String mp4_url;
    private int length;
    private String m3u8Hd_url;
    private String latest;
    private String vid;
    private String ptime;
    private String m3u8_url;

    public String getMp4Hd_url() {
        return mp4Hd_url;
    }

    public void setMp4Hd_url(String mp4Hd_url) {
        this.mp4Hd_url = mp4Hd_url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReplyBoard() {
        return replyBoard;
    }

    public void setReplyBoard(String replyBoard) {
        this.replyBoard = replyBoard;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getSectiontitle() {
        return sectiontitle;
    }

    public void setSectiontitle(String sectiontitle) {
        this.sectiontitle = sectiontitle;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getMp4_url() {
        return mp4_url;
    }

    public void setMp4_url(String mp4_url) {
        this.mp4_url = mp4_url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getM3u8Hd_url() {
        return m3u8Hd_url;
    }

    public void setM3u8Hd_url(String m3u8Hd_url) {
        this.m3u8Hd_url = m3u8Hd_url;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getM3u8_url() {
        return m3u8_url;
    }

    public void setM3u8_url(String m3u8_url) {
        this.m3u8_url = m3u8_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mp4Hd_url);
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeString(this.replyBoard);
        dest.writeInt(this.playCount);
        dest.writeString(this.sectiontitle);
        dest.writeString(this.replyid);
        dest.writeString(this.mp4_url);
        dest.writeInt(this.length);
        dest.writeString(this.m3u8Hd_url);
        dest.writeString(this.latest);
        dest.writeString(this.vid);
        dest.writeString(this.ptime);
        dest.writeString(this.m3u8_url);
    }

    public VideoBean() {
    }

    protected VideoBean(Parcel in) {
        this.mp4Hd_url = in.readString();
        this.cover = in.readString();
        this.title = in.readString();
        this.replyBoard = in.readString();
        this.playCount = in.readInt();
        this.sectiontitle = in.readString();
        this.replyid = in.readString();
        this.mp4_url = in.readString();
        this.length = in.readInt();
        this.m3u8Hd_url = in.readString();
        this.latest = in.readString();
        this.vid = in.readString();
        this.ptime = in.readString();
        this.m3u8_url = in.readString();
    }

    public static final Parcelable.Creator<VideoBean> CREATOR = new Parcelable.Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel source) {
            return new VideoBean(source);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };
}
