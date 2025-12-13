export class SseService {
    constructor() {
        this.eventSource = null;
        this.isConnected = false;
        this.listeners = new Map();
        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 5;
        this.reconnectDelay = 3000;
        this.sessionId = null;
    }

    connect(sessionId, callbacks = {}) {
        this.disconnect();
        console.log('Connecting to sse')
        this.sessionId = sessionId;
        const url = `/sse/stream/${sessionId}`;
        this.eventSource = new EventSource(url);

        this.eventSource.onopen = () => {
            this.isConnected = true;
            this.reconnectAttempts = 0;
            console.log('sse connected');
            callbacks.onOpen?.();
        };

        this.eventSource.onerror = (error) => {
            console.error('sse error', error);

            if (this.eventSource.readyState === EventSource.CLOSED) {
                this.isConnected = false;
                console.log('sse connection closed');
                callbacks.onClose?.();

                if (this.reconnectAttempts < this.maxReconnectAttempts) {
                    this.reconnectAttempts++;
                    console.log(`reconnecting... (attempt ${this.reconnectAttempts}/${this.maxReconnectAttempts})`);

                    setTimeout(() => {
                        if (this.sessionId && !this.isConnected) {
                            this.connect(this.sessionId, callbacks);
                        }
                    }, this.reconnectDelay);
                } else {
                    console.error('max sse reconnect attempts reached');
                    callbacks.onMaxReconnectFailed?.()
                }
            }

            callbacks.onError?.(error);
        }

        this.addEventListener("CONNECTED", (event) => {
            const data = JSON.parse(event.data);
            callbacks.onConnected?.(data);
        });

        this.addEventListener("HEARTBEAT", (event) => {
            const data = JSON.parse(event.data);
            callbacks.onHeartbeat?.(data);
        });
    }

    addEventListener(eventName, callback) {
        if (!this.eventSource) {
            console.warn("EventSource not initialized");
            return;
        }

        if (!this.listeners.has(eventName)) {
            this.listeners.set(eventName, []);
        }
        this.listeners.get(eventName).push(callback);
        this.eventSource.addEventListener(eventName, callback);
    }

    removeEventListener(eventName, callback) {
        if (!this.eventSource) return;

        this.eventSource.removeEventListener(eventName, callback);
        const listeners = this.listeners.get(eventName);
        if (listeners) {
            const index = listeners.indexOf(callback);
            if (index > -1) {
                listeners.splice(index, 1);
            }
        }
    }

    disconnect() {
        if (this.eventSource) {
            console.log('sse disconnecting...');

            this.listeners.forEach((callbacks, eventName) => {
                callbacks.forEach(callback => {
                    this.eventSource.removeEventListener(eventName, callback);
                });
            });
            this.listeners.clear();
            this.eventSource.close();
            this.eventSource = null;
        }
        this.isConnected = false;
        this.reconnectAttempts = 0;
        this.sessionId = null;
    }
}

export const sseService = new SseService();