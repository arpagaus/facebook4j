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

package facebook4j.api;

import facebook4j.CommentUpdate;
import facebook4j.Conversation;
import facebook4j.FacebookException;
import facebook4j.InboxResponseList;
import facebook4j.Message;
import facebook4j.Reading;
import facebook4j.ResponseList;

public interface ConversationMethods {
    /**
     * Returns the messages in the current user's inbox.
     * @return messages
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/reference/api/user/">User - Facebook Developers</a> - Connections - inbox
     */
    InboxResponseList<Conversation> getConversations() throws FacebookException;

    /**
     * Returns the messages in the current user's inbox.
     * @param reading optional reading parameters. see <a href="https://developers.facebook.com/docs/reference/api/#reading">Graph API#reading - Facebook Developers</a>
     * @return messages
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/reference/api/user/">User - Facebook Developers</a> - Connections - inbox
     */
    InboxResponseList<Conversation> getConversations(Reading reading) throws FacebookException;

    /**
     * Returns the messages in a user's inbox.
     * @param userId the ID of a user
     * @return messages
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/reference/api/user/">User - Facebook Developers</a> - Connections - inbox
     */
    InboxResponseList<Conversation> getConversations(String userId) throws FacebookException;

    /**
     * Returns the messages in a user's inbox.
     * @param userId the ID of a user
     * @param reading optional reading parameters. see <a href="https://developers.facebook.com/docs/reference/api/#reading">Graph API#reading - Facebook Developers</a>
     * @return messages
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/reference/api/user/">User - Facebook Developers</a> - Connections - inbox
     */
    InboxResponseList<Conversation> getConversations(String userId, Reading reading) throws FacebookException;


    /**
     * Returns a single message.
     * @param messageId the ID of the message
     * @return message
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/reference/api/message/">Message - Facebook Developers</a>
     */
    Conversation getConversation(String conversationId) throws FacebookException;

    /**
     * Returns a single message.
     * @param messageId the ID of the message
     * @param reading optional reading parameters. see <a href="https://developers.facebook.com/docs/reference/api/#reading">Graph API#reading - Facebook Developers</a>
     * @return message
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/reference/api/message/">Message - Facebook Developers</a>
     */
    Conversation getConversation(String conversationId, Reading reading) throws FacebookException;

    /**
     * Answer conversation.
     * @param conversationId the ID of the conversation
     * @param message comment text
     * @return The new message ID
     * @throws FacebookException when Facebook service or network is unavailable
     * @see <a href="https://developers.facebook.com/docs/graph-api/reference/v2.7/conversation/messages">Conversation#messages - Facebook Developers</a>
     */
    String answerConversation(String conversationId, String message) throws FacebookException;

}
