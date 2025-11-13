import type {ChatMessage, ChatSession, Advisor} from '../types/chat/chat.ts';

class ChatService {
    // private apiUrl = process.env.REACT_APP_API_URL;
    private apiUrl = '/api/v1';
    private simulatedMessages: Map<string, ChatMessage[]> = new Map();
    private simulatedSessions: ChatSession[] = [];
    private simulatedAdvisors: Advisor[] = [];

    constructor() {
        console.log("apiUrl: " + this.apiUrl);
        this.initializeSimulatedData();
    }

    private initializeSimulatedData() {
        // Simulierte Berater
        this.simulatedAdvisors = [
            {
                id: '1',
                name: 'Dr. Maria Schmidt',
                specialization: 'Psychologische Beratung',
                isOnline: true,
                responseTime: 'Innerhalb von 2 Stunden',
                rating: 4.8
            },
            {
                id: '2',
                name: 'Prof. Thomas Weber',
                specialization: 'Karriereberatung',
                isOnline: false,
                responseTime: 'Innerhalb von 4 Stunden',
                rating: 4.6
            },
            {
                id: '3',
                name: 'Lisa Hoffmann',
                specialization: 'Familienberatung',
                isOnline: true,
                responseTime: 'Innerhalb von 1 Stunde',
                rating: 4.9
            },
            {
                id: '4',
                name: 'Peter Hoffmann',
                specialization: 'Familienberatung',
                isOnline: true,
                responseTime: 'Innerhalb von 1 Stunde',
                rating: 4.9
            }
        ];

        // Simulierte Chat-Sessions
        this.simulatedSessions = [
            {
                id: 'session-1',
                advisorId: '1',
                advisorName: 'Dr. Maria Schmidt',
                lastMessage: 'Vielen Dank für Ihre Nachricht. Wie kann ich helfen?',
                lastMessageTime: new Date(Date.now() - 1000 * 60 * 30), // 30 Minuten her
                unreadCount: 2,
                isOnline: true
            },
            {
                id: 'session-2',
                advisorId: '2',
                advisorName: 'Prof. Thomas Weber',
                lastMessage: 'Habe Ihre Unterlagen erhalten.',
                lastMessageTime: new Date(Date.now() - 1000 * 60 * 60 * 2), // 2 Stunden her
                unreadCount: 0,
                isOnline: false
            }
        ];

        // Simulierte Nachrichten
        this.simulatedMessages.set('1', [
            {
                id: '1',
                content: 'Guten Tag, ich hätte eine Frage zu meiner Beratung.',
                senderId: 'user-1',
                senderName: 'Max Mustermann',
                senderType: 'user',
                timestamp: new Date(Date.now() - 1000 * 60 * 35),
                type: 'text',
                read: true
            },
            {
                id: '2',
                content: 'Guten Tag, gerne helfe ich Ihnen. Was möchten Sie wissen?',
                senderId: '1',
                senderName: 'Dr. Maria Schmidt',
                senderType: 'advisor',
                timestamp: new Date(Date.now() - 1000 * 60 * 32),
                type: 'text',
                read: true
            },
            {
                id: '3',
                content: 'Ich würde gerne wissen, wie die weiteren Schritte aussehen.',
                senderId: 'user-1',
                senderName: 'Max Mustermann',
                senderType: 'user',
                timestamp: new Date(Date.now() - 1000 * 60 * 30),
                type: 'text',
                read: true
            },
            {
                id: '4',
                content: 'Vielen Dank für Ihre Nachricht. Wie kann ich helfen?',
                senderId: '1',
                senderName: 'Dr. Maria Schmidt',
                senderType: 'advisor',
                timestamp: new Date(Date.now() - 1000 * 60 * 28),
                type: 'text',
                read: false
            }
        ]);
    }

    // ECHTE API-IMPLEMENTIERUNG (auskommentiert)
    /*
    async getChatHistory(advisorId: string): Promise<ChatMessage[]> {
      try {
        const response = await fetch(`${this.apiUrl}/chat/${advisorId}/history`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return data.messages.map((msg: any) => ({
          ...msg,
          timestamp: new Date(msg.timestamp)
        }));
      } catch (error) {
        console.error('Error fetching chat history:', error);
        throw error;
      }
    }
    */

    // SIMULIERTE IMPLEMENTIERUNG
    async getChatHistory(advisorId: string): Promise<ChatMessage[]> {
        // Simuliere Netzwerk-Latenz
        await new Promise(resolve => setTimeout(resolve, 500));

        const messages = this.simulatedMessages.get(advisorId) || [];
        return messages;
    }

    // ECHTE API-IMPLEMENTIERUNG (auskommentiert)
    /*
    async sendMessage(advisorId: string, content: string): Promise<ChatMessage> {
      try {
        const response = await fetch(`${this.apiUrl}/chat/${advisorId}/message`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ content })
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return {
          ...data.message,
          timestamp: new Date(data.message.timestamp)
        };
      } catch (error) {
        console.error('Error sending message:', error);
        throw error;
      }
    }
    */

    // SIMULIERTE IMPLEMENTIERUNG
    async sendMessage(advisorId: string, content: string): Promise<ChatMessage> {
        await new Promise(resolve => setTimeout(resolve, 300));

        const newMessage: ChatMessage = {
            id: `msg-${Date.now()}`,
            content,
            senderId: 'user-1', // In echter Implementierung von Auth-Context
            senderName: 'Aktueller Benutzer',
            senderType: 'user',
            timestamp: new Date(),
            type: 'text',
            read: false
        };

        // Nachricht zur Historie hinzufügen
        const currentMessages = this.simulatedMessages.get(advisorId) || [];
        this.simulatedMessages.set(advisorId, [...currentMessages, newMessage]);

        // Session aktualisieren
        const sessionIndex = this.simulatedSessions.findIndex(s => s.advisorId === advisorId);
        if (sessionIndex !== -1) {
            this.simulatedSessions[sessionIndex] = {
                ...this.simulatedSessions[sessionIndex],
                lastMessage: content,
                lastMessageTime: new Date()
            };
        }

        return newMessage;
    }

    // ECHTE API-IMPLEMENTIERUNG (auskommentiert)
    /*
    async getChatSessions(): Promise<ChatSession[]> {
      try {
        const response = await fetch(`${this.apiUrl}/chat/sessions`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return data.sessions.map((session: any) => ({
          ...session,
          lastMessageTime: new Date(session.lastMessageTime)
        }));
      } catch (error) {
        console.error('Error fetching chat sessions:', error);
        throw error;
      }
    }
    */

    // SIMULIERTE IMPLEMENTIERUNG
    async getChatSessions(): Promise<ChatSession[]> {
        await new Promise(resolve => setTimeout(resolve, 600));
        return this.simulatedSessions;
    }

    // ECHTE API-IMPLEMENTIERUNG (auskommentiert)
    /*
    async getAvailableAdvisors(): Promise<Advisor[]> {
      try {
        const response = await fetch(`${this.apiUrl}/advisors/available`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return data.advisors;
      } catch (error) {
        console.error('Error fetching advisors:', error);
        throw error;
      }
    }
    */

    // SIMULIERTE IMPLEMENTIERUNG
    async getAvailableAdvisors(): Promise<Advisor[]> {
        await new Promise(resolve => setTimeout(resolve, 400));
        return this.simulatedAdvisors;
    }

    // ECHTE API-IMPLEMENTIERUNG (auskommentiert)
    /*
    async markMessagesAsRead(advisorId: string, messageIds: string[]): Promise<void> {
      try {
        await fetch(`${this.apiUrl}/chat/${advisorId}/read`, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({ messageIds })
        });
      } catch (error) {
        console.error('Error marking messages as read:', error);
        throw error;
      }
    }
    */

    // SIMULIERTE IMPLEMENTIERUNG
    async markMessagesAsRead(advisorId: string, messageIds: string[]): Promise<void> {
        await new Promise(resolve => setTimeout(resolve, 200));

        const messages = this.simulatedMessages.get(advisorId) || [];
        const updatedMessages = messages.map(msg =>
            messageIds.includes(msg.id) ? { ...msg, read: true } : msg
        );
        this.simulatedMessages.set(advisorId, updatedMessages);

        // Unread Count in Session zurücksetzen
        const sessionIndex = this.simulatedSessions.findIndex(s => s.advisorId === advisorId);
        if (sessionIndex !== -1) {
            this.simulatedSessions[sessionIndex].unreadCount = 0;
        }
    }

    // WebSocket Simulation für Echtzeit-Updates
    simulateRealtimeMessages(advisorId: string, callback: (message: ChatMessage) => void) {
        // In echter Implementierung: WebSocket connection
        // Für Demo: Simuliere eingehende Nachrichten nach zufälliger Zeit

        const simulateAdvisorResponse = () => {
            const responses = [
                "Vielen Dank für Ihre Nachricht. Ich bearbeite Ihre Anfrage.",
                "Könnten Sie mir bitte mehr Details mitteilen?",
                "Ich verstehe Ihr Anliegen. Lassen Sie mich das prüfen.",
                "Gerne stehe ich Ihnen für weitere Fragen zur Verfügung.",
                "Das ist eine interessante Frage. Dazu kann ich Ihnen Folgendes sagen..."
            ];

            const val = Math.random(); // Sensitive
            const randomResponse = responses[Math.floor(val * responses.length)];

            const advisorMessage: ChatMessage = {
                id: `advisor-${Date.now()}`,
                content: randomResponse,
                senderId: advisorId,
                senderName: this.simulatedAdvisors.find(a => a.id === advisorId)?.name || 'Berater',
                senderType: 'advisor',
                timestamp: new Date(),
                type: 'text',
                read: false
            };

            callback(advisorMessage);

            // Nachricht zur Historie hinzufügen
            const currentMessages = this.simulatedMessages.get(advisorId) || [];
            this.simulatedMessages.set(advisorId, [...currentMessages, advisorMessage]);

            // Unread Count erhöhen
            const sessionIndex = this.simulatedSessions.findIndex(s => s.advisorId === advisorId);
            if (sessionIndex !== -1) {
                this.simulatedSessions[sessionIndex].unreadCount += 1;
                this.simulatedSessions[sessionIndex].lastMessage = randomResponse;
                this.simulatedSessions[sessionIndex].lastMessageTime = new Date();
            }
        };

        // Simuliere Advisor-Antwort nach 3-8 Sekunden
        const responseDelay = 3000 + Math.random() * 5000;
        const timeoutId = setTimeout(simulateAdvisorResponse, responseDelay);

        return () => clearTimeout(timeoutId);
    }
}

export const chatService = new ChatService();