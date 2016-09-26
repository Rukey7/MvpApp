package com.dl7.myapp.api.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by long on 2016/9/2.
 * 美女图片
 */
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

    private int adtype;
    private String boardid;
    private int clkNum;
    private String digest;
    private String docid;
    private int downTimes;
    private String img;
    private int imgType;
    private String imgsrc;
    private int picCount;
    private String pixel;
    private String program;
    private String prompt;
    private int recType;
    private int replyCount;
    private String replyid;
    private String source;
    private String title;
    private int upTimes;

    public int getAdtype() {
        return adtype;
    }

    public void setAdtype(int adtype) {
        this.adtype = adtype;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public int getClkNum() {
        return clkNum;
    }

    public void setClkNum(int clkNum) {
        this.clkNum = clkNum;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public int getDownTimes() {
        return downTimes;
    }

    public void setDownTimes(int downTimes) {
        this.downTimes = downTimes;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public int getPicCount() {
        return picCount;
    }

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }

    public String getPixel() {
        return pixel;
    }

    public void setPixel(String pixel) {
        this.pixel = pixel;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getRecType() {
        return recType;
    }

    public void setRecType(int recType) {
        this.recType = recType;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getReplyid() {
        return replyid;
    }

    public void setReplyid(String replyid) {
        this.replyid = replyid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUpTimes() {
        return upTimes;
    }

    public void setUpTimes(int upTimes) {
        this.upTimes = upTimes;
    }

    @Override
    public String toString() {
        return "BeautyPhotoBean{" +
                "adtype=" + adtype +
                ", boardid='" + boardid + '\'' +
                ", clkNum=" + clkNum +
                ", digest='" + digest + '\'' +
                ", docid='" + docid + '\'' +
                ", downTimes=" + downTimes +
                ", img='" + img + '\'' +
                ", imgType=" + imgType +
                ", imgsrc='" + imgsrc + '\'' +
                ", picCount=" + picCount +
                ", pixel='" + pixel + '\'' +
                ", program='" + program + '\'' +
                ", prompt='" + prompt + '\'' +
                ", recType=" + recType +
                ", replyCount=" + replyCount +
                ", replyid='" + replyid + '\'' +
                ", source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", upTimes=" + upTimes +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.adtype);
        dest.writeString(this.boardid);
        dest.writeInt(this.clkNum);
        dest.writeString(this.digest);
        dest.writeString(this.docid);
        dest.writeInt(this.downTimes);
        dest.writeString(this.img);
        dest.writeInt(this.imgType);
        dest.writeString(this.imgsrc);
        dest.writeInt(this.picCount);
        dest.writeString(this.pixel);
        dest.writeString(this.program);
        dest.writeString(this.prompt);
        dest.writeInt(this.recType);
        dest.writeInt(this.replyCount);
        dest.writeString(this.replyid);
        dest.writeString(this.source);
        dest.writeString(this.title);
        dest.writeInt(this.upTimes);
    }

    public BeautyPhotoBean() {
    }

    protected BeautyPhotoBean(Parcel in) {
        this.adtype = in.readInt();
        this.boardid = in.readString();
        this.clkNum = in.readInt();
        this.digest = in.readString();
        this.docid = in.readString();
        this.downTimes = in.readInt();
        this.img = in.readString();
        this.imgType = in.readInt();
        this.imgsrc = in.readString();
        this.picCount = in.readInt();
        this.pixel = in.readString();
        this.program = in.readString();
        this.prompt = in.readString();
        this.recType = in.readInt();
        this.replyCount = in.readInt();
        this.replyid = in.readString();
        this.source = in.readString();
        this.title = in.readString();
        this.upTimes = in.readInt();
    }

    public static final Parcelable.Creator<BeautyPhotoBean> CREATOR = new Parcelable.Creator<BeautyPhotoBean>() {
        @Override
        public BeautyPhotoBean createFromParcel(Parcel source) {
            return new BeautyPhotoBean(source);
        }

        @Override
        public BeautyPhotoBean[] newArray(int size) {
            return new BeautyPhotoBean[size];
        }
    };
}
