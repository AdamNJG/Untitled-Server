package Content;

import org.junit.jupiter.api.Test;

import javax.security.sasl.AuthorizeCallback;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ContentTest {

    @Test
    public void InstantiateContent_NotNull() {
        Content content = new Content("test");
        assertNotNull(content);
    }

    @Test
    public void InstantiateContent_HasGivenContent() {
        String input = "content";
        Content content = new Content(input);
        assertEquals(input, content.getContent());
    }

    @Test
    public void InstantiateContent_HasDateCreated() {
        Content content = new Content("");
        assertNotNull(content.getDateCreated());
    }

    @Test
    public void InstantiateContent_DateCreatedMatchesNowToSecond() {
        Content content = new Content("");
        Date now = new Date();
        assertEquals(now, content.getDateCreated());
    }

    @Test
    public void InstantiateContent_DateUpdatedNull() {
        Content content = new Content("");
        assertNull(content.getDateUpdated());
    }

    @Test
    public void ChangeContent_ContentMatches() {
        String newContent = "new Content!";
        Content content = new Content("test");
        content.changeContent(newContent);
        assertEquals(newContent, content.getContent());
    }

    @Test
    public void ChangeContent_UpdatedDateUpdated() {
        String newContent = "new Content!";
        Content content = new Content("test");

        Date originalDateUpdated = content.getDateUpdated();
        content.changeContent(newContent);
        Date newDateUpdated = new Date();

        assertNotEquals(originalDateUpdated, content.getDateUpdated());
        assertEquals(newDateUpdated, content.getDateUpdated());
    }

    @Test
    void ChangeContent_DateCreatedNotChanged() {
        String newContent = "new Content!";
        Content content = new Content("test");

        Date originalDateCreated = content.getDateCreated();
        content.changeContent(newContent);

        assertEquals(originalDateCreated, content.getDateCreated());
    }

    @Test
    void InitialiseContent_CheckEquals() {
        Content content = new Content("");
        Content content2 = new Content("");

        assertTrue(content.equals(content2));
    }

    @Test
    void ChangeContent_CheckEquals() {
        Content content = new Content("");
        Content content2 = new Content("");

        content.changeContent("new");

        assertFalse(content.equals(content2));
    }

    @Test
    void ChangeContent_CheckEqualsChangedDate() {
        Content content = new Content("");
        Content content2 = new Content("");

        content.changeContent("");

        assertFalse(content.equals(content2));
    }

    @Test
    void ChangeContent_CheckEqualsBothChangedDate() {
        Content content = new Content("");
        Content content2 = new Content("");

        content.changeContent("");
        content2.changeContent("");

        assertTrue(content.equals(content2));
    }

    @Test
    void Content_CheckEqualsNotContent() {
        Content content = new Content("");

        assertFalse(content.equals(new Object()));
    }

    @Test
    void Content_CheckHashCode() {
        Content content = new Content("");
        Content content2 = new Content("");

        assertEquals(content.hashCode(), content2.hashCode());
    }
}