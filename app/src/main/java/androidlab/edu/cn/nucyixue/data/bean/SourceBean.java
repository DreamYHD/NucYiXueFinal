package androidlab.edu.cn.nucyixue.data.bean;

/**
 * Created by dreamY on 2017/8/20.
 */

public class SourceBean {

    private String title;//标题
    private String school;//使用的学校
    private int  downNum; //下载量
    private String updateTime;//上传时间
    private String fileType;//文件类型
    private double size;//文件大小

    public String getTitle() {
        return title;
    }
    public void setTitle(String mTitle) {
        title = mTitle;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String mSchool) {
        school = mSchool;
    }

    public int getDownNum() {
        return downNum;
    }

    public void setDownNum(int mDownNum) {
        downNum = mDownNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String mUpdateTime) {
        updateTime = mUpdateTime;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String mFileType) {
        fileType = mFileType;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double mSize) {
        size = mSize;
    }
}


