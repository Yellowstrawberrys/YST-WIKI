package cf.thdisstudio.ystwiki.Web.Wiki;

public class WikiDocument {
    int pageId;
    String title;
    String path;
    String createdTime;
    String lastEdited;
    String contents;
    int permission;
    public WikiDocument(int pageId, String title, String path, String createdTime, String lastEdited, int permission, String contents){
        this.pageId = pageId;
        this.title = title;
        this.path = path;
        this.createdTime = createdTime;
        this.lastEdited = lastEdited;
        this.permission = permission;
        this.contents = contents;
    }

    public int getPageId() {
        return pageId;
    }

    public int getPermission() {
        return permission;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getLastEdited() {
        return lastEdited;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }
}
