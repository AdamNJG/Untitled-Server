package Content;

import java.util.Date;

public class Content {
    private ContentVO valueObject;

    public Content(String content) {
        valueObject = new ContentVO(content);
    }

    public String getContent() {
        return valueObject.getContent();
    }

    public Date getDateCreated() {
        return valueObject.getDateCreated();
    }

    public Date getDateUpdated() {
        return valueObject.getDateUpdated();
    }

    public void changeContent(String newContent) {
        valueObject = new ContentVO(newContent, valueObject.getDateCreated());
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Content other)){
            return false;
        }
        return valueObject.equals(other.valueObject);
    }

    @Override
    public int hashCode(){
        return valueObject.hashCode();
    }
}
