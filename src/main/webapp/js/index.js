$(function () {
    hljs.initHighlightingOnLoad();

    var init = function (data) {
        $('title').html(data.path);
        $('#path').val(data.path);
        $('.markdown-body').html(data.content);
        highlight_code();
        scroll_if_possible();
        MathJax.Hub.Queue(['Typeset', MathJax.Hub])
    };

    var sync = function (data) {
        $('.markdown-body').html(data.content);
        highlight_code();
        scroll_if_possible();
        MathJax.Hub.Queue(['Typeset', MathJax.Hub])
    };

    var close = function () {
        window.opener = null;
        window.open('', '_self');
        window.close();
    };

    var highlight_code = function () {
        $('pre code').each(function (i, block) {
            hljs.highlightBlock(block);
        });
    };

    var scroll_if_possible = function () {
        var marker = document.getElementById('_markdown_preview_sync_marker');
        if (marker !== null) {
            marker.scrollIntoView(false);
        }
    };

    var url = 'ws://127.0.0.1:' + window.location.port + '/ws';
    if (!WebSocket) {
        console.warn('WebSocket is not support');
    } else {
        console.log('Try to connect ' + url);
        var ws = new WebSocket(url);
        ws.onerror = function (e) {
            console.error('Error : ' + e.message);
        };

        ws.onopen = function () {
            console.log('Connected');
        };

        ws.onclose = function () {
            console.log('Disconnected');
        };

        ws.onmessage = function (d) {
            console.log('Response length: ' + d.data.length);
            var data = JSON.parse(d.data);
            if ($('#path').val() === '') {
                init(data);
            } else {
                if ($('#path').val() === data.path) {
                    if (data.command === 'close') {
                        close()
                    } else if (data.command === 'sync') {
                        sync(data);
                    }
                }
            }
        };
    }

    self.mark_line = function (line) {
        if (ws !== null) {
            ws.send(line);
        } else {
            console.warn('WebSocket is close');
        }
    };
});
