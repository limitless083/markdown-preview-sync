$(function () {
    hljs.initHighlightingOnLoad();

    var init = function (data) {
        $('title').html(data.path);
        $('#path').val(data.path);
        refresh_content(data.content);
        highlight_code();
        scroll_if_possible();
        MathJax.Hub.Queue(['Typeset', MathJax.Hub]);
    };

    var sync = function (data) {
        refresh_content(data.content);
        highlight_code();
        scroll_if_possible();
    };

    var old_html = null;
    var refresh_content = function (html_string) {
        var new_html = $(html_string).not('text');

        if (old_html === null) {
            $('.markdown-body').html(new_html);
        } else {
            var children = $('.markdown-body').children();
            var old_len = old_html.length;
            var new_len = new_html.length;
            var old_ele = null;
            var new_ele = null;
            var child = null;
            var formula_id = '';
            if (new_len > old_len) {
                for (var i = 0; i < new_len; i++) {
                    new_ele = new_html[i];
                    if (i < old_len) {
                        old_ele = old_html[i];
                        if (old_ele.outerHTML !== new_ele.outerHTML) {
                            console.log('old:' + old_ele.outerHTML);
                            console.log('new:' + new_ele.outerHTML);
                            child = $(children[i]);
                            child.replaceWith($(new_ele).clone());

                            if (new_ele.outerHTML.indexOf('$') > -1) {
                                formula_id = 'formula-' + i;
                                child.attr('id', formula_id);
                                MathJax.Hub.Queue(['Typeset', MathJax.Hub, formula_id]);
                                child.removeAttr('id')
                            }
                        }
                    } else {
                        var new_child = $(new_ele).clone();
                        $('.markdown-body').append(new_child);
                        if (new_ele.outerHTML.indexOf('$') > -1) {
                            formula_id = 'formula-' + i;
                            new_child.attr('id', formula_id);
                            MathJax.Hub.Queue(['Typeset', MathJax.Hub, formula_id]);
                            new_child.removeAttr('id')
                        }
                    }
                }
            } else {
                for (var j = 0; j < old_len; j++) {
                    child = $(children[j]);
                    if (j < new_len) {
                        old_ele = old_html[j];
                        new_ele = new_html[j];
                        if (old_ele.outerHTML !== new_ele.outerHTML) {
                            console.log('old:' + old_ele.outerHTML);
                            console.log('new:' + new_ele.outerHTML);

                            $(children[j]).replaceWith($(new_ele).clone());
                            if (new_ele.outerHTML.indexOf('$') > -1) {
                                formula_id = 'formula-' + j;
                                child.attr('id', formula_id);
                                MathJax.Hub.Queue(['Typeset', MathJax.Hub, formula_id]);
                                child.removeAttr('id')
                            }
                        }
                    } else {
                        child.remove();
                    }
                }
            }

        }
        old_html = new_html;
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
        var marker = document.getElementById('_markdown_preview_sync_bottom_marker');
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
