package com.dl7.myapp.local.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by long on 2016/9/2.
 * 美女图片
 */
@Entity
public class BeautyPhotoBean implements Parcelable {

    /**
     * adtype : 0
     * boardid : comment_bbs
     * clkNum : 0
     * digest : 长发美女  郭碧婷
     * docid : BQSOIFA29001IFA3
     * downTimes : 1264
     * img : http://dmr.nosdn.127.net/w_TEGSpvSxskhGnIuNAt5Q==/6896093022273156436.jpg
     * imgType : 0
     * imgsrc : http://dmr.nosdn.127.net/w_TEGSpvSxskhGnIuNAt5Q==/6896093022273156436.jpg
     * picCount : 0
     * pixel : 700*467
     * program : HY
     * prompt : 成功为您推荐20条新内容
     * recType : 0
     * replyCount : 74
     * replyid : BQSOIFA29001IFA3
     * source : 堆糖网
     * title : 长发美女  郭碧婷
     * upTimes : 4808
     */
//    private int adtype;
//    private String boardid;
//    private int clkNum;
//    private String digest;
//    private String docid;
//    private int downTimes;
//    private int imgType;
//    private int picCount;
    //    private String program;
//    private String prompt;
//    private int recType;
//    private int replyCount;
//    private String replyid;
//    private String source;
//    private int upTimes;
    //    private String img;
    @Id
    private String imgsrc;
    private String pixel;
    private String title;
    // 喜欢
    private boolean isLove;
    // 点赞
    private boolean isPraise;
    // 下载
    private boolean isDownload;

    public String getImgsrc() {
        return imgsrc;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setPraise(boolean praise) {
        isPraise = praise;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isLove() {
        return isLove;
    }

    public void setLove(boolean love) {
        isLove = love;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgsrc);
        dest.writeString(this.pixel);
        dest.writeString(this.title);
        dest.writeByte(this.isLove ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPraise ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDownload ? (byte) 1 : (byte) 0);
    }

    public boolean getIsDownload() {
        return this.isDownload;
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public boolean getIsPraise() {
        return this.isPraise;
    }

    public void setIsPraise(boolean isPraise) {
        this.isPraise = isPraise;
    }

    public boolean getIsLove() {
        return this.isLove;
    }

    public void setIsLove(boolean isLove) {
        this.isLove = isLove;
    }

    public BeautyPhotoBean() {
    }

    protected BeautyPhotoBean(Parcel in) {
        this.imgsrc = in.readString();
        this.pixel = in.readString();
        this.title = in.readString();
        this.isLove = in.readByte() != 0;
        this.isPraise = in.readByte() != 0;
        this.isDownload = in.readByte() != 0;
    }

    @Generated(hash = 2074936425)
    public BeautyPhotoBean(String imgsrc, String pixel, String title, boolean isLove,
            boolean isPraise, boolean isDownload) {
        this.imgsrc = imgsrc;
        this.pixel = pixel;
        this.title = title;
        this.isLove = isLove;
        this.isPraise = isPraise;
        this.isDownload = isDownload;
    }

    public static final Creator<BeautyPhotoBean> CREATOR = new Creator<BeautyPhotoBean>() {
        @Override
        public BeautyPhotoBean createFromParcel(Parcel source) {
            return new BeautyPhotoBean(source);
        }

        @Override
        public BeautyPhotoBean[] newArray(int size) {
            return new BeautyPhotoBean[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BeautyPhotoBean)) {
            return false;
        }
        BeautyPhotoBean other = (BeautyPhotoBean) o;
        if (imgsrc.equals(other.getImgsrc())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "BeautyPhotoBean{" +
                "imgsrc='" + imgsrc + '\'' +
                ", pixel='" + pixel + '\'' +
                ", title='" + title + '\'' +
                ", isLove=" + isLove +
                ", isPraise=" + isPraise +
                ", isDownload=" + isDownload +
                '}';
    }
}
