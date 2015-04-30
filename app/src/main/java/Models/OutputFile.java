package models;


public class OutputFile {

    private String id;
    private String path;
    private String ext;

    public OutputFile(){

    }

    public OutputFile(String i,String p,String e){
        this.id=i;
        this.path=p;
        this.ext=e;
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getExt() {
        return ext;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
