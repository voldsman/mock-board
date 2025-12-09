$(document).ready(function(){
    console.log("Ready!");

    const uuid = globalThis.MockBoard ? globalThis.MockBoard.sessionId : "unknown";
    const ws = new BoardWS('ws://'+ location.host + '/ws/' + uuid);

    ws.onopen = () => {
        $("#connection-status").removeClass("bg-danger").addClass("bg-success");
    };

    ws.onclose = () => {
        $("#connection-status").removeClass("bg-success").addClass("bg-danger");
    }

    ws.onmessage = (data) => {
        console.log("msg received: " + data);
    };

    loadRules();

    $("#saveRuleBtn").click(function() {
        console.log("clicked");

        const ruleData = {
            method: $("#ruleMethod").val(),
            path: $("#rulePath").val(),
            status: parseInt($("#ruleStatus").val()) || 200,
            contentType: $("#ruleContentType").val(),
            delay: parseInt($("#ruleDelay").val()) || 0,
            body: $("#ruleBody").val()
        };

        if(!ruleData.path) {
            alert("Path is required!");
            return;
        }

        $.ajax({
            url: '/api/board/' + window.MockBoard.sessionId + '/rules',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(ruleData),
            success: function(newRule) {
                $('#ruleModal').modal('hide');
                $('#ruleForm')[0].reset();
                addRuleCard(newRule);
            },
            error: function(err) {
                alert("Error saving rule: " + err.responseText);
            }
        });
    });
});

function loadRules() {
    $.get('/api/board/' + globalThis.MockBoard.sessionId + '/rules', function(rules) {
        if(rules.length > 0) {
            $("#rules-empty-state").hide();
            rules.forEach(addRuleCard);
        }
    });
}

function addRuleCard(rule) {
    $("#rules-empty-state").hide();

    // Color coding for methods
    let badgeClass = "badge-secondary";
    if(rule.method === "GET") badgeClass = "badge-success";
    if(rule.method === "POST") badgeClass = "badge-primary";
    if(rule.method === "DELETE") badgeClass = "badge-danger";
    if(rule.method === "PUT") badgeClass = "badge-warning";

    const cardHtml = `
        <div class="col-md-6 col-lg-4 col-xl-3 mb-3 rule-col">
            <div class="card h-100 border-secondary shadow-sm rule-card" 
                 style="background-color: var(--brand-card) !important;">
                <div class="card-body d-flex flex-column">
                    <div class="d-flex justify-content-between align-items-start mb-3">
                        <span class="badge ${badgeClass} p-2" style="font-size: 0.85rem;">${rule.method}</span>
                        <div class="dropdown">
                            <button class="btn btn-link text-muted p-0" type="button" data-toggle="dropdown">
                                <span style="font-size: 1.2rem;">&hellip;</span>
                            </button>
                            <div class="dropdown-menu dropdown-menu-right bg-dark border-secondary">
                                <a class="dropdown-item text-danger" href="#" onclick="alert('Delete not implemented yet')">Delete</a>
                            </div>
                        </div>
                    </div>
                    <h6 class="card-title text-light mb-1 text-break font-weight-normal" 
                        style="font-family: 'Roboto Mono', monospace;">
                        ${rule.path}
                    </h6>
                    <small class="text-muted mb-3">Latency: ${rule.delayMs}ms</small>
                    <div class="mt-auto pt-3 border-top border-secondary d-flex justify-content-between align-items-center">
                        <span class="text-success font-weight-bold">${rule.status}</span>
                        <small class="text-muted text-truncate" style="max-width: 80px;">${rule.contentType}</small>
                    </div>
                </div>
            </div>
        </div>
    `;

    $("#rules-grid").append(cardHtml);
}

// utility functions
function copyUrl() {
    const copyInput = document.getElementById("webhook-url");
    const textToCopy = copyInput.value;

    navigator.clipboard.writeText(textToCopy).then(() => {
        const btn = $(copyInput).next().find("button");
        const originalText = btn.text();

        btn.text("Copied!").addClass("btn-success").removeClass("btn-outline-primary");

        setTimeout(() => {
            btn.text(originalText).removeClass("btn-success").addClass("btn-outline-primary");
        }, 2000);
    }).catch(err => {
        console.error('Failed to copy text: ', err);
        alert("Could not copy to clipboard. Please select and copy manually.");
    });
}