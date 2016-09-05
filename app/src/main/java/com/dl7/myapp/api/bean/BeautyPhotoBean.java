package com.dl7.myapp.api.bean;

/**
 * Created by long on 2016/9/2.
 * 美女图片
 */
public class BeautyPhotoBean {

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
}
