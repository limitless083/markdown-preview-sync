echo "Loading"

if !has("python")
    echo "vim has to be compiled with +python to run this"
    finish
endif

if exists("g:markdown_preview_sync_loaded")
    finish
endif

let g:markdown_preview_sync_loaded = 1
let g:markdown_preview_sync_port = 7788
let g:markdown_preview_sync_chrome = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"

let s:plugin_root_dir = fnamemodify(resolve(expand("<sfile>:p")), ":h")

python << EOF
import sys
from os.path import normpath, join
import vim
plugin_root_dir = vim.eval('s:plugin_root_dir')
python_root_dir = normpath(join(plugin_root_dir, 'python'))
sys.path.insert(0, python_root_dir)
import java_vim_bridge
EOF

function! s:start()
    execute "silent !start /b java -jar \"" . s:plugin_root_dir . "\"/java/markdown-preview-sync.jar"

python <<EOF
port = vim.eval('g:markdown_preview_sync_port')
java_vim_bridge.start(int(port))
EOF
endfunction

function! s:is_start()
    return exists("g:markdown_preview_sync_start")
endfunction

function! s:open()
    execute "silent !start " . g:markdown_preview_sync_chrome . " http://127.0.0.1:" . g:markdown_preview_sync_port . "/index"
endfunction

function! s:sync()
python <<EOF
path = vim.eval('expand("%:p")')
lines = vim.eval('getline(1, line("$"))')
bottom = vim.eval('line("w$")')
java_vim_bridge.sync(path, lines, int(bottom))
EOF
endfunction

function! s:close()
python <<EOF
path = vim.eval('expand("%:p")')
java_vim_bridge.close(path)
EOF
endfunction

function! s:stop()
python <<EOF
path = vim.eval('expand("%:p")')
java_vim_bridge.stop()
EOF
endfunction

function! s:autocmd()
    augroup markdown_preview_sync
        autocmd!
        autocmd CursorMoved,CursorMovedI <buffer> :call s:sync()
        autocmd VimLeave * call s:trigger_stop()
    augroup end
endfunction

function! s:trigger_stop()
    call s:close()
    call s:stop()
endfunction

function! mpsync#preview()
    if !s:is_start()
        call s:start()
        let g:markdown_preview_sync_start = 1
    endif
    call s:open()
    call s:autocmd()
endfunction

function! mpsync#close()
    if s:is_start()
        call s:close()
    endif
endfunction
