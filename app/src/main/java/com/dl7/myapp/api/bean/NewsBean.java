package com.dl7.myapp.api.bean;

import java.util.List;

/**
 * Created by long on 2016/8/22.
 * 新闻实体
 */
public final class NewsBean {

    /**
     * postid : PHOT22SMT000100A
     * hasCover : false
     * hasHead : 1
     * replyCount : 3406
     * hasImg : 1
     * digest :
     * hasIcon : false
     * docid : 9IG74V5H00963VRO_BV3ADP8MbjjikeupdateDoc
     * title : "长征五号"运抵天津码头 "出征"首飞
     * order : 1
     * priority : 340
     * lmodify : 2016-08-22 17:22:35
     * boardid : photoview_bbs
     * ads : [{"title":"南京村庄建\u201c楼叠楼\u201d 为多拿拆迁补偿","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/4693747579184988ad6e903e3e07793620160822095553.jpeg","subtitle":"","url":"00AP0001|2192041"},{"title":"安倍晋三扮马里奥亮相奥运会闭幕式","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/14c874ae5e8c41818a1d754de935830920160822094144.jpeg","subtitle":"","url":"6TSU0005|147568"},{"title":"法国华人团体游行呼吁保障华人安全","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/9c221db80bc54e068b84f6833701df0d20160822075828.jpeg","subtitle":"","url":"00AO0001|2192019"},{"title":"济南家长自戴头灯 领小孩黑虎泉边捕鱼","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/1a12be5fb7f3495ab7626a99dbd75ace20160822101953.jpeg","subtitle":"","url":"00AP0001|2192051"},{"title":"长春现一平米\u201c弹丸\u201d车位 仅停摩托车","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/c121e479d0ca46fd937a09d2a275a96b20160822101343.jpeg","subtitle":"","url":"00AP0001|2192050"}]
     * photosetID : 00AN0001|2192093
     * template : normal1
     * votecount : 3067
     * skipID : 00AN0001|2192093
     * alias : Top News
     * skipType : photoset
     * cid : C1348646712614
     * hasAD : 1
     * imgextra : [{"imgsrc":"http://cms-bucket.nosdn.127.net/381ed60794ff4b0785b9888453f514a420160822163245.jpeg"},{"imgsrc":"http://cms-bucket.nosdn.127.net/0155de5da75449dfbcc2e2c54a45fdfa20160822163246.jpeg"}]
     * source : 网易原创
     * ename : androidnews
     * imgsrc : http://cms-bucket.nosdn.127.net/3bbed1d93b2e47888938b5d3067e561320160822172233.jpeg
     * tname : 头条
     * ptime : 2016-08-22 16:31:59
     */

    private String postid;
    private boolean hasCover;
    private int hasHead;
    private int replyCount;
    private int hasImg;
    private String digest;
    private boolean hasIcon;
    private String docid;
    private String title;
    private int order;
    private int priority;
    private String lmodify;
    private String boardid;
    private String photosetID;
    private String template;
    private int votecount;
    private String skipID;
    private String alias;
    private String skipType;
    private String cid;
    private int hasAD;
    private String source;
    private String ename;
    private String imgsrc;
    private String tname;
    private String ptime;
    private String specialID;

    /**
     * title : 南京村庄建“楼叠楼” 为多拿拆迁补偿
     * tag : photoset
     * imgsrc : http://cms-bucket.nosdn.127.net/4693747579184988ad6e903e3e07793620160822095553.jpeg
     * subtitle :
     * url : 00AP0001|2192041
     */

    private List<AdData> ads;
    /**
     * imgsrc : http://cms-bucket.nosdn.127.net/381ed60794ff4b0785b9888453f514a420160822163245.jpeg
     */

    private List<ImgExtraData> imgextra;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isHasCover() {
        return hasCover;
    }

    public void setHasCover(boolean hasCover) {
        this.hasCover = hasCover;
    }

    public int getHasHead() {
        return hasHead;
    }

    public void setHasHead(int hasHead) {
        this.hasHead = hasHead;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getHasImg() {
        return hasImg;
    }

    public void setHasImg(int hasImg) {
        this.hasImg = hasImg;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isHasIcon() {
        return hasIcon;
    }

    public void setHasIcon(boolean hasIcon) {
        this.hasIcon = hasIcon;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getBoardid() {
        return boardid;
    }

    public void setBoardid(String boardid) {
        this.boardid = boardid;
    }

    public String getPhotosetID() {
        return photosetID;
    }

    public void setPhotosetID(String photosetID) {
        this.photosetID = photosetID;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getVotecount() {
        return votecount;
    }

    public void setVotecount(int votecount) {
        this.votecount = votecount;
    }

    public String getSkipID() {
        return skipID;
    }

    public void setSkipID(String skipID) {
        this.skipID = skipID;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSkipType() {
        return skipType;
    }

    public void setSkipType(String skipType) {
        this.skipType = skipType;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getHasAD() {
        return hasAD;
    }

    public void setHasAD(int hasAD) {
        this.hasAD = hasAD;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public List<AdData> getAds() {
        return ads;
    }

    public void setAds(List<AdData> ads) {
        this.ads = ads;
    }

    public List<ImgExtraData> getImgextra() {
        return imgextra;
    }

    public void setImgextra(List<ImgExtraData> imgextra) {
        this.imgextra = imgextra;
    }

    public String getSpecialID() {
        return specialID;
    }

    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }

    public static class AdData {
        private String title;
        private String tag;
        private String imgsrc;
        private String subtitle;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ImgExtraData {
        private String imgsrc;

        public String getImgsrc() {
            return imgsrc;
        }

        public void setImgsrc(String imgsrc) {
            this.imgsrc = imgsrc;
        }
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "postid='" + postid + '\'' +
                ", hasCover=" + hasCover +
                ", hasHead=" + hasHead +
                ", replyCount=" + replyCount +
                ", hasImg=" + hasImg +
                ", digest='" + digest + '\'' +
                ", hasIcon=" + hasIcon +
                ", docid='" + docid + '\'' +
                ", title='" + title + '\'' +
                ", order=" + order +
                ", priority=" + priority +
                ", lmodify='" + lmodify + '\'' +
                ", boardid='" + boardid + '\'' +
                ", photosetID='" + photosetID + '\'' +
                ", template='" + template + '\'' +
                ", votecount=" + votecount +
                ", skipID='" + skipID + '\'' +
                ", alias='" + alias + '\'' +
                ", skipType='" + skipType + '\'' +
                ", cid='" + cid + '\'' +
                ", hasAD=" + hasAD +
                ", source='" + source + '\'' +
                ", ename='" + ename + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", tname='" + tname + '\'' +
                ", ptime='" + ptime + '\'' +
                ", specialID='" + specialID + '\'' +
                ", ads=" + ads +
                ", imgextra=" + imgextra +
                '}';
    }
}
