CREATE SCHEMA identity;
CREATE SCHEMA messaging;

CREATE TABLE identity.users (
    id UUID PRIMARY KEY,
    phone_number VARCHAR(20) UNIQUE NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE messaging.chats (
    id UUID PRIMARY KEY,
    type VARCHAR(20) NOT NULL, -- 'DM', etc.
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE messaging.chat_members (
    chat_id UUID REFERENCES messaging.chats(id) ON DELETE CASCADE,
    user_id UUID NOT NULL, -- Soft reference to Identity Context
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (chat_id, user_id)
);
-- For querying all chats for a user
-- No need index for chat_id since left most column in a composite key is auto indexed
CREATE INDEX idx_chat_members_user ON messaging.chat_members(user_id);

CREATE TABLE messaging.messages (
    id UUID PRIMARY KEY,
    chat_id UUID REFERENCES messaging.chats(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL, -- Soft reference to Identity Context
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_messages_chat ON messaging.messages(chat_id, created_at DESC); -- For querying last messages of a chat
