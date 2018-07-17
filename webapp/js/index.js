$(function () {
    hljs.initHighlightingOnLoad();

    var init = function (data) {
        $('#path').val(data.path);
        $('.markdown-body').html(data.content);
        highlight_code();
        scroll_if_possible();
        MathJax.Hub.Queue(["Typeset",MathJax.Hub])
    };

    var sync = function (data) {
        $('.markdown-body').html(data.content);
        highlight_code();
        scroll_if_possible();
        MathJax.Hub.Queue(["Typeset",MathJax.Hub])
    };

    var close = function () {
        window.opener = null;
        window.open('', '_self');
        window.close();
    };

    var highlight_code = function () {
        var marker = '<a href="#" id="_6d61726b646f776e2d707265766965772d73796e63"></a>';
        $('code[class^=language]').each(function (i, e) {
            var cls = $(e).attr('class');
            var language = cls.substring(cls.indexOf('-') + 1);
            console.log(language);

            var codes = $(e).html().split('\n');
            $.each(codes, function (i, e) {
                var mark_idx = e.lastIndexOf('<a');
                if (mark_idx <= -1) {
                    codes[i] = hljs.highlight(language, e).value;
                } else {
                    console.log(hljs.highlight(language, e.substring(0, mark_idx)).value);
                    codes[i] += hljs.highlight(language, e.substring(0, mark_idx)).value + marker;
                }
            });
            $(e).html(codes.join('\n'));
        });
    };

    var scroll_if_possible = function () {
        var marker = document.getElementById('_6d61726b646f776e2d707265766965772d73796e63');
        if (marker !== null) {
            marker.scrollIntoView(false);
        }
    };

    var url = 'ws://127.0.0.1:7788/ws';
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
            var data = JSON.parse(Base64.decode(d.data));
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
            console.warn("WebSocket is close");
        }
    };
});
