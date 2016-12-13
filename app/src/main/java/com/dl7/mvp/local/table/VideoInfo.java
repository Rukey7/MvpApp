package com.dl7.mvp.local.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.dl7.downloaderlib.model.DownloadStatus;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by long on 2016/10/11.
 * 视频实体
 */
@Entity
public class VideoInfo implements Parcelable {

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
    @Id
    private String vid;
    private String mp4Hd_url;
    private String cover;
    private String title;
//    private String replyBoard;
//    private int playCount;
    private String sectiontitle;
//    private String replyid;
    private String mp4_url;
    private int length;
    private String m3u8Hd_url;
//    private String latest;
    private String ptime;
    private String m3u8_url;

    /**
     * 下载地址，可能有多个视频源，统一用一个字段
     */
    private String videoUrl;
    /**
     * 文件大小，字节
     */
    private long totalSize;
    /**
     * 已加载大小
     */
    private long loadedSize;
    /**
     * 下载状态
     */
    private int downloadStatus = DownloadStatus.NORMAL;
    /**
     * 下载速度
     */
    private long downloadSpeed;
    /**
     * 是否收藏
     */
    private boolean isCollect;

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

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

    public String getSectiontitle() {
        return sectiontitle;
    }

    public void setSectiontitle(String sectiontitle) {
        this.sectiontitle = sectiontitle;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getLoadedSize() {
        return loadedSize;
    }

    public void setLoadedSize(long loadedSize) {
        this.loadedSize = loadedSize;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public long getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "vid='" + vid + '\'' +
                ", mp4Hd_url='" + mp4Hd_url + '\'' +
                ", cover='" + cover + '\'' +
                ", title='" + title + '\'' +
                ", sectiontitle='" + sectiontitle + '\'' +
                ", mp4_url='" + mp4_url + '\'' +
                ", length=" + length +
                ", m3u8Hd_url='" + m3u8Hd_url + '\'' +
                ", ptime='" + ptime + '\'' +
                ", m3u8_url='" + m3u8_url + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", totalSize=" + totalSize +
                ", loadedSize=" + loadedSize +
                ", downloadStatus=" + downloadStatus +
                ", downloadSpeed=" + downloadSpeed +
                ", isCollect=" + isCollect +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.vid);
        dest.writeString(this.mp4Hd_url);
        dest.writeString(this.cover);
        dest.writeString(this.title);
        dest.writeString(this.sectiontitle);
        dest.writeString(this.mp4_url);
        dest.writeInt(this.length);
        dest.writeString(this.m3u8Hd_url);
        dest.writeString(this.ptime);
        dest.writeString(this.m3u8_url);
        dest.writeString(this.videoUrl);
        dest.writeLong(this.totalSize);
        dest.writeLong(this.loadedSize);
        dest.writeInt(this.downloadStatus);
        dest.writeLong(this.downloadSpeed);
        dest.writeByte(this.isCollect ? (byte) 1 : (byte) 0);
    }

    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        this.vid = in.readString();
        this.mp4Hd_url = in.readString();
        this.cover = in.readString();
        this.title = in.readString();
        this.sectiontitle = in.readString();
        this.mp4_url = in.readString();
        this.length = in.readInt();
        this.m3u8Hd_url = in.readString();
        this.ptime = in.readString();
        this.m3u8_url = in.readString();
        this.videoUrl = in.readString();
        this.totalSize = in.readLong();
        this.loadedSize = in.readLong();
        this.downloadStatus = in.readInt();
        this.downloadSpeed = in.readLong();
        this.isCollect = in.readByte() != 0;
    }

    @Generated(hash = 1048022349)
    public VideoInfo(String vid, String mp4Hd_url, String cover, String title, String sectiontitle,
            String mp4_url, int length, String m3u8Hd_url, String ptime, String m3u8_url,
            String videoUrl, long totalSize, long loadedSize, int downloadStatus, long downloadSpeed,
            boolean isCollect) {
        this.vid = vid;
        this.mp4Hd_url = mp4Hd_url;
        this.cover = cover;
        this.title = title;
        this.sectiontitle = sectiontitle;
        this.mp4_url = mp4_url;
        this.length = length;
        this.m3u8Hd_url = m3u8Hd_url;
        this.ptime = ptime;
        this.m3u8_url = m3u8_url;
        this.videoUrl = videoUrl;
        this.totalSize = totalSize;
        this.loadedSize = loadedSize;
        this.downloadStatus = downloadStatus;
        this.downloadSpeed = downloadSpeed;
        this.isCollect = isCollect;
    }


    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VideoInfo)) {
            return false;
        }
        VideoInfo other = (VideoInfo) o;
        if (vid.equals(other.getVid())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return vid.hashCode();
    }

    public boolean getIsCollect() {
        return this.isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }
}
