package cn.yj.videoplay.event;

/**
 * Created by yangjie on 2019/4/5.
 */

public class VideoPrepareEvent {
    private int totalVideoTime;// 影片总时长，毫秒级

    public VideoPrepareEvent(int totalVideoTime) {
        this.totalVideoTime = totalVideoTime;
    }

    public int getTotalVideoTime() {
        return totalVideoTime;
    }

    public void setTotalVideoTime(int totalVideoTime) {
        this.totalVideoTime = totalVideoTime;
    }
}
