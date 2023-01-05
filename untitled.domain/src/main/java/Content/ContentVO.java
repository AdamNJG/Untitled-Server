package Content;

import java.util.Date;
import java.util.Objects;

public class ContentVO {
    private String content;
    private Date dateCreated;
    private Date dateUpdated;

    protected ContentVO(String content){
        this.content = content;
        this.dateCreated = new Date();
    }

    protected ContentVO(String content, Date dateCreated){
        this.content = content;
        this.dateCreated = dateCreated;
        this.dateUpdated = new Date();
    }

    protected String getContent() {
        return content;
    }

    protected Date getDateCreated(){
        return dateCreated;
    }

    protected Date getDateUpdated(){
        return dateUpdated;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ContentVO other)){
            return false;
        }

        boolean dateUpdatedEquals = (dateUpdated == null && other.dateUpdated == null)
                || (dateUpdated != null && other.dateUpdated != null && dateUpdated.equals(other.dateUpdated));

        return (content.equals(other.content)
                    && dateCreated.equals(other.dateCreated)
                    && dateUpdatedEquals);
    }

    @Override
    public int hashCode(){
        return Objects.hash(content, dateCreated, dateUpdated) * 31;
    }
}
