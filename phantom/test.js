// Usage: phantomjs test.js <test.html>
// Expects 'window.rand_cljc_error_count' to be set by the cljs
// test runner. This will only work with synchronous tests.

var page = require('webpage').create();
var system = require('system');
var url = system.args[1];

page.onConsoleMessage = function (message) {
    console.log(message);
};

function getCount() {
    return page.evaluate(function() {
        return window.failedTestCount;
    });
}

function check() {
    var count = getCount();
    if (!count) {
        console.log("Waiting...");
        setTimeout(check, 1000);
    }
    else {
        console.log("GOT COUNT:", count.count);
        phantom.exit(count.count);
    }
}
page.open(url, function (status) {
    if (status != "success") {
        console.error('Failed to open ' + url);
        phantom.exit(1);
    }
    var result = page.evaluate(function() {
        return window.rand_cljc_error_count;
    });
    phantom.exit(result);
});
