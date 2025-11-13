import React, { useState } from 'react';
import './MessageInput.css';

interface MessageInputProps {
    onSendMessage: (message: string) => void;
    disabled: boolean;
    placeholder: string;
}

const MessageInput: React.FC<MessageInputProps> = ({
                                                       onSendMessage,
                                                       disabled,
                                                       placeholder
                                                   }) => {
    const [message, setMessage] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (message.trim() && !disabled) {
            onSendMessage(message);
            setMessage('');
        }
    };

    const handleKeyPress = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSubmit(e);
        }
    };

    return (
        <form className="message-input" onSubmit={handleSubmit}>
            <div className="input-container">
                <button type="button" className="attachment-button" disabled={disabled}>
                    <i className="fas fa-paperclip"></i>
                </button>

                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyPress={handleKeyPress}
                    placeholder={placeholder}
                    disabled={disabled}
                    className="message-field"
                />

                <button
                    type="submit"
                    disabled={!message.trim() || disabled}
                    className="send-button"
                >
                    <i className="fas fa-paper-plane"></i>
                </button>
            </div>
        </form>
    );
};

export default MessageInput;