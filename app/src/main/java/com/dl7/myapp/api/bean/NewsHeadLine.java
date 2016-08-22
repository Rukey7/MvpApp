package com.dl7.myapp.api.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by long on 2016/8/22.
 * 头条新闻列表
 */
public class NewsHeadLine {

    /**
     * postid : PHOT22SO3000100A
     * hasCover : false
     * hasHead : 1
     * replyCount : 1579
     * hasImg : 1
     * digest :
     * hasIcon : false
     * docid : 9IG74V5H00963VRO_BV3N61SAbjjikeupdateDoc
     * title : 秦皇岛巴铁“进站” 游客驻足拍照
     * order : 1
     * priority : 340
     * lmodify : 2016-08-22 20:20:50
     * boardid : photoview_bbs
     * ads : [{"docid":"BV392SF005169QC9","title":"为了它 各国愿意签订\u201c不平等条约\u201d","tag":"doc","imgsrc":"http://cms-bucket.nosdn.127.net/07d6b68c912246ccbec519f16bf9f07c20160822201231.jpeg","subtitle":"","url":"BV392SF005169QC9"},{"title":"湖北14人被困酒店电梯 消防拆墙营救","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/6ec039efef194362911b1596cfc222d220160822192740.jpeg","subtitle":"","url":"00AP0001|2192129"},{"title":"\"长征五号\"运抵天津码头 \"出征\"首飞","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/9374e441b9134ebba5189aaaa96ff67220160822163244.jpeg","subtitle":"","url":"00AN0001|2192093"},{"title":"南京村庄建\u201c楼叠楼\u201d 为多拿拆迁补偿","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/4693747579184988ad6e903e3e07793620160822095553.jpeg","subtitle":"","url":"00AP0001|2192041"},{"title":"安倍晋三扮马里奥亮相奥运会闭幕式","tag":"photoset","imgsrc":"http://cms-bucket.nosdn.127.net/14c874ae5e8c41818a1d754de935830920160822094144.jpeg","subtitle":"","url":"6TSU0005|147568"}]
     * photosetID : 00AP0001|2192131
     * template : normal1
     * votecount : 1090
     * skipID : 00AP0001|2192131
     * alias : Top News
     * skipType : photoset
     * cid : C1348646712614
     * hasAD : 1
     * imgextra : [{"imgsrc":"http://cms-bucket.nosdn.127.net/bacae5be5dcd4063827f3d257e48df1c20160822201548.jpeg"},{"imgsrc":"http://cms-bucket.nosdn.127.net/0300a704b79149409b69027b394cec6120160822201549.jpeg"}]
     * source : 网易原创
     * ename : androidnews
     * imgsrc : http://cms-bucket.nosdn.127.net/9f544dc352994a1ea87300d60d90ed2820160822201546.jpeg
     * tname : 头条
     * ptime : 2016-08-22 20:14:57
     */

    @SerializedName("T1348647909107")
    private List<NewsBean> news;

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "NewsHeadLine{" +
                "news=" + news +
                '}';
    }
}
