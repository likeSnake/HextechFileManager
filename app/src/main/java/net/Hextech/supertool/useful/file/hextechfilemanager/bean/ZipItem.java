package net.Hextech.supertool.useful.file.hextechfilemanager.bean;


public class ZipItem {
    private boolean isSelect = false;
    private String path;
    private String size;
    private String time;
    private String name;


    public ZipItem(String path, String size, String time, String name) {
        this.path = path;
        this.size = size;
        this.time = time;
        this.name = name;
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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
