import axios from "axios";

export const boardApi = {
    async startSession() {
        try {
            const res = await axios.post('/api/start', {});
            return res.data?.sessionId || null;
        } catch (e) {
            console.error("Error starting session", e);
            return null;
        }
    },

    async reset() {
        try {
            await axios.post('/api/reset', {});
        } catch (e) {
            console.error("Failed to reset", e);
            throw e;
        }
    }
};