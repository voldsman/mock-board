<div class="modal fade" id="requestPreviewModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content text-white" style="background-color: var(--brand-card); border: 1px solid #444;">

            <div class="modal-header border-secondary">
                <h5 class="modal-title">
                    <span id="modal-method" class="badge badge-primary mr-2">GET</span>
                    <span id="modal-path" class="text-monospace">/api/path</span>
                </h5>
                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <ul class="nav nav-tabs border-secondary mb-3" id="reqTabs" role="tablist">
                    <li class="nav-item">
                        <a class="nav-link active" id="headers-tab" data-toggle="tab" href="#headers" role="tab">Headers</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="body-tab" data-toggle="tab" href="#body" role="tab">Body</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="query-tab" data-toggle="tab" href="#query" role="tab">Query Params</a>
                    </li>
                </ul>

                <div class="tab-content" id="reqTabsContent">
                    <div class="tab-pane fade show active" id="headers" role="tabpanel">
                        <pre class="bg-dark p-3 rounded border border-secondary"><code id="modal-headers-content" class="text-success">...</code></pre>
                    </div>
                    <div class="tab-pane fade" id="body" role="tabpanel">
                        <pre class="bg-dark p-3 rounded border border-secondary"><code id="modal-body-content" class="text-warning">...</code></pre>
                    </div>
                    <div class="tab-pane fade" id="query" role="tabpanel">
                        <pre class="bg-dark p-3 rounded border border-secondary"><code id="modal-query-content" class="text-info">...</code></pre>
                    </div>
                </div>
            </div>

            <div class="modal-footer border-secondary">
                <span class="mr-auto text-muted text-monospace" id="modal-timestamp">Time: --:--:--</span>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>