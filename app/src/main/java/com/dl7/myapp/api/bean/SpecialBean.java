package com.dl7.myapp.api.bean;

import java.util.List;

/**
 * Created by long on 2016/8/23.
 * 专题数据
 */
public final class SpecialBean {

    private String sid;
    private String shownav;
    private String tag;
    private String photoset;
    private String digest;
    private Object webviews;
    private String type;
    private String sname;
    private String ec;
    private String lmodify;
    private String imgsrc;
    private int del;
    private String ptime;
    private String sdocid;
    private String banner;
    private List<?> topicslatest;
    private List<?> topicspatch;
    private List<?> headpics;
    private List<?> topicsplus;

    private List<TopicsEntity> topics;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getShownav() {
        return shownav;
    }

    public void setShownav(String shownav) {
        this.shownav = shownav;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPhotoset() {
        return photoset;
    }

    public void setPhotoset(String photoset) {
        this.photoset = photoset;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Object getWebviews() {
        return webviews;
    }

    public void setWebviews(Object webviews) {
        this.webviews = webviews;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getLmodify() {
        return lmodify;
    }

    public void setLmodify(String lmodify) {
        this.lmodify = lmodify;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getSdocid() {
        return sdocid;
    }

    public void setSdocid(String sdocid) {
        this.sdocid = sdocid;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<?> getTopicslatest() {
        return topicslatest;
    }

    public void setTopicslatest(List<?> topicslatest) {
        this.topicslatest = topicslatest;
    }

    public List<?> getTopicspatch() {
        return topicspatch;
    }

    public void setTopicspatch(List<?> topicspatch) {
        this.topicspatch = topicspatch;
    }

    public List<?> getHeadpics() {
        return headpics;
    }

    public void setHeadpics(List<?> headpics) {
        this.headpics = headpics;
    }

    public List<?> getTopicsplus() {
        return topicsplus;
    }

    public void setTopicsplus(List<?> topicsplus) {
        this.topicsplus = topicsplus;
    }

    public List<TopicsEntity> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsEntity> topics) {
        this.topics = topics;
    }

    public static class TopicsEntity {
        private int index;
        private String tname;
        private String shortname;
        private String type;
        /**
         * postid : BV55MRHF00237VT1
         * votecount : 0
         * replyCount : 0
         * tag :
         * ltitle : 学习进行时：中国体育的习近平印记
         * digest :
         * url : http://3g.163.com/ntes/16/0823/09/BV55MRHF00237VT1.html
         * ipadcomment :
         * docid : BV55MRHF00237VT1
         * title : 学习进行时：中国体育的习近平印记
         * source : 新华网
         * lmodify : 2016-08-23 10:31:09
         * imgsrc : http://cms-bucket.nosdn.127.net/fc18ad4cf44d4c76a54f3325032b114320160823102514.jpeg
         * ptime : 2016-08-23 09:48:02
         */

        private List<DocsEntity> docs;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public String getShortname() {
            return shortname;
        }

        public void setShortname(String shortname) {
            this.shortname = shortname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<DocsEntity> getDocs() {
            return docs;
        }

        public void setDocs(List<DocsEntity> docs) {
            this.docs = docs;
        }

        public static class DocsEntity {
            private String postid;
            private int votecount;
            private int replyCount;
            private String tag;
            private String ltitle;
            private String digest;
            private String url;
            private String ipadcomment;
            private String docid;
            private String title;
            private String source;
            private String lmodify;
            private String imgsrc;
            private String ptime;

            public String getPostid() {
                return postid;
            }

            public void setPostid(String postid) {
                this.postid = postid;
            }

            public int getVotecount() {
                return votecount;
            }

            public void setVotecount(int votecount) {
                this.votecount = votecount;
            }

            public int getReplyCount() {
                return replyCount;
            }

            public void setReplyCount(int replyCount) {
                this.replyCount = replyCount;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public String getLtitle() {
                return ltitle;
            }

            public void setLtitle(String ltitle) {
                this.ltitle = ltitle;
            }

            public String getDigest() {
                return digest;
            }

            public void setDigest(String digest) {
                this.digest = digest;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getIpadcomment() {
                return ipadcomment;
            }

            public void setIpadcomment(String ipadcomment) {
                this.ipadcomment = ipadcomment;
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

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getLmodify() {
                return lmodify;
            }

            public void setLmodify(String lmodify) {
                this.lmodify = lmodify;
            }

            public String getImgsrc() {
                return imgsrc;
            }

            public void setImgsrc(String imgsrc) {
                this.imgsrc = imgsrc;
            }

            public String getPtime() {
                return ptime;
            }

            public void setPtime(String ptime) {
                this.ptime = ptime;
            }
        }

        @Override
        public String toString() {
            return "TopicsEntity{" +
                    "index=" + index +
                    ", tname='" + tname + '\'' +
                    ", shortname='" + shortname + '\'' +
                    ", type='" + type + '\'' +
                    ", docs=" + docs +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SpecialBean{" +
                "sid='" + sid + '\'' +
                ", shownav='" + shownav + '\'' +
                ", tag='" + tag + '\'' +
                ", photoset='" + photoset + '\'' +
                ", digest='" + digest + '\'' +
                ", webviews=" + webviews +
                ", type='" + type + '\'' +
                ", sname='" + sname + '\'' +
                ", ec='" + ec + '\'' +
                ", lmodify='" + lmodify + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", del=" + del +
                ", ptime='" + ptime + '\'' +
                ", sdocid='" + sdocid + '\'' +
                ", banner='" + banner + '\'' +
                ", topicslatest=" + topicslatest +
                ", topicspatch=" + topicspatch +
                ", headpics=" + headpics +
                ", topicsplus=" + topicsplus +
                ", topics=" + topics +
                '}';
    }
}
