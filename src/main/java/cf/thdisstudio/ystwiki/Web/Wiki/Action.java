package cf.thdisstudio.ystwiki.Web.Wiki;

public enum Action {
    Create("생성"),
    Lock("보호"),
    Edit("수정"),
    Delete("삭제");

    String s;

    Action(String s) {
        this.s = s;
    }

    public String getValue(){
        return s;
    }
}
