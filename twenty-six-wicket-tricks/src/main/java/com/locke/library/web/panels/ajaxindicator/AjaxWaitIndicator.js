
var requestInProgress = false;

function ajaxRequest(newRequestInProgress)
{
	if (requestInProgress != newRequestInProgress)
	{
		requestInProgress = newRequestInProgress;
		var ajaxIndicator = document.getElementById('ajaxWaitIndicator');
		ajaxIndicator.style['visibility'] = (requestInProgress ? 'visible' : 'hidden');
	}
}
		
function onStartAjax() {
	ajaxRequest(true);
}

function onStopAjax() {
	ajaxRequest(false);
}

function initialize() {
	Wicket.Ajax.registerPreCallHandler(onStartAjax);
	Wicket.Ajax.registerPostCallHandler(onStopAjax);
	Wicket.Ajax.registerFailureHandler(onStopAjax);
}

window.onload = initialize;

