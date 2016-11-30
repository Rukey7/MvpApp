package com.dl7.downloaderlib.db;

import com.dl7.downloaderlib.entity.FileInfo;

/**
 * Created by long on 2016/5/26.
 * 数据库访问接口
 */
public interface FileDAO {
    /**
     * 插入线程信息
     * @param info
     * @return void
     */
    void insert(FileInfo info);
    /**
     * 删除线程信息
     * @param url
     * @return void
     */
    void delete(String url);
    /**
     * 更新线程下载进度
     * @param info
     */
    void update(FileInfo info);
    /**
     * 查询文件的线程信息
     * @param url
     * @return
     */
    FileInfo query(String url);
    /**
     * 线程信息是否存在
     * @param url
     * @return boolean
     */
    boolean isExists(String url);
}
