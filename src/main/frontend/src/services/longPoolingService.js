export class LongPollingService {
    constructor() {
        this.isPolling = false;
        this.sessionId = null;
        this.pollTimeout = 30000;
        this.retryDelay = 2000;
        this.maxRetries = 3;
        this.currentRetries = 0;
        this.abortController = null;
        this.listeners = new Map();
    }

    async connect(sessionId, callbacks = {}) {
        if (this.isPolling) {
            console.warn('Already connected, disconnecting first');
            this.disconnect();
        }

        this.sessionId = sessionId;
        this.isPolling = true;
        this.currentRetries = 0;

        console.log('Starting session:', sessionId);

        callbacks.onConnected?.({ sessionId, timestamp: Date.now() });
        this.poll(callbacks);
    }

    async poll(callbacks) {
        while (this.isPolling) {
            try {
                this.abortController = new AbortController();
                const timeoutId = setTimeout(
                    () => this.abortController.abort(),
                    this.pollTimeout + 5000
                );

                const response = await fetch(
                    `/api/events/${this.sessionId}?timeout=${this.pollTimeout}`,
                    {
                        method: 'GET',
                        headers: { 'Content-Type': 'application/json' },
                        signal: this.abortController.signal
                    }
                );

                clearTimeout(timeoutId);

                this.currentRetries = 0;

                if (response.ok) {
                    if (response.status === 200) {
                        const event = await response.json();
                        console.log('Event received:', event.type);

                        this.handleEvent(event, callbacks);
                    } else if (response.status === 204) {
                        console.log('Poll timeout, reconnecting...');
                    }
                } else {
                    console.error('Poll error:', response.status, response.statusText);
                    callbacks.onError?.({
                        status: response.status,
                        message: response.statusText
                    });

                    await this.delay(this.retryDelay);
                }

            } catch (error) {
                if (error.name === 'AbortError') {
                    console.log('Request aborted');
                    continue;
                }

                console.error('Polling error:', error);
                callbacks.onError?.(error);

                this.currentRetries++;

                if (this.currentRetries >= this.maxRetries) {
                    console.error('Max retries reached, stopping...');
                    callbacks.onMaxRetriesFailed?.();
                    this.disconnect();
                    break;
                }

                const delay = this.retryDelay * Math.pow(2, this.currentRetries - 1);
                console.log(`Retry ${this.currentRetries}/${this.maxRetries} in ${delay}ms`);
                await this.delay(delay);
            }
        }
    }

    handleEvent(event, callbacks) {
        // Call type-specific listener
        const typeListeners = this.listeners.get(event.type);
        if (typeListeners) {
            typeListeners.forEach(callback => {
                try {
                    callback(event);
                } catch (error) {
                    console.error('Error in event listener:', error);
                }
            });
        }

        callbacks.onEvent?.(event);
    }

    addEventListener(eventType, callback) {
        if (!this.listeners.has(eventType)) {
            this.listeners.set(eventType, new Set());
        }
        this.listeners.get(eventType).add(callback);

        console.log(`Added listener for event type: ${eventType}`);
    }

    removeEventListener(eventType, callback) {
        const listeners = this.listeners.get(eventType);
        if (listeners) {
            listeners.delete(callback);
            if (listeners.size === 0) {
                this.listeners.delete(eventType);
            }
        }
    }

    disconnect() {
        console.log('Disconnecting...');

        this.isPolling = false;

        if (this.abortController) {
            this.abortController.abort();
            this.abortController = null;
        }

        if (this.sessionId) {
            fetch(`/api/events/${this.sessionId}`, {
                method: 'DELETE'
            }).catch(error => {
                console.error('Error notifying disconnect:', error);
            });
        }

        this.sessionId = null;
        this.currentRetries = 0;
        this.listeners.clear();
    }

    isConnected() {
        return this.isPolling && this.sessionId !== null;
    }

    delay(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }
}

export const longPollingService = new LongPollingService();