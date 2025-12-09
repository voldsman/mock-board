<div class="col-md-8 col-lg-9 h-100 d-flex flex-column">
    <div class="d-flex justify-content-between align-items-center mb-3 pt-3">
        <h4 class="text-light mb-0">Mock Rules</h4>
        <button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#ruleModal">
            <span class="mr-1">+</span> New Rule
        </button>
    </div>

    <div class="flex-grow-1" style="overflow-y: auto; overflow-x: hidden;">
        <div id="rules-grid" class="row">

<#--            <div class="col-md-6 col-lg-4 col-xl-3 mb-3">-->
<#--                <div class="card h-100 border-secondary shadow-sm rule-card"-->
<#--                     style="background-color: var(--brand-card) !important;">-->
<#--                    <div class="card-body d-flex flex-column">-->

<#--                        <div class="d-flex justify-content-between align-items-start mb-3">-->
<#--                            <span class="badge badge-success p-2" style="font-size: 0.85rem;">GET</span>-->

<#--                            <div class="dropdown">-->
<#--                                <button class="btn btn-link text-muted p-0" type="button" data-toggle="dropdown">-->
<#--                                    <span style="font-size: 1.2rem;">&hellip;</span>-->
<#--                                </button>-->
<#--                                <div class="dropdown-menu dropdown-menu-right bg-dark border-secondary">-->
<#--                                    <a class="dropdown-item text-light" href="#">Edit</a>-->
<#--                                    <a class="dropdown-item text-danger" href="#">Delete</a>-->
<#--                                </div>-->
<#--                            </div>-->
<#--                        </div>-->

<#--                        <h6 class="card-title text-light mb-1 text-break font-weight-normal"-->
<#--                            style="font-family: 'Roboto Mono', monospace;">-->
<#--                            /api/users/*-->
<#--                        </h6>-->
<#--                        <small class="text-muted mb-3">Latency: 0ms</small>-->

<#--                        <div class="mt-auto pt-3 border-top border-secondary d-flex justify-content-between align-items-center">-->
<#--                            <span class="text-success font-weight-bold">200 OK</span>-->
<#--                            <small class="text-muted">json</small>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->

        <div id="rules-empty-state" class="text-center text-muted mt-5" style="display:none;">
            <div class="py-5">
                <span style="font-size: 3rem; opacity: 0.2;">🗂️</span>
                <h5 class="mt-3">No rules defined</h5>
                <p class="small text-secondary">Click "+ New Rule" to create your first mock endpoint.</p>
            </div>
        </div>
    </div>
</div>

<#--<div class="col-md-8 col-lg-9 h-100 d-flex flex-column">-->
<#--    <div class="d-flex justify-content-between align-items-center mb-3 pt-3">-->
<#--        <h4 class="text-light mb-0">Mock Rules</h4>-->
<#--        <button class="btn btn-primary btn-sm" data-toggle="modal" data-target="#ruleModal">-->
<#--            <span class="mr-1">+</span> New Rule-->
<#--        </button>-->
<#--    </div>-->

<#--    <div class="card bg-dark border-secondary shadow-sm flex-grow-1" style="background-color: var(--brand-card) !important; overflow: hidden;">-->

<#--        <div class="card-header border-secondary py-2">-->
<#--            <div class="row text-muted font-weight-bold text-uppercase" style="font-size: 0.75rem; letter-spacing: 1px;">-->
<#--                <div class="col-2">Method</div>-->
<#--                <div class="col-4">Path</div>-->
<#--                <div class="col-2">Status</div>-->
<#--                <div class="col-2">Content-Type</div>-->
<#--                <div class="col-2 text-right">Actions</div>-->
<#--            </div>-->
<#--        </div>-->

<#--        <div class="card-body p-0" style="overflow-y: auto;">-->
<#--            <div id="rules-list" class="list-group list-group-flush">-->
<#--                <div class="list-group-item bg-transparent border-secondary py-3 rule-item">-->
<#--                    <div class="row align-items-center">-->
<#--                        <div class="col-2">-->
<#--                            <span class="badge badge-success px-2 py-1">GET</span>-->
<#--                        </div>-->
<#--                        <div class="col-4">-->
<#--                            <code class="text-light">/api/users/*</code>-->
<#--                        </div>-->
<#--                        <div class="col-2">-->
<#--                            <span class="text-success font-weight-bold">200 OK</span>-->
<#--                        </div>-->
<#--                        <div class="col-2">-->
<#--                            <small class="text-muted">app/json</small>-->
<#--                        </div>-->
<#--                        <div class="col-2 text-right">-->
<#--                            <button class="btn btn-sm btn-link text-muted p-0 mr-2" title="Edit">✏️</button>-->
<#--                            <button class="btn btn-sm btn-link text-danger p-0" title="Delete">&times;</button>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </div>-->
<#--                 <div class="text-center text-muted mt-5 p-5" id="rules-empty-state" style="display:none;">-->
<#--                    <h5>No rules defined</h5>-->
<#--                    <p class="small">Requests matching no rules will return 404.</p>-->
<#--                </div>-->

<#--                <div class="list-group-item bg-transparent border-secondary py-3 rule-item">-->
<#--                    <div class="row align-items-center">-->
<#--                        <div class="col-2">-->
<#--                            <span class="badge badge-success px-2 py-1">GET</span>-->
<#--                        </div>-->
<#--                        <div class="col-4">-->
<#--                            <code class="text-light">/api/users/*</code>-->
<#--                        </div>-->
<#--                        <div class="col-2">-->
<#--                            <span class="text-success font-weight-bold">200 OK</span>-->
<#--                        </div>-->
<#--                        <div class="col-2">-->
<#--                            <small class="text-muted">app/json</small>-->
<#--                        </div>-->
<#--                        <div class="col-2 text-right">-->
<#--                            <button class="btn btn-sm btn-link text-muted p-0 mr-2" title="Edit">✏️</button>-->
<#--                            <button class="btn btn-sm btn-link text-danger p-0" title="Delete">&times;</button>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </div>-->
<#--                <div class="text-center text-muted mt-5 p-5" id="rules-empty-state" style="display:none;">-->
<#--                    <h5>No rules defined</h5>-->
<#--                    <p class="small">Requests matching no rules will return 404.</p>-->
<#--                </div>-->
<#--            </div>-->
<#--        </div>-->
<#--    </div>-->
<#--</div>-->