<div class="modal fade" id="ruleModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content text-light" style="background-color: var(--brand-card); border: 1px solid #444;">

            <div class="modal-header border-secondary">
                <h5 class="modal-title">Define Mock Rule</h5>
                <button type="button" class="close text-white" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div class="modal-body">
                <form id="ruleForm">
                    <div class="form-row">
                        <div class="form-group col-md-3">
                            <label class="small text-muted">Method</label>
                            <select class="form-control bg-dark text-light border-secondary" id="ruleMethod">
                                <option value="GET">GET</option>
                                <option value="POST">POST</option>
                                <option value="PUT">PUT</option>
                                <option value="DELETE">DELETE</option>
                                <option value="PATCH">PATCH</option>
                                <option value="ANY">ANY</option>
                            </select>
                        </div>
                        <div class="form-group col-md-9">
                            <label class="small text-muted">Path Pattern (supports wildcard *)</label>
                            <input type="text" class="form-control bg-dark text-light border-secondary"
                                   id="rulePath" placeholder="/api/resource or /api/*">
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group col-md-3">
                            <label class="small text-muted">Response Status</label>
                            <input type="number" class="form-control bg-dark text-light border-secondary"
                                   id="ruleStatus" value="200">
                        </div>
                        <div class="form-group col-md-5">
                            <label class="small text-muted">Content-Type</label>
                            <input type="text" class="form-control bg-dark text-light border-secondary"
                                   id="ruleContentType" value="application/json">
                        </div>
                        <div class="form-group col-md-4">
                            <label class="small text-muted">Delay (ms)</label>
                            <input type="number" class="form-control bg-dark text-light border-secondary"
                                   id="ruleDelay" value="0">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="small text-muted">Response Body</label>
                        <textarea class="form-control bg-dark text-light border-secondary text-monospace"
                                  id="ruleBody" rows="6" placeholder='{"message": "Hello World"}'></textarea>
                    </div>
                </form>
            </div>

            <div class="modal-footer border-secondary">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" id="saveRuleBtn">Save Rule</button>
            </div>
        </div>
    </div>
</div>