package br.com.stemcell.android.beans;

/**
 * Created by spassu on 25/07/15.
 */
public class HttpMultipartBean {

    private String name;
    private String mediaType;
    private Object content;

    public HttpMultipartBean(String name, String mediaType, Object content) {
        this.name = name;
        this.mediaType = mediaType;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
