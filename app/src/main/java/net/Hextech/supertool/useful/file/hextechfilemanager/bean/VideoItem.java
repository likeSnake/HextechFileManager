package net.Hextech.supertool.useful.file.hextechfilemanager.bean;


public class VideoItem {
    private boolean isSelect = false;
    private String path;

    public VideoItem(String path) {
        this.path = path;
    }


    public boolean getSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
