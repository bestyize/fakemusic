package com.yize.fakemusic.qqmusic;

/**
 * /**
 *  * 文件名       ：SearchListener.java
 *  * 作用         :外界调用时的回调接口
 *  * By           :亦泽
 *  * Date         :2019-1-19 21.11
 *  * 微信公众号    ：从来不想
 *  *
 *  * /
 */

public interface SearchListener {
    void onSuccess(MusicInfo musicInfo);
    void onFailed(String reason);
}

