/*
 * Copyright 2012 Ryuji Yamashita
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package facebook4j.internal.json;

import facebook4j.Comment;
import facebook4j.FacebookException;
import facebook4j.IdNameEntity;
import facebook4j.Image;
import facebook4j.InboxResponseList;
import facebook4j.Message;
import facebook4j.PagableList;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.internal.http.HttpResponse;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static facebook4j.internal.util.z_F4JInternalParseUtil.*;

/**
 * @author Ryuji Yamashita - roundrop at gmail.com
 */
/*package*/ final class MessageJSONImpl extends FacebookResponseImpl implements Message, java.io.Serializable {
    private static final long serialVersionUID = -2666008917993827020L;

    private String id;
    private IdNameEntity from;
    private List<IdNameEntity> to;
    private PagableList<Attachment> attachments;
    private String message;
    private Date createdTime;
    private Date updatedTime;
    private PagableList<Comment> comments;
    private Integer unread;
    private Integer unseen;

    /*package*/MessageJSONImpl(HttpResponse res, Configuration conf) throws FacebookException {
        super(res);
        JSONObject json = res.asJSONObject();
        init(json);
        if (conf.isJSONStoreEnabled()) {
            DataObjectFactoryUtil.clearThreadLocalMap();
            DataObjectFactoryUtil.registerJSONObject(this, json);
        }
    }

    /*package*/MessageJSONImpl(JSONObject json) throws FacebookException {
        super();
        init(json);
    }

    private void init(JSONObject json) throws FacebookException {
        try {
            id = getRawString("id", json);
            if (!json.isNull("from")) {
                JSONObject fromJSONObject = json.getJSONObject("from");
                from = new IdNameEntityJSONImpl(fromJSONObject);
            }
            if (!json.isNull("to")) {
                JSONObject toJSONObject = json.getJSONObject("to");
                JSONArray toJSONArray = toJSONObject.getJSONArray("data");
                to = new ArrayList<IdNameEntity>();
                for (int i = 0; i < toJSONArray.length(); i++) {
                    to.add(new IdNameEntityJSONImpl(toJSONArray.getJSONObject(i)));
                }
            } else {
                to = Collections.emptyList();
            }
            message = getRawString("message", json);
            createdTime = getISO8601Datetime("created_time", json);
            updatedTime = getISO8601Datetime("updated_time", json);
            if (!json.isNull("comments")) {
                JSONObject commentsJSONObject = json.getJSONObject("comments");
                if (!commentsJSONObject.isNull("data")) {
                    JSONArray list = commentsJSONObject.getJSONArray("data");
                    final int size = list.length();
                    comments = new PagableListImpl<Comment>(size, commentsJSONObject);
                    for (int i = 0; i < size; i++) {
                        CommentJSONImpl comment = new CommentJSONImpl(list.getJSONObject(i));
                        comments.add(comment);
                    }
                } else {
                    comments = new PagableListImpl<Comment>(1, commentsJSONObject);
                }
            } else {
                comments = new PagableListImpl<Comment>(0);
            }
            if (!json.isNull("attachments")) {
            	JSONObject attachmentsJSONObject = json.getJSONObject("attachments");
            	if (!attachmentsJSONObject.isNull("data")) {
            		JSONArray list = attachmentsJSONObject.getJSONArray("data");
            		final int size = list.length();
            		attachments = new PagableListImpl<Attachment>(size, attachmentsJSONObject);
            		for (int i = 0; i < size; i++) {
            			AttachmentJSONImpl attachment = new AttachmentJSONImpl(list.getJSONObject(i));
            			attachments.add(attachment);
            		}
            	} else {
            		attachments = new PagableListImpl<Attachment>(1, attachmentsJSONObject);
            	}
            } else {
            	attachments = new PagableListImpl<Attachment>(0);
            }
            if (!json.isNull("unread")) {
                unread = getPrimitiveInt("unread", json);
            }
            if (!json.isNull("unseen")) {
                unseen = getPrimitiveInt("unseen", json);
            }
        } catch (JSONException jsone) {
            throw new FacebookException(jsone.getMessage(), jsone);
        }
    }

    public String getId() {
        return id;
    }
    public IdNameEntity getFrom() {
        return from;
    }
    public List<IdNameEntity> getTo() {
        return to;
    }
    public PagableList<Attachment> getAttachments() {
    	return attachments;
    }
    public String getMessage() {
        return message;
    }
    public Date getCreatedTime() {
        return createdTime;
    }
    public Date getUpdatedTime() {
        return updatedTime;
    }
    public PagableList<Comment> getComments() {
        return comments;
    }
    public Integer getUnread() {
        return unread;
    }

    public Integer getUnseen() {
        return unseen;
    }

    /*package*/
    static ResponseList<Message> createMessageList(HttpResponse res, Configuration conf) throws FacebookException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("data");
            final int size = list.length();
            ResponseList<Message> messages = new ResponseListImpl<Message>(size, json);
            for (int i = 0; i < size; i++) {
                JSONObject messageJSONObject = list.getJSONObject(i);
                Message message = new MessageJSONImpl(messageJSONObject);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(message, messageJSONObject);
                }
                messages.add(message);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(messages, list);
            }
            return messages;
        } catch (JSONException jsone) {
            throw new FacebookException(jsone);
        }
    }

    /*package*/
    static InboxResponseList<Message> createInboxMessageList(HttpResponse res, Configuration conf) throws FacebookException {
        try {
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.clearThreadLocalMap();
            }
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("data");
            final int size = list.length();
            InboxResponseList<Message> messages = new InboxResponseListImpl<Message>(size, json);
            for (int i = 0; i < size; i++) {
                JSONObject messageJSONObject = list.getJSONObject(i);
                Message message = new MessageJSONImpl(messageJSONObject);
                if (conf.isJSONStoreEnabled()) {
                    DataObjectFactoryUtil.registerJSONObject(message, messageJSONObject);
                }
                messages.add(message);
            }
            if (conf.isJSONStoreEnabled()) {
                DataObjectFactoryUtil.registerJSONObject(messages, list);
            }
            return messages;
        } catch (JSONException jsone) {
            throw new FacebookException(jsone);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MessageJSONImpl other = (MessageJSONImpl) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "MessageJSONImpl{" +
                "id='" + id + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", attachments='" + attachments + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", comments=" + comments +
                ", unread=" + unread +
                ", unseen=" + unseen +
                '}';
    }

    private final class AttachmentJSONImpl implements Message.Attachment, java.io.Serializable {
		private static final long serialVersionUID = 2383005779931406513L;

        private String description;
        private AttachmentMedia media;
        private AttachmentTarget target;
        private String title;
        private String type;
        private String url;

        AttachmentJSONImpl(JSONObject json) throws FacebookException {
            try {
                description = getRawString("description", json);
                if (!json.isNull("media")) {
                    media = new AttachmentMediaJSONImpl(json.getJSONObject("media"));
                }
                if (!json.isNull("target")) {
                    target = new AttachmentTargetJSONImpl(json.getJSONObject("target"));
                }
                title = getRawString("title", json);
                type = getRawString("type", json);
                url = getRawString("url", json);
            } catch (JSONException jsone) {
                throw new FacebookException(jsone.getMessage(), jsone);
            }
        }

        public String getDescription() {
            return description;
        }

        public AttachmentMedia getMedia() {
            return media;
        }

        public AttachmentTarget getTarget() {
            return target;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AttachmentJSONImpl)) return false;

            AttachmentJSONImpl that = (AttachmentJSONImpl) o;

            if (description != null ? !description.equals(that.description) : that.description != null) return false;
            if (media != null ? !media.equals(that.media) : that.media != null) return false;
            if (target != null ? !target.equals(that.target) : that.target != null) return false;
            if (title != null ? !title.equals(that.title) : that.title != null) return false;
            if (type != null ? !type.equals(that.type) : that.type != null) return false;
            if (url != null ? !url.equals(that.url) : that.url != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = description != null ? description.hashCode() : 0;
            result = 31 * result + (media != null ? media.hashCode() : 0);
            result = 31 * result + (target != null ? target.hashCode() : 0);
            result = 31 * result + (title != null ? title.hashCode() : 0);
            result = 31 * result + (type != null ? type.hashCode() : 0);
            result = 31 * result + (url != null ? url.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "AttachmentJSONImpl{" +
                    "description='" + description + '\'' +
                    ", media=" + media +
                    ", target=" + target +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        private final class AttachmentMediaJSONImpl implements AttachmentMedia, java.io.Serializable {
            private static final long serialVersionUID = -4030126370782822645L;

            private final Image image;

            AttachmentMediaJSONImpl(JSONObject json) throws FacebookException {
                try {
                    image = new ImageJSONImpl(json.getJSONObject("image"));
                } catch (JSONException jsone) {
                    throw new FacebookException(jsone.getMessage(), jsone);
                }
            }

            public Image getImage() {
                return image;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof AttachmentMediaJSONImpl)) return false;

                AttachmentMediaJSONImpl that = (AttachmentMediaJSONImpl) o;

                return !(!image.equals(that.image));

            }

            @Override
            public int hashCode() {
                return image.hashCode();
            }

            @Override
            public String toString() {
                return "AttachmentMediaJSONImpl{" +
                        "image=" + image +
                        '}';
            }
        }

        private final class AttachmentTargetJSONImpl implements AttachmentTarget, java.io.Serializable {
            private String id;
            private String url;

            AttachmentTargetJSONImpl(JSONObject json) {
                id = getRawString("id", json);
                url = getRawString("url", json);
            }

            public String getId() {
                return id;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof AttachmentTargetJSONImpl)) return false;

                AttachmentTargetJSONImpl that = (AttachmentTargetJSONImpl) o;

                if (id != null ? !id.equals(that.id) : that.id != null) return false;
                if (url != null ? !url.equals(that.url) : that.url != null) return false;

                return true;
            }

            @Override
            public int hashCode() {
                int result = id != null ? id.hashCode() : 0;
                result = 31 * result + (url != null ? url.hashCode() : 0);
                return result;
            }

            @Override
            public String toString() {
                return "AttachmentTargetJSONImpl{" +
                        "id='" + id + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }
    }
}
