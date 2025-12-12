export function getMethodColor(method) {
    const map = {
        GET: 'info',
        POST: 'success',
        PUT: 'warning',
        DELETE: 'danger',
        PATCH: 'secondary'
    }
    return map[method] || 'secondary'
}

export function getStatusColor(status) {
    if (status >= 200 && status < 300) return 'success'
    if (status >= 400 && status < 500) return 'warning'
    if (status >= 500) return 'danger'
    return 'secondary'
}