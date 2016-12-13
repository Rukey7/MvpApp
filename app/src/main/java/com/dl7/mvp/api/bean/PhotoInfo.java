package com.dl7.mvp.api.bean;

import java.util.List;

/**
 * Created by long on 2016/9/2.
 * 图片栏目列表实体
 */
public class PhotoInfo {

    /**
     * desc : 当地时间2016年9月2日，澳大利亚墨尔本，澳大利亚男子团体穿高跟鞋示威，反对对女性施暴。图/视觉中国
     * pvnum :
     * createdate : 2016-09-02 14:45:51
     * scover : http://img4.cache.netease.com/photo/0096/2016-09-02/s_BVVELJV254GI0096.jpg
     * setname : 澳男性团体穿高跟鞋反对女性暴力
     * cover : http://img4.cache.netease.com/photo/0096/2016-09-02/BVVELJV254GI0096.jpg
     * pics : ["http://img4.cache.netease.com/photo/0096/2016-09-02/BVVELJV154GI0096.jpg","http://img4.cache.netease.com/photo/0096/2016-09-02/BVVELJV254GI0096.jpg","http://img4.cache.netease.com/photo/0096/2016-09-02/BVVELJV354GI0096.jpg"]
     * clientcover1 : http://img4.cache.netease.com/photo/0096/2016-09-02/BVVELJV254GI0096.jpg
     * replynum : 217
     * topicname :
     * setid : 106580
     * seturl : http://help.3g.163.com/photoview/54GI0096/106580.html
     * datetime : 2016-09-02 14:45:54
     * clientcover :
     * imgsum : 5
     * tcover : http://img4.cache.netease.com/photo/0096/2016-09-02/t_BVVELJV254GI0096.jpg
     */

    private String desc;
    private String pvnum;
    private String createdate;
    private String scover;
    private String setname;
    private String cover;
    private String clientcover1;
    private String replynum;
    private String topicname;
    private String setid;
    private String seturl;
    private String datetime;
    private String clientcover;
    private String imgsum;
    private String tcover;
    private List<String> pics;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPvnum() {
        return pvnum;
    }

    public void setPvnum(String pvnum) {
        this.pvnum = pvnum;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getScover() {
        return scover;
    }

    public void setScover(String scover) {
        this.scover = scover;
    }

    public String getSetname() {
        return setname;
    }

    public void setSetname(String setname) {
        this.setname = setname;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getClientcover1() {
        return clientcover1;
    }

    public void setClientcover1(String clientcover1) {
        this.clientcover1 = clientcover1;
    }

    public String getReplynum() {
        return replynum;
    }

    public void setReplynum(String replynum) {
        this.replynum = replynum;
    }

    public String getTopicname() {
        return topicname;
    }

    public void setTopicname(String topicname) {
        this.topicname = topicname;
    }

    public String getSetid() {
        return setid;
    }

    public void setSetid(String setid) {
        this.setid = setid;
    }

    public String getSeturl() {
        return seturl;
    }

    public void setSeturl(String seturl) {
        this.seturl = seturl;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getClientcover() {
        return clientcover;
    }

    public void setClientcover(String clientcover) {
        this.clientcover = clientcover;
    }

    public String getImgsum() {
        return imgsum;
    }

    public void setImgsum(String imgsum) {
        this.imgsum = imgsum;
    }

    public String getTcover() {
        return tcover;
    }

    public void setTcover(String tcover) {
        this.tcover = tcover;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
}
