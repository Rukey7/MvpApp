package com.dl7.mvp.api.bean;

import java.util.List;

/**
 * Created by long on 2016/10/10.
 */

public class WelfarePhotoList {

    /**
     * error : false
     * results : [{"_id":"57facc74421aa95de3b8ab6b","createdAt":"2016-10-10T07:02:12.35Z","desc":"10-10","publishedAt":"2016-10-10T11:41:33.500Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034jw1f8mssipb9sj20u011hgqk.jpg","used":true,"who":"daimajia"},{"_id":"57f98925421aa95de3b8ab5a","createdAt":"2016-10-09T08:02:45.353Z","desc":"10-9","publishedAt":"2016-10-09T11:45:38.236Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f8lox7c1pbj20u011h12x.jpg","used":true,"who":"daimajia"},{"_id":"57f85461421aa95dd78e8daf","createdAt":"2016-10-08T10:05:21.431Z","desc":"10-8","publishedAt":"2016-10-08T11:42:07.440Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034jw1f8kmud15q1j20u011hdjg.jpg","used":true,"who":"daimajia"},{"_id":"57edba71421aa95dd78e8d93","createdAt":"2016-09-30T09:05:53.26Z","desc":"出发去旅行","publishedAt":"2016-09-30T11:46:31.941Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f8bc5c5n4nj20u00irgn8.jpg","used":true,"who":"嗲马甲"},{"_id":"57ec633b421aa95dd78e8d86","createdAt":"2016-09-29T08:41:31.815Z","desc":"9-29","publishedAt":"2016-09-29T11:36:17.680Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034jw1f8a5uj64mpj20u00u0tca.jpg","used":true,"who":"daimajia"},{"_id":"57eb0495421aa95dd78e8d79","createdAt":"2016-09-28T07:45:25.283Z","desc":"9-28","publishedAt":"2016-09-28T11:35:12.91Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034jw1f88ylsqjvqj20u011hn5i.jpg","used":true,"who":"代码家"},{"_id":"57e9e46f421aa95ddb9cb547","createdAt":"2016-09-27T11:15:59.299Z","desc":"秋天来了","publishedAt":"2016-09-27T11:41:22.507Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034jw1f87z2n2taej20u011h11h.jpg","used":true,"who":"代码家"},{"_id":"57e7e200421aa95dd351b076","createdAt":"2016-09-25T22:41:04.582Z","desc":"9-26","publishedAt":"2016-09-26T11:52:58.626Z","source":"chrome","type":"福利","url":"http://ww1.sinaimg.cn/large/610dc034jw1f867mvc6qjj20u00u0wh7.jpg","used":true,"who":"daimajia"},{"_id":"57e477fa421aa95bc338987d","createdAt":"2016-09-23T08:31:54.365Z","desc":"9-23","publishedAt":"2016-09-23T11:38:57.170Z","source":"chrome","type":"福利","url":"http://ww3.sinaimg.cn/large/610dc034jw1f837uocox8j20f00mggoo.jpg","used":true,"who":"daimajia"},{"_id":"57e319fd421aa95bc338986a","createdAt":"2016-09-22T07:38:37.240Z","desc":"9-22","publishedAt":"2016-09-22T11:44:08.156Z","source":"chrome","type":"福利","url":"http://ww4.sinaimg.cn/large/610dc034jw1f820oxtdzzj20hs0hsdhl.jpg","used":true,"who":"代码家"}]
     */
    private boolean error;

    private List<WelfarePhotoInfo> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<WelfarePhotoInfo> getResults() {
        return results;
    }

    public void setResults(List<WelfarePhotoInfo> results) {
        this.results = results;
    }
}
