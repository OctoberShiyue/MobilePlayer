package www.r886.top.mobileplayer.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by Shiyue on 2018/10/27.
 */

public class Constants {

    private static long time=0;
    /**
     * 网络视频的联网地址
     */
//    public static final String NET_URL="https://jjw.r886.top/video/index.php/api/Video/index";
//    public static final  String NET_URL="https://aivideo.baidu.com/data/recommendvideo?size=15";

        public static   String NET_URL="https://rc.vrs.sohu.com/rc/like?pageSize=20&u="+time;
        public static String NET_URL_VIDEO="http://my.tv.sohu.com/play/videonew.do?vid=";
        public static String NET_URL_VIDEO_DOWN="https://data.vod.itc.cn/ip?new=";
        public static void setRequTime()
        {
            time=System.currentTimeMillis();
            NET_URL="https://rc.vrs.sohu.com/rc/like?pageSize=10&u="+time;
        }
        public static String getNet_Url_Load(String loc)
        {
            try {
                return "https://aivideo.baidu.com/data/recommendvideo?size=10&pn=0&ishttps=1&loc=http%253A%252F%252Fxiongzhanghao.baidu.com%252Fshortvideo%252Ff1f20d3d206d722414456c1457887460.html&log_loc="+URLEncoder.encode(loc,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
}
