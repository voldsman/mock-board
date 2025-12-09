<nav class="navbar navbar-expand-md navbar-dark border-bottom border-secondary mb-3"
     style="background-color: var(--brand-card) !important; height: 60px;">

    <div class="d-flex align-items-center">
        <a class="navbar-brand font-weight-bold mr-3" href="#">
            <span style="color: var(--brand-primary);">&lt;/&gt;</span> MockBoard
        </a>

        <div class="dropdown">
            <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="menuDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Menu
            </button>
            <div class="dropdown-menu bg-dark border-secondary" aria-labelledby="menuDropdown">
                <a class="dropdown-item text-light" href="#" onclick="alert('Import coming soon')">Import JSON</a>
                <a class="dropdown-item text-light" href="#" onclick="alert('Export coming soon')">Export Session</a>
                <div class="dropdown-divider border-secondary"></div>
                <a class="dropdown-item text-danger" href="/reset">Reset Session</a>
            </div>
        </div>
    </div>

    <div class="mx-auto w-50 d-none d-md-block">
        <div class="input-group input-group-sm">
            <div class="input-group-prepend">
                <span class="input-group-text bg-dark border-secondary text-muted">URL</span>
            </div>
            <input type="text" class="form-control bg-dark text-light border-secondary text-center text-monospace"
                   value="http://localhost:8080/m/${sessionId}/"
                   id="webhook-url" readonly>
            <div class="input-group-append">
                <button class="btn btn-outline-primary" type="button" onclick="copyUrl()">
                    Copy
                </button>
            </div>
        </div>
    </div>

    <div class="d-flex align-items-center">
        <div class="mr-3" data-toggle="tooltip" title="WebSocket Status">
            <span id="connection-status" class="badge badge-dot bg-danger"
                  style="height: 10px; width: 10px; border-radius: 50%; display: inline-block;"></span>
        </div>

        <a href="/reset" class="btn btn-sm btn-danger" title="End Session">
            End Session
        </a>
    </div>
</nav>

<div class="d-md-none bg-dark p-2 border-bottom border-secondary text-center">
    <small class="text-muted">Webhook URL:</small><br>
    <code class="text-light user-select-all">http://localhost:8080/m/${sessionId}/</code>
</div>